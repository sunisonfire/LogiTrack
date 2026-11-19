# LogiTrack – Sistema de Gestión y Auditoría de Bodegas

## 1. Descripción del proyecto

**LogiTrack** es un sistema backend desarrollado para LogiTrack S.A., una empresa que administra bodegas ubicadas en diferentes ciudades.

El sistema permite gestionar bodegas, productos e inventarios, registrar movimientos de mercancía y llevar un control de auditoría de las operaciones realizadas por los usuarios.

El proyecto busca centralizar la información, mejorar el control de inventarios, garantizar la trazabilidad de los movimientos y proteger el acceso mediante autenticación y autorización con JWT.

### Objetivo general

Desarrollar un sistema de gestión y auditoría de bodegas que permita registrar transacciones de inventario y generar reportes auditables de los cambios realizados por cada usuario.

## 2. Tecnologías utilizadas

- **Java 21:** lenguaje de programación.
- **Spring Boot 4.1.2:** framework para el desarrollo del backend.
- **Spring Security:** seguridad y control de acceso.
- **JWT:** autenticación mediante tokens.
- **Spring Data JPA / Hibernate:** persistencia y gestión de entidades.
- **MySQL:** sistema gestor de base de datos.
- **Maven:** gestión de dependencias y construcción del proyecto.
- **Swagger / OpenAPI 3:** documentación y pruebas de endpoints.
- **Lombok:** reducción de código repetitivo.

## 3. Funcionalidades principales

- Gestión de bodegas: registro, consulta, actualización y eliminación.
- Gestión de productos: operaciones CRUD.
- Gestión de inventario por bodega.
- Registro de entradas, salidas y transferencias de productos.
- Auditoría de operaciones de tipo INSERT, UPDATE y DELETE.
- Autenticación y autorización mediante JWT.
- Control de acceso según roles ADMIN y EMPLEADO.
- Consulta de productos con stock bajo.
- Filtros de movimientos por fechas.
- Consultas de auditoría por usuario, entidad y tipo de operación.
- Reporte general de inventario y productos más movidos.
- Manejo global de excepciones y validaciones.

## 4. Estructura del proyecto

La organización principal del backend es la siguiente:

```text
LogiTrack/
│
├── src/
│   ├── main/
│   │   ├── java/com/DannaS2/LogiTrack/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── LogiTrackApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/DannaS2/LogiTrack/
│
├── MySQL/
│   ├── database.sql
│   └── inserts.sql
│
├── Evidencias/
│   ├── Swagger/
│   ├── Autenticacion/
│   ├── Bodegas/
│   ├── Productos/
│   ├── Inventario/
│   ├── Movimientos/
│   ├── Auditoria/
│   ├── Reportes/
│   └── Pruebas/
│
├── pom.xml
└── README.md
```

## 5. Requisitos para la instalación

Antes de ejecutar el proyecto, es necesario contar con:

- Java JDK 21.
- Maven.
- MySQL Server.
- Un IDE como IntelliJ IDEA, Eclipse o Visual Studio Code.
- Git para clonar el repositorio.

## 6. Instalación y configuración

### 6.1. Clonar el repositorio

Abrir una terminal y ejecutar:

```bash
git clone https://github.com/sunisonfire/LogiTrack.git
```

Ingresar a la carpeta del proyecto:

```bash
cd LogiTrack/LogiTrack
```

### 6.2. Configurar la base de datos

1. Iniciar el servidor MySQL.
2. Crear la base de datos que utiliza el proyecto.
3. Ejecutar los scripts SQL disponibles en la carpeta `MySQL/`, según corresponda.
4. Abrir el archivo `src/main/resources/application.properties`.
5. Configurar las credenciales y la conexión a MySQL.

Ejemplo de configuración:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/NOMBRE_BASE_DATOS
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**Nota:** adaptar los valores del ejemplo a la configuración real del entorno. No publicar contraseñas ni claves JWT privadas en el repositorio.

### 6.3. Instalar dependencias

Desde la carpeta donde se encuentra el archivo `pom.xml`, ejecutar:

```bash
mvn clean install
```

### 6.4. Ejecutar el proyecto

Ejecutar:

