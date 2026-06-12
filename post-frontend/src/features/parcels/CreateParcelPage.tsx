import { useEffect, useMemo, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Checkbox,
  Container,
  Divider,
  FormControl,
  FormControlLabel,
  FormHelperText,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import { useAuth } from "../../context/AuthContext";

interface ParcelTypeDetails {
  id: number;
  maxWeight: number;
  maxWidth: number;
  maxHeight: number;
  maxLength: number;
  price: number;
  description: string;
  isAvailable: boolean;
}

interface AdditionalServiceDetails {
  id: number;
  name: string;
  price: number;
  isAvailable: boolean;
}

interface CreateParcelResponse {
  trackingNumber: string;
  totalPrice: number;
}

interface ParcelSubjectFormState {
  fullName: string;
  street: string;
  city: string;
  zipCode: string;
  email: string;
  phone: string;
}

const emptySubject: ParcelSubjectFormState = {
  fullName: "",
  street: "",
  city: "",
  zipCode: "",
  email: "",
  phone: "",
};

const formatPrice = (value: number) => value.toLocaleString("pl-PL", {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});

export const CreateParcelPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  const [parcelTypes, setParcelTypes] = useState<ParcelTypeDetails[]>([]);
  const [additionalServices, setAdditionalServices] = useState<AdditionalServiceDetails[]>([]);
  const [sender, setSender] = useState<ParcelSubjectFormState>(emptySubject);
  const [recipient, setRecipient] = useState<ParcelSubjectFormState>(emptySubject);
  const [parcelTypeId, setParcelTypeId] = useState("");
  const [cashOnDelivery, setCashOnDelivery] = useState("");
  const [selectedServiceIds, setSelectedServiceIds] = useState<number[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<CreateParcelResponse | null>(null);

  useEffect(() => {
    const fetchCatalog = async () => {
      setIsLoading(true);
      setError(null);

      try {
        const [typesResponse, servicesResponse] = await Promise.all([
          api.get<{ items: ParcelTypeDetails[]; totalCount: number }>("/parcels/types"),
          api.get<{ items: AdditionalServiceDetails[]; totalCount: number }>("/parcels/additional-services"),
        ]);

        setParcelTypes(typesResponse.data.items.filter((item) => item.isAvailable));
        setAdditionalServices(servicesResponse.data.items.filter((item) => item.isAvailable));
      } catch (requestError) {
        setError("Nie udało się pobrać katalogu nadania.");
        console.error(requestError);
      } finally {
        setIsLoading(false);
      }
    };

    fetchCatalog();
  }, []);

  useEffect(() => {
    if (!user) {
      return;
    }

    setSender((previous) => ({
      ...previous,
      fullName: `${user.firstName} ${user.lastName}`.trim(),
      email: user.email,
    }));
  }, [user]);

  const selectedParcelType = useMemo(
    () => parcelTypes.find((item) => String(item.id) === parcelTypeId),
    [parcelTypeId, parcelTypes],
  );

  const selectedServices = useMemo(
    () => additionalServices.filter((item) => selectedServiceIds.includes(item.id)),
    [additionalServices, selectedServiceIds],
  );

  const basePrice = selectedParcelType?.price ?? 0;
  const servicesPrice = selectedServices.reduce((sum, service) => sum + service.price, 0);
  const totalPrice = basePrice + servicesPrice;

  const updateSubject = (
    setter: React.Dispatch<React.SetStateAction<ParcelSubjectFormState>>,
    field: keyof ParcelSubjectFormState,
    value: string,
  ) => {
    setter((previous) => ({
      ...previous,
      [field]: value,
    }));
  };

  const toggleService = (serviceId: number) => {
    setSelectedServiceIds((previous) =>
      previous.includes(serviceId)
        ? previous.filter((item) => item !== serviceId)
        : [...previous, serviceId],
    );
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);
    setSuccess(null);

    if (!parcelTypeId) {
      setError("Wybierz typ przesyłki.");
      return;
    }

    setIsSubmitting(true);

    try {
      const response = await api.post<CreateParcelResponse>("/parcels", {
        sender,
        recipient,
        parcelTypeId: Number(parcelTypeId),
        cashOnDelivery: cashOnDelivery ? Number(cashOnDelivery) : null,
        selectedServiceIds,
      });

      setSuccess(response.data);
    } catch (requestError: any) {
      if (requestError.response?.status === 400) {
        setError("Sprawdź poprawność danych nadania.");
      } else if (requestError.response?.status === 403) {
        setError("Nie masz uprawnień do nadania przesyłki.");
      } else {
        setError("Nie udało się nadać przesyłki.");
      }
      console.error(requestError);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSimulatePayment = async () => {
    if (!success) return;
    setError(null);
    try {
      const checkout = await api.post<{ paymentId: string; checkoutUrl: string }>(
        "/payments/checkout",
        { trackingNumber: success.trackingNumber },
      );

      // Immediately confirm (simulation)
      await api.post(`/payments/${checkout.data.paymentId}/confirm`);

      setSuccess((prev) => prev ? { ...prev, totalPrice: prev.totalPrice } : prev);
      setError(null);
      alert("Płatność zasymulowana i potwierdzona.");
    } catch (err) {
      console.error(err);
      setError("Wystąpił błąd przy symulacji płatności.");
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        py: 6,
        background:
          "radial-gradient(circle at top, rgba(232, 245, 233, 0.9), rgba(245, 247, 250, 1) 45%, rgba(236, 240, 243, 1))",
      }}
    >
      <Container maxWidth="lg">
        <Stack spacing={3} sx={{ mb: 4 }}>
          <Box>
            <Typography variant="overline" color="primary.main">
              Nadanie przesyłki
            </Typography>
            <Typography variant="h3" fontWeight={800} gutterBottom>
              Utwórz nową przesyłkę
            </Typography>
            <Typography color="text.secondary">
              Wypełnij dane nadawcy, odbiorcy oraz wybierz typ i usługi dodatkowe.
            </Typography>
          </Box>

          {error && (
            <Alert severity="error" onClose={() => setError(null)}>
              {error}
            </Alert>
          )}

          {success && (
            <Alert severity="success" action={<Button color="inherit" size="small" onClick={() => navigate("/tracking")}>Śledź</Button>}>
              Przesyłka nadana. Numer: {success.trackingNumber}
            </Alert>
          )}

          {success && (
            <Box sx={{ mt: 2 }}>
              <Button variant="contained" color="secondary" onClick={handleSimulatePayment}>
                Zapłać (symulacja)
              </Button>
            </Box>
          )}

          <Card elevation={4} sx={{ borderRadius: 4 }}>
            <CardContent sx={{ p: 3 }}>
              <Box component="form" onSubmit={handleSubmit}>
                <Stack spacing={3}>
                  <Stack direction={{ xs: "column", md: "row" }} spacing={3}>
                    <Paper variant="outlined" sx={{ p: 3, flex: 1, borderRadius: 3 }}>
                      <Typography variant="h6" fontWeight={700} gutterBottom>
                        Nadawca
                      </Typography>
                      <Stack spacing={2}>
                        <TextField label="Imię i nazwisko" value={sender.fullName} onChange={(event) => updateSubject(setSender, "fullName", event.target.value)} fullWidth required />
                        <TextField label="Ulica i numer" value={sender.street} onChange={(event) => updateSubject(setSender, "street", event.target.value)} fullWidth required />
                        <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
                          <TextField label="Miasto" value={sender.city} onChange={(event) => updateSubject(setSender, "city", event.target.value)} fullWidth required />
                          <TextField label="Kod pocztowy" value={sender.zipCode} onChange={(event) => updateSubject(setSender, "zipCode", event.target.value)} fullWidth required />
                        </Stack>
                        <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
                          <TextField label="E-mail" type="email" value={sender.email} onChange={(event) => updateSubject(setSender, "email", event.target.value)} fullWidth />
                          <TextField label="Telefon" value={sender.phone} onChange={(event) => updateSubject(setSender, "phone", event.target.value)} fullWidth />
                        </Stack>
                      </Stack>
                    </Paper>

                    <Paper variant="outlined" sx={{ p: 3, flex: 1, borderRadius: 3 }}>
                      <Typography variant="h6" fontWeight={700} gutterBottom>
                        Odbiorca
                      </Typography>
                      <Stack spacing={2}>
                        <TextField label="Imię i nazwisko" value={recipient.fullName} onChange={(event) => updateSubject(setRecipient, "fullName", event.target.value)} fullWidth required />
                        <TextField label="Ulica i numer" value={recipient.street} onChange={(event) => updateSubject(setRecipient, "street", event.target.value)} fullWidth required />
                        <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
                          <TextField label="Miasto" value={recipient.city} onChange={(event) => updateSubject(setRecipient, "city", event.target.value)} fullWidth required />
                          <TextField label="Kod pocztowy" value={recipient.zipCode} onChange={(event) => updateSubject(setRecipient, "zipCode", event.target.value)} fullWidth required />
                        </Stack>
                        <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
                          <TextField label="E-mail" type="email" value={recipient.email} onChange={(event) => updateSubject(setRecipient, "email", event.target.value)} fullWidth />
                          <TextField label="Telefon" value={recipient.phone} onChange={(event) => updateSubject(setRecipient, "phone", event.target.value)} fullWidth />
                        </Stack>
                      </Stack>
                    </Paper>
                  </Stack>

                  <Stack direction={{ xs: "column", md: "row" }} spacing={3}>
                    <Paper variant="outlined" sx={{ p: 3, flex: 1, borderRadius: 3 }}>
                      <Typography variant="h6" fontWeight={700} gutterBottom>
                        Parametry przesyłki
                      </Typography>
                      <Stack spacing={2}>
                        <FormControl fullWidth>
                          <InputLabel id="parcel-type-label">Typ przesyłki</InputLabel>
                          <Select
                            labelId="parcel-type-label"
                            label="Typ przesyłki"
                            value={parcelTypeId}
                            onChange={(event) => setParcelTypeId(event.target.value)}
                            disabled={isLoading}
                          >
                            {parcelTypes.map((item) => (
                              <MenuItem key={item.id} value={item.id}>
                                {item.description} — {formatPrice(item.price)} zł
                              </MenuItem>
                            ))}
                          </Select>
                          <FormHelperText>
                            {selectedParcelType
                              ? `Maks. ${selectedParcelType.maxWeight} kg, ${selectedParcelType.maxWidth} × ${selectedParcelType.maxHeight} × ${selectedParcelType.maxLength} cm`
                              : "Wybierz rozmiar i cenę bazową."}
                          </FormHelperText>
                        </FormControl>

                        <TextField
                          label="Pobranie (opcjonalnie)"
                          type="number"
                          value={cashOnDelivery}
                          onChange={(event) => setCashOnDelivery(event.target.value)}
                          inputProps={{ min: 0, step: 0.01 }}
                          fullWidth
                        />

                        <Box>
                          <Typography variant="subtitle1" fontWeight={700} gutterBottom>
                            Usługi dodatkowe
                          </Typography>
                          <Stack spacing={1}>
                            {additionalServices.length === 0 && (
                              <Typography color="text.secondary">Brak dostępnych usług.</Typography>
                            )}
                            {additionalServices.map((service) => (
                              <FormControlLabel
                                key={service.id}
                                control={
                                  <Checkbox
                                    checked={selectedServiceIds.includes(service.id)}
                                    onChange={() => toggleService(service.id)}
                                  />
                                }
                                label={`${service.name} (+${formatPrice(service.price)} zł)`}
                              />
                            ))}
                          </Stack>
                        </Box>
                      </Stack>
                    </Paper>

                    <Paper variant="outlined" sx={{ p: 3, width: { xs: "100%", md: 320 }, borderRadius: 3 }}>
                      <Typography variant="h6" fontWeight={700} gutterBottom>
                        Podsumowanie
                      </Typography>
                      <Stack spacing={1.5} sx={{ mb: 2 }}>
                        <Typography variant="body2" color="text.secondary">
                          Cena bazowa: {formatPrice(basePrice)} zł
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          Usługi dodatkowe: {formatPrice(servicesPrice)} zł
                        </Typography>
                        <Divider />
                        <Typography variant="h5" fontWeight={800}>
                          {formatPrice(totalPrice)} zł
                        </Typography>
                      </Stack>
                      <Button
                        type="submit"
                        variant="contained"
                        size="large"
                        fullWidth
                        disabled={isSubmitting || isLoading}
                      >
                        {isSubmitting ? "Nadaję..." : "Nadaj przesyłkę"}
                      </Button>
                    </Paper>
                  </Stack>
                </Stack>
              </Box>
            </CardContent>
          </Card>
        </Stack>
      </Container>
    </Box>
  );
};