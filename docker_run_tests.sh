#!/bin/bash

# build params
# $1 - http port number

# Запускаем контейнер интеграционных тестов (с ожидание ответа от ключевых систем), результат выполнения интеграционных тестов будет сохранен в директорию билда
docker-compose -f docker-compose.test.yml -f docker-compose.integration-tests.yml run -u $(id -u) integration-test \
  ../wait-for-it.sh auth-service-app:$1 -s -t 100 -- \
  ../wait-for-it.sh 172.16.34.167:390 -s -t 100 --  \
  mvn verify