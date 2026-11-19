# Biblioteca API

API REST desarrollada con Java y Spring Boot para la gestión de una biblioteca universitaria.

El sistema permite administrar libros, usuarios, préstamos y devoluciones, utilizando PostgreSQL como base de datos relacional, Spring Data JPA para la persistencia y JWT para la autenticación y autorización basada en roles.

## Funcionalidades principales

- Registro y autenticación de usuarios.
- Autenticación mediante JWT.
- Access Token y Refresh Token.
- Roles `ROLE_ADMIN` y `ROLE_USUARIO`.
- Registro, consulta, actualización y eliminación de libros.
- Consulta de libros disponibles.
- Registro de préstamos.
- Registro de devoluciones.
- Consulta del historial de préstamos de cada usuario.
- Control de acceso según roles.
- Validación de datos de entrada.
- Manejo centralizado de excepciones.
- Documentación interactiva con Swagger/OpenAPI.
- Pruebas funcionales mediante Postman.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Spring Security
- JWT - JJWT 0.13.0
- Jakarta Validation
- PostgreSQL 17
- Maven
- Swagger / OpenAPI
- Postman
- IntelliJ IDEA
- Git
- GitHub

## Arquitectura del proyecto

El proyecto utiliza una arquitectura organizada por capas:

```text
Cliente / Postman / Swagger
            |
            v
       Controller
            |
            v
         Service
            |
            v
      ServiceImpl
            |
            v
       Repository
            |
            v
     JPA / Hibernate
            |
            v
       PostgreSQL
```

Esta estructura permite separar las responsabilidades del sistema, reducir el acoplamiento y facilitar el mantenimiento del código.

## Estructura principal

```text
src/main/java/com/idat/biblioteca
|
+-- config
|   +-- DataInitializer.java
|   +-- OpenApiConfig.java
|   +-- SecurityConfig.java
|
+-- controller
|   +-- AuthController.java
|   +-- LibroController.java
|   +-- PrestamoController.java
|   +-- UsuarioController.java
|
+-- dto
|   +-- request
|   |   +-- LibroRequest.java
|   |   +-- LoginRequest.java
|   |   +-- PrestamoRequest.java
|   |   +-- RefreshTokenRequest.java
|   |   +-- RegistroUsuarioRequest.java
|   |
|   +-- response
|       +-- AuthResponse.java
|       +-- LibroResponse.java
|       +-- PrestamoResponse.java
|       +-- UsuarioResponse.java
|
+-- exception
|   +-- AccesoDenegadoException.java
|   +-- GlobalExceptionHandler.java
|   +-- RecursoNoEncontradoException.java
|   +-- ReglaNegocioException.java
|
+-- model
|   +-- EstadoPrestamo.java
|   +-- Libro.java
|   +-- NombreRol.java
|   +-- Prestamo.java
|   +-- Rol.java
|   +-- Usuario.java
|
+-- repository
|   +-- LibroRepository.java
|   +-- PrestamoRepository.java
|   +-- RolRepository.java
|   +-- UsuarioRepository.java
|
+-- security
|   +-- CustomUserDetailsService.java
|   +-- JwtAuthenticationFilter.java
|   +-- JwtService.java
|
+-- service
|   +-- AuthService.java
|   +-- LibroService.java
|   +-- PrestamoService.java
|   +-- UsuarioService.java
|   |
|   +-- impl
|       +-- AuthServiceImpl.java
|       +-- LibroServiceImpl.java
|       +-- PrestamoServiceImpl.java
|       +-- UsuarioServiceImpl.java
|
+-- BibliotecaApiApplication.java
```

## Modelo de datos

El sistema utiliza cuatro entidades principales: `Libro`, `Usuario`, `Rol` y `Prestamo`.

### Libro

Representa los libros registrados en la biblioteca.

Principales atributos:

- `id`
- `titulo`
- `autor`
- `isbn`
- `categoria`
- `disponible`

### Usuario

Representa las cuentas registradas en el sistema.

