package ru.softlab.efr.services.auth.model;

import ru.softlab.efr.services.auth.exchange.model.MotivationDocumentTypes;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "motivation_program_attachments")
public class MotivationAttachmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Employee user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "motivation_program_id")
    private MotivationEntity motivationEntity;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "motivation_document_type")
    @Enumerated(EnumType.STRING)
    private MotivationDocumentTypes motivationDocumentTypes;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Employee getUser() {
        return user;
    }
    public void setUser(Employee user) {
        this.user = user;
    }
    public MotivationEntity getMotivationEntity() {
        return motivationEntity;
    }
    public void setMotivationEntity(MotivationEntity motivationEntity) {
        this.motivationEntity = motivationEntity;
    }
    public Boolean getDeleted() {
        return isDeleted;
    }
    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public MotivationDocumentTypes getMotivationDocumentTypes() {
        return motivationDocumentTypes;
    }
    public void setMotivationDocumentTypes(MotivationDocumentTypes motivationDocumentTypes) {
        this.motivationDocumentTypes = motivationDocumentTypes;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public void setCreationDate() {
        this.creationDate = LocalDateTime.now();
    }
}
