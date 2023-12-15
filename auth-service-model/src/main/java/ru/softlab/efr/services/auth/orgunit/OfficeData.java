package ru.softlab.efr.services.auth.orgunit;

/**
 * Полные данные по офису (для списочных структур)
 */
public class OfficeData{

    private Long officeId;
    private String name;
    private String city;

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
