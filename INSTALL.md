# 📋 Guía de Instalación - Proyecto NES_MUP

> ℹ️ **Nota para usuarios de macOS (Mac Neo)**: Este proyecto es completamente compatible con macOS en arquitectura ARM64. Usa Homebrew para instalar las herramientas de forma sencilla.

## ⚡ Instalación Rápida para macOS (usando Homebrew)

Si eres usuario de macOS y quieres instalar todo rápidamente:

```bash
# 1. Instalar Homebrew (si no lo tienes)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 2. Instalar todas las herramientas
brew install openjdk@21 maven postgresql node

# 3. Iniciar PostgreSQL
brew services start postgresql

# 4. Crear la base de datos
psql -U postgres << EOF
CREATE DATABASE nesssoft_db;
CREATE USER nesssoft_user WITH PASSWORD 'tu_contraseña';
ALTER ROLE nesssoft_user SET client_encoding TO 'utf8';
ALTER ROLE nesssoft_user SET default_transaction_isolation TO 'read committed';
ALTER ROLE nesssoft_user SET timezone TO 'UTC';
GRANT ALL PRIVILEGES ON DATABASE nesssoft_db TO nesssoft_user;
EOF

# 5. Clonar/descargar el proyecto y navegar a él
cd NES_MUP

# 6. Instalar dependencias del backend
cd \"backend PR\"
mvn clean install

# 7. Instalar dependencias del frontend
cd ../\"frontend PR\"
npm install

# ¡Listo! El proyecto está instalado
```

---

## 📌 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

