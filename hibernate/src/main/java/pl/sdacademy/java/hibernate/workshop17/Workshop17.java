package pl.sdacademy.java.hibernate.workshop17;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.sdacademy.java.hibernate.common.sakila.Staff;
import pl.sdacademy.java.hibernate.utils.ApplicationPropertiesProvider;

import java.util.List;

public class Workshop17 {
    public static void main(String[] args) {
        var results = getStaff("Jon", "Stephens");
        System.out.println(results);

    }

    public static List<Staff> getStaff(String firstName, String lastName) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SakilaPU", ApplicationPropertiesProvider.getSakilaProperties());
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            final CriteriaQuery<Staff> cq = cb.createQuery(Staff.class);

            final Root<Staff> from = cq.from(Staff.class);
            from
                .fetch("address")
                .fetch("city")
                .fetch("country");

            cq.where(cb.and(
                cb.equal(from.get("firstName"), firstName),
                cb.equal(from.get("lastName"), lastName)
            ));

            final TypedQuery<Staff> typedQuery = entityManager.createQuery(cq);
            return typedQuery.getResultList();

        } finally {
            entityManagerFactory.close();
        }
    }
}
