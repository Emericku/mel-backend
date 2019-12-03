package fr.polytech.melusine.models.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @NonNull
    @Column(unique = true)
    private String email;

    @NonNull
    private String password;

    private boolean isBarman;

    @NonNull
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @NonNull
    private OffsetDateTime createdAt;

    @NonNull
    private OffsetDateTime updatedAt;

}
