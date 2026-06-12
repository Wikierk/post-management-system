import React from "react";
import {
  Box,
  Button,
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
} from "@mui/material";
import {
  DeleteOutline as DeleteIcon,
  CheckCircleOutline as CheckCircleIcon,
  AddBusiness as AddBusinessIcon,
  Edit as EditIcon,
} from "@mui/icons-material";
import type { Branch } from "../types/Branch";

interface BranchesTableProps {
  branches: Branch[];
  totalBranches: number;
  page: number;
  pageSize: number;
  onPageChange: (event: unknown, newPage: number) => void;
  onPageSizeChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  onToggleStatus: (branchId: string, isActive: boolean) => void;
  onCreateBranch: () => void;
  onEditBranch: (branch: Branch) => void;
}

export default function BranchesTable({
  branches,
  totalBranches,
  page,
  pageSize,
  onPageChange,
  onPageSizeChange,
  onToggleStatus,
  onCreateBranch,
  onEditBranch,
}: BranchesTableProps) {
  return (
    <>
      <Box display="flex" justifyContent="flex-end" mb={2}>
        <Button
          variant="contained"
          startIcon={<AddBusinessIcon />}
          onClick={onCreateBranch}
        >
          Dodaj Placówkę
        </Button>
      </Box>
      <TableContainer>
        <Table>
          <TableHead sx={{ bgcolor: "grey.50" }}>
            <TableRow>
              <TableCell>
                <strong>Typ</strong>
              </TableCell>
              <TableCell>
                <strong>Miasto</strong>
              </TableCell>
              <TableCell>
                <strong>Adres</strong>
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
            {branches.map((branch) => (
              <TableRow key={branch.id} hover>
                <TableCell>
                  <Chip
                    label={
                      branch.type === "POST_OFFICE"
                        ? "Oddział Poczty"
                        : "Sortownia"
                    }
                    color={
                      branch.type === "POST_OFFICE" ? "primary" : "secondary"
                    }
                    variant="outlined"
                    size="small"
                  />
                </TableCell>
                <TableCell>{branch.address.city}</TableCell>
                <TableCell>
                  {branch.address.street} {branch.address.number},{" "}
                  {branch.address.zipCode}
                </TableCell>
                <TableCell>
                  <Chip
                    label={branch.isActive ? "Otwarta" : "Zamknięta"}
                    color={branch.isActive ? "success" : "default"}
                    size="small"
                  />
                </TableCell>
                <TableCell align="right">
                  <Tooltip title="Edytuj adres">
                    <IconButton color="primary" onClick={() => onEditBranch(branch)}>
                      <EditIcon />
                    </IconButton>
                  </Tooltip>
                  <Tooltip
                    title={
                      branch.isActive ? "Zamknij placówkę" : "Otwórz placówkę"
                    }
                  >
                    <IconButton
                      color={branch.isActive ? "error" : "success"}
                      onClick={() => onToggleStatus(branch.id, branch.isActive)}
                    >
                      {branch.isActive ? <DeleteIcon /> : <CheckCircleIcon />}
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
        count={totalBranches}
        rowsPerPage={pageSize}
        page={page}
        onPageChange={onPageChange}
        onRowsPerPageChange={onPageSizeChange}
        labelRowsPerPage="Wierszy na stronę:"
      />
    </>
  );
}
