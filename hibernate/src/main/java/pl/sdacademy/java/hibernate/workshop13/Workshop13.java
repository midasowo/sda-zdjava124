package pl.sdacademy.java.hibernate.workshop13;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pl.sdacademy.java.hibernate.common.sakila.Country;
import pl.sdacademy.java.hibernate.utils.ApplicationPropertiesProvider;

import java.util.Properties;
import java.util.Scanner;

/*
    Przygotuj metodę przyjmującą identyfikator i nazwę kraju, niech zwraca obiekt zarządzany.

    Utwórz obiekt kraju o podanej nazwie i id (dodaj setter jeśli nie ma).

    Wykonaj na nim operację merge(...) w transakcji.

    Przetestuj następujące scenariusze:
        * Kraj nie istnieje (ma powstać nowy)
        * Kraj istnieje (ma zostać zmieniony)
 */
public class Workshop13 {
    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);

        System.out.println("Podaj id kraju:");
        final long countryId = Long.parseLong(scanner.nextLine());
        System.out.println("Podaj nazwę kraju:");
        final String name = scanner.nextLine();

        final var country = new Country();
        country.setCountryId(countryId);
        country.setName(name);

        final var managedCountry = mergeCountry(ApplicationPropertiesProvider.getSakilaProperties(), country);
        System.out.println("Id kraju: " + managedCountry.getCountryId());
    }

    public static Country mergeCountry(Properties properties, Country country) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SakilaPU", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            var managedCountry = entityManager.merge(country);

            entityManager.getTransaction().commit();

            return managedCountry;
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
