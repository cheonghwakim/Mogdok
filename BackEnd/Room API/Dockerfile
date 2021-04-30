# 이 스프링 부트 앱이 openjdk 8 위에서 실행된다는 뜻
FROM openjdk:8

# Add Author info
LABEL maintainer="doingnow94@gmail.com"

# VOLUME이라는 디렉토리를 지정하고, 이 디렉토리의 /tmp 아래에 컨테이너가 필요한 여러가지 데이터를 저장한다
VOLUME /tmp

# 컨테이너 내부에서 이 앱이 가질 포트는 8084이며, 따라서 이 포트를 외부로 노출시킨다
EXPOSE 8084

# JAR 파일의 경로를 적고, 이름을 붙여준다. (여기서는 app.jar)
ARG JAR_FILE=target/roomapi-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} mongdok-roomapi-0.0.1.jar

# 앱을 실행시키기 위한 명령어
ENTRYPOINT ["java", "-jar", "/mongdok-roomapi-0.0.1.jar"]