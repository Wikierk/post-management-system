ALTER TABLE employees_in_branches RENAME TO employees;

ALTER TABLE employees DROP CONSTRAINT employees_in_branches_pkey;

ALTER TABLE employees ADD COLUMN employee_id UUID PRIMARY KEY;

ALTER TABLE employees ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE employees ALTER COLUMN branch_id SET NOT NULL;