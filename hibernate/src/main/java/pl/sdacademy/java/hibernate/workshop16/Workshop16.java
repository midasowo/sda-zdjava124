package pl.sdacademy.java.hibernate.workshop16;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pl.sdacademy.java.hibernate.common.sakila.City;
import pl.sdacademy.java.hibernate.common.sakila.Country;
import pl.sdacademy.java.hibernate.utils.ApplicationPropertiesProvider;

import java.util.Properties;
import java.util.Scanner;

public class Workshop16 {
    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);

        System.out.println("Podaj id kraju:");
        final long countryId = Long.parseLong(scanner.nextLine());
        System.out.println("Podaj nazwę kraju:");
        final String newName = scanner.nextLine();

        final var affectedRows = renameCountry(ApplicationPropertiesProvider.getSakilaProperties(), countryId, newName);
        System.out.println("Wynik: " + affectedRows);
    }

    public static int renameCountry(Properties properties, long countryId, String newName) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SakilaPU", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            final var query = entityManager.createQuery(
                """
                    update Country country set country.name = :newName
                    where country.countryId = :countryId
                """
            );

             query.setParameter("newName", newName);
             query.setParameter("countryId", countryId);
             int result = query.executeUpdate();

            entityManager.getTransaction().commit();

            return result;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            entityManagerFactory.close();
        }
    }
}
