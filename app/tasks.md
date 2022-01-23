Tasks for the project.

- Configure registration
  - configure email verification (needs a little research on how to do it in google for devs)
  - add roles (can be done a little later on as well)
  -
- Configure authentication.
  - ~~write jwt token implementation to authenticate user on each request~~
  - ~~add refresh token implementation later on~~
  - write mechanism to request refresh token and update normal token
  - configure session management (might be done later on)
  - configure remember me
  - look into encrypting and decrypting a secret for the application
  - configure logout
- Configure authorization
  - Look into the annotations of spring security  (https://www.baeldung.com/spring-security-method-security)
  - figure out which roles do what, how to join different roles for the correct kind of access

- Cache management (?)
- Facebook API
- Instagram API
- Twitter API (if they allow me to)
- YouTube under question
- Pinterest (not familiar with the API, might want to have a look)

- Refactoring
  - create jwt token service and inject where necessary