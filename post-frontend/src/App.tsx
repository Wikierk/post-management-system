import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { LoginPage } from "./features/auth/LoginPage";
import { DashboardPage } from "./features/parcels/DashboardPage";
import { AdminDashboardPage } from "./features/admin/AdminDashboardPage";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { RegisterPage } from "./features/auth/RegisterPage";
import { TrackingPage } from "./features/parcels/TrackingPage";

export const App = () => {
  const googleClientId =
    import.meta.env.VITE_GOOGLE_CLIENT_ID ||
    "TWOJ_KLUCZ_Z_BACKENDU.apps.googleusercontent.com";

  return (
    <GoogleOAuthProvider clientId={googleClientId}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/tracking" element={<TrackingPage />} />

            <Route element={<ProtectedRoute />}>
              <Route path="/dashboard" element={<DashboardPage />} />
            </Route>

            <Route element={<ProtectedRoute requiredRole="ADMIN" />}>
              <Route path="/admin/dashboard" element={<AdminDashboardPage />} />
            </Route>

            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </GoogleOAuthProvider>
  );
};

export default App;
