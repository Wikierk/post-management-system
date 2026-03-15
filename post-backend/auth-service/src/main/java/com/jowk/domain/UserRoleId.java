package com.jowk.domain;

import domain.Role;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId {

    private UUID user;
    private Role role;

}
