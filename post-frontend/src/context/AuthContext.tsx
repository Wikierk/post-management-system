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
  user: User | null;
  isLoading: boolean;
  login: (token: string) => Promise<void>;
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

  useEffect(() => {
    const checkAuth = async () => {
      const token = localStorage.getItem("jwt_token");
      if (token) {
        try {
          const response = await api.get<User>("/users/me");
          setUser(response.data);
        } catch (error) {
          console.error("Błąd autoryzacji, token wygasł lub jest nieważny");
          localStorage.removeItem("jwt_token");
        }
      }
      setIsLoading(false);
    };

    checkAuth();
  }, []);

  const login = async (token: string) => {
    localStorage.setItem("jwt_token", token);
    try {
      const response = await api.get<User>("/users/me");
      setUser(response.data);
    } catch (error) {
      localStorage.removeItem("jwt_token");
      throw new Error("Nie udało się pobrać profilu po zalogowaniu");
    }
  };

  const logout = () => {
    localStorage.removeItem("jwt_token");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, isLoading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
