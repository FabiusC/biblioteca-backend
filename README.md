# Biblioteca Backend

API REST para la gestión de una biblioteca construida con Java 17, Spring Boot 3.5.4, Maven y PostgreSQL. La solución está preparada para ejecutarse con Docker y utiliza una arquitectura N-capas con DTOs para aislar el contrato HTTP del modelo de dominio.

## Instrucciones de Despliegue Rápido

Pasos en terminal para levantar todo el entorno con datos de prueba cargados.

### 1: Clonar el repositorio y acceder a la carpeta

```bash
git clone https://github.com/FabiusC/biblioteca-backend.git
cd biblioteca-backend
```

### 2: Crear el archivo `.env`

Copia la plantilla de entorno para crear el archivo `.env`:

- **En Linux / macOS / Git Bash:**
  ```bash
  cp .env.example .env
  ```
- **En Windows (PowerShell):**
  ```powershell
  Copy-Item .env.example .env
  ```
- **En Windows (CMD):**
  ```cmd
  copy .env.example .env
  ```
  > **Importante:** Abre el archivo `.env` recién creado y cambia las variables `DB_USER` y `DB_PASSWORD` por el usuario y contraseña que prefieras (por defecto son `postgres` / `postgres`).

### 3: Construir y levantar los contenedores de Docker

```bash
docker compose up -d --build
```

### 4: Inicializar y cargar los datos de prueba

- **Comando estándar (CMD / Linux / macOS / Git Bash):**
  ```bash
  docker compose exec -T db psql -U postgres -d library_db < db-backup/backup.dump
  ```
- **Alternativa PowerShell (Si el caracter `<` da error):**
  ```powershell
  Get-Content db-backup/backup.dump | docker compose exec -T db psql -U postgres -d library_db
  ```
- **Otra opción universal (Usando cat):**
  ```bash
  cat db-backup/backup.dump | docker compose exec -T db psql -U postgres -d library_db
  ```
  _(Nota: Si cambiaste el usuario `DB_USER` en el paso 2, reemplaza `postgres` por tu usuario en los comandos anteriores)._

---

## Dirección y puerto por defecto

La API queda expuesta por defecto en:

- Dirección: `http://localhost`
- Puerto: `8080`

Por tanto, la base URL local es `http://localhost:8080`.
Prueba rápida con `curl`:

```bash
curl http://localhost:8080/api/users
```

## Endpoints principales

- `GET /api/users`
- `POST /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`
- `GET /api/books`
- `POST /api/books`
- `GET /api/books/{id}`
- `PUT /api/books/{id}`
- `DELETE /api/books/{id}`
- `GET /api/books/{isbn}/available-copies`
- `GET /api/loans/search?userId=1&bookId=2`
- `GET /api/loans/user/{userId}`
- `GET /api/loans/book/{bookId}`
- `POST /api/loans`
- `PUT /api/loans/{id}`

## Arquitectura y diseño

La aplicación se organiza en capas bien definidas:

- Controllers: exponen los endpoints REST y trabajan únicamente con DTOs de request y response.
- Services: concentran la lógica de negocio, las validaciones transaccionales y las reglas de préstamo.
- Repositories: encapsulan el acceso a datos con Spring Data JPA.
- DTOs: evitan exponer directamente las entidades JPA y estabilizan el contrato de la API.

El manejo global de excepciones se centraliza en un `@RestControllerAdvice`, que traduce errores de negocio y recursos no encontrados a respuestas HTTP consistentes.

El diseño de la base de datos sigue una relación Libro-Ejemplar: un libro puede tener varios ejemplares, y cada ejemplar pertenece a un solo libro. Los préstamos referencian usuario, libro y ejemplar para conservar trazabilidad completa.

## Variables de entorno

La conexión a base de datos y el puerto de la API se inyectan exclusivamente por variables de entorno:

- `DB_URI`: cadena JDBC de conexión a PostgreSQL.
- `DB_USER`: usuario de base de datos.
- `DB_PASSWORD`: contraseña de base de datos.
- `DB_DRIVER`: clase del driver JDBC, por ejemplo `org.postgresql.Driver`.
- `APP_PORT`: puerto expuesto por la API, con valor por defecto `8080`.
