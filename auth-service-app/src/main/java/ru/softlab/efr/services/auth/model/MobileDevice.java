package ru.softlab.efr.services.auth.model;

import ru.softlab.efr.services.auth.exchange.model.ShortCodeRequest;

import javax.persistence.*;

@Entity
@Table(name = "user_mobile_devices")
public class MobileDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Employee user;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    private ShortCodeRequest.PlatformEnum mobileDeviceType;

    @Column(name = "code")
    private String code;

    @Column(name = "hashed_pin")
    private String hashedPin;

    @Column(name = "token_uuid")
    private String tokenUuid;

    @Column(name = "is_active")
    private boolean isActive;


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

    public ShortCodeRequest.PlatformEnum getMobileDeviceType() {
        return mobileDeviceType;
    }

    public void setMobileDeviceType(ShortCodeRequest.PlatformEnum mobileDeviceType) {
        this.mobileDeviceType = mobileDeviceType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHashedPin() {
        return hashedPin;
    }

    public void setHashedPin(String hashedPin) {
        this.hashedPin = hashedPin;
    }

    public String getTokenUuid() {
        return tokenUuid;
    }

    public void setTokenUuid(String tokenUuid) {
        this.tokenUuid = tokenUuid;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


    @Override
    public String toString() {
        return "MobileDevice{" +
                "id=" + id +
                ", mobileDeviceType='" + mobileDeviceType + '\'' +
                ", code='" + code + "...\'" +
                ", hashedPin='" + hashedPin + '\'' +
                ", tokenUuid='" + tokenUuid + '\'' +
                ", isActive='" + isActive +
                '}';
    }
}
