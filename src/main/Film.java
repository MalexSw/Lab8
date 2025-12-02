package main;

public class Film extends LibraryItem {
    private final String genre;
    private final String director;
    private final int year;
    private final int runtimeMinutes;
    private final String rating;

    public Film(int libraryId, String title, String genre, String director, int year, int runtimeMinutes, String rating) {
        super(libraryId, title, 5.0, 2, 2);
        this.genre = genre;
        this.director = director;
        this.year = year;
        this.runtimeMinutes = runtimeMinutes;
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public int getYear() {
        return year;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public ItemType getType() {
        return ItemType.FILM;
    }
}
