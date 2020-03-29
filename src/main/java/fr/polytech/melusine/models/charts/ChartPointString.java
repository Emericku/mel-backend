package fr.polytech.melusine.models.charts;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChartPointString implements ChartPoint<String> {

    double ordinate;

    String abscissa;

}