### 1. **Java Development Kit (JDK) 21**
   
   **Windows**:
   - Descarga desde [oracle.com](https://www.oracle.com/java/technologies/downloads/)
   - Ejecuta el instalador y sigue los pasos
   - Configurar variable de entorno JAVA_HOME (si no lo hace automáticamente)
   
   **macOS**:
   - Usando Homebrew (recomendado):
     ```bash
     brew install openjdk@21
     ```
   - O descarga desde [oracle.com](https://www.oracle.com/java/technologies/downloads/) (versión ARM64 para Mac Neo)
   - Configurar JAVA_HOME en `~/.zshrc`:
     ```bash
     echo 'export JAVA_HOME=$(brew --prefix openjdk@21)' >> ~/.zshrc
     source ~/.zshrc
     ```
   
   **Verificar instalación** (Windows, macOS y Linux):
   ```bash
   java -version
   ```

### 2. **Apache Maven 3.8.1 o superior**
   
   **Windows**:
   - Descarga desde [maven.apache.org](https://maven.apache.org/download.cgi)
   - Extrae el archivo ZIP
   - Configura la variable de entorno M2_HOME
   
   **macOS**:
   - Usando Homebrew (recomendado):
     ```bash
     brew install maven
     ```
   - O descarga manualmente desde [maven.apache.org](https://maven.apache.org/download.cgi)
   
   **Verificar instalación** (Windows, macOS y Linux):
   ```bash
   mvn -version
   ```

### 3. **PostgreSQL 12 o superior** (Base de datos)
   
   **Windows**:
   - Descarga desde [postgresql.org](https://www.postgresql.org/download/)
   - Ejecuta el instalador y sigue los pasos
   - El servicio se iniciará automáticamente
   
   **macOS**:
   - Usando Homebrew (recomendado):
     ```bash
     brew install postgresql
     brew services start postgresql
     ```
   - O descarga desde [postgresql.org](https://www.postgresql.org/download/)
   
   **Crear base de datos** (Windows, macOS y Linux - reemplaza los valores según tu configuración):
   ```sql
   CREATE DATABASE nesssoft_db;
   CREATE USER nesssoft_user WITH PASSWORD 'tu_contraseña';
   ALTER ROLE nesssoft_user SET client_encoding TO 'utf8';
   ALTER ROLE nesssoft_user SET default_transaction_isolation TO 'read committed';
   ALTER ROLE nesssoft_user SET timezone TO 'UTC';
   GRANT ALL PRIVILEGES ON DATABASE nesssoft_db TO nesssoft_user;
   ```
   
   **Acceder a PostgreSQL**:
   - Windows: Usa pgAdmin o psql
   - macOS: 
     ```bash
     psql -U postgres
     ```

### 4. **Node.js y npm** (Para el frontend)
   
   **Windows**:
   - Descarga desde [nodejs.org](https://nodejs.org/)
   - Ejecuta el instalador
   
   **macOS**:
   - Usando Homebrew (recomendado):
     ```bash
     brew install node
     ```
   - O descarga desde [nodejs.org](https://nodejs.org/)
   
   **Verificar instalación** (Windows, macOS y Linux):
   ```bash
   node --version
   npm --version
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

**Windows**:
```bash
cd ../frontend\ PR
```

**macOS/Linux**:
```bash
cd ../frontend\ PR
# o sin espacios escapados
cd "../frontend PR"
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

**Windows**:
```bash
cd "frontend PR"
python -m http.server 3000
```

**macOS/Linux**:
```bash
cd "frontend PR"
python3 -m http.server 3000
```

Luego abre `login.html` en tu navegador: `http://localhost:3000`

O simplemente abre `login.html` directamente en tu navegador si no necesitas un servidor.

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
**Windows**:
- Asegúrate que Maven está instalado correctamente
- Verifica que M2_HOME está en las variables de entorno
- Reinicia la terminal después de instalar Maven

**macOS**:
- Si usaste Homebrew: `brew install maven`
- Verifica: `which mvn` (debe mostrar la ruta)
- Si no, agrega a `~/.zshrc`: `export PATH="$(brew --prefix maven)/bin:$PATH"`

### Error: "No suitable driver found for jdbc"
- Verifica que PostgreSQL está corriendo
- Comprueba las credenciales en `application.properties`
- En macOS: `brew services list` (PostgreSQL debe estar "started")

### Error: "Connection refused" en PostgreSQL
**Windows**:
- Verifica que PostgreSQL está iniciado: Services > PostgreSQL > Iniciar

**macOS**:
- Inicia PostgreSQL: `brew services start postgresql`
- Verifica estado: `brew services list`
- Para detenerlo: `brew services stop postgresql`

### Error de puerto 8080 en uso
- Cambia el puerto en `application.properties`:
  ```properties
  server.port=8081
  ```

### macOS: Error de permisos con Homebrew
- Asegúrate que Homebrew está instalado correctamente
- Si no: `ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`

### macOS: M1/M2/M3 (Mac Neo) - Arquitectura ARM64
- Asegúrate descargar versiones compatibles con ARM64
- Al instalar con Homebrew, se instala automáticamente la versión correcta
- Si descargas manualmente, busca la versión "ARM 64" o "Apple Silicon"

## ✅ Verificar Instalación

Una vez completados todos los pasos, verifica que todo está correcto:

**Verificar componentes individuales**:
```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Node.js
node --version
npm --version

# macOS: Verificar PostgreSQL
brew services list
# Debe mostrar "postgresql started"

# O conectarse a PostgreSQL
psql -U postgres -c "SELECT version();"
```

**Verificar el proyecto completo**:
```bash
# Terminal 1 - Backend
cd "backend PR"
mvn spring-boot:run
# Debe mostrar: Started Main in X.XXX seconds

# Terminal 2 - Verificar que el servidor está activo
# Windows/macOS/Linux
curl http://localhost:8080
# O en el navegador: http://localhost:8080
```

## 📞 Soporte

Si encuentras problemas:
1. Verifica que Java 21 está instalado: `java -version`
2. Verifica que Maven está instalado: `mvn -version`
3. Verifica que PostgreSQL está corriendo
4. Revisa los logs en la consola para más detalles

## 🖥️ Compatibilidad de Sistemas Operativos

| Sistema | Versión | Estado | Notas |
|---------|---------|--------|-------|
| **Windows** | 10, 11 | ✅ Compatible | Usa PowerShell o CMD |
| **macOS (Intel)** | 10.15+ | ✅ Compatible | Usa Homebrew o descargas manuales |
| **macOS (Apple Silicon/M1/M2/M3)** | 11+ | ✅ Compatible | **Mac Neo** - Asegúrate de descargar versiones ARM64 |
| **Linux (Ubuntu/Debian)** | 18.04+ | ✅ Compatible | Usa apt-get o similares |

### 🍎 Notas Específicas para Mac Neo (M1/M2/M3)

Este proyecto es totalmente compatible con procesadores Apple Silicon. Solo necesitas asegurar lo siguiente:

1. **Java 21 ARM64**: 
   - Si usas Homebrew: `brew install openjdk@21` (automáticamente correcto)
   - Si descargas manualmente: Busca la opción "Arm 64" en oracle.com

2. **Maven**: 
   - Homebrew: `brew install maven` (compatible automáticamente)
   - Manual: Descarga la versión genérica (funciona en todos lados)

3. **PostgreSQL**:
   - Homebrew: `brew install postgresql` (ARM64 automático)
   - Manual: Descarga la versión macOS para "Apple Silicon"

4. **Verificar arquitectura**:
   ```bash
   uname -m
   # Debe mostrar: arm64
   ```

---

**Última actualización**: 2024
**Versión del proyecto**: 1.0-SNAPSHOT
**Compatibilidad**: Windows, macOS (Intel/Apple Silicon), Linux
