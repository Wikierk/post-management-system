import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { Box, CircularProgress, Typography } from "@mui/material";

export const OAuthRedirect = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { login } = useAuth();
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const handleOAuth = async () => {
      const token = searchParams.get("token");
      const errorParam = searchParams.get("error");

      if (errorParam) {
        setError("Logowanie przez dostawcę zewnętrznego nie powiodło się.");
        setTimeout(() => navigate("/login", { replace: true }), 3000);
        return;
      }

      if (token) {
        try {
          await login(token);
          navigate("/dashboard", { replace: true });
        } catch (err) {
          setError("Nie udało się zweryfikować Twojego konta po logowaniu.");
          setTimeout(() => navigate("/login", { replace: true }), 3000);
        }
      } else {
        navigate("/login", { replace: true });
      }
    };

    handleOAuth();
  }, [searchParams, login, navigate]);

  if (error) {
    return (
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        minHeight="100vh"
      >
        <Typography color="error" variant="h6">
          {error}
        </Typography>
        <Typography variant="body2" sx={{ mt: 1 }}>
          Przekierowywanie do strony logowania...
        </Typography>
      </Box>
    );
  }

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      minHeight="100vh"
    >
      <CircularProgress size={60} />
      <Typography variant="h6" sx={{ mt: 3 }}>
        Trwa bezpieczne logowanie...
      </Typography>
      <Typography variant="body2" color="text.secondary">
        Zaraz zostaniesz przekierowany.
      </Typography>
    </Box>
  );
};
