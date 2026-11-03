# CodingKit

CodingKit is a full-stack Spring Boot application for managing coding resources, learning notes, saved YouTube history, and website shortcuts.

## Stack
- Java 21
- Spring Boot 3.3.x
- Spring Security
- Spring Data JPA
- MySQL
- Thymeleaf + Bootstrap 5

## Run
1. Configure MySQL and set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` environment variables.
2. Run:

```bash
mvn spring-boot:run
```

## Deploy to Vercel

This Spring Boot application uses Vercel's beta Container Runtime, which requires a Vercel account that can access that feature, a GitHub repository, and an externally reachable MySQL database.

1. Create a hosted MySQL database. The MySQL server on your local computer cannot be reached by Vercel.
2. Push this project to a GitHub repository.
3. In Vercel, import the repository as a new project. Set the framework to **Container** if Vercel does not select it automatically. The project includes `Dockerfile.vercel` at its root.
4. In the Vercel project settings, add these environment variables for the Production environment:
   - `DB_URL`: JDBC URL for the hosted database, for example `jdbc:mysql://HOST:PORT/DATABASE?sslMode=REQUIRED&serverTimezone=UTC`. Use the connection details and SSL requirements supplied by your database provider.
   - `DB_USERNAME`: database username.
   - `DB_PASSWORD`: database password.
5. Deploy from Vercel and check the deployment logs. Open the generated URL and test registration, login, and saving data.

Do not commit database credentials to GitHub. Vercel's Container Runtime is in beta and may scale an idle application down; this application stores web login sessions in the app process, so users may need to sign in again after an instance restarts.

## Notes
This project includes a basic implementation of the dashboard, auth flow, profile page, history page, and secure user data storage.
