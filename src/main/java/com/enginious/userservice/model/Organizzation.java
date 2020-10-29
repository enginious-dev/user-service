package com.enginious.userservice.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "organizzation",
        uniqueConstraints = @UniqueConstraint(
                name = "unq_organizzation_vatNumber",
                columnNames = "vatNumber"
        )
)
public class Organizzation {

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "vatNumber", nullable = false)
    private String vatNumber;

    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "addedAt", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime addedAt = LocalDateTime.now();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "organizzation")
    private Set<Application> applications = new HashSet<>();

    @Builder
    public Organizzation(@Size(min = 2) String vatNumber, @Size(min = 2) String name) {
        this.vatNumber = vatNumber;
        this.name = name;
    }
}
