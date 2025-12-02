package main;

public class Book extends LibraryItem {
    private final String author;
    private final String genre;
    private final String publisher;

    public Book(int libraryId, String title, String author, String genre, String publisher) {
        super(libraryId, title, 0.5, 14, 14);
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    @Override
    public ItemType getType() {
        return ItemType.BOOK;
    }
}
