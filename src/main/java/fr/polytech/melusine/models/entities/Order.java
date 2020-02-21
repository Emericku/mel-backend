package fr.polytech.melusine.models.entities;

import fr.polytech.melusine.models.enums.OrderStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String displayName;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = true)
    private User user;

    @Builder.Default
    private long total = 0;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @NonNull
    private OffsetDateTime createdAt;

    @NonNull
    private OffsetDateTime updatedAt;

}
