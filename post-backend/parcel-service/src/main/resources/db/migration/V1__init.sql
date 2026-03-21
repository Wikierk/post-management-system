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
  parcel_type_id SMALLSERIAL PRIMARY KEY,
  max_weight NUMERIC(5,2) NOT NULL,
  max_width SMALLINT NOT NULL,
  max_height SMALLINT NOT NULL,
  max_length SMALLINT NOT NULL,
  price NUMERIC(5,2) NOT NULL,
  description TEXT NOT NULL,
  is_available BOOLEAN NOT NULL
);

CREATE TABLE parcel_type_snapshots (
  parcel_type_snapshot_id UUID PRIMARY KEY,
  max_weight NUMERIC(5,2) NOT NULL,
  max_width SMALLINT NOT NULL,
  max_height SMALLINT NOT NULL,
  max_length SMALLINT NOT NULL,
  price NUMERIC(5,2) NOT NULL,
  description TEXT NOT NULL,
  parcel_type_id SMALLINT NOT NULL
);

CREATE TABLE parcels (
  tracking_number TEXT PRIMARY KEY,
  status TEXT NOT NULL,
  total_price NUMERIC(6,2) NOT NULL,
  cash_on_delivery NUMERIC(7,2),
  sender_id UUID NOT NULL,
  recipient_id UUID NOT NULL,
  parcel_type_snapshot_id UUID NOT NULL
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
  additional_service_id SMALLSERIAL PRIMARY KEY,
  name TEXT UNIQUE NOT NULL,
  price NUMERIC(5,2) NOT NULL,
  is_available BOOLEAN NOT NULL
);

CREATE TABLE selected_services (
  selected_service_id UUID PRIMARY KEY,
  tracking_number TEXT NOT NULL,
  name TEXT NOT NULL,
  price NUMERIC(5,2) NOT NULL,
  additional_service_id SMALLINT NOT NULL
);

CREATE UNIQUE INDEX ON selected_services (tracking_number, additional_service_id);

ALTER TABLE parcel_type_snapshots ADD FOREIGN KEY (parcel_type_id) REFERENCES parcel_types (parcel_type_id);

ALTER TABLE parcels ADD FOREIGN KEY (sender_id) REFERENCES parcel_subjects (parcel_subject_id);

ALTER TABLE parcels ADD FOREIGN KEY (recipient_id) REFERENCES parcel_subjects (parcel_subject_id);

ALTER TABLE parcels ADD FOREIGN KEY (parcel_type_snapshot_id) REFERENCES parcel_type_snapshots (parcel_type_snapshot_id);

ALTER TABLE parcel_histories ADD FOREIGN KEY (tracking_number) REFERENCES parcels (tracking_number);

ALTER TABLE parcel_histories ADD FOREIGN KEY (logistic_holder_id) REFERENCES logistic_holders (logistic_holder_id);

ALTER TABLE selected_services ADD FOREIGN KEY (tracking_number) REFERENCES parcels (tracking_number);

ALTER TABLE selected_services ADD FOREIGN KEY (additional_service_id) REFERENCES additional_services (additional_service_id);
