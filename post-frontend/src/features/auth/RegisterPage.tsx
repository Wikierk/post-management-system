import React, { useState } from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Alert,
  Link,
  Grid,
} from "@mui/material";
import api from "../../api/axiosConfig";

export const RegisterPage = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    setSuccess(false);

    try {
      await api.post("/auth/v1/register", formData);

      setSuccess(true);
      setTimeout(() => {
        navigate("/login", { replace: true });
      }, 2000);
    } catch (err: any) {
      console.error("Błąd rejestracji:", err);
      if (err.response && err.response.status === 400) {
        setError(
          "Podano nieprawidłowe dane lub użytkownik o takim adresie email już istnieje.",
        );
      } else {
        setError("Wystąpił problem z połączeniem. Spróbuj ponownie później.");
      }
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
        sx={{ maxWidth: 500, width: "100%", borderRadius: 3, boxShadow: 3 }}
      >
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ textAlign: "center", mb: 3 }}>
            <Typography variant="h5" fontWeight="bold" color="primary">
              Załóż nowe konto
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Wypełnij poniższe dane, aby dołączyć.
            </Typography>
          </Box>

          {error && (
            <Alert severity="error" sx={{ mb: 3 }}>
              {error}
            </Alert>
          )}

          {success && (
            <Alert severity="success" sx={{ mb: 3 }}>
              Konto zostało pomyślnie utworzone! Za chwilę nastąpi
              przekierowanie do logowania...
            </Alert>
          )}

          <form onSubmit={handleRegister}>
            <Grid container spacing={2}>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  fullWidth
                  label="Imię"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                  disabled={isLoading || success}
                />
              </Grid>
              <Grid size={{ xs: 12, sm: 6 }}>
                <TextField
                  fullWidth
                  label="Nazwisko"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                  disabled={isLoading || success}
                />
              </Grid>
              <Grid size={{ xs: 12 }}>
                <TextField
                  fullWidth
                  label="Adres e-mail"
                  name="email"
                  type="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  disabled={isLoading || success}
                />
              </Grid>
              <Grid size={{ xs: 12 }}>
                <TextField
                  fullWidth
                  label="Hasło"
                  name="password"
                  type="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  inputProps={{ minLength: 8 }}
                  helperText="Hasło musi mieć co najmniej 8 znaków"
                  disabled={isLoading || success}
                />
              </Grid>
            </Grid>

            <Button
              fullWidth
              type="submit"
              variant="contained"
              size="large"
              disabled={isLoading || success}
              sx={{ mt: 4, mb: 2 }}
            >
              {isLoading ? "Tworzenie konta..." : "Zarejestruj się"}
            </Button>
          </form>

          <Typography variant="body2" align="center" sx={{ mt: 2 }}>
            Masz już konto?{" "}
            <Link component={RouterLink} to="/login" underline="hover">
              Zaloguj się
            </Link>
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
};
