package ru.softlab.efr.services.auth.model;

import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "motivation_program")
public class MotivationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Employee user;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bik_code")
    private String bikCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "inn")
    private String inn;

    @Column(name = "registration_address")
    private String registrationAddress;

    @Column(name = "index")
    private String index;

    @Column(name = "comment")
    private String comment;

    @Column(name = "motivation_status")
    @Enumerated(EnumType.STRING)
    private MotivationCorrectStatus motivationCorrectStatus;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "last_edited")
    private Date lastEdited;



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
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate date) {
        this.endDate = date;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getBikCode() {
        return bikCode;
    }
    public void setBikCode(String bikCode) {
        this.bikCode = bikCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getInn() {
        return inn;
    }
    public void setInn(String inn) {
        this.inn = inn;
    }
    public String getRegistrationAddress() {
        return registrationAddress;
    }
    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public MotivationCorrectStatus getMotivationCorrectStatus() {
        return motivationCorrectStatus;
    }
    public void setMotivationCorrectStatus(MotivationCorrectStatus motivationCorrectStatus) {
        this.motivationCorrectStatus = motivationCorrectStatus;
    }
    public Date getLastEdited() {
        return lastEdited;
    }
    public void setLastEdited() {
        this.lastEdited = new Date();
    }
}
