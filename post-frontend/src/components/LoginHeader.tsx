import { Box, Typography } from "@mui/material";
import { LocalShipping } from "@mui/icons-material";

export const LoginHeader = () => {
  return (
    <>
      <Box
        sx={{
          bgcolor: "primary.main",
          color: "primary.contrastText",
          p: 4,
          textAlign: "center",
        }}
      >
        <Box sx={{ mb: 2, display: "flex", justifyContent: "center" }}>
          <Box
            sx={{
              bgcolor: "rgba(255, 255, 255, 0.15)",
              borderRadius: "50%",
              p: 2,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <LocalShipping sx={{ fontSize: 48, color: "white" }} />
          </Box>
        </Box>
        <Typography variant="h5" component="h1" fontWeight="bold">
          Poczta System
        </Typography>
        <Typography variant="body2" sx={{ opacity: 0.8, mt: 0.5 }}>
          Zarządzanie przesyłkami i korespondencją
        </Typography>
      </Box>
    </>
  );
};
