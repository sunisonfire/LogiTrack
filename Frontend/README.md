# LogiTrack Frontend

Interfaz web estática para el backend Spring Boot de LogiTrack. Esta versión incluye inicio de sesión JWT, resumen, consulta de personas y CRUD de administradores, con diseño responsive inspirado en Sunnie & Moonie.

## 1. Estructura

```text
frontend/
├── index.html
├── css/
│   └── styles.css
├── js/
│   ├── config.js
│   └── app.js
└── README.md
```

## 2. Configurar la URL del backend

Edita **`js/config.js`**. La variable global centralizada es:

```js
window.LOGITRACK_CONFIG = Object.freeze({
  API_BASE_URL: "http://localhost:8080",
  REQUEST_TIMEOUT_MS: 15000
});
```

- `API_BASE_URL`: dirección base de Spring Boot (sin `/` al final).
- `REQUEST_TIMEOUT_MS`: tiempo máximo de espera de cada petición.

Ejemplo si el frontend corre en `http://localhost:5500` y Spring Boot en `http://localhost:8081`:

```js
API_BASE_URL: "http://localhost:8081"
```

No necesitas cambiar las URLs en `app.js`.

## 3. CORS del backend

El backend actual (`SecurityConfig.java`) permite los orígenes `http://127.0.0.1:5500` y `http://localhost:5500`. Si ejecutas el frontend en otro puerto, agrega ese origen a `config.setAllowedOrigins(...)` en el backend y reinicia Spring Boot. El puerto del backend y el del frontend pueden ser diferentes; CORS controla qué origen del navegador está autorizado.

Para Live Server usa una de estas direcciones:
- `http://127.0.0.1:5500`
- `http://localhost:5500`

Si abres el HTML con `file://`, algunos navegadores pueden bloquear las peticiones. Usa un servidor estático local.

## 4. Ejecutar

1. Inicia MySQL y verifica la configuración del backend.
2. Inicia Spring Boot desde el proyecto Maven.
3. Abre la carpeta `frontend` en Visual Studio Code.
4. Ejecuta `index.html` con Live Server, o inicia otro servidor estático.
5. Ajusta `API_BASE_URL` si el backend no está en `localhost:8080`.

## 5. Funcionalidades y endpoints usados

| Función | Método y endpoint |
|---|---|
| Inicio de sesión | `POST /auth/login` |
| Listar administradores | `GET /api/administrador` |
| Crear administrador | `POST /api/administrador` |
| Consultar administrador | `GET /api/administrador/{id}` |
| Actualizar administrador | `PUT /api/administrador/{id}` |
| Eliminar administrador | `DELETE /api/administrador/{id}` |
| Listar personas | `GET /api/persona` |

El login envía `username`, `password` y `rol` (`ADMIN` o `EMPLEADO`). La respuesta JWT se conserva en `sessionStorage` y se envía como `Authorization: Bearer <token>`.

El alta/edición de administrador envía el objeto anidado que espera el backend:

```json
{
  "persona": {
    "nombre": "Nombre",
    "apellido": "Apellido",
    "documento": "123456"
  },
  "username": "usuario",
  "password": "ClaveSegura123"
}
```

En una edición, si se deja la contraseña vacía, el frontend no envía el campo `password`. Confirma que el servicio del backend acepte ese comportamiento para actualizaciones; si exige contraseña, introduce una nueva.

## 6. Roles

La configuración de seguridad del backend reserva `/api/administrador/**` y `/api/persona/**` para `ADMIN`. La navegación oculta estas secciones a `EMPLEADO`. El control real de permisos siempre lo realiza Spring Security.

## 7. Notas

- El frontend no contiene credenciales de MySQL ni secretos JWT.
- El botón **Probar conexión** verifica que el servidor responda a una petición OPTIONS; no sustituye una prueba autenticada de endpoints.
- La API debe estar ejecutándose y CORS debe permitir el origen desde el que se sirve esta interfaz.
