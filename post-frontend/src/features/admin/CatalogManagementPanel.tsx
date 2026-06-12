import { useEffect, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  IconButton,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import {
  Add as AddIcon,
  Archive as ArchiveIcon,
  Edit as EditIcon,
  Inventory2 as ParcelTypeIcon,
  LocalShipping as ServiceIcon,
} from "@mui/icons-material";
import api from "../../api/axiosConfig";

interface ListResponse<T> {
  items: T[];
  totalCount: number;
}

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

interface ParcelTypeFormState {
  maxWeight: string;
  maxWidth: string;
  maxHeight: string;
  maxLength: string;
  price: string;
  description: string;
}

interface AdditionalServiceFormState {
  name: string;
  price: string;
}

const emptyParcelTypeForm: ParcelTypeFormState = {
  maxWeight: "",
  maxWidth: "",
  maxHeight: "",
  maxLength: "",
  price: "",
  description: "",
};

const emptyAdditionalServiceForm: AdditionalServiceFormState = {
  name: "",
  price: "",
};

export function CatalogManagementPanel() {
  const [parcelTypes, setParcelTypes] = useState<ParcelTypeDetails[]>([]);
  const [additionalServices, setAdditionalServices] = useState<AdditionalServiceDetails[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [isParcelTypeDialogOpen, setIsParcelTypeDialogOpen] = useState(false);
  const [editingParcelTypeId, setEditingParcelTypeId] = useState<number | null>(null);
  const [parcelTypeForm, setParcelTypeForm] = useState<ParcelTypeFormState>(emptyParcelTypeForm);
  const [isParcelTypeSaving, setIsParcelTypeSaving] = useState(false);
  const [parcelTypeDialogError, setParcelTypeDialogError] = useState<string | null>(null);

  const [isServiceDialogOpen, setIsServiceDialogOpen] = useState(false);
  const [editingServiceId, setEditingServiceId] = useState<number | null>(null);
  const [serviceForm, setServiceForm] = useState<AdditionalServiceFormState>(emptyAdditionalServiceForm);
  const [isServiceSaving, setIsServiceSaving] = useState(false);
  const [serviceDialogError, setServiceDialogError] = useState<string | null>(null);

  useEffect(() => {
    fetchCatalog();
  }, []);

  const fetchCatalog = async () => {
    setLoading(true);
    setError(null);

    try {
      const [typesResponse, servicesResponse] = await Promise.all([
        api.get<ListResponse<ParcelTypeDetails>>("/admin/parcels/types"),
        api.get<ListResponse<AdditionalServiceDetails>>("/admin/parcels/additional-services"),
      ]);

      setParcelTypes(typesResponse.data.items);
      setAdditionalServices(servicesResponse.data.items);
    } catch (requestError) {
      setError("Nie udało się pobrać katalogu cennika i usług.");
      console.error(requestError);
    } finally {
      setLoading(false);
    }
  };

  const openCreateParcelTypeDialog = () => {
    setEditingParcelTypeId(null);
    setParcelTypeForm(emptyParcelTypeForm);
    setParcelTypeDialogError(null);
    setIsParcelTypeDialogOpen(true);
  };

  const openEditParcelTypeDialog = (parcelType: ParcelTypeDetails) => {
    setEditingParcelTypeId(parcelType.id);
    setParcelTypeForm({
      maxWeight: String(parcelType.maxWeight),
      maxWidth: String(parcelType.maxWidth),
      maxHeight: String(parcelType.maxHeight),
      maxLength: String(parcelType.maxLength),
      price: String(parcelType.price),
      description: parcelType.description,
    });
    setParcelTypeDialogError(null);
    setIsParcelTypeDialogOpen(true);
  };

  const closeParcelTypeDialog = () => {
    setIsParcelTypeDialogOpen(false);
    setEditingParcelTypeId(null);
    setParcelTypeDialogError(null);
  };

  const openCreateServiceDialog = () => {
    setEditingServiceId(null);
    setServiceForm(emptyAdditionalServiceForm);
    setServiceDialogError(null);
    setIsServiceDialogOpen(true);
  };

  const openEditServiceDialog = (service: AdditionalServiceDetails) => {
    setEditingServiceId(service.id);
    setServiceForm({
      name: service.name,
      price: String(service.price),
    });
    setServiceDialogError(null);
    setIsServiceDialogOpen(true);
  };

  const closeServiceDialog = () => {
    setIsServiceDialogOpen(false);
    setEditingServiceId(null);
    setServiceDialogError(null);
  };

  const handleParcelTypeFormChange = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const { name, value } = event.target;
    setParcelTypeForm((previous) => ({
      ...previous,
      [name]: value,
    }));
  };

  const handleServiceFormChange = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const { name, value } = event.target;
    setServiceForm((previous) => ({
      ...previous,
      [name]: value,
    }));
  };

  const saveParcelType = async () => {
    const payload = {
      maxWeight: Number(parcelTypeForm.maxWeight),
      maxWidth: Number(parcelTypeForm.maxWidth),
      maxHeight: Number(parcelTypeForm.maxHeight),
      maxLength: Number(parcelTypeForm.maxLength),
      price: Number(parcelTypeForm.price),
      description: parcelTypeForm.description,
    };

    setIsParcelTypeSaving(true);
    setParcelTypeDialogError(null);

    try {
      if (editingParcelTypeId === null) {
        await api.post("/admin/parcels/types", payload);
      } else {
        await api.patch(`/admin/parcels/types/${editingParcelTypeId}`, payload);
      }

      closeParcelTypeDialog();
      await fetchCatalog();
    } catch (requestError) {
      setParcelTypeDialogError("Nie udało się zapisać typu przesyłki.");
      console.error(requestError);
    } finally {
      setIsParcelTypeSaving(false);
    }
  };

  const saveAdditionalService = async () => {
    const payload = {
      name: serviceForm.name,
      price: Number(serviceForm.price),
    };

    setIsServiceSaving(true);
    setServiceDialogError(null);

    try {
      if (editingServiceId === null) {
        await api.post("/admin/parcels/additional-services", payload);
      } else {
        await api.patch(`/admin/parcels/additional-services/${editingServiceId}`, payload);
      }

      closeServiceDialog();
      await fetchCatalog();
    } catch (requestError) {
      setServiceDialogError("Nie udało się zapisać usługi dodatkowej.");
      console.error(requestError);
    } finally {
      setIsServiceSaving(false);
    }
  };

  const archiveParcelType = async (typeId: number) => {
    try {
      await api.post(`/admin/parcels/types/${typeId}/archive`);
      await fetchCatalog();
    } catch (requestError) {
      setError("Nie udało się zarchiwizować typu przesyłki.");
      console.error(requestError);
    }
  };

  const archiveAdditionalService = async (serviceId: number) => {
    try {
      await api.post(`/admin/parcels/additional-services/${serviceId}/archive`);
      await fetchCatalog();
    } catch (requestError) {
      setError("Nie udało się zarchiwizować usługi dodatkowej.");
      console.error(requestError);
    }
  };

  return (
    <Box>
      <Stack spacing={3}>
        <Box>
          <Typography variant="h5" fontWeight={800} gutterBottom>
            Cennik i usługi dodatkowe
          </Typography>
          <Typography color="text.secondary">
            Zarządzaj typami przesyłek oraz usługami dodatkowymi wykorzystywanymi przy nadaniu.
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {loading && <Alert severity="info">Ładowanie katalogu...</Alert>}

        <Card elevation={3} sx={{ borderRadius: 3 }}>
          <CardContent>
            <Stack
              direction={{ xs: "column", sm: "row" }}
              justifyContent="space-between"
              alignItems={{ xs: "flex-start", sm: "center" }}
              spacing={2}
              sx={{ mb: 2 }}
            >
              <Box>
                <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 0.5 }}>
                  <ParcelTypeIcon color="primary" />
                  <Typography variant="h6" fontWeight={700}>
                    Typy przesyłek
                  </Typography>
                </Stack>
                <Typography variant="body2" color="text.secondary">
                  Dostępne gabaryty, limity i bazowe ceny.
                </Typography>
              </Box>
              <Button variant="contained" startIcon={<AddIcon />} onClick={openCreateParcelTypeDialog}>
                Dodaj typ
              </Button>
            </Stack>

            <TableContainer component={Paper} variant="outlined">
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>ID</TableCell>
                    <TableCell>Opis</TableCell>
                    <TableCell>Waga</TableCell>
                    <TableCell>Wymiary</TableCell>
                    <TableCell>Cena</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell align="right">Akcje</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {parcelTypes.map((parcelType) => (
                    <TableRow key={parcelType.id} hover>
                      <TableCell>{parcelType.id}</TableCell>
                      <TableCell>{parcelType.description}</TableCell>
                      <TableCell>{parcelType.maxWeight} kg</TableCell>
                      <TableCell>
                        {parcelType.maxWidth} x {parcelType.maxHeight} x {parcelType.maxLength} cm
                      </TableCell>
                      <TableCell>{parcelType.price.toFixed(2)} zł</TableCell>
                      <TableCell>
                        <Chip
                          label={parcelType.isAvailable ? "Aktywny" : "Zarchiwizowany"}
                          color={parcelType.isAvailable ? "success" : "default"}
                          size="small"
                        />
                      </TableCell>
                      <TableCell align="right">
                        <IconButton color="primary" onClick={() => openEditParcelTypeDialog(parcelType)}>
                          <EditIcon />
                        </IconButton>
                        <IconButton
                          color="warning"
                          onClick={() => archiveParcelType(parcelType.id)}
                          disabled={!parcelType.isAvailable}
                        >
                          <ArchiveIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>

        <Divider />

        <Card elevation={3} sx={{ borderRadius: 3 }}>
          <CardContent>
            <Stack
              direction={{ xs: "column", sm: "row" }}
              justifyContent="space-between"
              alignItems={{ xs: "flex-start", sm: "center" }}
              spacing={2}
              sx={{ mb: 2 }}
            >
              <Box>
                <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 0.5 }}>
                  <ServiceIcon color="secondary" />
                  <Typography variant="h6" fontWeight={700}>
                    Usługi dodatkowe
                  </Typography>
                </Stack>
                <Typography variant="body2" color="text.secondary">
                  Ubezpieczenia, priorytet i inne rozszerzenia oferty.
                </Typography>
              </Box>
              <Button variant="contained" startIcon={<AddIcon />} onClick={openCreateServiceDialog}>
                Dodaj usługę
              </Button>
            </Stack>

            <TableContainer component={Paper} variant="outlined">
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>ID</TableCell>
                    <TableCell>Nazwa</TableCell>
                    <TableCell>Cena</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell align="right">Akcje</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {additionalServices.map((service) => (
                    <TableRow key={service.id} hover>
                      <TableCell>{service.id}</TableCell>
                      <TableCell>{service.name}</TableCell>
                      <TableCell>{service.price.toFixed(2)} zł</TableCell>
                      <TableCell>
                        <Chip
                          label={service.isAvailable ? "Aktywna" : "Zarchiwizowana"}
                          color={service.isAvailable ? "success" : "default"}
                          size="small"
                        />
                      </TableCell>
                      <TableCell align="right">
                        <IconButton color="primary" onClick={() => openEditServiceDialog(service)}>
                          <EditIcon />
                        </IconButton>
                        <IconButton
                          color="warning"
                          onClick={() => archiveAdditionalService(service.id)}
                          disabled={!service.isAvailable}
                        >
                          <ArchiveIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      </Stack>

      <Dialog open={isParcelTypeDialogOpen} onClose={closeParcelTypeDialog} fullWidth maxWidth="sm">
        <DialogTitle>
          {editingParcelTypeId === null ? "Dodaj typ przesyłki" : "Edytuj typ przesyłki"}
        </DialogTitle>
        <DialogContent dividers>
          <Stack spacing={2} sx={{ pt: 1 }}>
            {parcelTypeDialogError && <Alert severity="error">{parcelTypeDialogError}</Alert>}
            <TextField
              fullWidth
              label="Opis"
              name="description"
              value={parcelTypeForm.description}
              onChange={handleParcelTypeFormChange}
            />
            <TextField
              fullWidth
              label="Maksymalna waga (kg)"
              name="maxWeight"
              type="number"
              inputProps={{ step: "0.01", min: 0 }}
              value={parcelTypeForm.maxWeight}
              onChange={handleParcelTypeFormChange}
            />
            <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
              <TextField
                fullWidth
                label="Maks. szerokość (cm)"
                name="maxWidth"
                type="number"
                inputProps={{ step: 1, min: 0 }}
                value={parcelTypeForm.maxWidth}
                onChange={handleParcelTypeFormChange}
              />
              <TextField
                fullWidth
                label="Maks. wysokość (cm)"
                name="maxHeight"
                type="number"
                inputProps={{ step: 1, min: 0 }}
                value={parcelTypeForm.maxHeight}
                onChange={handleParcelTypeFormChange}
              />
              <TextField
                fullWidth
                label="Maks. długość (cm)"
                name="maxLength"
                type="number"
                inputProps={{ step: 1, min: 0 }}
                value={parcelTypeForm.maxLength}
                onChange={handleParcelTypeFormChange}
              />
            </Stack>
            <TextField
              fullWidth
              label="Cena bazowa (zł)"
              name="price"
              type="number"
              inputProps={{ step: "0.01", min: 0 }}
              value={parcelTypeForm.price}
              onChange={handleParcelTypeFormChange}
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeParcelTypeDialog}>Anuluj</Button>
          <Button variant="contained" onClick={saveParcelType} disabled={isParcelTypeSaving}>
            {isParcelTypeSaving ? "Zapisywanie..." : "Zapisz"}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={isServiceDialogOpen} onClose={closeServiceDialog} fullWidth maxWidth="sm">
        <DialogTitle>
          {editingServiceId === null ? "Dodaj usługę dodatkową" : "Edytuj usługę dodatkową"}
        </DialogTitle>
        <DialogContent dividers>
          <Stack spacing={2} sx={{ pt: 1 }}>
            {serviceDialogError && <Alert severity="error">{serviceDialogError}</Alert>}
            <TextField
              fullWidth
              label="Nazwa"
              name="name"
              value={serviceForm.name}
              onChange={handleServiceFormChange}
            />
            <TextField
              fullWidth
              label="Cena (zł)"
              name="price"
              type="number"
              inputProps={{ step: "0.01", min: 0 }}
              value={serviceForm.price}
              onChange={handleServiceFormChange}
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeServiceDialog}>Anuluj</Button>
          <Button variant="contained" onClick={saveAdditionalService} disabled={isServiceSaving}>
            {isServiceSaving ? "Zapisywanie..." : "Zapisz"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}