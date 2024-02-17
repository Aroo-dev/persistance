package pl.edu.agh.mwo.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {

    Session session;

    public static void main(String[] args) {
        Main main = new Main();

        main.initializeDatabase(); // Dodana funkcja inicjalizacji bazy danych

        main.addUserAlbumAndPhoto();
        main.addLike();
        main.removeLike();
        main.removePhoto();
        main.removeAlbum();
        main.removeUser();

        main.close();
    }

    public Main() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void initializeDatabase() {
        Transaction tx = session.beginTransaction();

        try {
            // Wczytaj plik initial_data.sql
            String filePath = "/initial_data.sql";
            InputStream inputStream = Main.class.getResourceAsStream(filePath);

            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    StringBuilder queryBuilder = new StringBuilder();

                    while ((line = reader.readLine()) != null) {

                        if (line.startsWith("--")) {
                            continue;
                        }

                        queryBuilder.append(line);

                        if (line.endsWith(";")) {
                            // Znaleziono pełne zapytanie SQL, wykonaj je
                            String query = queryBuilder.toString();
                            session.createNativeQuery(query).executeUpdate();
                            queryBuilder.setLength(0); // Wyczyść bufor
                        }
                    }
                }
            } else {
                System.err.println("Nie można wczytać pliku: " + filePath);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void addUserAlbumAndPhoto() {
        Transaction tx = session.beginTransaction();

        // Dodaj użytkownika
        User user = new User();
        user.setUsername("JohnDoe");

        // Dodaj album
        Album album = new Album();
        album.setName("Summer Vacation");
        album.setOwner(user);

        // Dodaj zdjęcie
        Photo photo = new Photo();
        photo.setDescription("Sunset at the beach");
        photo.setAlbum(album);

        // Dodaj zdjęcie do albumu
        album.getPhotos().add(photo);

        // Dodaj użytkownika do znajomych
        User friend = new User();
        friend.setUsername("JaneDoe");
        user.getFriends().add(friend);

        session.persist(user);
        session.persist(album);
        session.persist(photo);

        tx.commit();
    }

    public void addLike() {
        Transaction tx = session.beginTransaction();

        // Pobierz użytkownika i zdjęcie z bazy danych
        User user = session.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", "JohnDoe")
                .uniqueResult();

        Photo photo = session.createQuery("FROM Photo WHERE description = :description", Photo.class)
                .setParameter("description", "Sunset at the beach")
                .uniqueResult();

        // Dodaj polubienie
        user.getLikedPhotos().add(photo);

        session.update(user);

        tx.commit();
    }

    public void removeLike() {
        Transaction tx = session.beginTransaction();

        // Pobierz użytkownika i zdjęcie z bazy danych
        User user = session.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", "JohnDoe")
                .uniqueResult();

        Photo photo = session.createQuery("FROM Photo WHERE description = :description", Photo.class)
                .setParameter("description", "Sunset at the beach")
                .uniqueResult();

        // Usuń polubienie
        user.getLikedPhotos().remove(photo);

        session.update(user);

        tx.commit();
    }

    public void removePhoto() {
        Transaction tx = session.beginTransaction();

        // Pobierz album i zdjęcie z bazy danych
        Album album = session.createQuery("FROM Album WHERE name = :name", Album.class)
                .setParameter("name", "Summer Vacation")
                .uniqueResult();

        Photo photo = session.createQuery("FROM Photo WHERE description = :description", Photo.class)
                .setParameter("description", "Sunset at the beach")
                .uniqueResult();

        // Usuń zdjęcie
        album.getPhotos().remove(photo);

        session.delete(photo);

        tx.commit();
    }

    public void removeAlbum() {
        Transaction tx = session.beginTransaction();

        // Pobierz użytkownika i album z bazy danych
        User user = session.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", "JohnDoe")
                .uniqueResult();

        Album album = session.createQuery("FROM Album WHERE name = :name", Album.class)
                .setParameter("name", "Summer Vacation")
                .uniqueResult();

        // Usuń album (zgodnie z wymaganiami, usunie to również zdjęcia)
        user.getAlbums().remove(album);

        session.delete(album);

        tx.commit();
    }

    public void removeUser() {
        Transaction tx = session.beginTransaction();

        // Pobierz użytkownika z bazy danych
        User user = session.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", "JohnDoe")
                .uniqueResult();

        // Usuń użytkownika (zgodnie z wymaganiami, usunie to również albumy i polubienia)
        session.delete(user);

        tx.commit();
    }

    public void close() {
        session.close();
        HibernateUtil.shutdown();
    }
}
