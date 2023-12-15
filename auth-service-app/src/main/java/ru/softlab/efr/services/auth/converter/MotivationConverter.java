package ru.softlab.efr.services.auth.converter;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.exchange.model.MotivationProgramAdmin;
import ru.softlab.efr.services.auth.exchange.model.MotivationProgramUser;
import ru.softlab.efr.services.auth.model.MotivationEntity;

import java.time.ZoneId;
import java.util.Objects;

@Service
public class MotivationConverter {

    public MotivationProgramAdmin convertToAdminMotivation(MotivationEntity motivationByUserId) {
        MotivationProgramAdmin motivationProgramAdmin = new MotivationProgramAdmin();
        if (Objects.nonNull(motivationByUserId.getId())) {
            motivationProgramAdmin.setId(motivationByUserId.getId());
            motivationProgramAdmin.setUserId(motivationByUserId.getUser().getId());
            if (Objects.nonNull(motivationByUserId.getStartDate())) {
                motivationProgramAdmin.setStartDate(motivationByUserId.getStartDate().toString());
            }
            if (Objects.nonNull(motivationByUserId.getEndDate())) {
                motivationProgramAdmin.setEndDate(motivationByUserId.getEndDate().toString());
            }
            motivationProgramAdmin.setAccountNumber(motivationByUserId.getAccountNumber());
            motivationProgramAdmin.setBikCode(motivationByUserId.getBikCode());
            motivationProgramAdmin.setBankName(motivationByUserId.getBankName());
            motivationProgramAdmin.setInn(motivationByUserId.getInn());
            motivationProgramAdmin.setRegistrationAddress(motivationByUserId.getRegistrationAddress());
            motivationProgramAdmin.setIndex(motivationByUserId.getIndex());
            motivationProgramAdmin.setComment(motivationByUserId.getComment());
            motivationProgramAdmin.setMotivationCorrectStatus(motivationByUserId.getMotivationCorrectStatus());
            motivationProgramAdmin.setIsActive(motivationByUserId.isActive());
        }
        return motivationProgramAdmin;
    }

    public MotivationProgramUser convertToUserMotivation(MotivationEntity motivationBySession) {
        MotivationProgramUser motivationProgramUser = new MotivationProgramUser();
        if (Objects.nonNull(motivationBySession.getId())) {
            motivationProgramUser.setId(motivationBySession.getId());
            if (Objects.nonNull(motivationBySession.getStartDate())) {
                motivationProgramUser.setStartDate(motivationBySession.getStartDate().toString());
            }
            motivationProgramUser.setAccountNumber(motivationBySession.getAccountNumber());
            motivationProgramUser.setBikCode(motivationBySession.getBikCode());
            motivationProgramUser.setBankName(motivationBySession.getBankName());
            motivationProgramUser.setInn(motivationBySession.getInn());
            motivationProgramUser.setRegistrationAddress(motivationBySession.getRegistrationAddress());
            motivationProgramUser.setIndex(motivationBySession.getIndex());
            motivationProgramUser.setComment(motivationBySession.getComment());
            motivationProgramUser.setMotivationCorrectStatus(motivationBySession.getMotivationCorrectStatus());
        }
        return motivationProgramUser;
    }
}
