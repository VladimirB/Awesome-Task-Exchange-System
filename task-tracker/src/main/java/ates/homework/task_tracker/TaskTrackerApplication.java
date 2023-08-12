package ates.homework.task_tracker;

import ates.homework.task_tracker.util.DatabaseUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class TaskTrackerApplication {

	public static void main(String[] args) {
		try {
			DatabaseUtil.createIfNotExists();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SpringApplication.run(TaskTrackerApplication.class, args);
	}
}
