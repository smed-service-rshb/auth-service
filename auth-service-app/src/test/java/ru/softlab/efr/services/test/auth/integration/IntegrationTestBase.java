package ru.softlab.efr.services.test.auth.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.HttpClientErrorException;
import ru.softlab.efr.services.auth.AuthServiceInfo;

import java.util.concurrent.TimeUnit;

/**
 * @author niculichev
 * @ created 21.04.2017
 * @ $Author$
 * @ $Revision$
 */
public class IntegrationTestBase {
    protected static final long TIMEOUT = 15 * 1000;

    @Value("${server.uri.auth-service}")
    private String authServiceUrl;

    protected static HttpStatus getStatusCode(Exception e) throws Exception {
        if (e instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) e).getStatusCode();
        }

        if (e.getCause() instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) e.getCause()).getStatusCode();
        }

        throw e;
    }

    protected static HttpStatus getStatusCode(ListenableFuture<ResponseEntity<String>> future, long timeoutSeconds) throws Exception {
        try {
            ResponseEntity<?> result = future.get(timeoutSeconds, TimeUnit.SECONDS);
            return result.getStatusCode();
        } catch (Exception e) {
            return getStatusCode(e);
        }
    }

    protected static HttpClient createClient() {
        return HttpClients.
                custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, false))
                .build();
    }

    protected String getServiceUrl() {
        return authServiceUrl + "/" + AuthServiceInfo.SERVICE_NAME + AuthServiceInfo.SERVICE_PREFIX;
    }
}
