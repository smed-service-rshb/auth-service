call docker-compose stop auth-service-app
call docker-compose rm --force auth-service-app
call mvn clean package -Dmaven.test.skip=true -DskipITs -DskipTests=true -Darguments=-DskipTests -Dmaven.javadoc.skip=true
call docker image rm --force docker.dos.softlab.ru/smedservice/auth-service/dev/auth-service-app:default
call docker-compose build auth-service-app
call docker-compose up -d --no-deps auth-service-app