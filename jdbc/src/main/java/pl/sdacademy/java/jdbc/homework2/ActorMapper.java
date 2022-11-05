package pl.sdacademy.java.jdbc.homework2;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import pl.sdacademy.java.jdbc.model.Actor;

import java.util.List;

public interface ActorMapper {

    @Select("SELECT DISTINCT actor.first_name, actor.last_name FROM actor\n" +
            "JOIN film_actor ON actor.actor_id = film_actor.actor_id\n" +
            "JOIN film ON film_actor.film_id = film.film_id\n" +
            "WHERE UPPER(actor.first_name) LIKE UPPER(#{query}) \n" +
            "OR UPPER(actor.last_name) LIKE UPPER(#{query})\n" +
            "OR UPPER(film.title) LIKE UPPER(#{query})\n" +
            "ORDER BY last_name, first_name;")
    @Results({
            @Result(property = "first_name", column = "first_name"),
            @Result(property = "last_name", column = "last_name"),
    })
    List<Actor> getActors(String query);
}
