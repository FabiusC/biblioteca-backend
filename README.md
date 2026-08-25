# Biblioteca Backend

API REST para la gestión de una biblioteca construida con Java 17, Spring Boot 3.5.4, Maven y PostgreSQL. La solución está preparada para ejecutarse con Docker y utiliza una arquitectura N-capas con DTOs para aislar el contrato HTTP del modelo de dominio.

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

Puedes definirlas en un archivo `.env` o directamente en `docker-compose.yml`. La aplicación no contiene credenciales quemadas en el código fuente.

## Ejecución con Docker

1. Crear un archivo `.env` con las variables necesarias.
2. Levantar los contenedores.
3. Revisar los logs de la API si es necesario.

```bash
docker compose up --build -d
docker compose logs -f api
docker compose ps
```

## Restauración de datos

El archivo `db-backup/backup.dump` contiene datos de prueba en formato SQL compatible con PostgreSQL. Para restaurarlo en una base local levantada con Docker:

1. Asegúrate de que el contenedor de PostgreSQL esté en ejecución.
2. Ejecuta la restauración contra la base `library_db`.
3. Verifica que los registros se hayan cargado correctamente.

```bash
docker compose up -d db
docker compose exec -T db psql -U "$DB_USER" -d library_db < db-backup/backup.dump
docker compose exec db psql -U "$DB_USER" -d library_db -c "SELECT COUNT(*) FROM users;"
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
- `POST /api/loans`
