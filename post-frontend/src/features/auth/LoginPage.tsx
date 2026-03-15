import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Box, Card, CardContent, Alert } from "@mui/material";
import { useAuth } from "../../context/AuthContext";
import { LoginHeader } from "../../components/LoginHeader";
import { LoginForm } from "../../components/LoginForm";
import { OAuthButtons } from "../../components/OAuthButtons";
import { LoginFooter } from "../../components/LoginFooter";
import { Divider } from "@mui/material";
import api from "../../api/axiosConfig";

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
      const response = await api.post("/auth/login", { email, password });

      const { token } = response.data;

      if (!token) {
        throw new Error("Brak tokenu w odpowiedzi serwera");
      }

      await login(token);

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

  const handleOAuthLogin = (provider: "google") => {
    const backendUrl =
      import.meta.env.VITE_API_URL || "http://localhost:8080/api";
    window.location.href = `${backendUrl}/oauth2/authorization/${provider}`;
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
              {error || "Wystąpił problem podczas logowania (OAuth)."}
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

          <OAuthButtons isLoading={isLoading} onOAuthLogin={handleOAuthLogin} />
        </CardContent>

        <LoginFooter />
      </Card>
    </Box>
  );
};
