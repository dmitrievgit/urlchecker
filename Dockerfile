FROM eclipse-temurin:17-jre-focal

ADD build/libs/*.jar /usr/share/dmitrievaa/url-checker.jar

USER 65534

WORKDIR /usr/share/dmitrievaa
ENTRYPOINT ["java", "-Duser.language=ru", "-Duser.country=RU", "-jar", "/usr/share/dmitrievaa/url-checker.jar"]
