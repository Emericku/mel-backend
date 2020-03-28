package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.InternalServerErrorException;
import fr.polytech.melusine.exceptions.errors.SystemError;
import fr.polytech.melusine.mappers.OrderMapper;
import fr.polytech.melusine.models.charts.ChartPoint;
import fr.polytech.melusine.models.charts.ChartPointInteger;
import fr.polytech.melusine.models.charts.ChartPointString;
import fr.polytech.melusine.models.charts.OrderPoint;
import fr.polytech.melusine.models.dtos.requests.ChartRequest;
import fr.polytech.melusine.models.dtos.responses.ChartResponse;
import fr.polytech.melusine.models.enums.Interval;
import fr.polytech.melusine.models.enums.OrderStatus;
import fr.polytech.melusine.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ChartService {

    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private Clock clock;

    public ChartService(OrderRepository orderRepository, OrderMapper orderMapper, Clock clock) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.clock = clock;
    }

    public ChartResponse findChart(ChartRequest chartRequest) {
        OffsetDateTime now = OffsetDateTime.now(clock);
        OffsetDateTime start = getStartDate(chartRequest.getInterval(), now);

        List<ChartPoint> orderCharts = new ArrayList<>();
        orderRepository.findByCreatedAtBetweenAndStatus(start, now, OrderStatus.DELIVER).stream()
                .map(orderMapper::mapToOrderChart)
                .map(orderPoint -> getChartPoint(chartRequest.getInterval(), orderPoint))
                .collect(groupingBy(ChartPoint::getOrdinate))
                .forEach((time, total) -> addChartPointInList(orderCharts, time, total));

        return ChartResponse.builder()
                .points(orderCharts)
                .build();
    }

    private void addChartPointInList(List<ChartPoint> orderCharts, Serializable time, List<? extends ChartPoint<? extends Serializable>> total) {
        double newTotal = total.stream()
                .map(ChartPoint::getAbscissa)
                .mapToDouble(Double::valueOf)
                .sum();
        if (time instanceof Integer) {
            orderCharts.add(
                    ChartPointInteger.builder()
                            .abscissa(newTotal)
                            .ordinate((Integer) time)
                            .build());
        }
        if (time instanceof String) {
            orderCharts.add(ChartPointString.builder()
                    .abscissa(newTotal)
                    .ordinate((String) time)
                    .build());
        }
    }

    private ChartPoint<? extends Serializable> getChartPoint(Interval interval, OrderPoint orderPoint) {
        if (interval.equals(Interval.DECADE)) {
            return ChartPointInteger.builder()
                    .abscissa(formatToDouble(orderPoint.getTotal()))
                    .ordinate(orderPoint.getTime().getYear())
                    .build();
        }
        if (interval.equals(Interval.YEAR)) {
            return ChartPointString.builder()
                    .abscissa(formatToDouble(orderPoint.getTotal()))
                    .ordinate(orderPoint.getTime().getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE))
                    .build();
        }
        if (interval.equals(Interval.MONTH)) {
            return ChartPointString.builder()
                    .abscissa(formatToDouble(orderPoint.getTotal()))
                    .ordinate(orderPoint.getTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE)
                            + " " + orderPoint.getTime().getDayOfMonth())
                    .build();
        }
        if (interval.equals(Interval.WEEK)) {
            return ChartPointString.builder()
                    .abscissa(formatToDouble(orderPoint.getTotal()))
                    .ordinate(orderPoint.getTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE))
                    .build();
        }
        if (interval.equals(Interval.DAY)) {
            return ChartPointInteger.builder()
                    .abscissa(formatToDouble(orderPoint.getTotal()))
                    .ordinate(orderPoint.getTime().getHour())
                    .build();
        }
        throw new InternalServerErrorException(SystemError.TECHNICAL_ERROR);
    }

    private OffsetDateTime getStartDate(Interval interval, OffsetDateTime now) {
        if (interval.equals(Interval.DECADE)) {
            return now.minusYears(10);
        }
        if (interval.equals(Interval.YEAR)) {
            return now.minusYears(1);
        }
        if (interval.equals(Interval.MONTH)) {
            return now.minusMonths(1);
        }
        if (interval.equals(Interval.WEEK)) {
            return now.minusWeeks(1);
        }
        if (interval.equals(Interval.DAY)) {
            return now.minusDays(1);
        }
        throw new InternalServerErrorException(SystemError.TECHNICAL_ERROR);
    }

}
