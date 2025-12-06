Para testear en local, la imagen de docker es:

docker pull registry.gitlab.com/diegog0477/directorio-springboot:latest

Para ejecución:

docker run -d -p 8080:8080 registry.gitlab.com/diegog0477/directorio-springboot:latest

Endpoints:

base: http://localhost:8080

swagger: http://localhost:8080/swagger-ui.html

h2 database console: http://localhost:8080/h2-console
