package fr.polytech.melusine.models.charts;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class OrderPoint {

    private long total;
    private OffsetDateTime time;
}
