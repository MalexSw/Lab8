package main;

public class User {
    private final String name;
    private final UserRole role;
    private final boolean returnsOnTime;
    private int booksBorrowed;
    private int filmsBorrowed;
    private int journalsBorrowed;

    private static final int MAX_BOOKS = 3;
    private static final int MAX_FILMS = 1;
    private static final int MAX_JOURNALS = 3;

    public User(String name, UserRole role, boolean returnsOnTime) {
        this.name = name;
        this.role = role;
        this.returnsOnTime = returnsOnTime;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isFaculty() {
        return role == UserRole.FACULTY;
    }

    public boolean returnsOnTime() {
        return returnsOnTime;
    }

    public boolean borrow(ItemType itemType) {
        if (isFaculty()) {
            incrementCount(itemType);
            return true;
        }
        switch (itemType) {
            case BOOK:
                if (booksBorrowed < MAX_BOOKS) {
                    booksBorrowed++;
                    return true;
                }
                break;
            case FILM:
                if (filmsBorrowed < MAX_FILMS) {
                    filmsBorrowed++;
                    return true;
                }
                break;
            case JOURNAL:
                if (journalsBorrowed < MAX_JOURNALS) {
                    journalsBorrowed++;
                    return true;
                }
                break;
        }
        return false;
    }

    private void incrementCount(ItemType itemType) {
        switch (itemType) {
            case BOOK:
                booksBorrowed++;
                break;
            case FILM:
                filmsBorrowed++;
                break;
            case JOURNAL:
                journalsBorrowed++;
                break;
        }
    }

    public void returnItem(ItemType itemType) {
        switch (itemType) {
            case BOOK:
                if (booksBorrowed > 0) {
                    booksBorrowed--;
                }
                break;
            case FILM:
                if (filmsBorrowed > 0) {
                    filmsBorrowed--;
                }
                break;
            case JOURNAL:
                if (journalsBorrowed > 0) {
                    journalsBorrowed--;
                }
                break;
        }
    }
}
