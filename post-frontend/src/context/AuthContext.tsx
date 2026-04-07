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
  login: () => Promise<void>;
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

  useEffect(() => {
    const checkSession = async () => {
      try {
        // const response = await api.get("/v1/users/me");
        // setUser(response.data);

        setIsAuthenticated(true);
      } catch (error) {
        setIsAuthenticated(false);
        setUser(null);
      } finally {
        setIsLoading(false);
      }
    };

    checkSession();
  }, []);
  const login = async () => {
    setIsAuthenticated(true);

    try {
      //const userProfile = await api.get("/user/me");
      //setUser(userProfile.data);
    } catch (e) {}
  };

  const logout = async () => {
    try {
      // await api.post('/auth/logout');
    } finally {
      setIsAuthenticated(false);
      setUser(null);
      // Zamiast nawigacji w kontekście, lepiej zrobić to po wykonaniu akcji wstawiając w komponencie: navigate('/login')
    }
  };

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, user, isLoading, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};
