package pl.sdacademy.java.hibernate.common.sakila;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class City {
    @Id
    @Column(name = "city_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @Column(name = "city")
    private String name;
    @ManyToOne(cascade = { CascadeType.PERSIST }) //w {} jest tablica - wartości może być więcej
    @JoinColumn(name = "country_id")
    private Country country;
    @OneToMany(mappedBy = "city")
    private List<Address> addresses;

    public Long getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
