-- ============================================================================
-- SEED: seguridad.usuarios
-- Usuario administrador inicial para poder entrar al sistema por primera vez.
--
-- IMPORTANTE: 'CAMBIAR_ESTE_HASH' NO es un hash real. Reemplázalo por un
-- BCrypt generado con el mismo encoder que use tu Spring Security
-- (ej. new BCryptPasswordEncoder().encode("tu_password")), o crea este
-- usuario desde la aplicación una vez levantada y borra este INSERT.
-- ============================================================================

INSERT INTO seguridad.usuarios (
    id_rol, nombres, username, email, password_hash,
    estado, email_verificado, requiere_cambio_password
)
SELECT
    r.id_rol, 'Administrador del Sistema', 'admin', 'admin@nesssoft.local',
    'CAMBIAR_ESTE_HASH',
    'ACTIVO', true, true
FROM seguridad.roles r
WHERE r.codigo = 'ADSIS';
