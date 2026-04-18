export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  status: string;
  roles: string[];
}

export const hasRole = (user: User | null, role: string): boolean => {
  const roleToCheck = role.startsWith("ROLE_") ? role : `ROLE_${role}`;
  return user?.roles?.includes(roleToCheck) ?? false;
};

export const isAdmin = (user: User | null): boolean => {
  return hasRole(user, "ADMIN");
};
