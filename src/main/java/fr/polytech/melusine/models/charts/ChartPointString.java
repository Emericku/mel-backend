package fr.polytech.melusine.models.charts;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChartPointString implements ChartPoint<Double, String> {

    Double ordinate;

    String abscissa;

}
