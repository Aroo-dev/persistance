package pl.edu.agh.mwo.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml"); // Przypisanie nazwy pliku konfiguracyjnego

			return configuration.buildSessionFactory(
					new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build()
			);
		} catch (Exception e) {
			System.err.println("Błąd konfiguracji Hibernate: " + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}
}