Principales atributos:

- `id`
- `nombre`
- `email`
- `password`
- `roles`

### Rol

Define los permisos que puede tener cada usuario.

Roles implementados:

```text
ROLE_ADMIN
ROLE_USUARIO
```

La relación entre `Usuario` y `Rol` es de muchos a muchos.

### Prestamo

Registra el préstamo de un libro a un usuario.

Principales atributos:

- `id`
- `usuario`
- `libro`
- `fechaPrestamo`
- `fechaDevolucion`
- `estado`

Estados disponibles:

```text
PRESTADO
DEVUELTO
```

## Relaciones entre entidades

```text
Usuario N:M Rol

Usuario 1:N Prestamo

Libro 1:N Prestamo
```

La relación entre usuarios y roles utiliza la tabla intermedia:

```text
usuario_roles
```

## Requisitos previos

Para ejecutar el proyecto se requiere:

- Java 21
- PostgreSQL 17 o compatible
- Maven o Maven Wrapper
- IntelliJ IDEA, Eclipse, VS Code u otro IDE compatible
- Postman para realizar pruebas funcionales

## Configuración de PostgreSQL

Crear una base de datos llamada:

```sql
CREATE DATABASE biblioteca_db;
```

La aplicación utiliza una configuración similar a la siguiente:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca_db
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA
spring.datasource.driver-class-name=org.postgresql.Driver
```

Hibernate crea y actualiza automáticamente las tablas mediante:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## Configuración de JPA e Hibernate

La configuración principal utilizada es:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

El acceso a datos se realiza mediante repositorios de Spring Data JPA.

## Configuración JWT

La aplicación utiliza JWT para implementar autenticación sin estado.

Configuración:

```properties
jwt.expiration=86400000
jwt.refresh-expiration=604800000
```

Duración configurada:

```text
Access Token  -> 24 horas
Refresh Token -> 7 días
```

El sistema diferencia entre tokens de acceso y tokens de renovación.

Un Refresh Token no puede utilizarse directamente para acceder a los endpoints protegidos.

## Administrador inicial

El sistema crea una cuenta administrativa inicial para las pruebas del proyecto.

```text
Nombre: Administrador
Email: admin@biblioteca.com
Rol: ROLE_ADMIN
```

La contraseña del administrador se configura en el proyecto para el entorno de pruebas.

## Ejecución del proyecto

Desde la raíz del proyecto se puede ejecutar:

### Windows PowerShell

```bash
.\mvnw.cmd spring-boot:run
```

También se puede compilar primero:

```bash
.\mvnw.cmd clean package
```

Y posteriormente ejecutar el archivo generado:

```bash
java -jar target/biblioteca-api-0.0.1-SNAPSHOT.jar
```

El proyecto también puede ejecutarse directamente desde:

```text
BibliotecaApiApplication.java
```

utilizando IntelliJ IDEA.

La aplicación se inicia por defecto en:

```text
http://localhost:8080
```

## Swagger / OpenAPI

La documentación interactiva de la API está disponible en:

```text
http://localhost:8080/swagger-ui.html
```

Swagger permite:

- visualizar los endpoints disponibles;
- consultar los métodos HTTP;
- enviar datos JSON;
- utilizar autenticación Bearer;
- comprobar respuestas HTTP;
- visualizar los modelos utilizados por la aplicación.

## Autenticación

### Registro de usuario

Endpoint:

```text
POST /api/auth/registro
```

Ejemplo de entrada:

```json
{
  "nombre": "Maria Lopez",
  "email": "maria@gmail.com",
  "password": "Java1234"
}
```

Los usuarios registrados mediante este endpoint reciben automáticamente el rol:

```text
ROLE_USUARIO
```

### Login

Endpoint:

```text
POST /api/auth/login
```

Ejemplo:

```json
{
  "email": "admin@biblioteca.com",
  "password": "Admin1234"
}
```

Ejemplo de respuesta:

```json
{
  "email": "admin@biblioteca.com",
  "refreshToken": "eyJ...",
  "tipo": "Bearer",
  "token": "eyJ..."
}
```

El campo `token` corresponde al Access Token utilizado para acceder a las rutas protegidas.

### Refresh Token

Endpoint:

```text
POST /api/auth/refresh
```

Ejemplo:

```json
{
  "refreshToken": "eyJ..."
}
```

El endpoint valida el Refresh Token y devuelve nuevos tokens para continuar utilizando la API.

## Control de acceso

El sistema implementa autorización basada en roles.

### ROLE_ADMIN

El administrador puede:

- registrar libros;
- actualizar libros;
- eliminar libros;
- consultar usuarios;
- consultar libros;
- registrar préstamos;
- registrar devoluciones;
- consultar todos los préstamos;
- consultar préstamos por usuario;
- consultar préstamos por estado.

### ROLE_USUARIO

El usuario puede:

- consultar libros;
- consultar libros disponibles;
- consultar su propio historial de préstamos.

El usuario normal no puede acceder a las operaciones administrativas.

## Endpoints principales

### Autenticación

```text
POST /api/auth/registro
POST /api/auth/login
POST /api/auth/refresh
```

### Libros

```text
GET    /api/libros
GET    /api/libros/{id}
GET    /api/libros/disponibles
GET    /api/libros/buscar
POST   /api/libros
PUT    /api/libros/{id}
DELETE /api/libros/{id}
```

### Usuarios

```text
GET /api/usuarios
GET /api/usuarios/{id}
GET /api/usuarios/email
```

### Préstamos

```text
POST /api/prestamos
GET  /api/prestamos
GET  /api/prestamos/{id}
GET  /api/prestamos/mis-prestamos
GET  /api/prestamos/usuario/{usuarioId}
GET  /api/prestamos/estado/{estado}
PUT  /api/prestamos/{id}/devolucion
```

## Reglas de negocio

Entre las principales reglas implementadas se encuentran:

- No se puede prestar un libro que no se encuentre disponible.
- Al registrar un préstamo, el libro cambia automáticamente a no disponible.
- Al registrar una devolución, el libro vuelve a estar disponible.
- No se puede devolver dos veces el mismo préstamo.
- El ISBN de cada libro debe ser único.
- El correo electrónico de cada usuario debe ser único.
- Un usuario normal no puede ejecutar operaciones administrativas.
- El Refresh Token no puede utilizarse como Access Token.
- Las rutas protegidas requieren autenticación mediante JWT.

## Validaciones

El proyecto utiliza Jakarta Validation para verificar la información recibida.

Entre las anotaciones utilizadas se encuentran:

```text
@NotBlank
@NotNull
@Email
@Size
@Positive
@Pattern
```

Cuando los datos enviados no cumplen con las reglas definidas, la API responde con:

```text
400 Bad Request
```

Ejemplo:

```json
{
  "titulo": "El título debe tener entre 2 y 150 caracteres",
  "autor": "El autor es obligatorio",
  "categoria": "La categoría es obligatoria",
  "isbn": "El ISBN debe tener un formato válido"
}
```

## Manejo centralizado de errores

La aplicación utiliza un manejador global de excepciones mediante:

```text
@RestControllerAdvice
```

Esto permite devolver respuestas uniformes cuando ocurre un error en la aplicación.

Principales códigos HTTP utilizados:

| Código | Significado |
| --- | --- |
| 200 | Operación realizada correctamente |
| 201 | Recurso creado correctamente |
| 400 | Datos inválidos o incumplimiento de una regla de negocio |
| 401 | Usuario no autenticado o token inválido |
| 403 | Usuario autenticado sin permisos suficientes |
| 404 | Recurso no encontrado |
| 409 | Conflicto o restricción de integridad referencial |

### Ejemplo 401

```json
{
  "estado": 401,
  "error": "Unauthorized",
  "mensaje": "Debe proporcionar un token de acceso"
}
```

### Ejemplo 403

```json
{
  "estado": 403,
  "error": "Forbidden",
  "mensaje": "No tiene permisos para realizar esta operación"
}
```

### Ejemplo 404

```json
{
  "estado": 404,
  "error": "Not Found",
  "mensaje": "Libro no encontrado"
}
```

### Ejemplo 409

```json
{
  "estado": 409,
  "error": "Conflict",
  "mensaje": "La operación no puede realizarse porque existen datos relacionados o duplicados"
}
```

## Persistencia de datos

La persistencia utiliza:

```text
Spring Data JPA
       |
       v
   Hibernate
       |
       v
  PostgreSQL
