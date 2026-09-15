-- ============================================================================
-- SEED: seguridad.roles
-- ============================================================================

INSERT INTO seguridad.roles (codigo, nombre, descripcion, nivel_jerarquia, estado) VALUES
('ADSIS', 'Administrador del sistema', 'Administrador del sistema', 5, true),
('CEO', 'Gerente', 'Gerencia CEO', 4, true),
('AC', 'Administrador Comercial', 'Acceso total a las funciones administrativas del sistema comercial.', 4, true),
('ASC', 'Asesor Comercial', 'Acceso únicamente a los procesos comerciales asignados.', 3, true),
('CE', 'Cliente Ecommerce', 'Acceso únicamente a la información asociada a su propia cuenta', 1, true),
('SIS', 'Sistemas', 'Acceso a funciones técnicas y de seguridad.', 4, true);
