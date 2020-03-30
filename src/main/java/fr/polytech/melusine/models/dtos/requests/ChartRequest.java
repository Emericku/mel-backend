package fr.polytech.melusine.models.dtos.requests;

import fr.polytech.melusine.models.charts.ChartInterval;
import fr.polytech.melusine.models.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class ChartRequest {

    ChartInterval interval;

    OffsetDateTime from;

    List<Category> categories;

}
