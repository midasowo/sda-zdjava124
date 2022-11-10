package pl.sdacademy.java.hibernate.workshop12;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pl.sdacademy.java.hibernate.common.sakila.Country;
import pl.sdacademy.java.hibernate.utils.ApplicationPropertiesProvider;

import java.util.Properties;
import java.util.Scanner;

/*
    Przygotuj metodę zmieniającą kraj według podanego identyfikatora i nazwy, niech zwraca true jeśli istniał i false jeśli nie (find(...) zwróci null).

    Zmień stan zarządzanego obiektu w trakcie transakcji używając zwykłego settera.

    Przetestuj następujące scenariusze:
        * Kraj nie istnieje (ma zwrócić false).
        * Dowolny inny kraj (ma go zmienić i zwrócić true).
 */
public class Workshop12 {
    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);

        System.out.println("Podaj id kraju:");
        final long countryId = Long.parseLong(scanner.nextLine());
        System.out.println("Podaj nazwę kraju:");
        final String newName = scanner.nextLine();

        final var result = renameCountry(ApplicationPropertiesProvider.getSakilaProperties(), countryId, newName);
        System.out.println("Wynik: " + result);
    }

    public static boolean renameCountry(Properties properties, long countryId, String newName) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SakilaPU", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            final Country country = entityManager.find(Country.class, countryId);

            // Nie powinniśmy wychodzić z metody w trakcie otwartej transakcji!
            if (country == null) {
                return false;
            }

            entityManager.getTransaction().begin();

            country.setName(newName);

            entityManager.getTransaction().commit();
        }
        catch(Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        finally {
            entityManagerFactory.close();
        }

        return true;
    }
}
