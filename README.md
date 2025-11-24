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

- Url del tablero Trello: https://trello.com/b/trpHWEhr/desarrollo-de-aplicaciones-moviles
- Url del repositorio Github: https://github.com/7Naai/Heartz-App
