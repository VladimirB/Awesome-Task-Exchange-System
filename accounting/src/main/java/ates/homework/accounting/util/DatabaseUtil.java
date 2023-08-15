package ates.homework.accounting.util;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String DATABASE_NAME = "ates_accounting_db";

    public static void createIfNotExists() throws SQLException {
        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/",
                "postgres",
                "postgres");
             var statement = connection.createStatement()
        ) {
            statement.executeQuery("SELECT count(*) FROM pg_database WHERE datname = '" + DATABASE_NAME + "'");
            var resultSet = statement.getResultSet();
            resultSet.next();

            var count = resultSet.getInt(1);
            if (count <= 0) {
                statement.executeUpdate("CREATE DATABASE " + DATABASE_NAME);
                System.out.println("Database created");
            }
        }
    }
}
