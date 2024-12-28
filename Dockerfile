# Usar una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo build.gradle y el directorio gradle al contenedor
COPY --chown=gradle:gradle ./.gradle /home/gradle/.gradle
COPY --chown=gradle:gradle ./build.gradle /app

# Cambiar al usuario gradle
USER gradle

# Descargar todas las dependencias
RUN gradle --no-daemon dependencies

# Volver al usuario root
USER root

# Copiar el resto de la aplicación al contenedor
COPY . /app

# Construir la aplicación
RUN gradle --no-daemon build

# Establecer el puerto en el que la aplicación escuchará
ENV PORT 8080

# Exponer el puerto en el que la aplicación escuchará
EXPOSE $PORT

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/build/libs/TiendaInventario-0.0.1-SNAPSHOT.jar"]
