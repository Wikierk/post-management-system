package jwt;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import javax.crypto.SecretKey;
import java.util.UUID;
import java.util.function.Function;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtClaimExtractor {

    private final SecretKey signingKey;

    public UUID extractUserId(String token) {
        return extractClaim(token, claims -> {
            String idAsText = claims.get(JwtConstants.USER_ID_CLAIM, String.class);
            if ((idAsText == null) || idAsText.isBlank()) {
                throw new MissingJwtClaimException(String.format(
                        "There is no %s claim in the token.",
                        JwtConstants.USER_ID_CLAIM
                ));
            }
            return UUID.fromString(idAsText);
        });
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            List<String> roles = claims.get(JwtConstants.ROLES_CLAIM, List.class);
            if (roles == null) {
                throw new MissingJwtClaimException(String.format(
                        "There is no %s claim in the token.",
                        JwtConstants.ROLES_CLAIM
                ));
            }
            return roles;
        });
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
