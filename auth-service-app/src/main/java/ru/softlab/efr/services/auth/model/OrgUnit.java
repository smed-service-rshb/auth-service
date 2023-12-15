package ru.softlab.efr.services.auth.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;


/**
 * Сущность для хранения данных организационной штатной структуры.
 */
@Entity
@Table(name = "orgunit")
public class OrgUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Родительское подразделение.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "parent", nullable = true)
    private OrgUnit parent;

    /**
     * Тип орг.структуры, допустимые значения:
     * РФ (региональный филиал),
     * ВСП (внутреннее структурное подразделение).
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrgUnitType type;

    /**
     * Название регионального филиала (РФ)
     * (заполняется, если type = РФ)
     * или Номер внутреннего структурного подразделения (ВСП)
     * (заполняется, если type = ВСП).
     */
    @Column(name = "name", nullable = true)
    private String name;

    /**
     * Адрес внутреннего структурного подразделения (ВСП).
     */
    @Column(name = "address", nullable = true)
    private String address;

    /**
     * Город.
     */
    @Column(name = "city", nullable = true)
    private String city;

    /**
     * Подчиненные подразделения.
     */
    @OneToMany(mappedBy = "parent")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<OrgUnit> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public OrgUnit getParent() {
        return parent;
    }

    public void setParent(OrgUnit parent) {
        this.parent = parent;
    }

    public OrgUnitType getType() {
        return type;
    }

    public void setType(OrgUnitType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<OrgUnit> getChildren() {
        return children;
    }

    public void setChildren(List<OrgUnit> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "OrgUnit{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
