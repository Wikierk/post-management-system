import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Box, Card, CardContent, Alert, Divider } from "@mui/material";
import { useAuth } from "../../context/AuthContext";
import { LoginHeader } from "../../components/LoginHeader";
import { LoginForm } from "../../components/LoginForm";
import { LoginFooter } from "../../components/LoginFooter";
import api from "../../api/axiosConfig";
import { GoogleLogin } from "@react-oauth/google";

export const LoginPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const searchParams = new URLSearchParams(location.search);
  const urlError = searchParams.get("error");

  const handleStandardLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      await api.post("/auth/v1/login", { email, password });

      await login();
      navigate("/dashboard", { replace: true });
    } catch (err: any) {
      console.error("Błąd logowania:", err);
      if (err.response && err.response.status === 401) {
        setError("Nieprawidłowy adres e-mail lub hasło.");
      } else {
        setError("Wystąpił problem z połączeniem. Spróbuj ponownie później.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleSuccess = async (tokenResponse: any) => {
    setIsLoading(true);
    setError(null);
    try {
      await api.post("/auth/v1/oauth2/google", {
        id_token: tokenResponse.credential,
      });

      await login();
      navigate("/dashboard", { replace: true });
    } catch (err: any) {
      console.error("Błąd logowania Google:", err);
      setError("Logowanie przez Google nie powiodło się.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "grey.100",
        p: 2,
      }}
    >
      <Card
        sx={{ maxWidth: 450, width: "100%", borderRadius: 3, boxShadow: 3 }}
      >
        <LoginHeader />

        <CardContent sx={{ p: 4 }}>
          {(error || urlError) && (
            <Alert severity="error" sx={{ mb: 3 }}>
              {error || "Wystąpił problem podczas logowania."}
            </Alert>
          )}

          <LoginForm
            email={email}
            password={password}
            isLoading={isLoading}
            onEmailChange={setEmail}
            onPasswordChange={setPassword}
            onSubmit={handleStandardLogin}
          />

          <Divider sx={{ my: 3, typography: "body2", color: "text.secondary" }}>
            LUB
          </Divider>
          <Box display="flex" justifyContent="center">
            <GoogleLogin
              onSuccess={handleGoogleSuccess}
              onError={() => setError("Błąd inicjalizacji okna Google")}
            />
          </Box>
        </CardContent>
        <LoginFooter />
      </Card>
    </Box>
  );
};
