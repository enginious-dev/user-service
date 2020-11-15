package com.enginious.userservice.model;

import com.enginious.userservice.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "application",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unq_application_organization_name",
                        columnNames = {"organization", "name"}
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

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "organization",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_application_organization")
    )
    private Organization organization;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "secret", nullable = false)
    private String secret;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "addedAt", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime addedAt = LocalDateTime.now();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "application")
    private Set<User> users = new HashSet<>();

    @Builder
    public Application(Organization organization, String name, String uuid, String secret, Role role) {
        this.organization = organization;
        this.name = name;
        this.uuid = uuid;
        this.secret = secret;
        this.role = role;
    }
}
