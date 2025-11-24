# HeartzApp (V1.0)

## 1. Integrantes
- Alan Fuentes 
- Ian Gomez

## 2. Funcionalidades
- Gestión de usuarios (Administrador, Empleado y Cliente)
- Inventario de Vinilos
- Visualización de datos desde microservicios
- Interfaz amigable y navegación fluida

## 3. Endpoints utilizados

### Microservicios propios (Usuarios)

- `GET /usuarios` → Lista de usuarios
- `GET /usuarios/{rut}` → Obtener usuario por RUT
- `POST /usuarios` → Crear usuario
- `PUT /usuarios/{rut}` → Actualizar usuario
- `DELETE /usuarios/{rut}` → Eliminar usuario


### Microservicios propios (Vinilos)
- `GET /vinilos` → Lista todos los vinilos
- `GET /vinilos/{id}` → Obtener vinilo por ID
- `POST /vinilos` → Crear vinilo
- `PUT /vinilos/{id}` → Actualizar vinilo
- `DELETE /vinilos/{id}` → Eliminar vinilo

### Microservicios propios (Carrito)
- `GET /carrito` → Lista todos los items del carrito
- `POST /carrito` → Agrega item al carrito
- `DELETE /carrito/{id}` → Elimina item del carrito
- `DELETE /carrito` → Vacía el carrito

## 4. Pasos para ejecutar

### Requisitos previos
- Android Studio 2023+
- IntelliJ IDEA (para microservicios Spring Boot)
- JDK 17+
- Gradle 8+
- MySQL (XAMPP u otro servidor)
- Conexión a internet (para API externa)

### Base de datos
1. Abrir XAMPP y levantar MySQL.
2. Crear la base de datos si no existe.
3. Importar tablas o scripts SQL necesarios para los microservicios.

### Backend (microservicios)
1. Abrir el proyecto Spring Boot en IntelliJ.
2. Configurar `application.properties` o `application.yml` con:
    - URL de la base de datos
    - Puerto del servidor
3. Ejecutar el microservicio
4. Verificar que los en

## 5. Captura del APK firmado y .jks
<img width="511" height="597" alt="apk_firmado" src="https://github.com/user-attachments/assets/c2b2a3c4-4f82-44cf-92e6-92abde65d6f2" />
<img width="1567" height="705" alt="commit_firmado" src="https://github.com/user-attachments/assets/95556077-46b6-492f-8f79-0a1edd8b7965" />


- Url del tablero Trello: https://trello.com/b/trpHWEhr/desarrollo-de-aplicaciones-moviles
- Url del repositorio Github: https://github.com/7Naai/Heartz-App

## Trello

<img width="1597" height="764" alt="tablero_trello" src="https://github.com/user-attachments/assets/040dc879-3a9b-43c0-8677-4a45ddf55000" />

## Commits Github

- Alan Fuentes
<img width="1035" height="743" alt="commits_alan" src="https://github.com/user-attachments/assets/4c2b238f-a50c-420a-8566-bbc8de6f9e2a" />

- Ian Gomez
<img width="1048" height="712" alt="commits_ian" src="https://github.com/user-attachments/assets/fc047a49-2190-4ec1-9592-7f917dd53e50" />
<img width="1069" height="525" alt="commit_ian_api" src="https://github.com/user-attachments/assets/ad1fe55f-8e10-4ce6-8001-5548bdf18aaf" />


