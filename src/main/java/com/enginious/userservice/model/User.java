package com.enginious.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user",
        uniqueConstraints = @UniqueConstraint(
                name = "unq_user_application_username",
                columnNames = {"application", "username"}
        )
)
public class User {

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "application",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_application")
    )
    private Application application;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "addedAt", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime addedAt = LocalDateTime.now();

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user", foreignKey = @ForeignKey(name = "fk_user_role_user")),
            inverseJoinColumns = @JoinColumn(name = "role", foreignKey = @ForeignKey(name = "fk_user_role_role"))
    )
    private Set<Role> roles = new HashSet<>();

    @Builder
    public User(Application application, String username, String password, boolean enabled, Set<Role> roles) {
        this.application = application;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }
}
