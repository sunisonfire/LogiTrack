/*
 * CONFIGURACIÓN GLOBAL DE LOGITRACK
 * Cambia API_BASE_URL para apuntar al puerto/host donde se ejecuta Spring Boot.
 * Ejemplo: "http://localhost:8080" o "http://192.168.1.20:8080"
 * El frontend puede servirse desde otro puerto (por ejemplo Live Server: 5500).
 */
window.LOGITRACK_CONFIG = Object.freeze({
  API_BASE_URL: "http://localhost:8080",
  REQUEST_TIMEOUT_MS: 15000
});
