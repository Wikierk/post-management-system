CREATE TABLE addresses (
  address_id UUID PRIMARY KEY,
  city TEXT NOT NULL,
  street TEXT NOT NULL,
  number TEXT NOT NULL,
  zip_code TEXT NOT NULL
);

CREATE TABLE branches (
  branch_id UUID PRIMARY KEY,
  branch_type TEXT NOT NULL,
  address_id UUID NOT NULL
);

CREATE TABLE users (
  user_id UUID PRIMARY KEY,
  email TEXT UNIQUE NOT NULL,
  created_at TIMESTAMP NOT NULL,
  status TEXT NOT NULL,
  first_name TEXT,
  last_name TEXT
);

CREATE TABLE employees_in_branches (
  user_id UUID UNIQUE,
  branch_id UUID,
  PRIMARY KEY (user_id, branch_id)
);

CREATE TABLE saved_recipients (
  saved_recipient_id UUID PRIMARY KEY,
  full_name TEXT NOT NULL,
  street TEXT NOT NULL,
  city TEXT NOT NULL,
  zip_code TEXT NOT NULL,
  email TEXT,
  phone TEXT,
  owner_id UUID NOT NULL
);

CREATE TABLE reports (
  report_id UUID PRIMARY KEY,
  subject TEXT NOT NULL,
  description TEXT NOT NULL,
  status TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  tracking_number TEXT NOT NULL,
  assigned_admin_id UUID NOT NULL,
  author_id UUID NOT NULL
);

ALTER TABLE branches ADD FOREIGN KEY (address_id) REFERENCES addresses (address_id);

ALTER TABLE employees_in_branches ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE employees_in_branches ADD FOREIGN KEY (branch_id) REFERENCES branches (branch_id);

ALTER TABLE saved_recipients ADD FOREIGN KEY (owner_id) REFERENCES users (user_id);

ALTER TABLE reports ADD FOREIGN KEY (assigned_admin_id) REFERENCES users (user_id);

ALTER TABLE reports ADD FOREIGN KEY (author_id) REFERENCES users (user_id);
