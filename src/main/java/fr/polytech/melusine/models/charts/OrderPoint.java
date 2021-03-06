package fr.polytech.melusine.models.charts;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class OrderPoint implements ChartPoint<Long, OffsetDateTime> {

    private Long ordinate;

    private OffsetDateTime abscissa;

}
