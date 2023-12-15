package ru.softlab.efr.services.testapps.someservice.services;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;

/**
 * Эхо-сервис
 */
@Service
public class EchoService {


    /**
     * вернуть строку из параметра
     *
     * @param request запрос
     * @return ответ
     */
    @HasRight(Right.EDIT_ROLES)
    public String echo(String request) {
        return request;
    }
}
