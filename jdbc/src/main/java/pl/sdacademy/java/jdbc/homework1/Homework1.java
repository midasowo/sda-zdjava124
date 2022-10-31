package pl.sdacademy.java.jdbc.homework1;

import pl.sdacademy.java.jdbc.model.Actor;
import pl.sdacademy.java.jdbc.utils.ApplicationPropertiesProvider;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Homework1 {

    public static void main(String[] args) {
        final String jdbcUrl = ApplicationPropertiesProvider.getApplicationProperties().getProperty("jdbc.url");

        System.out.println("Podaj frazę wyszukiwania:");
        final var query = new Scanner(System.in).nextLine();

        final var actorStrings = getActors(jdbcUrl, query).stream()
                .map(Actor::toString)
                .collect(Collectors.toList());

        System.out.printf("Aktorzy pasujący do frazy '%s':\n\n%s", query, String.join("\n", actorStrings));
    }

    public static List<Actor> getActors(String jdbcUrl, String query) {
        final List<Actor> actors = new LinkedList<>();
        if (query == null || query.isBlank() || query.length() < 3) {
            return Collections.emptyList();
        }
        try (final Connection connection = DriverManager.getConnection(jdbcUrl)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT actor.first_name, actor.last_name FROM actor\n" +
                    "JOIN film_actor ON actor.actor_id = film_actor.actor_id\n" +
                    "JOIN film ON film_actor.film_id = film.film_id\n" +
                    "WHERE UPPER(actor.first_name) LIKE UPPER(?) \n" +
                    "OR UPPER(actor.last_name) LIKE UPPER(?)\n" +
                    "OR UPPER(film.title) LIKE UPPER(?)\n" +
                    "ORDER BY last_name, first_name;");
            String asking = "%" + query + "%";
            preparedStatement.setString(1, asking);
            preparedStatement.setString(2, asking);
            preparedStatement.setString(3, asking);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Actor actor = new Actor(
                  resultSet.getString("first_name"),
                  resultSet.getString("last_name")
                );
                actors.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }
}
