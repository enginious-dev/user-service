package com.enginious.userservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "organizzation",
        uniqueConstraints = @UniqueConstraint(
                name = "unq_organizzation_name",
                columnNames = "name"
        )
)
public class Organizzation {

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "addedAt", columnDefinition = "TIMESTAMP", nullable = false)
    private Date addedAt = new Date();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "organizzation")
    private Set<Application> applications = new HashSet<>();
}
