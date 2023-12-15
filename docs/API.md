# API микросервиса аутентификации

## Сессии

### Создание сессии (аутентификация)

Адрес: `/auth/v1/session`  
Метод: `POST`  
Запрос:
```javascript
{
     login: "vasya",             // логин
     password: "pupkin"         // пароль
}
```

Ответ:  
`200 OK`:  
```javascript
{
     id: "CF7A3A10D154E32B8FA1B7DB890EF9E9"
     user: {
          id: 123,                          // идентификатор 
          login: "vasya",                   // логин
          firstName: "Вася",                // имя
          secondName: "Пупкин",             // фамилия
          middleName: "Сергеевич",          // отчество
          mobilePhone: "900 000 00 00",     // мобильный телефон
          email: "mail@mail.ru",            // адрес электронной почты
          position: "ОПР",                  // должность
          office: "",                       // офис
          branch: "",                       // филиал
          personnelNumber: "",              // табельный номер
          rights: ["EDIT_ROLES"]            // назначенненные права
     }
}
```
`400 BAD_REQUEST`: Ошибка валидации
```javascript
response {
     errors:[{ 
          fieldName: "login",                    // название поля, в котором ошибка(nullable)
          errorMessage: "отсутствует логин"     // описание ошибки
     ]}   
}
```
`401 UNAUTHORIZED`: ошибка аутентификации
`406 Not Acceptable`: ошибка состояния сотрудника
```javascript
response {
    type: 'BLOCKED',                        // тип ошибки (BLOCKED, OFFICE_NOT_FOUND, ROLE_NOT_FOUND, EMPTY_ROLE)
    message: 'Пользователь заблокировн'     // серверное сообщение
}
```
`500 INTERNAL_SERVER_ERROR`:  Непредвиденная ошибка при выполнении  
`502 Bad Gateway`: недоступен active directory  
`504 Gateway Timeout`: таймаут к active directory  

### Закрытие сессии

Адрес: `/auth/v1/session/{id}`  
Метод: `DELETE`  
Запрос: -

Ответ:  
`200 OK`:  Сессия закрыта  
`404 NOT_FOUND`: Cессия не существует(или уже закрыта)
`500 INTERNAL_SERVER_ERROR`: Cистемная ошибка


## Сотрудники

### Список сотрудников
Адрес: `/auth/v1/employees`  
Метод: `GET`  
Запрос: 
    `office` - офис сотрудника

Ответ:  
`200 OK`:  
```javascript
{
     employees: [{
        personnelNumber: '0001',             // идентификатор сотрудника
        fullName: "Пупкин Вася Сергеевич",   // полное имя сотрдуника
        roles: [{                            // привязанные роли
            name: "OPR",                     // название роли
            desc: "Начальник отдела программных разработок" // краткое описание для отображения
        }]
    }]
}
```
`403 FORBIDDEN`: Доступ запрещен  
`500 INTERNAL_SERVER_ERROR`: Системная ошибка  

### Информация о сотруднике
Адрес: `/auth/v1/employee`
Метод: `GET`
Запрос:
    `personnelNumber` - персональный номер
    `office`          - офис сотрудника

Ответ:
`200 OK`:
    ```javascript
        {
            login: "vasya",                   // логин
            firstName: "Вася",                // имя
            secondName: "Пупкин",             // фамилия
            middleName: "Сергеевич",          // отчество
            mobilePhone: "900 000 00 00",     // мобильный телефон
            email: "mail@mail.ru",            // адрес электронной почты
            position: "ОПР",                  // должность
            office: "",                       // офис
            branch: "",                       // филиал
            personnelNumber: "",              // табельный номер
            rights: ["EDIT_ROLES"]            // назначенненные права
        }
    ```
`500 INTERNAL_SERVER_ERROR`: Системная ошибка


## Роли и разрешения

### Список прав
Адрес: `/auth/v1/rights`  
Метод: `GET`  
Запрос: -  

