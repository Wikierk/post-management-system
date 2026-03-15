CREATE TABLE parcel_subjects (
  parcel_subject_id UUID PRIMARY KEY,
  full_name TEXT NOT NULL,
  street TEXT NOT NULL,
  city TEXT NOT NULL,
  zip_code TEXT NOT NULL,
  email TEXT,
  phone TEXT,
  user_id UUID
);

CREATE TABLE parcel_types (
  parcel_type_id SMALLINT PRIMARY KEY,
  max_weight NUMERIC(5,2) NOT NULL,
  max_width SMALLINT NOT NULL,
  max_height SMALLINT NOT NULL,
  max_length SMALLINT NOT NULL,
  price NUMERIC(5,2) NOT NULL,
  description TEXT NOT NULL,
  is_available BOOLEAN NOT NULL
);

CREATE TABLE cash_on_delivery_details (
  cash_on_delivery_details_id UUID PRIMARY KEY,
  cash_amount NUMERIC(5,2) NOT NULL
);

CREATE TABLE parcels (
  tracking_number TEXT PRIMARY KEY,
  status TEXT NOT NULL,
  sender_id UUID NOT NULL,
  recipient_id UUID NOT NULL,
  parcel_type_id SMALLINT NOT NULL,
  cash_on_delivery_details_id UUID
);

CREATE TABLE logistic_holders (
  logistic_holder_id UUID PRIMARY KEY,
  logistic_holder_type TEXT NOT NULL
);

CREATE TABLE parcel_histories (
  parcel_history_id UUID PRIMARY KEY,
  tracking_number TEXT NOT NULL,
  status TEXT NOT NULL,
  description TEXT,
  created_at TIMESTAMP NOT NULL,
  actor_id UUID,
  logistic_holder_id UUID NOT NULL
);

CREATE TABLE additional_services (
  additional_service_id SMALLINT PRIMARY KEY,
  name VARCHAR UNIQUE NOT NULL,
  price NUMERIC(5,2) NOT NULL,
  is_available BOOLEAN NOT NULL
);

CREATE TABLE parcel_additional_services (
  tracking_number TEXT,
  additional_service_id SMALLINT,
  price NUMERIC(5,2) NOT NULL,
  PRIMARY KEY (tracking_number, additional_service_id)
);

ALTER TABLE parcels ADD FOREIGN KEY (sender_id) REFERENCES parcel_subjects (parcel_subject_id);

ALTER TABLE parcels ADD FOREIGN KEY (recipient_id) REFERENCES parcel_subjects (parcel_subject_id);

ALTER TABLE parcels ADD FOREIGN KEY (parcel_type_id) REFERENCES parcel_types (parcel_type_id);

ALTER TABLE parcels ADD FOREIGN KEY (cash_on_delivery_details_id) REFERENCES cash_on_delivery_details (cash_on_delivery_details_id);

ALTER TABLE parcel_histories ADD FOREIGN KEY (tracking_number) REFERENCES parcels (tracking_number);

ALTER TABLE parcel_histories ADD FOREIGN KEY (logistic_holder_id) REFERENCES logistic_holders (logistic_holder_id);

ALTER TABLE parcel_additional_services ADD FOREIGN KEY (tracking_number) REFERENCES parcels (tracking_number);

ALTER TABLE parcel_additional_services ADD FOREIGN KEY (additional_service_id) REFERENCES additional_services (additional_service_id);
