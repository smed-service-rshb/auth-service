# Микросервис аутентификации

### 1. API для взаимодействия с сервисом
[rest-api](docs/API.md)

### 2. Механиз проверки прав

[efr-authorization-support](efr-authorization-support/README.md)

### 3. Тестовая утилита

[auth-service-test](auth-service-test/README.md)

## Docker

[Работа с docker](docs/docker.md)

## Публикация артефактов в maven-репозитории (http://coral:18088/)
Для выпуска нового релиза и публикации артефактов в maven-репозитории необходимо выполнить следующие команды.
```bash
mvn release:prepare -Dmaven.test.skip=true -DskipITs -DskipTests=true -Darguments=-DskipTests
mvn release:perform -Dmaven.test.skip=true -DskipITs -DskipTests=true -Darguments=-DskipTests
```
Команда `release:prepare` выполнит следующие действия:
* Внесение изменений в файлы pom.xml. В указании версий артефактов убирается суффикс `-SNAPSHOT`. Например, `1.0.19-SNAPSHOT` заменяеся на `1.0.19`.
* Выполняеся commit выполненных изменений в git-репозиторий. Выполненному commit'у присваивается таг с именем, соответствующем версии релиза.
* Внесение изменений в файлы pom.xml. Для артефактов поднимается номер версии, добавляется суффикс `-SNAPSHOT`.
* Выполняеся commit выполненных изменений в git-репозиторий.

Команда `release:perform` выполняет публикацию изменений в maven-репозиторий на сервере Coral (http://coral:18088/).
Публикаци будет выполняться под учётной записью, указанной в файле `~/.m2/settings.xml`.
```xml
<?xml version="1.0"?>
<settings> 
  <profiles>
   <profile>
     <id>coral</id>
     <repositories>
       <repository>
         <id>efr-repository</id>
         <url>http://coral:18088/repository/efr/</url>
       </repository>
       <repository>
         <id>archetype</id>
         <url>http://coral:18088/repository/efr/</url>
       </repository>
     </repositories>
   </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>coral</activeProfile>
  </activeProfiles>

  <servers>
   <server>
      <id>coral</id>
      <username>username</username>
      <password>password</password>
    </server>
  </servers>
</settings>
```
Если у вас нет учётной записи, то обратитесь к администраторам maven-репозитория на Coral для того, чтобы её создали.