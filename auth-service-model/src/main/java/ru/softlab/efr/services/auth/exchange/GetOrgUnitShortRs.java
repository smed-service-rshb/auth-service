package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.orgunit.OrgUnitShortData;

import java.util.ArrayList;
import java.util.List;

/**
 * Тело ответа на запрос сокращённого списка орг. структур
 */
public class GetOrgUnitShortRs{
    private List<OrgUnitShortData> orgUnits = new ArrayList<>();

    public List<OrgUnitShortData> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<OrgUnitShortData> orgUnits) {
        this.orgUnits = orgUnits;
    }
}
