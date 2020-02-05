package com.dev.cinema;

import com.dev.cinema.lib.Injector;
import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.User;
import com.dev.cinema.service.AuthenticationService;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.MovieService;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.security.sasl.AuthenticationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static Injector injector = Injector.getInstance("com.dev.cinema");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);

        Movie movie = new Movie();
        movie.setTitle("Men in Black");
        movie.setDescription("Movie about secret agents whom fight with aliens");
        movieService.add(movie);

        movieService.getAll().forEach(System.out::println);

        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(50);
        cinemaHall = cinemaHallService.add(cinemaHall);

        MovieSession movieSession = new MovieSession();
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.of(LocalDate.now(),
                LocalTime.of(19, 20)));
        MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);

        movieSessionService.findAvailableSessions(movie.getId(), LocalDate.now())
                .forEach(System.out::println);

        System.out.println("________________________________________________");
        User user = new User();
        user.setEmail("email@email.com");
        user.setPassword("psw");
        UserService userService = (UserService) injector.getInstance(UserService.class);
        AuthenticationService authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);

        try {
            authenticationService.register("email@email.com", "psw");
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(userService.findByEmail("email@email.com"));

        System.out.println("_______________________________________________");
        System.out.println("Try to signIn with true pass and email");
        try {
            authenticationService.login("email@gmail.com", "psw");
            System.out.println("True email and login");
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("_______________________________________________");
        System.out.println("Try to signIn by wrong email and true psw");
        try {
            authenticationService.login("wrong@email.com", "psw");
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("_______________________________________________");
        System.out.println("Try to signIn by email and wrong psw");
        try {
            authenticationService.login("email@email.com", "wrong");
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }
}
