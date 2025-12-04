package main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Main {
    private static final int STUDENT_COUNT = 80;
    private static final int FACULTY_COUNT = 20;
    private static final int TOTAL_USERS = STUDENT_COUNT + FACULTY_COUNT;
    private static final int ON_TIME_USERS = 67;
    private static final int DAYS_TO_SIMULATE = 365;
    private static final double ALPHA_BOOK = 0.05;
    private static final double ALPHA_JOURNAL = 0.08;
    private static final double ALPHA_FILM = 0.05;
    private static final double BETA_RETURN = 0.02;

    public static void main(String[] args) {
        Random random = new Random();
        Library library = buildLibrary();
        List<User> users = buildUsers(random);
        SimulationStats stats = new SimulationStats();

        for (int day = 1; day <= DAYS_TO_SIMULATE; day++) {
            for (User user : users) {
                stats.borrowAttempts++;
                if (attemptBorrow(library, user, ItemType.BOOK, ALPHA_BOOK, day, random)) {
                    stats.successfulBorrows++;
                }
                stats.borrowAttempts++;
                if (attemptBorrow(library, user, ItemType.JOURNAL, ALPHA_JOURNAL, day, random)) {
                    stats.successfulBorrows++;
                }
                stats.borrowAttempts++;
                if (attemptBorrow(library, user, ItemType.FILM, ALPHA_FILM, day, random)) {
                    stats.successfulBorrows++;
                }

                List<Library.LoanRecord> loans = new ArrayList<>(library.getActiveLoansFor(user));
                for (Library.LoanRecord loan : loans) {
                    boolean returned = false;
                    if (user.returnsOnTime() && day == loan.getDueDay()) {
                        returned = true;
                    } else if (!user.returnsOnTime() && random.nextDouble() < BETA_RETURN) {
                        returned = true;
                    }
                    if (!returned) {
                        continue;
                    }
                    Library.LoanRecord finished = library.returnItem(loan.getItem().getLibraryId(), day);
                    stats.returns++;
                    stats.totalFine += finished.getFine();
                    if (finished.wasOverdue()) {
                        stats.overdueReturns++;
                    }
                }
            }
        }

        System.out.printf("Simulated %d days for %d users and %d items.%n", DAYS_TO_SIMULATE, users.size(),
                library.getCatalog().size());
        System.out.printf("Borrow attempts: %d, successful loans: %d%n", stats.borrowAttempts, stats.successfulBorrows);
        System.out.printf("Returns: %d (%d overdue) with total fines $%.2f.%n", stats.returns, stats.overdueReturns,
                stats.totalFine);
    }

    private static boolean attemptBorrow(Library library, User user, ItemType type, double probability, int day,
            Random random) {
        if (random.nextDouble() >= probability) {
            return false;
        }
        Optional<LibraryItem> optional = library.findRandomAvailable(type, random);
        if (optional.isEmpty()) {
            return false;
        }
        try {
            library.borrowItem(optional.get().getLibraryId(), user, day);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private static Library buildLibrary() {
        Library library = new Library();
        int id = 1;
        try {
            BufferedReader br = Files.newBufferedReader(Paths.get("Lab8", "testExamples", "books.csv"));
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    String title = parts[0];
                    String author = parts[1];
                    String genre = parts[2];
                    String publisher = parts[4];
                    library.addItem(new Book(id++, title, author, genre, publisher));
                }
            }
            br.close();

            br = Files.newBufferedReader(Paths.get("Lab8", "testExamples", "jlist.csv"));
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 13) {
                    String title = parts[0];
                    String eISSN = parts[3];
                    String publisher = parts[4];
                    String latestIssue = parts[6];
                    String journalUrl = parts[12];
                    library.addItem(new Journal(id++, title, eISSN, publisher, latestIssue, journalUrl));
                }
            }
            br.close();

            br = Files.newBufferedReader(Paths.get("Lab8", "testExamples", "movies.csv"));
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 9) {
                    String title = parts[1];
                    String genre = parts[2];
                    String director = parts[4];
                    try {
                        int year = Integer.parseInt(parts[6]);
                        int runtime = Integer.parseInt(parts[7]);
                        String rating = parts[8];
                        library.addItem(new Film(id++, title, genre, director, year, runtime, rating));
                    } catch (NumberFormatException e) {
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return library;
    }

    private static List<User> buildUsers(Random random) {
        List<Boolean> statuses = new ArrayList<>();
        for (int i = 0; i < ON_TIME_USERS; i++) {
            statuses.add(true);
        }
        for (int i = 0; i < TOTAL_USERS - ON_TIME_USERS; i++) {
            statuses.add(false);
        }
        Collections.shuffle(statuses, random);
        List<User> users = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < STUDENT_COUNT; i++) {
            users.add(new User("Student" + (i + 1), UserRole.STUDENT, statuses.get(index++)));
        }
        for (int i = 0; i < FACULTY_COUNT; i++) {
            users.add(new User("Faculty" + (i + 1), UserRole.FACULTY, statuses.get(index++)));
        }
        return users;
    }

    private static final class SimulationStats {
        private int borrowAttempts;
        private int successfulBorrows;
        private int returns;
        private int overdueReturns;
        private double totalFine;
    }
}