```

Los repositorios utilizan:

```text
JpaRepository
```

Además de consultas derivadas de Spring Data, el proyecto incorpora consultas personalizadas para obtener información específica.

Por ejemplo, el historial de préstamos de un usuario puede consultarse utilizando una consulta personalizada en el repositorio de préstamos.

## DTO

El proyecto utiliza DTO para separar los datos enviados y recibidos por la API de las entidades utilizadas internamente por JPA.

La estructura se divide en:

```text
dto
+-- request
|   +-- LibroRequest
|   +-- LoginRequest
|   +-- PrestamoRequest
|   +-- RefreshTokenRequest
|   +-- RegistroUsuarioRequest
|
+-- response
    +-- AuthResponse
    +-- LibroResponse
    +-- PrestamoResponse
    +-- UsuarioResponse
```

Esta separación evita exponer directamente las entidades de persistencia y permite controlar la información enviada al cliente.

Por ejemplo, la contraseña del usuario no se devuelve en las respuestas de la API.

## Seguridad

La seguridad está implementada mediante Spring Security y JWT.

Los principales componentes son:

```text
SecurityConfig
JwtAuthenticationFilter
JwtService
CustomUserDetailsService
```

### SecurityConfig

Define:

- rutas públicas;
- rutas protegidas;
- permisos según roles;
- autenticación sin sesión;
- respuestas 401 y 403.

### JwtAuthenticationFilter

Intercepta las peticiones protegidas y verifica:

- existencia del token;
- formato Bearer;
- validez del token;
- expiración;
- tipo de token;
- usuario asociado al token.

### JwtService

Se encarga de:

- generar Access Tokens;
- generar Refresh Tokens;
- obtener el usuario del token;
- comprobar la expiración;
- validar Access Tokens;
- validar Refresh Tokens.

## Autenticación sin estado

La aplicación utiliza una política:

```text
STATELESS
```

Esto significa que el servidor no almacena sesiones de usuario.

Cada petición a una ruta protegida debe incluir:

```text
Authorization: Bearer <access_token>
```

## Postman

El proyecto fue probado mediante una colección organizada de Postman.

Estructura utilizada:

```text
Biblioteca API
|
+-- Auth
|
+-- Libros
|
+-- Usuarios
|
+-- Prestamos
|
+-- Pruebas de seguridad y errores
```

La colección utiliza variables de entorno como:

```text
baseUrl
accessToken
refreshToken
userAccessToken
usuarioId
libroId
prestamoId
```

La URL base utilizada localmente es:

```text
http://localhost:8080
```

Los tokens generados durante el login se almacenan automáticamente en las variables del entorno de Postman.

## Pruebas funcionales realizadas

Se comprobaron los siguientes escenarios:

- Login del administrador.
- Login de un usuario.
- Generación de Access Token.
- Generación de Refresh Token.
- Renovación del token.
- Registro de libros.
- Consulta de libros.
- Consulta de libros disponibles.
- Registro de préstamos.
- Registro de devoluciones.
- Consulta del historial de préstamos.
- Protección de rutas administrativas.
- Acceso sin token.
- Acceso con un rol sin permisos.
- Validación de datos incorrectos.
- Consulta de recursos inexistentes.
- Restricciones de integridad referencial.

## Códigos HTTP comprobados con Postman

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
```

