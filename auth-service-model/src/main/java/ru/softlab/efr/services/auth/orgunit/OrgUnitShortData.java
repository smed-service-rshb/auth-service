package ru.softlab.efr.services.auth.orgunit;

/**
 * Сокращенные данные орг. структуры (для списочных структур)
 */
public class OrgUnitShortData{

    private Long id;
    private String office;
    private String branch;

    /**
     * Возвращает уникальный идентификатор в БД для ОШС
     * @return уникальный идентификатор в БД для ОШС
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает номер внутреннего структурного подразделения
     * @return номер внутреннего структурного подразделения
     */
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * Возвращает наименование регионального филиала
     * @return наименование регионального филиала
     */
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}
