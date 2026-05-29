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
} from "@mui/material";
import api from "../../api/axiosConfig";
import CustomTabPanel from "../../components/CustomTabPanel";
import UsersTable from "../../components/UsersTable";
import BranchesTable from "../../components/BranchesTable";
import type { AdminUser } from "../../types/AdminUser";
import type { Branch } from "../../types/Branch";

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

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
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
      await api.patch(`/users/v3/admin/branches/${branchId}/deactivate`);
      setBranches(
        branches.map((b) =>
          b.id === branchId ? { ...b, isActive: !isActive } : b,
        ),
      );
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
              />
            </CustomTabPanel>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
}
