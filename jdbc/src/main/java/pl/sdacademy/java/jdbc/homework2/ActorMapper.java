package pl.sdacademy.java.jdbc.homework2;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import pl.sdacademy.java.jdbc.model.Actor;

import java.util.List;

public interface ActorMapper {

    @Select("SELECT DISTINCT actor.first_name, actor.last_name FROM actor " +
            "JOIN film_actor ON actor.actor_id = film_actor.actor_id " +
            "JOIN film ON film_actor.film_id = film.film_id " +
            "WHERE UPPER(actor.first_name) LIKE UPPER(#{query}) " +
            "OR UPPER(actor.last_name) LIKE UPPER(#{query}) " +
            "OR UPPER(film.title) LIKE UPPER(#{query}) " +
            "ORDER BY last_name, first_name;")
    List<Actor> getActors(String query);
}
