import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

export function OAuthRedirectHandler() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const token = searchParams.get("token");
    if (token) {
      localStorage.setItem("jwt_token", token);
      navigate("/dashboard");
    } else {
      navigate("/login?error=OauthFailed");
    }
  }, [searchParams, navigate]);

  return <div>Logowanie... Proszę czekać.</div>;
}
