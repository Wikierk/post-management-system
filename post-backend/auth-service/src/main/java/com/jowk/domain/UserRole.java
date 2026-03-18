package com.jowk.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@IdClass(UserRoleId.class)
public class UserRole implements Persistable<UserRoleId> {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Include
    private User user;

    @Id
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private Role role;

    @Transient
    private boolean isNew = true;

    @Override
    public UserRoleId getId() {
        if ((user == null) || (role == null)) {
            return null;
        }
        return new UserRoleId(user.getId(), role);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        isNew = false;
    }

}
