package ates.homework.auth;

import ates.homework.auth.util.DatabaseUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		try {
			DatabaseUtil.createIfNotExists();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SpringApplication.run(AuthApplication.class, args);
	}
}
