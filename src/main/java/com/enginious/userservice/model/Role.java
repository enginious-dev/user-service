package com.enginious.userservice.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "role",
        uniqueConstraints = @UniqueConstraint(
                name = "unq_role_application_name",
                columnNames = {"application", "name"}
        )
)
public class Role {

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_role_application")
    )
    private Application application;

    @Column(name = "name", nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "addedAt", columnDefinition = "TIMESTAMP", nullable = false)
    private Date addedAt = new Date();

}
