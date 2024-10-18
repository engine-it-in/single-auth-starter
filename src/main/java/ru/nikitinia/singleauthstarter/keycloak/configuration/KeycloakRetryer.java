package ru.nikitinia.singleauthstarter.keycloak.configuration;

import feign.RetryableException;
import feign.Retryer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import ru.nikitinia.singleauthstarter.keycloak.exception.KeycloakProcessingException;

import java.util.concurrent.TimeUnit;

import static ru.nikitinia.singleauthstarter.util.Constant.KeycloakSetting.CACHE_NAME_TOKEN_KEYCLOAK;
import static ru.nikitinia.singleauthstarter.util.Constant.RetryerStatuses.RETRYER_STATUS_LIST;


@Getter
@Slf4j
public class KeycloakRetryer implements Retryer {

    private final int maxAttempts;
    private int currentAttempt;
    private final Retryer defaultRetryer;

    public KeycloakRetryer() {
        this.maxAttempts = 2;
        this.currentAttempt = 1;
        this.defaultRetryer = new Retryer.Default(1000, TimeUnit.SECONDS.toMillis(1), 3);
    }

    @Override
    public void continueOrPropagate(RetryableException exception) {
        if (RETRYER_STATUS_LIST.contains(exception.status())) {
            clearTokenCacheData();
            if (currentAttempt == maxAttempts) {
                throw new KeycloakProcessingException(
                        String.format(
                                "Keycloak. Ошибка при попытке получить данные. Выполнено %d попыток. Статус: %d, содержание: %s",
                                maxAttempts,
                                exception.status(),
                                exception.getMessage()
                        )
                );
            }
            currentAttempt++;
        } else {
            defaultRetryer.continueOrPropagate(exception);
        }
    }

    @Override
    public Retryer clone() {
        return new KeycloakRetryer();
    }

    @CacheEvict(value = CACHE_NAME_TOKEN_KEYCLOAK, allEntries = true)
    public void clearTokenCacheData() {
        log.error("clear token data after get 4xx status");
    }

}
