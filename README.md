# Микросервис аутентификации

### 1. API для взаимодействия с сервисом
[rest-api](docs/API.md)

### 2. Механизм проверки прав

[efr-authorization-support](efr-authorization-support/README.md)

### 3. Тестовая утилита

[auth-service-test](auth-service-test/README.md)

### 4. Docker

[Работа с docker](docs/docker.md)

## Публикация артефактов в [maven-репозиторий](http://nexus.dos.softlab.ru/)

Для выпуска нового релиза и публикации артефактов в maven-репозитории необходимо выполнить следующие команды:

```bash
mvn release:prepare -Dmaven.test.skip=true -DskipITs -DskipTests=true -Darguments=-DskipTests
mvn release:perform -Dmaven.test.skip=true -DskipITs -DskipTests=true -Darguments=-DskipTests
```

Команда `release:prepare` выполнит следующие действия:
* Внесение изменений в файлы pom.xml: из версий артефактов убирается суффикс `-SNAPSHOT`. Например, `1.0.19-SNAPSHOT` заменяется на `1.0.19`.
* Коммит выполненных изменений в гит-репозиторий и пуш в удалённую ветку. Для выполнения указанных действий может понадобиться SSH-ключ. Инструкцию по его генерации и добавлении в аккаунт можно посмотреть [тут](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent).
* Создание тега по выполненному коммиту с наименованием `v-${release.version}`. Например, наименование тега для релиза с номером `1.0.19` будет выглядеть так: `v-0.0.1`.
* Внесение изменений в файлы pom.xml. Для артефактов поднимается номер версии, добавляется суффикс `-SNAPSHOT`.
* Выполняется коммит и пуш выполненных изменений в гит-репозиторий.

Допускается так же проделать вышеперечисленные действия вручную при возможном возникновении ошибок, связанных с maven, или, например с SSH - при попытке запушить изменения в удалённую ветку в репозиторий.

Команда `release:perform` выполняет публикацию изменений в maven-репозиторий на [сервере Nexus](http://nexus.dos.softlab.ru/). Публикация будет выполняться под учётной записью, указанной в файле `~/.m2/settings.xml`. Для этого необходимо добавить следующее:
```xml
<settings> 
    ...
    <servers>
        <server>
            <id>coral</id>
            <username>username</username>
            <password>password</password>
        </server>
    </servers>
    ...
</settings>
```
где `username` - логин в [Nexus](http://nexus.dos.softlab.ru/), а `password`, соответственно, пароль. Если у вас нет учётной записи, обратитесь к администраторам maven-репозитория для того, чтобы её создали.

#### Внимание
При ошибке выполнении команды 2 можно выполнить checkout по созданному тегу и затем команду:
```bash
mvn deploy
```