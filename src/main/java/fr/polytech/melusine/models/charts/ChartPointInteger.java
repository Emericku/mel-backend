package fr.polytech.melusine.models.charts;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChartPointInteger implements ChartPoint<Integer> {

    double abscissa;

    Integer ordinate;

}
