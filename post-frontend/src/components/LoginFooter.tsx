import { Box, Typography, Link } from "@mui/material";

export const LoginFooter = () => {
  return (
    <Box
      sx={{
        bgcolor: "grey.50",
        p: 2,
        textAlign: "center",
        borderTop: 1,
        borderColor: "grey.200",
      }}
    >
      <Typography variant="body2" color="text.secondary">
        Nie masz jeszcze konta?{" "}
        <Link href="/register" underline="hover" fontWeight="medium">
          Zarejestruj się
        </Link>
      </Typography>
    </Box>
  );
};
