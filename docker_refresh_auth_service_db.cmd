call docker-compose stop auth-service-db
call docker-compose rm --force auth-service-db
call docker image rm --force docker.dos.softlab.ru/smedservice/auth-service/dev/auth-service-db:default
call docker-compose build auth-service-db
call docker-compose up -d --no-deps auth-service-db