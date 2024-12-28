# Usar una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo build.gradle al contenedor
COPY build.gradle /app

# Copiar el resto de la aplicación al contenedor
COPY . /app

# Asegurarse de que gradlew tenga permisos de ejecución
RUN chmod +x gradlew

# Descargar todas las dependencias y construir la aplicación
RUN ./gradlew build --no-daemon

# Establecer el puerto en el que la aplicación escuchará
ENV PORT 8080

# Exponer el puerto en el que la aplicación escuchará
EXPOSE $PORT

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/build/libs/TiendaInventario-0.0.1-SNAPSHOT.jar"]
