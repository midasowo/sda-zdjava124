package pl.sdacademy.java.hibernate.workshop14;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pl.sdacademy.java.hibernate.common.sakila.City;
import pl.sdacademy.java.hibernate.common.sakila.Country;
import pl.sdacademy.java.hibernate.utils.ApplicationPropertiesProvider;

import java.util.Properties;
import java.util.Scanner;

/*
    Przygotuj metodę przyjmującą identyfikator kraju i nazwę miasta, niech zwraca obiekt zarządzany.

    Uzyskaj obiekt kraju przez referencję.

    Utwórz i utrwal obiekt miasta o podanej nazwie używając referencji do kraju.

    Przetestuj działanie programu. Zauważ, że obiekt kraju nie zostanie leniwie doładowany.
 */
public class Workshop14 {
    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);

        System.out.println("Podaj id kraju:");
        final long countryId = Long.parseLong(scanner.nextLine());
        System.out.println("Podaj nazwę miasta:");
        final String name = scanner.nextLine();

        final var city = createCity(ApplicationPropertiesProvider.getSakilaProperties(), name, countryId);
        System.out.printf("name: %s, id: %d\n", city.getName(), city.getCityId());
    }

    public static City createCity(Properties properties, String name, long countryId) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SakilaPU", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            final City city = new City();
            city.setName(name);

            Country countryReference = entityManager.getReference(Country.class, countryId);
            city.setCountry(countryReference);

            entityManager.getTransaction().begin();

            entityManager.persist(city);

            entityManager.getTransaction().commit();

            return city;
        }
        catch(Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        finally {
            entityManagerFactory.close();
        }
    }
}
