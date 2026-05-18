# Evaluación Final - OrderFlow - Respuestas.

**Estudiante:** Andre Jesus Cruz Gonzales

---

## P1. ¿Por qué SecurityConfig no está en domain o application?

Porque no es parte del negocio, sino es parte de la capa de infraestructura porque pertenece al Framework Spring Security),
si se colocaria dentro del domain o application estas capas quedarían acopladas a Framework y ahi rompe la arquitectura Hexagonal, el domain y application deben ser independientes del Framework.

---

## P2. ¿Qué pasaría si un usuario envía un JWT válido pero sin ningún rol de Keycloak? ¿Podría acceder a GET /api/orders/{id}? Justifica tu respuesta con la configuración actual.

Sí, si podria acceder ya que se utiliza en el SecurityConfig `.requestMatchers(HttpMethod.GET, "/api/orders/**").authenticated()`
esta regla (authenticated) solo verifica que el usuario este autenticado, es decir tengo un token (JWT) valido, por tanto aunque el token no tenga ningun rol el usuario puede acceder al endpoint.
Si se deseara restringir con un rol se debe cambiar por `.requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("ADMIN", "USER")`.

---

## P3. ¿Qué función cumple KeycloakRoleConverter y qué sucedería si no existiera?

Extraer los roles del claim del JWT que nos da Keycloak (`realm_access`) y convertirlo a un rol aceptado por springboot ya que los `GrantedAuthority` deben tener el prefijo `ROLE_`
**si no existiera la KeycloakRoleConverter**, la autenticación sería exitosa pero las validaciones de los roles no podrian realizarse ya que en el claims de Keycloak tiene `USER` y `ADMIN` y springboot debe leer `ROLE_USER`, `ROLE_ADMIN`.

---

## P4. Explica la diferencia entre 401 Unauthorized y 403 Forbidden en el contexto de este proyecto. Da un ejemplo concreto de cuándo ocurre cada uno.

1. **401 Unauthorized**: Es cuando el usuario no tiene un token o el token es invalido (no esta autenticado). Ejemplo: Si quiero llamar al enpoint `POST: /api/orders` me responde 401 ya que no se ingresa un Bearer Token.
2. **403 Forbidden**: Es cuando el usuario esta autenticado pero no tiene permisos para el recurso que quiere acceder. Ejemplo: `POST: /api/orders` (Crear orden) posee un **hasAnyRole** ADMIN lo cual solo permite el rol ADMIN, si posee un rol USER u otro rol no podria acceder a este recurso. 