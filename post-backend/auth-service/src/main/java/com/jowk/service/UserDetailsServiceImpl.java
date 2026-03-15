package com.jowk.service;

import com.jowk.domain.User;
import com.jowk.domain.UserRole;
import com.jowk.dto.AuthenticatedUser;
import com.jowk.repository.UserRepository;
import domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User was not found."));
        Set<Role> roles = user.getRoles()
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                roles,
                user.isActive()
        );
    }

}
