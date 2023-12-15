## Модуль утилит тестирования сервисов с учетом механизма авторизации

### Утилита [AuthorizedRestRule]
Утилита [AuthorizedRestRule] является оберткой для `MockRestRule`, поставляемой транспортной компонентой.
При инициализации запроса (методы `init`) в него добавляется информация об авторизации.

#### Методы инициализации тестового запроса
`ru.softlab.efr.test.services.auth.rest.AuthorizedRestRule.init()` - запрос с данными авторизации клиента с ролью полных прав

`ru.softlab.efr.test.services.auth.rest.AuthorizedRestRule.init(ru.softlab.efr.services.auth.Right...)` - запрос с данными авторизации клиента с переданными правами

`ru.softlab.efr.test.services.auth.rest.AuthorizedRestRule.init(ru.softlab.efr.services.authorization.PrincipalData)` - запрос с данными авторизации переданным клиентом


### Конфиг [AuthTestConfiguration]
Для регистриции утилиты необходимо подключить конфиг [AuthTestConfiguration]. 
Например:
```java
@Import({AuthTestConfiguration.class})
```

### Примеры
- Конфиг [TestClientConfiguration]
- Тесты [PermissionDataIntegrationTest]


[AuthTestConfiguration]: /auth-service-test/src/main/java/ru/softlab/efr/test/services/auth/rest/config/AuthTestConfiguration.java
[AuthorizedRestRule]: /auth-service-test/src/main/java/ru/softlab/efr/test/services/auth/rest/AuthorizedRestRule.java
[TestClientConfiguration]: /test-apps/some-service/src/main/java/ru/softlab/efr/services/test/testapp/someservice/config/TestClientConfiguration.java
[PermissionDataIntegrationTest]: /test-apps/some-service/src/main/java/ru/softlab/efr/services/test/testapp/someservice/PermissionDataIntegrationTest.java
