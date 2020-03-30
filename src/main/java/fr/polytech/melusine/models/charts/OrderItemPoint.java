package fr.polytech.melusine.models.charts;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class OrderItemPoint implements ChartPoint<OffsetDateTime, String> {

    private OffsetDateTime ordinate;

    private String abscissa;

}
