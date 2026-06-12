import { useState } from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Container,
  Divider,
  List,
  ListItem,
  ListItemText,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import api from "../../api/axiosConfig";

interface ParcelHistoryItem {
  id: string;
  trackingNumber: string;
  status: string;
  description: string | null;
  createdAt: string;
}

interface ListResponse<T> {
  items: T[];
  totalCount: number;
}

export const TrackingPage = () => {
  const [trackingNumber, setTrackingNumber] = useState("");
  const [history, setHistory] = useState<ParcelHistoryItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searched, setSearched] = useState(false);

  const handleSearch = async (event: React.FormEvent) => {
    event.preventDefault();

    const normalizedTrackingNumber = trackingNumber.trim();
    if (!normalizedTrackingNumber) {
      setError("Podaj numer przesyłki.");
      setHistory([]);
      setSearched(false);
      return;
    }

    setIsLoading(true);
    setError(null);
    setSearched(true);

    try {
      const response = await api.get<ListResponse<ParcelHistoryItem>>(
        `/parcels/${encodeURIComponent(normalizedTrackingNumber)}/history`,
      );
      setHistory(response.data.items);
    } catch (requestError: any) {
      setHistory([]);
      if (requestError.response?.status === 404) {
        setError("Nie znaleziono przesyłki o podanym numerze.");
      } else {
        setError("Nie udało się pobrać historii przesyłki.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        py: 6,
        background:
          "radial-gradient(circle at top, rgba(227, 242, 253, 0.9), rgba(245, 247, 250, 1) 45%, rgba(236, 240, 243, 1))",
      }}
    >
      <Container maxWidth="md">
        <Stack spacing={3} sx={{ mb: 4 }}>
          <Box>
            <Typography variant="overline" color="primary.main">
              Publiczny tracking
            </Typography>
            <Typography variant="h3" fontWeight={800} gutterBottom>
              Sprawdź status przesyłki
            </Typography>
            <Typography color="text.secondary">
              Wpisz numer listu przewozowego, aby zobaczyć historię zmian
              statusu.
            </Typography>
          </Box>

          <Card elevation={4} sx={{ borderRadius: 4 }}>
            <CardContent sx={{ p: 3 }}>
              <Box component="form" onSubmit={handleSearch}>
                <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
                  <TextField
                    fullWidth
                    label="Numer przesyłki"
                    value={trackingNumber}
                    onChange={(event) => setTrackingNumber(event.target.value)}
                    placeholder="np. 550e8400-e29b-41d4-a716-446655440000"
                  />
                  <Button
                    type="submit"
                    variant="contained"
                    size="large"
                    disabled={isLoading}
                    sx={{ minWidth: 180 }}
                  >
                    {isLoading ? "Szukam..." : "Sprawdź"}
                  </Button>
                </Stack>
              </Box>
            </CardContent>
          </Card>
        </Stack>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        {searched && !error && history.length === 0 && !isLoading && (
          <Alert severity="info">Brak historii dla tej przesyłki.</Alert>
        )}

        {history.length > 0 && (
          <Paper elevation={3} sx={{ borderRadius: 4, overflow: "hidden" }}>
            <Box sx={{ p: 3, bgcolor: "primary.main", color: "primary.contrastText" }}>
              <Typography variant="h6" fontWeight={700}>
                Historia zmian statusu
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.9 }}>
                {trackingNumber.trim()}
              </Typography>
            </Box>
            <Divider />
            <List disablePadding>
              {history.map((item, index) => (
                <ListItem
                  key={item.id}
                  divider={index < history.length - 1}
                  sx={{ alignItems: "flex-start", py: 2.5 }}
                >
                  <ListItemText
                    primary={item.status}
                    secondary={
                      <Box component="span">
                        <Box component="span" sx={{ display: "block", mb: 0.5 }}>
                          {item.description || "Brak dodatkowego opisu"}
                        </Box>
                        <Box component="span" sx={{ display: "block", color: "text.secondary" }}>
                          {new Date(item.createdAt).toLocaleString("pl-PL")}
                        </Box>
                      </Box>
                    }
                    primaryTypographyProps={{ fontWeight: 700 }}
                  />
                </ListItem>
              ))}
            </List>
          </Paper>
        )}
      </Container>
    </Box>
  );
};