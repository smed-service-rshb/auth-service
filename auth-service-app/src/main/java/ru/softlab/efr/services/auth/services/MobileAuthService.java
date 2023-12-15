package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.exchange.model.ShortCodeRequest;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.MobileDevice;
import ru.softlab.efr.services.auth.model.Session;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.MobileDeviceRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;

import java.util.Objects;
import java.util.Optional;

/**
 * Сервис авторизации запросов, связанных с мобильными устройствами клиентов.
 *
 * @author danilov
 * @since 26.02.2019
 */
@Service
public class MobileAuthService {
    private static final Logger LOGGER = Logger.getLogger(MobileAuthService.class);

    private MobileDeviceRepository mobileDeviceRepository;
    private UserRepository userRepository;

    @Autowired
    public MobileAuthService(MobileDeviceRepository mobileDeviceRepository,
                             UserRepository userRepository) {
        this.mobileDeviceRepository = mobileDeviceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String registerNewDevice(Session sessionData, ShortCodeRequest shortCodeRequest) {
        Employee currentUser = userRepository.findById(sessionData.getOwnerId());
        String tokenUuid = PasswordGenerationUtil.getSalt();
        String pinHash = PasswordGenerationUtil.generateSecurePassword(shortCodeRequest.getShortCode(), tokenUuid);
        MobileDevice mobileDevice = Optional.ofNullable(mobileDeviceRepository.getByCode(shortCodeRequest.getIdentifierDevice())).orElse(new MobileDevice());

        if (Objects.isNull(mobileDevice.getId())) {
            LOGGER.info(String.format("Регистрация %s устройства для пользователя %s", shortCodeRequest.getIdentifierDevice(), currentUser.getLogin()));
            mobileDevice.setUser(currentUser);
            mobileDevice.setMobileDeviceType(shortCodeRequest.getPlatform());
            mobileDevice.setCode(shortCodeRequest.getIdentifierDevice());
            mobileDevice.setHashedPin(pinHash);
            mobileDevice.setTokenUuid(tokenUuid);
            mobileDevice.setIsActive(true);
            mobileDeviceRepository.save(mobileDevice);
        } else {
            LOGGER.info(String.format("Модификация данных уже имеющегося %s устройства для пользователя %s", shortCodeRequest.getIdentifierDevice(), currentUser.getLogin()));
            mobileDevice.setUser(currentUser);
            mobileDevice.setHashedPin(pinHash);
            mobileDevice.setTokenUuid(tokenUuid);
            mobileDevice.setIsActive(true);
            mobileDeviceRepository.save(mobileDevice);
        }

        return tokenUuid;
    }

    @Transactional(readOnly = true)
    public Employee getEmployerForRegisteredDevice(String tokenUuid, String pin) {
        String pinHash = PasswordGenerationUtil.generateSecurePassword(pin, tokenUuid);
        MobileDevice mobileDevice = mobileDeviceRepository.getByTokenUuidAndHashedPin(tokenUuid, pinHash);
        if (Objects.nonNull(mobileDevice)) {
            if (mobileDevice.getIsActive()) {
                return mobileDevice.getUser();
            } else {
                LOGGER.warn(String.format("Устройство %s пользователя %s неактивно", mobileDevice.getCode(), mobileDevice.getUser().getLogin()));
                return null;
            }
        } else {
            LOGGER.warn(String.format("Устройство с UUID %s не найдено", tokenUuid));
            return null;
        }
    }

}
