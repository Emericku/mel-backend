package fr.polytech.melusine.models.charts;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChartPointInteger implements ChartPoint<Double, Integer> {

    Double ordinate;

    Integer abscissa;

}
