package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.charts.ChartType;
import fr.polytech.melusine.models.enums.Interval;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartRequest {

    Interval interval;

    ChartType type;
    
}