```bash
mvn spring-boot:run
```

También es posible iniciar la aplicación desde el IDE ejecutando la clase principal `LogiTrackApplication`.

Una vez iniciada, la API estará disponible localmente en:

```text
http://localhost:8080
```

## 7. Documentación Swagger

El proyecto utiliza Swagger/OpenAPI 3 para documentar y probar los endpoints REST.

Con la aplicación en ejecución, ingresar a:

**Interfaz Swagger:**

http://localhost:8080/swagger-ui/index.html

**Especificación OpenAPI:**

http://localhost:8080/v3/api-docs

### Evidencias de Swagger (EndPoints Documentados)

![Documentación general de Swagger](Evidencias/Swagger/1.png)
![Documentación general de Swagger](Evidencias/Swagger/2.png)
![Documentación general de Swagger](Evidencias/Swagger/3.png)
![Documentación general de Swagger](Evidencias/Swagger/4.png)
![Documentación general de Swagger](Evidencias/Swagger/5.png)
![Documentación general de Swagger](Evidencias/Swagger/1.png)



## 8. Ejemplos de endpoints REST

Los siguientes endpoints permiten interactuar con las funcionalidades principales del sistema.

### 8.1. Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/auth/login` | Iniciar sesión y obtener JWT. |
| POST | `/auth/register` | Solicitar registro de empleado. |

Ejemplo de inicio de sesión:

```json
{
  "username": "juan.perez",
  "password": "Clave12345",
  "rol": "EMPLEADO"
}
```

La autenticación exitosa devuelve un token JWT que debe utilizarse para acceder a las rutas protegidas.

### 8.2. Bodegas

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/bodega` | Registrar una bodega. |
| GET | `/api/bodega` | Listar bodegas. |
| GET | `/api/bodega/{id}` | Consultar una bodega. |
| PUT | `/api/bodega/{id}` | Actualizar una bodega. |
| DELETE | `/api/bodega/{id}` | Eliminar una bodega. |

### 8.3. Productos

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/producto` | Registrar un producto. |
| GET | `/api/producto` | Listar productos. |
| GET | `/api/producto/{id}` | Consultar producto por ID. |
| PUT | `/api/producto/{id}` | Actualizar producto. |
| DELETE | `/api/producto/{id}` | Eliminar producto. |
| GET | `/api/producto/filtroCategoria` | Filtrar por categoría. |
| GET | `/api/producto/filtroNombre` | Buscar por nombre. |

### 8.4. Inventario

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/inventario/stockBajo?limite=10` | Consultar productos con stock bajo. |

### 8.5. Movimientos

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/movimiento/filtroFechas?desde=FECHA&hasta=FECHA` | Consultar movimientos por rango de fechas. |

Los tipos de movimiento contemplados son:

- ENTRADA.
- SALIDA.
- TRANSFERENCIA.

### 8.6. Auditoría

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/auditoria` | Consultar registros de auditoría. |
| GET | `/api/auditoria/{id}` | Consultar una auditoría específica. |
| GET | `/api/auditoria/filtroPersona` | Filtrar auditorías por usuario. |
| GET | `/api/auditoria/filtroTipo` | Filtrar por tipo de operación. |
| GET | `/api/auditoria/filtroEntidad` | Filtrar por entidad afectada. |
| GET | `/api/auditoria/filtroFechas` | Filtrar por fechas. |

### 8.7. Reportes

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/reportes/resumen` | Obtener el resumen general del inventario. |

El reporte contiene el stock total por bodega y los productos con mayor cantidad de unidades movilizadas.

**Nota:** consultar Swagger para verificar los parámetros requeridos y los permisos de cada endpoint.

## 9. Seguridad y autenticación

LogiTrack utiliza Spring Security y JWT para proteger los endpoints.

El sistema contempla dos roles:

- **ADMIN:** acceso a las operaciones administrativas autorizadas.
- **EMPLEADO:** acceso a las operaciones permitidas para empleados.

Para consumir los endpoints protegidos:

1. Iniciar sesión mediante `/auth/login`.
2. Obtener el token JWT.
3. Pulsar el botón **Authorize** en Swagger.
4. Introducir el token según el formato solicitado por Swagger.
5. Ejecutar las operaciones autorizadas para el usuario.

