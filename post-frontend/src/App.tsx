import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { OAuthRedirect } from "./features/auth/OAuthRedirect";
import { LoginPage } from "./features/auth/LoginPage";
import { DashboardPage } from "./features/parcels/DashboardPage";

export const App = () => {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/oauth2/redirect" element={<OAuthRedirect />} />

          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<DashboardPage />} />

            {/* <Route path="/parcels/new" element={<CreateParcelPage />} /> */}
            {/* <Route path="/admin" element={<AdminDashboard />} /> */}
          </Route>

          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
};

export default App;
