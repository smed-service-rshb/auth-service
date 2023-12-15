package ru.softlab.efr.services.auth.exchange;

/**
 * Тело ответа с идентификатором сущности (например, при создании роли)
 *
 * @author niculichev
 * @since 30.04.2017
 */
public class EntryIdRs {
    private Long id;

    public EntryIdRs(){
    }

    public EntryIdRs(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
