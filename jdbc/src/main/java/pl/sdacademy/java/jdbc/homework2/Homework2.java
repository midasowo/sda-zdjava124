package pl.sdacademy.java.jdbc.homework2;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import pl.sdacademy.java.jdbc.model.Actor;
import pl.sdacademy.java.jdbc.utils.ApplicationPropertiesProvider;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Homework2 {

    public static void main(String[] args) {
        final String jdbcUrl = ApplicationPropertiesProvider.getApplicationProperties().getProperty("jdbc.url");

        System.out.println("Podaj frazę wyszukiwania:");
        final var query = new Scanner(System.in).nextLine();

        final var actorStrings = getActors(sqlSessionFactory(jdbcUrl), query).stream()
                .map(Actor::toString)
                .collect(Collectors.toList());

        System.out.printf("Aktorzy pasujący do frazy '%s':\n\n%s", query, String.join("\n", actorStrings));
    }

    public static List<Actor> getActors(SqlSessionFactory sqlSessionFactory, String query) {
        if (query == null || query.isBlank() || query.length() < 3) {
            return Collections.emptyList();
        }
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ActorMapper actorMapper = session.getMapper(ActorMapper.class);
            return actorMapper.getActors(query);
        }
    }

    static SqlSessionFactory sqlSessionFactory(String jdbcUrl) {

        //A workaround as MyBatis doesn't determine driver on its own.
        final var driverName = ServiceLoader.load(Driver.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(d -> {
                    try {
                        return d.acceptsURL(jdbcUrl);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findAny()
                .map(Object::getClass)
                .map(Class::getCanonicalName)
                .orElseThrow();

        final var dataSource = new UnpooledDataSource();
        dataSource.setDriver(driverName);
        dataSource.setUrl(jdbcUrl);

        final var environment = new Environment("dev", new JdbcTransactionFactory(), dataSource);

        final var configuration = new Configuration(environment);
        configuration.addMapper(ActorMapper.class);

        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
