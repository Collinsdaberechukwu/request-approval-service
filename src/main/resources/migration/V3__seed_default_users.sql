INSERT INTO users
(first_name, last_name, email, role, department, active, created_at, version)
VALUES
('Collins', 'Manager', 'manager@infrest.com', 'MANAGER', 'OPERATIONS', true, NOW(), 0),

('Daberechukwu', 'Head', 'departmenthead@infrest.com', 'DEPARTMENT_HEAD', 'OPERATIONS', true, NOW(), 0),

('Okafor', 'Finance', 'finance@infrest.com', 'FINANCE', 'FINANCE', true, NOW(), 0),

('Lyra', 'HR', 'hr@infrest.com', 'HR', 'HUMAN_RESOURCES', true, NOW(), 0);