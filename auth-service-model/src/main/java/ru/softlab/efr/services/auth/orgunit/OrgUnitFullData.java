package ru.softlab.efr.services.auth.orgunit;

import java.util.List;

/**
 * Полные данные орг. структуры (для списочных структур)
 */
public class OrgUnitFullData{

    private Long branchId;
    private String name;
    private List<OfficeData> offices;

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OfficeData> getOffices() {
        return offices;
    }

    public void setOffices(List<OfficeData> offices) {
        this.offices = offices;
    }
}
