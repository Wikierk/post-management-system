import { Box, Button } from "@mui/material";
import { Google } from "@mui/icons-material";

interface OAuthButtonsProps {
  isLoading: boolean;
  onOAuthLogin: (provider: "google") => void;
}

export const OAuthButtons = ({
  isLoading,
  onOAuthLogin,
}: OAuthButtonsProps) => {
  return (
    <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
      <Button
        fullWidth
        variant="outlined"
        startIcon={<Google sx={{ color: "#DB4437" }} />}
        onClick={() => onOAuthLogin("google")}
        disabled={isLoading}
        sx={{
          color: "text.primary",
          borderColor: "grey.300",
          "&:hover": { bgcolor: "grey.50" },
        }}
      >
        Zaloguj się przez Google
      </Button>
    </Box>
  );
};
