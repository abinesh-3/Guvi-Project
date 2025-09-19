-- Optional: create an initial admin user (password: admin)
INSERT INTO users(id, email, password, role, enabled) VALUES
(1, 'admin@smartspend.local', '$2a$10$5F5m8oQm0ec7xZQ5nX8S7eA5jQw0dO5oQm2cFh3aGm7vS0z1q8E3a', 'ADMIN', true)
ON DUPLICATE KEY UPDATE email=email;