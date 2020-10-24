package com.enginious.userservice.model;

import com.enginious.userservice.model.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "application",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unq_application_organizzation_name",
                        columnNames = {"organizzation", "name"}
                ),
                @UniqueConstraint(
                        name = "unq_application_uuid",
                        columnNames = "uuid"
                )
        }
)
public class Application {

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizzation",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_application_organizzation")
    )
    private Organizzation organizzation;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "secret", nullable = false)
    private String secret;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "addedAt", columnDefinition = "TIMESTAMP", nullable = false)
    private Date addedAt = new Date();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "application")
    private Set<User> users = new HashSet<>();

    @Builder
    public Application(Organizzation organizzation, String name, String uuid, String secret, Role role) {
        this.organizzation = organizzation;
        this.name = name;
        this.uuid = uuid;
        this.secret = secret;
        this.role = role;
    }
}
