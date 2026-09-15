package hei.school.prog3rattrapage.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbConnection {

    private final String url;
    private final String user;
    private final String password;

    public DbConnection(
            @Value("${kofia.datasource.url}") String url,
            @Value("${kofia.datasource.username}") String user,
            @Value("${kofia.datasource.password}") String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
