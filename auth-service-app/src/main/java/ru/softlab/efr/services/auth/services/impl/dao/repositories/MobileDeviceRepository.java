package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.auth.model.MobileDevice;

@Repository
public interface MobileDeviceRepository  extends JpaRepository<MobileDevice, Long> {

    MobileDevice getByCode(String code);

    MobileDevice getByTokenUuidAndHashedPin(String tokenUuid, String hashedPin);

}
