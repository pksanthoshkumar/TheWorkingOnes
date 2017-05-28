package hibernatetutorial;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class App {
	private static final Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		// read configuration and build session factory
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate_cfg.xml")
				.build();

		SessionFactory sessionFactory = null;

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			logger.error("cannot create sessionFactory", e);
			System.exit(1);
		}

		// create session, open transaction and save test entity to db
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		try {
			Employee anEmployee = new Employee();
			anEmployee.setName("super foo");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			String date = "21/05/2017";

			// convert String to LocalDate
			LocalDate localDate = LocalDate.parse(date, formatter);
			anEmployee.setJoiningDate(localDate);
			anEmployee.setSalary(new BigDecimal (5000.12));
			anEmployee.setSsn("12345");
			
			
			session.persist(anEmployee);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("cannot commit transaction", e);
		} finally {
			session.close();
		}

		// clean up
		sessionFactory.close();
	}
}