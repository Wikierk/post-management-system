import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
  IconButton,
  Tooltip,
  TablePagination,
  Box,
} from "@mui/material";
import {
  Block as BlockIcon,
  CheckCircleOutline as CheckCircleIcon,
} from "@mui/icons-material";
import type { AdminUser } from "../types/AdminUser";

interface UsersTableProps {
  users: AdminUser[];
  totalUsers: number;
  page: number;
  pageSize: number;
  onPageChange: (event: unknown, newPage: number) => void;
  onPageSizeChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  onToggleStatus: (userId: string, currentStatus: string) => void;
}

export default function UsersTable({
  users,
  totalUsers,
  page,
  pageSize,
  onPageChange,
  onPageSizeChange,
  onToggleStatus,
}: UsersTableProps) {
  return (
    <Box>
      <TableContainer>
        <Table>
          <TableHead sx={{ bgcolor: "grey.50" }}>
            <TableRow>
              <TableCell>
                <strong>Imię i Nazwisko</strong>
              </TableCell>
              <TableCell>
                <strong>Email</strong>
              </TableCell>
              <TableCell>
                <strong>Status</strong>
              </TableCell>
              <TableCell align="right">
                <strong>Akcje</strong>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.id} hover>
                <TableCell>
                  {user.firstName} {user.lastName}
                </TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>
                  <Chip
                    label={user.status === "ACTIVE" ? "Aktywny" : "Zablokowany"}
                    color={user.status === "ACTIVE" ? "success" : "error"}
                    size="small"
                  />
                </TableCell>
                <TableCell align="right">
                  <Tooltip
                    title={
                      user.status === "ACTIVE"
                        ? "Zablokuj użytkownika"
                        : "Odblokuj użytkownika"
                    }
                  >
                    <IconButton
                      color={user.status === "ACTIVE" ? "error" : "success"}
                      onClick={() => onToggleStatus(user.id, user.status)}
                    >
                      {user.status === "ACTIVE" ? (
                        <BlockIcon />
                      ) : (
                        <CheckCircleIcon />
                      )}
                    </IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[5, 10, 25, 50]}
        component="div"
        count={totalUsers}
        rowsPerPage={pageSize}
        page={page}
        onPageChange={onPageChange}
        onRowsPerPageChange={onPageSizeChange}
        labelRowsPerPage="Wierszy na stronę:"
      />
    </Box>
  );
}
