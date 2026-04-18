export interface Address {
  city: string;
  street: string;
  number: string;
  zipCode: string;
}

export interface Branch {
  id: string;
  type: "POST_OFFICE" | "WAREHOUSE";
  address: Address;
  isActive: boolean;
}
