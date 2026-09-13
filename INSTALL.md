# 📋 Guía de Instalación - Proyecto NES_MUP

## 📌 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

### 1. **Java Development Kit (JDK) 21**
   - **Windows**: Descarga desde [oracle.com](https://www.oracle.com/java/technologies/downloads/)
   - **Verificar instalación**:
     ```bash
     java -version
     ```
   - **Configurar variable de entorno JAVA_HOME** (si no lo hace automáticamente)

### 2. **Apache Maven 3.8.1 o superior**
   - Descarga desde [maven.apache.org](https://maven.apache.org/download.cgi)
   - **Verificar instalación**:
     ```bash
     mvn -version
     ```
   - **Configurar variable de entorno M2_HOME** (si lo necesitas)

### 3. **PostgreSQL 12 o superior** (Base de datos)
   - Descarga desde [postgresql.org](https://www.postgresql.org/download/)
   - **Crear base de datos** (reemplaza los valores según tu configuración):
     ```sql
     CREATE DATABASE nesssoft_db;
     CREATE USER nesssoft_user WITH PASSWORD 'tu_contraseña';
     ALTER ROLE nesssoft_user SET client_encoding TO 'utf8';
     ALTER ROLE nesssoft_user SET default_transaction_isolation TO 'read committed';
     ALTER ROLE nesssoft_user SET timezone TO 'UTC';
     GRANT ALL PRIVILEGES ON DATABASE nesssoft_db TO nesssoft_user;
     ```

### 4. **Node.js y npm** (Para el frontend)
   - Descarga desde [nodejs.org](https://nodejs.org/)
   - **Verificar instalación**:
     ```bash
     node -version
     npm -version
     ```

## 🚀 Instalación del Proyecto

### Paso 1: Clonar o descargar el proyecto
```bash
cd NES_MUP
```

### Paso 2: Backend - Instalar dependencias de Maven
Navega a la carpeta del backend:
```bash
cd "backend PR"
```

Descarga todas las dependencias del archivo `pom.xml`:
```bash
mvn clean install
```

Este comando:
- **clean**: Elimina construcciones previas
- **install**: Descarga todas las dependencias definidas en `pom.xml`

### Paso 3: Configurar la base de datos
Edita el archivo `src/main/resources/application.properties`:

```properties
# Base de datos PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/nesssoft_db
spring.datasource.username=nesssoft_user
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Paso 4: Compilar el backend
```bash
mvn clean compile
```

### Paso 5: Frontend - Instalar dependencias
Navega a la carpeta del frontend:
```bash
cd ../frontend\ PR
```

Instala las dependencias con npm:
```bash
npm install
```

## 📦 Dependencias del Proyecto

### Backend (Maven - Spring Boot 3.3.5)
| Dependencia | Versión | Propósito |
|---|---|---|
| spring-boot-starter-web | 3.3.5 | API REST y Servidor Web |
| spring-boot-starter-data-jpa | 3.3.5 | Acceso a base de datos con JPA |
| postgresql | 42.7.7 | Driver PostgreSQL |
| lombok | Latest | Reducir boilerplate en código Java |

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)

## 🏃 Ejecutar el Proyecto

### Ejecutar Backend
```bash
cd "backend PR"
mvn spring-boot:run
```

El servidor se iniciará en: `http://localhost:8080`

### Ejecutar Frontend
En otra terminal:
```bash
cd "frontend PR"
# Si necesitas un servidor local:
# Windows
python -m http.server 3000
# O macOS/Linux
python3 -m http.server 3000
```

O simplemente abre `login.html` en tu navegador.

## 🛠️ Comandos Útiles de Maven

```bash
# Ver todas las dependencias
mvn dependency:tree

# Ejecutar pruebas
mvn test

# Empaquetar el proyecto
mvn package

# Limpiar cache
mvn clean

# Compilar solamente
mvn compile
```

## 📝 Estructura del Proyecto

```
NES_MUP/
├── backend PR/
│   ├── pom.xml (configuración Maven)
│   ├── src/main/java/com/nesssoft/
│   │   ├── Main.java
│   │   ├── comercial/
│   │   ├── seguridad/
│   │   ├── config/DatabaseConnection.java
│   │   └── ... (otros módulos)
│   ├── src/main/resources/
│   │   └── application.properties
│   └── target/ (generado después de compilar)
├── frontend PR/
│   ├── login.html
│   ├── css/login.css
│   ├── js/login.js
│   └── img/
└── .gitignore
```

## ⚙️ Variables de Entorno (Opcional)

Crea un archivo `.env` en la raíz del proyecto para configurar variables:

```
# Base de datos
DB_HOST=localhost
DB_PORT=5432
DB_NAME=nesssoft_db
DB_USER=nesssoft_user
DB_PASSWORD=tu_contraseña

# Aplicación
SERVER_PORT=8080
APP_NAME=NES_MUP
```

## 🔍 Solución de Problemas

### Error: "mvn command not found"
- Asegúrate que Maven está instalado correctamente
- Reinicia la terminal después de instalar Maven

### Error: "No suitable driver found for jdbc"
- Verifica que PostgreSQL está corriendo
- Comprueba las credenciales en `application.properties`

### Error: "Connection refused"
- Verifica que PostgreSQL está iniciado
- En Windows: Services > PostgreSQL > Iniciar

### Error de puerto 8080 en uso
- Cambia el puerto en `application.properties`:
  ```properties
  server.port=8081
  ```

## ✅ Verificar Instalación

Una vez completados todos los pasos, verifica que todo está correcto:

```bash
# Terminal 1 - Backend
cd "backend PR"
mvn spring-boot:run
# Debe mostrar: Started Main in X.XXX seconds

# Terminal 2 - Verificar que el servidor está activo
curl http://localhost:8080
```

## 📞 Soporte

Si encuentras problemas:
1. Verifica que Java 21 está instalado: `java -version`
2. Verifica que Maven está instalado: `mvn -version`
3. Verifica que PostgreSQL está corriendo
4. Revisa los logs en la consola para más detalles

---

**Última actualización**: 2024
**Versión del proyecto**: 1.0-SNAPSHOT
