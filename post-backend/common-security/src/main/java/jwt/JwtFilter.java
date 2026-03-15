package jwt;

import domain.Role;
import domain.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtClaimExtractor claimExtractor;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {
        if (isAlreadyAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            tryDoFilterInternal(request);
        } catch (Exception ex) {
            log.error("An error occurred: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private void tryDoFilterInternal(
            HttpServletRequest request) {
        String accessToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (JwtConstants.JWT_COOKIE_NAME.equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }
            }
        }
        if (accessToken == null) {
            return;
        }
        UUID userId = claimExtractor.extractUserId(accessToken);
        Set<Role> roles = mapToRoles(claimExtractor.extractRoles(accessToken));
        AuthenticatedUser user = new AuthenticatedUser(
                userId, null, null, roles, true);
        UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean isAlreadyAuthenticated() {
        return SecurityContextHolder.getContext()
                .getAuthentication() != null;
    }

    private Set<Role> mapToRoles(List<String> roles) {
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}
