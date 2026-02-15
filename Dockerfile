# Usamos apenas a imagem do Java para rodar o arquivo pronto
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o arquivo .jar que o seu Maven local gerou com sucesso na pasta target
COPY target/grafica-system-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]