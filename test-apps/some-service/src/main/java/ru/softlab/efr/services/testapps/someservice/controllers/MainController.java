package ru.softlab.efr.services.testapps.someservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.SecurityContext;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.testapps.someservice.config.Permissions;
import ru.softlab.efr.services.testapps.someservice.services.EchoService;

/**
 * Контроллер проверки интеграции с сервисом аутентификации
 *
 * @author niculichev
 * @since 23.05.2017
 */
@RestController
public class MainController {

    @Autowired
    private PrincipalDataSource principalDataSource;

    @Autowired
    private SecurityContext securityContext;

    private final EchoService echoService;

    /**
     * Конструктор
     *
     * @param echoService эхо-сервис
     */
    public MainController(EchoService echoService) {
        this.echoService = echoService;
    }

    /**
     * Запрос проверки ru.softlab.efr.services.authorization.annotations.HasPermission
     *
     * @return результат проверки
     */
    @RequestMapping(value = "/test-message", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @HasPermission(Permissions.PERMISSION1)
    public ResponseEntity<?> getTestMessage() {
        return ResponseEntity.ok().body("getTestMessage test message");
    }

    /**
     * Запрос проверки ru.softlab.efr.services.authorization.annotations.HasPermission (с использованием or)
     *
     * @return результат проверки
     */
    @RequestMapping(value = "/test-or-permission", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @HasPermission(or = {Permissions.PERMISSION1, Permissions.PERMISSION2})
    public ResponseEntity<?> orPermission() {
        return ResponseEntity.ok().body("orPermission test message");
    }

    /**
     * Запрос проверки ru.softlab.efr.services.authorization.annotations.HasPermission (с использованием and)
     *
     * @return результат проверки
     */
    @RequestMapping(value = "/test-and-permission", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @HasPermission(and = {Permissions.PERMISSION1, Permissions.PERMISSION2})
    public ResponseEntity<?> andPermission() {
        return ResponseEntity.ok().body("andPermission test message");
    }

    /**
     * Запрос проверки ru.softlab.efr.services.authorization.annotations.HasRight
     *
     * @return результат проверки
     */
    @RequestMapping(value = "/test-hasRight", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @HasRight(Right.EDIT_ROLES)
    public ResponseEntity<?> hasRolesEditRight() {
        return ResponseEntity.ok().body("hasRolesEditRight test message");
    }

    /**
     * Запрос проверки ru.softlab.efr.services.authorization.PrincipalDataSource#getPrincipalData()
     *
     * @return результат проверки
     */
    @RequestMapping(value = "/principal-data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getPrincipalData() {
        return ResponseEntity.ok().body(principalDataSource.getPrincipalData());
    }

    /**
     * Запрос проверки ru.softlab.efr.services.authorization.SecurityContext#implies(java.lang.String)
     *
     * @param permissionName проверяемое разрешение
     * @return результат проверки
     */
    @RequestMapping(value = "/imply/{permissionName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> isImplyPermission(@PathVariable String permissionName) {
        return ResponseEntity.ok().body(securityContext.implies(permissionName));
    }

    /**
     * Запрос проверки ru.softlab.efr.services.authorization.SecurityContext#implies(ru.softlab.efr.services.auth.Right)
     *
     * @param rightName проверяемое право
     * @return результат проверки
     */
    @RequestMapping(value = "/imply-right/{rightName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> isImplyRight(@PathVariable String rightName) {
        return ResponseEntity.ok().body(securityContext.implies(Right.valueOf(rightName)));
    }

    /**
     * Запрос проверки прокидывания исключений
     *
     * @param message сообщение об ошшибке
     * @return респонз
     */
    @GetMapping("/exception/{message}")
    public Void exception(@PathVariable String message) {
        throw new SomeException(message);
    }

    /**
     * Запрос проверки прав для сервисов
     *
     * @param message сообщение
     * @return респонз
     */
    @GetMapping("/service/{message}")
    public String callService(@PathVariable String message) {
        return echoService.echo(message);
    }
}
