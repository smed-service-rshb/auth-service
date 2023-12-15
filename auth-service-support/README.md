## Библиотека для поддержки механизма проверки прав.

### Описание

Данная библиотека предназначена для реализации механизма проверки прав пользователя в контексте микросервисной архитектуры.
Основная функциональность библиотеки:
* Установка контекста пользователя с возможностью его использования
* Проверка прав посредством аннотации и предоставление инструмента для ручной проверки

### Подключение к проекту

```xml
<dependency>
	<groupId>ru.softlab.efr.authentication</groupId>
    <artifactId>auth-service-support</artifactId>
    <version>{версия}</version>
</dependency>
```
### Настройка

#### 1. Модель БД
Для работы библиотеки необходима служебная таблица `rights_to_permission`.
В ней задается соответствие разрешения и права.

Для ее создания достаточно включить [auth-service-app/build/scripts/auth-service-support-model.sql](/auth-service-app/build/scripts/auth-service-support-model.sql) в число скриптов по созданию модели БД сервиса.
Данный скрипт (вместе с созданием служебной таблицы) создает процедуру, помогающую в настройке соответствия разрешения и права.
Процедура принимает два параметра: ключ разрешения и массив прав, в рамках которых доступно разрешение.
Пример использования:
```postgresplsql
SELECT setup_permission_rights('get-rights', ARRAY ['EDIT_ROLES', 'VIEW_ROLES']);
SELECT setup_permission_rights('get-roles', ARRAY ['EDIT_ROLES', 'VIEW_ROLES']);
```

#### 2. Установка DataSource

Для библиотеки необходимо наличие в spring-контексте датасурса. 
Он используется для сохранения в базе настроек, связанных с установленными правами. 
В библиотеке не предусмотрено указание датасурса, отличного от текущего (который используется нуждами  самого сервиса)

Пример установки датасоурса:
```java
@Bean
public DataSource dataSource() {
	String name = env.getProperty(PROPERTY_NAME_HIBERNATE_CONNECTION_DATASOURCE);
    DataSource dataSource = null;
    try {
    	dataSource = new JndiTemplate().lookup(name, DataSource.class);
    } catch (NamingException e) {
    	log.error(String.format("Datasource %s not found", name), e);
    }
    return dataSource;
}
```

#### 3. Создание разрешений

Разрешения представляют собой строковые константы.

Пример создания разрешений:

```java
public class Permissions {
    /**
     * Разрешение на редактирование сущности
     */
    public static final String PERMISSION1 = "Permission1";

    /**
     * Разрешение на удаление сущности
     */
    public static final String PERMISSION2 = "Permission2";
}
```

#### 4. Включение функциональности

Чтобы библиотека начала функционировать необходимо пометить любую конфигурацию аннотацией ```@EnablePermissionControl```. Например:
```java
@EnablePermissionControl
```
Параметром ```source``` можно указать имя бина типа ```ru.softlab.efr.services.authorization.PrincipalData``` для кастомной реализации хранилища данных принципала. 
По-умолчанию данных храняться в контексте запроса (http request).

### Использование

#### 1. Проверка разрешений

Проверка разрешений осуществляется на этапе вызова методов контроллера, поэтому для проверки определенного разрешения необходимо пометить метод соответствующей аннотацией. Например:

```java
@RequestMapping(value = "/test-message", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
@HasPermission(Permissions.PERMISSION1)
public ResponseEntity<?> getTestMessage() {
	...
}    
```
Также поддерживается дизъюнкция и конъюнкция разрешений через параметры: or и and.

#### 2. Проверка парв

Проверка прав осуществляется на этапе вызова методов контроллера, поэтому для проверки определенного права необходимо пометить метод соответствующей аннотацией. Например:

```java
@RequestMapping(value = "/test-message", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
@HasRight(Right.VIEW_ROLES)
public ResponseEntity<?> getTestMessage() {
	...
}    
```
В качестве параметра аннотации передается значение енума [Right](/auth-service-model/src/main/java/ru/softlab/efr/services/auth/Right.java), содержащего все возможные права.

#### 3. Данные пользователя

Получить данные пользователя можно следующим образом:
```java
@Autowired
private PrincipalDataSource principalDataSource;
```

#### 4. Контекст безопасности

Для проверки прав вручную можно воспользоваться классом ```ru.softlab.efr.services.authorization.SecurityContext```

```java
@Autowired
private SecurityContext securityContext;
```

