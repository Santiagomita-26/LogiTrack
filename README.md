# LogiTrack S.A. 📦

**Sistema de gestión y control de inventario para bodegas**

LogiTrack S.A. es una aplicación web diseñada para administrar y controlar las operaciones de inventario de una empresa. Permite gestionar productos, marcas, ciudades, bodegas, usuarios y movimientos de mercancía, manteniendo un registro organizado de las operaciones realizadas.

El sistema busca facilitar el control de existencias, mejorar la trazabilidad de los movimientos y proporcionar información útil mediante consultas y reportes.

---

## 📋 Tabla de contenidos

- [Características principales](#-características-principales)
- [Tecnologías utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del proyecto](#-arquitectura-del-proyecto)
- [Módulos del sistema](#-módulos-del-sistema)
- [Base de datos](#-base-de-datos)
- [Seguridad y autenticación](#-seguridad-y-autenticación)
- [Reglas de negocio](#-reglas-de-negocio)
- [Documentación de la API](#-documentación-de-la-api)
- [Instalación y ejecución](#-instalación-y-ejecución)
- [Estructura del proyecto](#-estructura-del-proyecto)

---

## 🚀 Características principales

- Gestión de usuarios y control de acceso por roles.
- Registro, consulta, actualización y eliminación de información.
- Administración de productos y marcas.
- Administración de ciudades y bodegas.
- Control de inventario por producto y bodega.
- Registro de entradas, salidas y transferencias de mercancía.
- Validación de existencias antes de realizar salidas.
- Validación de capacidad máxima de las bodegas.
- Registro de auditoría de las operaciones.
- Generación de reportes para consultar el estado del inventario.
- Autenticación mediante JWT.
- Contraseñas protegidas mediante BCrypt.
- Documentación de endpoints mediante Swagger/OpenAPI.

## 🛠️ Tecnologías utilizadas

### Backend

| Tecnología | Uso |
|---|---|
| Java | Lenguaje de programación |
| Spring Boot | Desarrollo de la API REST |
| Spring Data JPA | Persistencia y acceso a datos |
| Hibernate | Mapeo objeto-relacional |
| Spring Security | Seguridad y autorización |
| JWT | Autenticación mediante tokens |
| BCrypt | Encriptación de contraseñas |
| Maven | Gestión de dependencias |
| Swagger / OpenAPI | Documentación de la API |

### Base de datos

- MySQL

### Frontend

- HTML5
- CSS3
- JavaScript
- Fetch API para la comunicación con el backend.

---

## 🏗️ Arquitectura del proyecto

El backend utiliza una arquitectura por capas, separando las responsabilidades para facilitar el mantenimiento, la organización y la escalabilidad del sistema.

| Capa | Responsabilidad |
|---|---|
| Controller | Recibe las solicitudes HTTP y devuelve las respuestas. |
| Service | Contiene la lógica de negocio. |
| Repository | Gestiona el acceso a la base de datos. |
| Model | Representa las entidades del sistema. |
| DTO | Define los datos que entran y salen de la API. |
| Mapper | Convierte entidades en DTO y viceversa. |
| Config | Contiene configuraciones de seguridad, JWT y documentación. |
| Exception | Gestiona los errores y excepciones del sistema. |

La comunicación entre las capas sigue este flujo:

```text
Frontend
   |
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
MySQL
```

Los DTO permiten controlar la información que recibe y devuelve la API sin exponer directamente las entidades de persistencia.

---

## 📦 Módulos del sistema

### 1. Usuarios

Permite gestionar las cuentas que tienen acceso al sistema.

Funcionalidades:

- Registro de nuevos usuarios.
- Inicio de sesión.
- Consulta de usuarios.
- Consulta de solicitudes pendientes.
- Aprobación y rechazo de solicitudes.
- Actualización de información de usuarios.
- Control de roles y estados.

**Roles disponibles:**

- `ADMIN`: administrador del sistema.
- `USUARIO`: usuario autorizado para utilizar las funcionalidades permitidas.

**Estados de usuario:**

- `PENDIENTE`
- `APROBADO`
- `RECHAZADO`

Los nuevos registros se crean con el rol `USUARIO` y estado `PENDIENTE`. Un administrador debe aprobarlos antes de que puedan iniciar sesión.

### 2. Ciudades

Permite administrar las ciudades y sus respectivos departamentos.

Funcionalidades:

- Registrar ciudades.
- Consultar ciudades.
- Buscar ciudades por identificador.
- Actualizar información.
- Eliminar registros.

### 3. Marcas

Administra las marcas asociadas a los productos.

Funcionalidades:

- Registrar marcas.
- Listar marcas.
- Consultar una marca por identificador.
- Actualizar marcas.
- Eliminar marcas.

### 4. Productos

Permite gestionar los productos que maneja la empresa.

Información principal:

- Identificador.
- Nombre.
- Categoría.
- Marca.
- Precio.

El precio se maneja mediante `BigDecimal` para conservar la precisión de los valores monetarios.

### 5. Bodegas

Permite administrar los espacios de almacenamiento.

Información principal:

- Identificador.
- Nombre.
- Dirección.
- Capacidad.
- Ciudad asociada.

Cada bodega tiene una capacidad máxima que determina la cantidad total de unidades que puede almacenar.

### 6. Inventario

Controla las existencias de cada producto dentro de cada bodega.

El inventario se encuentra relacionado con:

- Una bodega.
- Un producto.
- Una cantidad disponible.

El stock no se modifica directamente mediante operaciones CRUD. Las existencias se actualizan a través del módulo de movimientos para conservar la trazabilidad de las operaciones.

### 7. Movimientos

Registra las operaciones que modifican las existencias de los productos.

**Tipos de movimientos:**

| Tipo | Descripción |
|---|---|
| ENTRADA | Aumenta el inventario de una bodega. |
| SALIDA | Disminuye el inventario de una bodega. |
| TRANSFERENCIA | Traslada unidades entre dos bodegas. |

Cada movimiento registra información como el producto, la cantidad, el usuario responsable, las bodegas involucradas y la fecha.

Las operaciones se ejecutan mediante transacciones para mantener la consistencia de los inventarios.

### 8. Auditoría

Registra las operaciones realizadas sobre las entidades del sistema.

Permite conservar información sobre:

- Tipo de operación.
- Entidad afectada.
- Valores anteriores.
- Valores nuevos.
- Usuario responsable, cuando corresponde.
- Fecha de la operación.

Los tipos de operación contemplados son:

- `INSERT`
- `UPDATE`
- `DELETE`

### 9. Reportes

El sistema dispone de consultas para obtener información útil sobre las operaciones y las existencias.

Reportes implementados:

1. **Inventario con bajo stock:** identifica productos con existencias iguales o inferiores a 10 unidades.
2. **Movimientos por rango de fechas:** permite consultar las operaciones realizadas entre dos fechas.
3. **Inventario por bodega:** presenta el inventario agrupado por bodega y el valor correspondiente.
4. **Productos más movidos:** identifica los productos con mayor cantidad de movimientos registrados.

---

## 🗄️ Base de datos

El proyecto utiliza MySQL y una base de datos denominada `spmendoza`.

Está compuesta por las siguientes tablas principales:

| Tabla | Descripción |
|---|---|
| `ciudad` | Almacena ciudades y departamentos. |
| `usuario` | Guarda la información de los usuarios. |
| `bodega` | Almacena las bodegas y sus capacidades. |
| `marca` | Registra las marcas de los productos. |
| `producto` | Almacena la información de los productos. |
| `inventario` | Relaciona productos, bodegas y existencias. |
| `movimiento` | Registra las entradas, salidas y transferencias. |
| `auditoria` | Conserva el historial de operaciones. |

### Relaciones principales

- Una ciudad puede estar asociada a varias bodegas.
- Una marca puede estar asociada a varios productos.
- Una bodega puede contener varios productos.
- Un producto puede encontrarse en diferentes bodegas.
- Un usuario puede registrar diferentes movimientos.
- Los movimientos se relacionan con un producto y las bodegas involucradas.
- La auditoría conserva el historial de las operaciones del sistema.

La relación entre productos y bodegas se administra mediante la tabla `inventario`.

---

## 🔐 Seguridad y autenticación

LogiTrack utiliza Spring Security y JSON Web Tokens (JWT) para proteger los recursos de la API.

### Registro

Los usuarios pueden solicitar acceso mediante el endpoint público:

```http
POST /auth/register
```

Las contraseñas se almacenan utilizando BCrypt. El sistema no guarda las contraseñas originales en texto plano.

### Inicio de sesión

```http
POST /auth/login
```

Cuando las credenciales son correctas y el usuario está aprobado, el sistema genera un token JWT.

El token debe enviarse en las solicitudes protegidas mediante el encabezado:

```http
Authorization: Bearer <TOKEN>
```

### Autorización

El acceso a las funcionalidades se controla según el rol del usuario.

- Las operaciones administrativas están restringidas al rol `ADMIN`.
- Las operaciones de consulta y las demás funcionalidades requieren autenticación según las reglas configuradas en Spring Security.
- Las rutas de registro, inicio de sesión y documentación de Swagger son públicas.

---

## ⚙️ Reglas de negocio

El sistema implementa validaciones para proteger la integridad de la información.

### Validación de stock

No se permite realizar una salida o transferencia si el inventario de origen no tiene suficientes unidades.

### Validación de capacidad de las bodegas

No se permite agregar mercancía cuando el inventario total de la bodega supera su capacidad máxima.

**Ejemplo:**

| Concepto | Cantidad |
|---|---:|
| Capacidad de la bodega | 500 |
| Inventario actual | 420 |
| Nueva entrada | 100 |
| Inventario resultante | 520 |

La operación se rechaza porque supera la capacidad permitida.

La validación también se aplica a las transferencias, comprobando la capacidad de la bodega de destino.

### Validación de movimientos

- La cantidad debe ser mayor que cero.
- Una entrada requiere únicamente una bodega destino.
- Una salida requiere únicamente una bodega origen.
- Una transferencia requiere una bodega de origen y una de destino.
- No se permiten transferencias entre una misma bodega.
- Se debe comprobar la existencia del producto, usuario y bodegas involucradas.

### Transacciones

Las operaciones que modifican el inventario se gestionan mediante `@Transactional`, evitando que se guarden cambios parciales cuando ocurre un error durante la operación.

---

## 📖 Documentación de la API

La API cuenta con documentación mediante Swagger/OpenAPI.

Una vez iniciado el backend, se puede acceder desde:

```text
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger es posible:

- Consultar los endpoints disponibles.
- Revisar los parámetros de cada solicitud.
- Conocer los DTO de entrada y respuesta.
- Ejecutar solicitudes HTTP.
- Probar los endpoints protegidos utilizando el token JWT.

---

## 💻 Instalación y ejecución

### Requisitos previos

Antes de ejecutar el proyecto, es necesario contar con:

- Java compatible con la versión configurada en `pom.xml`.
- Maven.
- MySQL.
- Un entorno de desarrollo como IntelliJ IDEA, Eclipse o Visual Studio Code.

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

Ingresar al directorio:

```bash
cd LogiTrack
```

### 2. Crear la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE spmendoza;
```

Ejecutar el script SQL de creación de tablas y los datos iniciales del proyecto, si se encuentran disponibles.

### 3. Configurar la conexión

Abrir:

```text
src/main/resources/application.properties
```

Configurar la conexión a MySQL con los datos de tu entorno:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spmendoza
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

Verificar también la configuración de JPA y Hibernate utilizada por el proyecto.

### 4. Instalar dependencias

Desde la terminal, ejecutar:

```bash
mvn clean install
```

### 5. Ejecutar el backend

```bash
mvn spring-boot:run
```

El backend estará disponible en:

```text
http://localhost:8080
```

### 6. Ejecutar el frontend

Abrir el proyecto frontend con Live Server o un servidor local equivalente.

Si se utiliza Live Server con la configuración actual:

```text
http://127.0.0.1:5500
```

El frontend se comunica con el backend mediante solicitudes HTTP a la API REST.

---

## 📁 Estructura del proyecto

La organización principal del backend es la siguiente:

```text
LogiTrack/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/s2/LogiTrack/
│   │   │       │
│   │   │       ├── auth/
│   │   │       │
│   │   │       ├── config/
│   │   │       │   ├── JwtFilter.java
│   │   │       │   ├── JwtService.java
│   │   │       │   ├── SecurityConfig.java
│   │   │       │   └── OpenApiConfig.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       │
│   │   │       ├── exception/
│   │   │       │
│   │   │       ├── mapper/
│   │   │       │
│   │   │       ├── model/
│   │   │       │
│   │   │       ├── repository/
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── interfaces/
│   │   │       │   └── impl/
│   │   │       │
│   │   │       └── LogiTrackApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

---

## 🎯 Objetivo del proyecto

LogiTrack S.A. busca ofrecer una solución centralizada para la administración de inventarios, facilitando el control de mercancía en diferentes bodegas y permitiendo mantener un historial de las operaciones realizadas.

Mediante la integración de tecnologías como Java, Spring Boot, MySQL y JavaScript, el proyecto implementa una solución organizada, segura y orientada a las necesidades de gestión de inventario de una empresa.

---

**LogiTrack S.A. — Control, organización y trazabilidad de inventario.**