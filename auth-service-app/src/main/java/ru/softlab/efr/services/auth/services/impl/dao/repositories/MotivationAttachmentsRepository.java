package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.auth.exchange.model.MotivationDocumentTypes;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.MotivationAttachmentsEntity;
import ru.softlab.efr.services.auth.model.MotivationEntity;

import java.util.List;

@Repository
public interface MotivationAttachmentsRepository extends JpaRepository<MotivationAttachmentsEntity, Long> {

    MotivationAttachmentsEntity getByUserAndMotivationEntityAndIsDeletedAndMotivationDocumentTypes(Employee user, MotivationEntity motivation, Boolean deleted, MotivationDocumentTypes motivationDocumentTypes);

    MotivationAttachmentsEntity getByMotivationEntityAndIsDeletedAndMotivationDocumentTypes(MotivationEntity motivation, Boolean deleted, MotivationDocumentTypes motivationDocumentTypes);

    List<MotivationAttachmentsEntity> getByMotivationEntityAndIsDeleted(MotivationEntity motivation, Boolean deleted);

    /**
     * Получить контент вложения
     *
     * @param id Идентификатор вложения
     * @return контент
     */
    @Query(value = "SELECT content from {h-schema}motivation_program_attachments where id=:id", nativeQuery = true)
    byte[] getContent(@Param("id") Long id);

    /**
     * Обновить контент вложения
     *
     * @param identifier Идентификатор вложения
     * @param content    контент
     */
    @Modifying
    @Query(value = "update {h-schema}motivation_program_attachments set content =:content where id=:id", nativeQuery = true)
    void setContent(@Param("id") Long identifier, @Param("content") byte[] content);
}