### 200 OK

Se obtuvo durante operaciones como:

- login;
- refresh token;
- consultas;
- registro de devolución.

### 201 Created

Se obtuvo al:

- registrar un libro;
- registrar un préstamo.

### 400 Bad Request

Se comprobó enviando información inválida al registro de libros.

### 401 Unauthorized

Se comprobó accediendo a un endpoint protegido sin proporcionar un Access Token.

### 403 Forbidden

Se comprobó utilizando un usuario con `ROLE_USUARIO` para intentar acceder a una operación reservada para `ROLE_ADMIN`.

### 404 Not Found

Se comprobó consultando un libro inexistente.

### 409 Conflict

Se comprobó intentando eliminar un libro relacionado con registros de préstamos existentes.

## Principios y buenas prácticas

El proyecto aplica separación de responsabilidades mediante una arquitectura organizada por capas.

Entre las principales prácticas utilizadas se encuentran:

- controladores enfocados en las peticiones HTTP;
- interfaces para los servicios;
- implementaciones de servicio independientes;
- repositorios separados;
- DTO de entrada y salida;
- encapsulamiento;
- inyección de dependencias;
- manejo centralizado de excepciones;
- transacciones;
- filtros de seguridad personalizados;
- bajo acoplamiento;
- alta cohesión.

