# Evidencia 1: Backend Authentication & TLS/SSL

HU-SEC-001. API en Spring Boot que solo acepta HTTPS (TLS 1.2 y 1.3) y protege sus rutas con
Spring Security y tokens JWT.

## Tecnologías

- Java 21, Spring Boot 4.1.1, Maven Wrapper
- Spring Security con OAuth2 Resource Server (validación de JWT firmados con HS256)

## Configuración local

1. Generar el keystore PKCS12 de desarrollo (certificado autofirmado para `localhost`):

```bash
keytool -genkeypair -alias evidencia1 -keyalg RSA -keysize 2048 -storetype PKCS12 \
  -keystore src/main/resources/keystore.p12 -validity 365 \
  -dname "CN=localhost" -ext "SAN=dns:localhost,ip:127.0.0.1" -storepass <PASSWORD>
```

2. Copiar la plantilla y poner la contraseña del keystore y un secreto JWT:

```bash
cp src/main/resources/application-dev.properties.example src/main/resources/application-dev.properties
head -c 32 /dev/urandom | base64    # genera un valor para jwt.secret
```

El keystore y `application-dev.properties` están en `.gitignore`: no se suben.

3. Ejecutar:

```bash
./run.sh
```

La API queda en `https://localhost:8443`.

## Endpoints

| Método | Ruta | Acceso |
|---|---|---|
| POST | `/api/auth/login` | Público. Devuelve el token JWT |
| GET | `/api/profile` | Cualquier usuario autenticado |
| GET | `/api/admin` | Solo rol `ADMIN` |

Usuarios de prueba (en memoria): `user` / `user123` (USER) y `admin` / `admin123` (USER, ADMIN).

CORS permitido para el cliente Angular en `http://localhost:4200`.

## Pruebas con curl

`-k` acepta el certificado autofirmado.

```bash
# Handshake TLS: muestra el protocolo negociado (TLSv1.3 o TLSv1.2)
curl -k -v https://localhost:8443/api/profile 2>&1 | grep -i tls
openssl s_client -connect localhost:8443 </dev/null 2>/dev/null | grep "Protocol"

# TLS 1.1 se rechaza (alerta "protocol version") y queda registrado en el log
openssl s_client -connect localhost:8443 -tls1_1 -cipher 'DEFAULT@SECLEVEL=0' </dev/null

# HTTP sin cifrar se rechaza: 400 "This combination of host and port requires TLS."
curl http://localhost:8443/api/profile

# Login
curl -k -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}' https://localhost:8443/api/auth/login

# Con el token
curl -k -H "Authorization: Bearer <TOKEN>" https://localhost:8443/api/profile
```

| Caso | Respuesta |
|---|---|
| Sin token o token inválido | `401` `{"message":"Se requiere un token Bearer válido","httpStatus":"401 UNAUTHORIZED"}` |
| Login con contraseña incorrecta | `401` `{"message":"Usuario o contraseña incorrectos","httpStatus":"401 UNAUTHORIZED"}` |
| `user` en `/api/admin` | `403` `{"message":"No tienes permiso para acceder a este recurso","httpStatus":"403 FORBIDDEN"}` |
| `admin` en `/api/admin` | `200` |

## Registro de eventos de seguridad

Los 401, 403, logins fallidos y handshakes TLS rechazados se registran en el log como `WARN`/`DEBUG`:

```text
WARN  c.e.exception.SecurityErrorHandler : 403 en GET /api/admin: Access Denied
DEBUG o.a.t.util.net.NioEndpoint.handshake : Handshake failed ... (protocol_version) Client requested protocol TLSv1.1 is not enabled
```
