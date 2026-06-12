import React, { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Tabs,
  Tab,
  Paper,
  Container,
  Alert,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  Stack,
  Button,
} from "@mui/material";
import api from "../../api/axiosConfig";
import CustomTabPanel from "../../components/CustomTabPanel";
import UsersTable from "../../components/UsersTable";
import BranchesTable from "../../components/BranchesTable";
import { CatalogManagementPanel } from "./CatalogManagementPanel";
import type { AdminUser } from "../../types/AdminUser";
import type { Branch } from "../../types/Branch";

interface BranchFormState {
  type: "POST_OFFICE" | "WAREHOUSE";
  city: string;
  street: string;
  number: string;
  zipCode: string;
}

const emptyBranchForm: BranchFormState = {
  type: "POST_OFFICE",
  city: "",
  street: "",
  number: "",
  zipCode: "",
};

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export function AdminDashboardPage() {
  const [tabValue, setTabValue] = useState(0);
  const [users, setUsers] = useState<AdminUser[]>([]);
  const [branches, setBranches] = useState<Branch[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [branchDialogError, setBranchDialogError] = useState<string | null>(
    null,
  );
  const [isBranchDialogOpen, setIsBranchDialogOpen] = useState(false);
  const [isEditingBranch, setIsEditingBranch] = useState(false);
  const [selectedBranchId, setSelectedBranchId] = useState<string | null>(null);
  const [branchForm, setBranchForm] = useState<BranchFormState>(emptyBranchForm);
  const [isBranchSaving, setIsBranchSaving] = useState(false);

  const [userPage, setUserPage] = useState(0);
  const [userPageSize, setUserPageSize] = useState(10);
  const [totalUsers, setTotalUsers] = useState(0);

  const [branchPage, setBranchPage] = useState(0);
  const [branchPageSize, setBranchPageSize] = useState(10);
  const [totalBranches, setTotalBranches] = useState(0);

  useEffect(() => {
    fetchUsers();
  }, [userPage, userPageSize]);

  useEffect(() => {
    fetchBranches();
  }, [branchPage, branchPageSize]);

  const fetchUsers = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get<PageResponse<AdminUser>>(
        `/users/v3/admin/users?page=${userPage}&size=${userPageSize}`,
      );
      setUsers(res.data.content);
      setTotalUsers(res.data.totalElements);
    } catch (err) {
      setError("Błąd podczas ładowania użytkowników.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchBranches = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get<PageResponse<Branch>>(
        `/users/v3/admin/branches?page=${branchPage}&size=${branchPageSize}`,
      );
      setBranches(res.data.content);
      setTotalBranches(res.data.totalElements);
    } catch (err) {
      setError("Błąd podczas ładowania placówek.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const openCreateBranchDialog = () => {
    setIsEditingBranch(false);
    setSelectedBranchId(null);
    setBranchForm(emptyBranchForm);
    setBranchDialogError(null);
    setIsBranchDialogOpen(true);
  };

  const openEditBranchDialog = (branch: Branch) => {
    setIsEditingBranch(true);
    setSelectedBranchId(branch.id);
    setBranchForm({
      type: branch.type,
      city: branch.address.city,
      street: branch.address.street,
      number: branch.address.number,
      zipCode: branch.address.zipCode,
    });
    setBranchDialogError(null);
    setIsBranchDialogOpen(true);
  };

  const closeBranchDialog = () => {
    setIsBranchDialogOpen(false);
    setIsEditingBranch(false);
    setSelectedBranchId(null);
    setBranchDialogError(null);
  };

  const handleBranchFormChange = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const { name, value } = event.target;
    setBranchForm((previous) => ({
      ...previous,
      [name]: value,
    }));
  };

  const createBranch = async () => {
    setIsBranchSaving(true);
    setBranchDialogError(null);

    try {
      await api.post("/users/v3/admin/branches", {
        type: branchForm.type,
        address: {
          city: branchForm.city,
          street: branchForm.street,
          number: branchForm.number,
          zipCode: branchForm.zipCode,
        },
      });
      closeBranchDialog();
      await fetchBranches();
    } catch (requestError) {
      setBranchDialogError("Nie udało się dodać placówki.");
      console.error(requestError);
    } finally {
      setIsBranchSaving(false);
    }
  };

  const updateBranchAddress = async () => {
    if (!selectedBranchId) {
      return;
    }

    setIsBranchSaving(true);
    setBranchDialogError(null);

    try {
      await api.put(`/users/v3/admin/branches/${selectedBranchId}/address`, {
        city: branchForm.city,
        street: branchForm.street,
        number: branchForm.number,
        zipCode: branchForm.zipCode,
      });
      closeBranchDialog();
      await fetchBranches();
    } catch (requestError) {
      setBranchDialogError("Nie udało się zaktualizować adresu placówki.");
      console.error(requestError);
    } finally {
      setIsBranchSaving(false);
    }
  };

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleUserPageChange = (_event: unknown, newPage: number) => {
    setUserPage(newPage);
  };

  const handleUserPageSizeChange = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setUserPageSize(parseInt(event.target.value, 10));
    setUserPage(0);
  };

  const handleBranchPageChange = (_event: unknown, newPage: number) => {
    setBranchPage(newPage);
  };

  const handleBranchPageSizeChange = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setBranchPageSize(parseInt(event.target.value, 10));
    setBranchPage(0);
  };

  const toggleUserStatus = async (userId: string, currentStatus: string) => {
    const newStatus = currentStatus === "ACTIVE" ? "LOCKED" : "ACTIVE";
    try {
      await api.patch(`/users/v3/admin/users/${userId}/status`, {
        status: newStatus,
      });
      setUsers(
        users.map((u) => (u.id === userId ? { ...u, status: newStatus } : u)),
      );
    } catch (err) {
      setError("Błąd podczas zmiany statusu użytkownika.");
      console.error(err);
    }
  };

  const toggleBranchStatus = async (branchId: string, isActive: boolean) => {
    try {
      await api.patch(
        `/users/v3/admin/branches/${branchId}/${isActive ? "deactivate" : "activate"}`,
      );
      await fetchBranches();
    } catch (err) {
      setError("Błąd podczas zmiany statusu placówki.");
      console.error(err);
    }
  };

  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "grey.100", py: 4 }}>
      <Container maxWidth="lg">
        <Typography
          variant="h4"
          fontWeight="bold"
          gutterBottom
          color="primary.main"
        >
          Panel Administratora
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
          Zarządzaj użytkownikami systemu oraz siecią placówek pocztowych.
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {loading && (
          <Box display="flex" justifyContent="center" py={4}>
            <CircularProgress />
          </Box>
        )}

        <Paper
          sx={{
            width: "100%",
            borderRadius: 2,
            overflow: "hidden",
            boxShadow: 3,
          }}
        >
          <Box
            sx={{ borderBottom: 1, borderColor: "divider", bgcolor: "white" }}
          >
            <Tabs value={tabValue} onChange={handleTabChange} centered>
              <Tab label="Użytkownicy" />
              <Tab label="Placówki (Oddziały/Sortownie)" />
              <Tab label="Cennik i usługi" />
            </Tabs>
          </Box>

          <Box sx={{ p: 3, bgcolor: "white" }}>
            <CustomTabPanel value={tabValue} index={0}>
              <UsersTable
                users={users}
                totalUsers={totalUsers}
                page={userPage}
                pageSize={userPageSize}
                onPageChange={handleUserPageChange}
                onPageSizeChange={handleUserPageSizeChange}
                onToggleStatus={toggleUserStatus}
              />
            </CustomTabPanel>

            <CustomTabPanel value={tabValue} index={1}>
              <BranchesTable
                branches={branches}
                totalBranches={totalBranches}
                page={branchPage}
                pageSize={branchPageSize}
                onPageChange={handleBranchPageChange}
                onPageSizeChange={handleBranchPageSizeChange}
                onToggleStatus={toggleBranchStatus}
                onCreateBranch={openCreateBranchDialog}
                onEditBranch={openEditBranchDialog}
              />
            </CustomTabPanel>

            <CustomTabPanel value={tabValue} index={2}>
              <CatalogManagementPanel />
            </CustomTabPanel>
          </Box>
        </Paper>

        <Dialog open={isBranchDialogOpen} onClose={closeBranchDialog} fullWidth maxWidth="sm">
          <DialogTitle>
            {isEditingBranch ? "Edytuj placówkę" : "Dodaj placówkę"}
          </DialogTitle>
          <DialogContent dividers>
            <Stack spacing={2} sx={{ pt: 1 }}>
              {branchDialogError && <Alert severity="error">{branchDialogError}</Alert>}
              {!isEditingBranch && (
                <TextField
                  select
                  fullWidth
                  label="Typ placówki"
                  name="type"
                  value={branchForm.type}
                  onChange={handleBranchFormChange}
                >
                  <MenuItem value="POST_OFFICE">Oddział Poczty</MenuItem>
                  <MenuItem value="WAREHOUSE">Sortownia</MenuItem>
                </TextField>
              )}
              <TextField
                fullWidth
                label="Miasto"
                name="city"
                value={branchForm.city}
                onChange={handleBranchFormChange}
                required
              />
              <TextField
                fullWidth
                label="Ulica"
                name="street"
                value={branchForm.street}
                onChange={handleBranchFormChange}
                required
              />
              <TextField
                fullWidth
                label="Numer budynku"
                name="number"
                value={branchForm.number}
                onChange={handleBranchFormChange}
                required
              />
              <TextField
                fullWidth
                label="Kod pocztowy"
                name="zipCode"
                value={branchForm.zipCode}
                onChange={handleBranchFormChange}
                required
              />
            </Stack>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeBranchDialog}>Anuluj</Button>
            <Button
              variant="contained"
              onClick={isEditingBranch ? updateBranchAddress : createBranch}
              disabled={isBranchSaving}
            >
              {isBranchSaving ? "Zapisywanie..." : "Zapisz"}
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </Box>
  );
}
