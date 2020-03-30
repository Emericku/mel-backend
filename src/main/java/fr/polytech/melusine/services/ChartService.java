package fr.polytech.melusine.services;

import fr.polytech.melusine.exceptions.InternalServerErrorException;
import fr.polytech.melusine.exceptions.errors.SystemError;
import fr.polytech.melusine.mappers.OrderItemMapper;
import fr.polytech.melusine.mappers.OrderMapper;
import fr.polytech.melusine.models.charts.*;
import fr.polytech.melusine.models.dtos.requests.ChartRequest;
import fr.polytech.melusine.models.dtos.responses.ChartResponse;
import fr.polytech.melusine.models.enums.Category;
import fr.polytech.melusine.models.enums.OrderStatus;
import fr.polytech.melusine.repositories.OrderItemRepository;
import fr.polytech.melusine.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ChartService {

    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private OrderItemRepository orderItemRepository;
    private OrderItemMapper orderItemMapper;
    private Clock clock;

    public ChartService(OrderRepository orderRepository, OrderMapper orderMapper, OrderItemRepository orderItemRepository,
                        OrderItemMapper orderItemMapper, Clock clock) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.clock = clock;
    }

    public ChartResponse findRevenuesChart(ChartRequest chartRequest) {
        OffsetDateTime from = getFrom(chartRequest);
        OffsetDateTime start = getStartDate(chartRequest.getInterval(), from);

        List<ChartPoint> orderChartPoints = new ArrayList<>();
        orderRepository.findByCreatedAtBetweenAndStatus(start, from, OrderStatus.DELIVER).stream()
                .map(orderMapper::mapToOrderPoint)
                .map(orderPoint -> getChartPointForIntervalAndOrderPoint(chartRequest.getInterval(), orderPoint))
                .collect(groupingBy(ChartPoint::getAbscissa))
                .forEach((time, points) -> addChartPointInList(orderChartPoints, time, points));

        return ChartResponse.builder()
                .points(orderChartPoints)
                .build();
    }

    public ChartResponse findConsumptionsChart(ChartRequest chartRequest) {
        OffsetDateTime from = getFrom(chartRequest);
        OffsetDateTime start = getStartDate(chartRequest.getInterval(), from);
        List<ChartPoint> productChartPoints = new ArrayList<>();
        List<Category> categories = List.of(Category.values());
        if (Objects.nonNull(chartRequest.getCategories()) && !chartRequest.getCategories().isEmpty()) {
            categories = chartRequest.getCategories();
        }
        orderItemRepository.findByCreatedAtBetweenAndStatusAndProductCategoryIsIn(start, from, OrderStatus.DELIVER, categories).stream()
                .map(orderItemMapper::mapToOrderItemPoint)
                .collect(groupingBy(OrderItemPoint::getAbscissa))
                .forEach((productName, points) -> productChartPoints.add(ChartPointString.builder()
                        .ordinate(((double) points.size()))
                        .abscissa(productName)
                        .build()));

        return ChartResponse.builder()
                .points(productChartPoints)
                .build();
    }

    private OffsetDateTime getFrom(ChartRequest chartRequest) {
        if (Objects.nonNull(chartRequest.getFrom())) {
            return chartRequest.getFrom();
        }
        return OffsetDateTime.now(clock);
    }

    private void addChartPointInList(List<ChartPoint> orderCharts, Object time, List<? extends ChartPoint> points) {
        double newTotal = points.stream()
                .map(ChartPoint::getOrdinate)
                .map(Double.class::cast)
                .mapToDouble(Double::valueOf)
                .sum();
        if (time instanceof Integer) {
            orderCharts.add(
                    ChartPointInteger.builder()
                            .ordinate(newTotal)
                            .abscissa((Integer) time)
                            .build());
        }
        if (time instanceof String) {
            orderCharts.add(ChartPointString.builder()
                    .ordinate(newTotal)
                    .abscissa((String) time)
                    .build());
        }
    }

    private ChartPoint getChartPointForIntervalAndOrderPoint(ChartInterval chartInterval, OrderPoint orderPoint) {
        if (chartInterval.equals(ChartInterval.DECADE)) {
            return ChartPointInteger.builder()
                    .ordinate(formatToDouble(orderPoint.getOrdinate()))
                    .abscissa(orderPoint.getAbscissa().getYear())
                    .build();
        }
        if (chartInterval.equals(ChartInterval.YEAR)) {
            return ChartPointString.builder()
                    .ordinate(formatToDouble(orderPoint.getOrdinate()))
                    .abscissa(orderPoint.getAbscissa().getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE))
                    .build();
        }
        if (chartInterval.equals(ChartInterval.MONTH)) {
            return ChartPointString.builder()
                    .ordinate(formatToDouble(orderPoint.getOrdinate()))
                    .abscissa(orderPoint.getAbscissa().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE)
                            + " " + orderPoint.getAbscissa().getDayOfMonth())
                    .build();
        }
        if (chartInterval.equals(ChartInterval.WEEK)) {
            return ChartPointString.builder()
                    .ordinate(formatToDouble(orderPoint.getOrdinate()))
                    .abscissa(orderPoint.getAbscissa().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE)
                            + " " + orderPoint.getAbscissa().getDayOfMonth())
                    .build();
        }
        if (chartInterval.equals(ChartInterval.DAY)) {
            return ChartPointInteger.builder()
                    .ordinate(formatToDouble(orderPoint.getOrdinate()))
                    .abscissa(orderPoint.getAbscissa().getHour())
                    .build();
        }
        throw new InternalServerErrorException(SystemError.TECHNICAL_ERROR);
    }

    private OffsetDateTime getStartDate(ChartInterval chartInterval, OffsetDateTime now) {
        if (chartInterval.equals(ChartInterval.DECADE)) {
            return now.minusYears(10);
        }
        if (chartInterval.equals(ChartInterval.YEAR)) {
            return now.minusYears(1);
        }
        if (chartInterval.equals(ChartInterval.MONTH)) {
            return now.minusMonths(1);
        }
        if (chartInterval.equals(ChartInterval.WEEK)) {
            return now.minusWeeks(1);
        }
        if (chartInterval.equals(ChartInterval.DAY)) {
            return now.minusDays(1);
        }
        throw new InternalServerErrorException(SystemError.TECHNICAL_ERROR);
    }

}
