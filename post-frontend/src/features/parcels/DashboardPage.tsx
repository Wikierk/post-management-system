import { Box, Typography, Button, Paper, Container } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export const DashboardPage = () => {
  const { logout, user } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        bgcolor: "grey.100",
        py: 8,
      }}
    >
      <Container maxWidth="md">
        <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mb: 4,
            }}
          >
            <Typography
              variant="h4"
              component="h1"
              fontWeight="bold"
              color="primary"
            >
              Twój Panel (Dashboard)
            </Typography>
            <Button variant="outlined" color="primary" onClick={handleLogout}>
              Wyloguj się
            </Button>
          </Box>

          <Box sx={{ mb: 4, p: 3, bgcolor: "grey.100", borderRadius: 2 }}>
            <Typography
              variant="h6"
              sx={{ color: "primary.main" }}
              gutterBottom
            >
              Sukces! Jesteś zalogowany.
            </Typography>
          </Box>

          {user && (
            <Box sx={{ mt: 3 }}>
              <Typography variant="subtitle1" fontWeight="bold">
                Dane użytkownika:
              </Typography>
              <pre
                style={{
                  background: "#f5f5f5",
                  padding: "10px",
                  borderRadius: "4px",
                  color: "#333",
                  fontSize: "14px",
                }}
              >
                {JSON.stringify(user, null, 2)}
              </pre>
            </Box>
          )}
        </Paper>
      </Container>
    </Box>
  );
};
