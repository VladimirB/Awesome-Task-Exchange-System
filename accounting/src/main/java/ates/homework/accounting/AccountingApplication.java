package ates.homework.accounting;

import ates.homework.accounting.util.DatabaseUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class AccountingApplication {

	public static void main(String[] args) {
		try {
			DatabaseUtil.createIfNotExists();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SpringApplication.run(AccountingApplication.class, args);
	}
}