## Separación de responsabilidades

### Controller

Recibe las peticiones HTTP y devuelve las respuestas correspondientes.

### Service

Define las operaciones disponibles para la lógica de negocio.

### ServiceImpl

Implementa las reglas de negocio.

### Repository

Gestiona la comunicación con la base de datos mediante Spring Data JPA.

### Model

Representa las entidades persistidas en PostgreSQL.

### DTO

Controla la información que entra y sale de la API.

### Security

Gestiona autenticación, tokens JWT y autorización basada en roles.

## Compilación

Para comprobar que el proyecto compila correctamente:

```bash
.\mvnw.cmd clean compile
```

Para compilar, ejecutar las pruebas y generar el archivo JAR:

```bash
.\mvnw.cmd clean package
```

Si el proceso termina correctamente debe aparecer:

```text
BUILD SUCCESS
```

## Repositorio GitHub

El código fuente del proyecto se encuentra disponible en:

https://github.com/MichaelTacas/biblioteca-api

## Unidad didáctica

**Desarrollo de los Componentes del Negocio**

Proyecto final desarrollado como parte de la evaluación de la unidad didáctica.

## Estado del proyecto

```text
API REST                  LISTO
PostgreSQL                LISTO
JPA                       LISTO
Hibernate                 LISTO
Spring Data               LISTO
DTO                       LISTO
Validaciones              LISTO
JWT                       LISTO
Access Token              LISTO
Refresh Token             LISTO
Roles                     LISTO
Control de acceso         LISTO
Manejo de errores         LISTO
Swagger / OpenAPI         LISTO
Postman                   LISTO
Git / GitHub              LISTO
```

## Script de base de datos

El proyecto incluye el script SQL completo de PostgreSQL en:

`database/biblioteca_db.sql`

Este archivo permite reconstruir manualmente la estructura de la base de datos utilizada por la aplicación, incluyendo tablas, claves primarias, claves foráneas, restricciones y relaciones.

La aplicación también utiliza JPA e Hibernate para crear y actualizar automáticamente la estructura mediante:

`spring.jpa.hibernate.ddl-auto=update`

## Conclusión

El proyecto implementa una API REST para la gestión de una biblioteca universitaria utilizando Java, Spring Boot y PostgreSQL.

La solución incorpora persistencia mediante JPA, Hibernate y Spring Data, autenticación sin estado mediante JWT, autorización basada en roles, Access Token y Refresh Token, validaciones, manejo centralizado de excepciones y documentación mediante Swagger/OpenAPI.

Las pruebas realizadas con Postman permiten verificar el funcionamiento de los endpoints, las reglas de negocio y los mecanismos de seguridad implementados.

La organización por capas permite mantener separadas las responsabilidades del sistema, logrando un código más organizado, mantenible y preparado para futuras mejoras.