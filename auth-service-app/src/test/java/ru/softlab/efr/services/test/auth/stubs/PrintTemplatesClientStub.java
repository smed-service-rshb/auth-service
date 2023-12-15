package ru.softlab.efr.services.test.auth.stubs;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.softlab.efr.common.client.PrintTemplatesClient;
import ru.softlab.efr.common.dict.exchange.model.PrintTemplate;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

/**
 * Заглушка для клиента для получения данных по печатным формам (common-dict).
 *
 * @author Andrey Grigorov
 */
public class PrintTemplatesClientStub extends PrintTemplatesClient {

    public PrintTemplatesClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public Resource getContent(String type, long timeout) {
        return new ClassPathResource("templates/" + type + ".docx");
    }
}