Ответ:  
`200 OK`:  
```javascript
{
    rights: [{
          description: "Инициация операции и выполнение функции ввода и печати документов в операции «Открытие вклада»",  // Описание права
          externalId: "CLAIM_OPEN_DEPOSIT_PROCESS",   // Ключ права
     }]  
}
```
`403 FORBIDDEN`: Доступ запрещен  
`500 INTERNAL_SERVER_ERROR`: Системная ошибка  

### Список ролей
Адрес: `/auth/v1/roles`  
Метод: `GET`  
Запрос: -  

Ответ:  
`200 OK`:  
```javascript
{
    roles:[{
       id:  51,                                         // Идентификатор роли
       name: "OPR",                                     // Наименование роли
       desc: "Начальник отдела программных разработок"  // Описание роли
    }]
}
```
`403 FORBIDDEN`: Доступ запрещен  
`500 INTERNAL_SERVER_ERROR`: Системная ошибка  


### Получение данных роли
Адрес: `/auth/v1/roles/{id}`  
Метод: `GET`  
Запрос: -  

Ответ:  
`200 OK`:  
```javascript
{
    id: 123                                            // Идентификатор роли
    name: "OPR",                                       // Название роли
    desc: "Начальник отдела программных разработок",   // Краткое описание(для отображения пользователю)
    rights: [                                          // Назначенные права
        "CLAIM_OPEN_DEPOSIT_PROCESS"                   // Название права
    ]
}
```
`403 FORBIDDEN`: Доступ запрещен  
`500 INTERNAL_SERVER_ERROR`: Системная ошибка  
`404 NOT_FOUND`: Роль не найдена

### Создание роли

Адрес: `/auth/v1/roles`  
Метод: `POST`  
Запрос:
```javascript
{
     name: "OPR",                                       // Название роли  
     desc: "Начальник отдела программных разработок",   // Краткое описание(для отображения пользователю)
     rights: [                                           // Назначенные права
         "CLAIM_OPEN_DEPOSIT_PROCESS"                    // Название права
     ]
}
```

Ответ:  
`200 OK`   
`400 BAD_REQUEST`: Ошибка валидации
```javascript
{
     errors:[{ 
        fieldName: "login",                   // название поля, в котором ошибка(nullable)
        errorMessage: "отсутствует логин"     // описание ошибки
     ]}    
}
```
`403 FORBIDDEN`: Доступ запрещен
`500 INTERNAL_SERVER_ERROR`: Системная ошибка
`409 Conflict`: Роль уже существует и т.п


### Редактирование роли

Адрес: `/auth/v1/roles/{id}`  
Метод: `PUT`  
Запрос:
```javascript
{
    id: 123,                                            // Идентификатор роли
    name: "OPR",                                        // Название роли  
    desc: "Начальник отдела программных разработок",    // Краткое описание(для отображения пользователю)
    rights: [                                           // Назначенные права
        "CLAIM_OPEN_DEPOSIT_PROCESS"                    // Название права
    ]
}
```

Ответ:  
`200 OK`   
`400 BAD_REQUEST`: Ошибка валидации
```javascript
{
     errors:[{ 
        fieldName: "login",                   // название поля, в котором ошибка(nullable)
        errorMessage: "отсутствует логин"     // описание ошибки
     ]}    
}
```
`403 FORBIDDEN`: Доступ запрещен
`500 INTERNAL_SERVER_ERROR`: Системная ошибка
`409 Conflict`: Роль уже существует и т.п
`404 NOT_FOUND`: Роль не найдена

### Удаление роли

Адрес: `/auth/v1/roles/{id}`  
Метод: `DELETE`  
Запрос: -

Ответ:  
`200 OK`:  Роль успешно удалена  
`403 FORBIDDEN`: Доступ запрещен  
`500 INTERNAL_SERVER_ERROR`: Системная ошибка  
