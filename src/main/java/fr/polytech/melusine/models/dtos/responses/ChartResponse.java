package fr.polytech.melusine.models.dtos.responses;

import fr.polytech.melusine.models.charts.ChartPoint;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartResponse {

    List<ChartPoint> points;

}
