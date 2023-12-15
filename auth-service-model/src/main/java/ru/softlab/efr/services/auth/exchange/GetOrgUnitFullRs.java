package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.orgunit.OrgUnitFullData;

import java.util.ArrayList;
import java.util.List;

/**
 * Тело ответа на запрос полного списка орг. структур
 */
public class GetOrgUnitFullRs{
    private List<OrgUnitFullData> orgUnits = new ArrayList<>();

    public List<OrgUnitFullData> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<OrgUnitFullData> orgUnits) {
        this.orgUnits = orgUnits;
    }
}
