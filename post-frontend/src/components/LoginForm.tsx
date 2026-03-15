import React from "react";
import {
  TextField,
  Button,
  CircularProgress,
  InputAdornment,
  Box,
  Link,
  FormControlLabel,
  Checkbox,
  Typography,
} from "@mui/material";
import { MailOutline, LockOutlined } from "@mui/icons-material";

interface LoginFormProps {
  email: string;
  password: string;
  isLoading: boolean;
  onEmailChange: (email: string) => void;
  onPasswordChange: (password: string) => void;
  onSubmit: (e: React.SubmitEvent<HTMLFormElement>) => void;
}

export const LoginForm = ({
  email,
  password,
  isLoading,
  onEmailChange,
  onPasswordChange,
  onSubmit,
}: LoginFormProps) => {
  return (
    <form onSubmit={onSubmit}>
      <TextField
        fullWidth
        label="Adres e-mail"
        variant="outlined"
        type="email"
        placeholder="Wprowadź adres e-mail"
        required
        value={email}
        onChange={(e) => onEmailChange(e.target.value)}
        disabled={isLoading}
        sx={{ mb: 2 }}
        slotProps={{
          input: {
            startAdornment: (
              <InputAdornment position="start">
                <MailOutline color="action" />
              </InputAdornment>
            ),
          },
        }}
      />
      <TextField
        fullWidth
        label="Hasło"
        variant="outlined"
        type="password"
        placeholder="Wprowadź hasło"
        required
        value={password}
        onChange={(e) => onPasswordChange(e.target.value)}
        disabled={isLoading}
        sx={{ mb: 3 }}
        slotProps={{
          input: {
            startAdornment: (
              <InputAdornment position="start">
                <LockOutlined color="action" />
              </InputAdornment>
            ),
          },
        }}
      />
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 3,
        }}
      >
        <FormControlLabel
          control={<Checkbox size="small" color="primary" />}
          label={
            <Typography variant="body2" color="text.secondary">
              Zapamiętaj mnie
            </Typography>
          }
          sx={{ ml: 0 }}
        />
        <Link href="#" variant="body2" underline="hover" fontWeight="medium">
          Zapomniałeś hasła?
        </Link>
      </Box>

      <Button
        fullWidth
        type="submit"
        variant="contained"
        size="large"
        disabled={isLoading}
        sx={{ py: 1.5, fontWeight: "bold" }}
      >
        {isLoading ? (
          <CircularProgress size={24} color="inherit" />
        ) : (
          "Zaloguj się"
        )}
      </Button>
    </form>
  );
};
