import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  type ReactNode,
} from "react";
import api from "../api/axiosConfig";
import type { User } from "../types/User";

interface AuthContextType {
  isAuthenticated: boolean;
  user: User | null;
  isLoading: boolean;
  checkSession: () => Promise<User | null>;
  login: () => Promise<User | null>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth musi być użyte wewnątrz AuthProvider");
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  const checkSession = async (
    retries = 3,
    delayMs = 500,
  ): Promise<User | null> => {
    setIsLoading(true);
    try {
      const response = await api.get<User>("/users/v3/me");
      setUser(response.data);
      setIsAuthenticated(true);
      return response.data;
    } catch (error: any) {
      const status = error.response?.status;
      const shouldRetry = retries > 0 && (status === 404 || status === 500);

      if (shouldRetry) {
        console.warn(
          `User fetch failed (${status}), retrying in ${delayMs}ms... (${retries} retries left)`,
        );
        await new Promise((resolve) => setTimeout(resolve, delayMs));
        return checkSession(retries - 1, delayMs * 1.5); // Exponential backoff
      }

      setIsAuthenticated(false);
      setUser(null);
      return null;
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("authToken");
    if (token) {
      checkSession();
    } else {
      setIsLoading(false);
    }
  }, []);
  const login = async (): Promise<User | null> => {
    const userData = await checkSession();
    if (!userData) {
      console.error("Login session check failed");
    }
    return userData;
  };

  const logout = async () => {
    try {
      // await api.post('/auth/logout');
    } finally {
      setIsAuthenticated(false);
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, user, isLoading, checkSession, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};
