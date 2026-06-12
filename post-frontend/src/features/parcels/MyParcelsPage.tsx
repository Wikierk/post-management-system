import { useEffect, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Container,
  Dialog,
  DialogContent,
  DialogTitle,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import api from "../../api/axiosConfig";

interface ParcelSummary {
  trackingNumber: string;
  status: string;
  totalPrice: number | null;
  cashOnDelivery: number | null;
}

interface ParcelHistoryItem {
  id: string;
  status: string;
  description: string | null;
  createdAt: string;
}

export const MyParcelsPage = () => {
  const [parcels, setParcels] = useState<ParcelSummary[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [historyOpen, setHistoryOpen] = useState(false);
  const [historyItems, setHistoryItems] = useState<ParcelHistoryItem[]>([]);
  const [selectedTracking, setSelectedTracking] = useState<string | null>(null);

  useEffect(() => {
    const fetch = async () => {
      setLoading(true);
      try {
        const resp = await api.get<{ items: ParcelSummary[] }>("/parcels/my");
        setParcels(resp.data.items ?? []);
      } catch (err) {
        console.error(err);
        setError("Nie udało się pobrać listy przesyłek.");
      } finally {
        setLoading(false);
      }
    };
    fetch();
  }, []);

  const openHistory = async (tracking: string) => {
    setSelectedTracking(tracking);
    setHistoryOpen(true);
    try {
      const resp = await api.get<{ items: ParcelHistoryItem[] }>(`/parcels/${encodeURIComponent(tracking)}/history`);
      setHistoryItems(resp.data.items ?? []);
    } catch (err) {
      console.error(err);
      setHistoryItems([]);
    }
  };

  return (
    <Box sx={{ minHeight: "100vh", py: 6 }}>
      <Container maxWidth="lg">
        <Stack spacing={3} sx={{ mb: 4 }}>
          <Box>
            <Typography variant="overline" color="primary.main">Twoje przesyłki</Typography>
            <Typography variant="h4" fontWeight={800}>Lista przesyłek</Typography>
          </Box>

          {error && <Alert severity="error">{error}</Alert>}

          <Paper>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Numer</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Cena</TableCell>
                    <TableCell>Akcje</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {parcels.map((p) => (
                    <TableRow key={p.trackingNumber} hover>
                      <TableCell>{p.trackingNumber}</TableCell>
                      <TableCell>{p.status}</TableCell>
                      <TableCell>{p.totalPrice != null ? `${p.totalPrice.toFixed(2)} zł` : "-"}</TableCell>
                      <TableCell>
                        <Button onClick={() => openHistory(p.trackingNumber)}>Historia</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Stack>

        <Dialog open={historyOpen} onClose={() => setHistoryOpen(false)} fullWidth maxWidth="md">
          <DialogTitle>Historia przesyłki {selectedTracking}</DialogTitle>
          <DialogContent>
            {historyItems.length === 0 && <Typography>Brak historii dla tej przesyłki.</Typography>}
            {historyItems.map((h) => (
              <Box key={h.id} sx={{ mb: 2 }}>
                <Typography fontWeight={700}>{h.status}</Typography>
                <Typography>{h.description ?? "Brak opisu"}</Typography>
                <Typography color="text.secondary">{new Date(h.createdAt).toLocaleString("pl-PL")}</Typography>
                <hr />
              </Box>
            ))}
          </DialogContent>
        </Dialog>
      </Container>
    </Box>
  );
};