### Evidencias de autenticación

![Json de inicio de sesion exitoso](Evidencias/Autenticacion/Autenticacion_registro_json)

![Autorización con JWT](Evidencias/Autenticacion/Autenticacion_registro_exitoso)

![Json con errores](Evidencias/Autenticacion/Autenticacion_registro_eror401)

![Acceso sin token](Evidencias/Autenticacion/Autenticacion_registro_fallo)



## 10. Capturas de pruebas funcionales

En esta sección se documentan las pruebas manuales realizadas mediante Swagger, incluyendo operaciones exitosas y respuestas de error.

### 10.1. Pruebas de bodegas

![Registro de bodega](Evidencias/Bodegas/crear-bodega.png)

![Consulta de bodegas](Evidencias/Bodegas/listar-bodegas.png)

![Actualización de bodega](Evidencias/Bodegas/actualizar-bodega.png)

![Protección de eliminación de bodega](Evidencias/Bodegas/eliminar-bodega-409.png)

### 10.2. Pruebas de productos

![Registro de producto](Evidencias/Productos/crear-producto.png)

![Consulta de productos](Evidencias/Productos/listar-productos.png)

![Validación de producto](Evidencias/Productos/validacion-producto.png)

### 10.3. Pruebas de inventario

![Consulta de stock bajo](Evidencias/Inventario/stock-bajo.png)

### 10.4. Pruebas de movimientos

![Registro de entrada](Evidencias/Movimientos/entrada.png)

![Registro de salida](Evidencias/Movimientos/salida.png)

![Transferencia entre bodegas](Evidencias/Movimientos/transferencia.png)

![Validación de stock insuficiente](Evidencias/Movimientos/stock-insuficiente.png)

### 10.5. Pruebas de auditoría

![Consulta de auditorías](Evidencias/Auditoria/listar-auditorias.png)

![Filtro de auditorías](Evidencias/Auditoria/filtro-auditoria.png)

### 10.6. Pruebas de reportes

![Reporte general](Evidencias/Reportes/reporte-general.png)

### 10.7. Pruebas de errores y validaciones

![Error 400 - Validación](Evidencias/Pruebas/error-400.png)

![Error 401 - No autenticado](Evidencias/Pruebas/error-401.png)

![Error 403 - Sin permisos](Evidencias/Pruebas/error-403.png)

![Error 404 - Recurso no encontrado](Evidencias/Pruebas/error-404.png)

![Error 409 - Conflicto](Evidencias/Pruebas/error-409.png)

## 11. Manejo de excepciones y validaciones

El proyecto implementa un manejo global de excepciones mediante `@ControllerAdvice`.

Se contemplan respuestas personalizadas para situaciones como:

- 400 – Bad Request: datos inválidos o reglas de negocio incumplidas.
- 401 – Unauthorized: autenticación requerida o credenciales incorrectas.
- 403 – Forbidden: usuario autenticado sin permisos suficientes.
- 404 – Not Found: recurso no encontrado.
- 409 – Conflict: recurso duplicado o relacionado con otros registros.
- 500 – Internal Server Error: error interno inesperado.

También se utilizan anotaciones de validación como `@NotNull`, `@NotBlank`, `@Size` y `@Positive`.

## 12. Estado de las pruebas

Las pruebas funcionales se realizan manualmente mediante Swagger, verificando las respuestas HTTP y el comportamiento de los endpoints.

| Área | Estado |
|---|---|
| Autenticación y seguridad | Por documentar |
| Gestión de bodegas | Por documentar |
| Gestión de productos | Por documentar |
| Inventario | Por documentar |
| Movimientos | Por documentar |
| Auditoría | Por documentar |
| Reportes | Por documentar |
| Validaciones y excepciones | Por documentar |

Los estados se actualizarán conforme se ejecuten y documenten las pruebas.

## 13. Repositorio

Código fuente del proyecto:

[GitHub – LogiTrack](https://github.com/sunisonfire/LogiTrack)

---

**LogiTrack S.A. – Sistema de Gestión y Auditoría de Bodegas**

Proyecto académico desarrollado con Java y Spring Boot.