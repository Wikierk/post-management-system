package jwt;

public class MissingJwtClaimException extends RuntimeException {

    public MissingJwtClaimException(String message) {
        super(message);
    }

}
