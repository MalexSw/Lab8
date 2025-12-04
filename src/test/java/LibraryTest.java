<<<<<<< HEAD
=======


>>>>>>> 2543cf3 (V2(repaired tests))
import org.junit.jupiter.api.Test;

import main.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryTest {
    @Test
    void bookFineAndOverdue() {
        Book book = new Book(1, "The Tower", "A. Writer", "Fantasy", "Beacon Press");
        assertEquals(0, book.daysOverdue(14, false));
        assertFalse(book.isOverdue(14, false));
        assertEquals(1, book.daysOverdue(15, false));
        assertTrue(book.isOverdue(15, false));
        assertEquals(0.5, book.computeFine(15, false), 1e-9);
    }

    @Test
    void journalLoanLimits() {
<<<<<<< HEAD
        Journal journal = new Journal(2, "Journal of Testing", "1234-5678", "Field Press", "Vol. 1", "http://journal.example");
=======
        Journal journal = new Journal(2, "Journal of Testing", "1234-5678", "Field Press", "Vol. 1",
                "http://journal.example");
>>>>>>> 2543cf3 (V2(repaired tests))
        assertEquals(-1, journal.daysOverdue(2, false));
        assertFalse(journal.isOverdue(2, false));
        assertEquals(2, journal.daysOverdue(9, true));
        assertTrue(journal.isOverdue(9, true));
        assertEquals(4.0, journal.computeFine(9, true), 1e-9);
    }

    @Test
    void filmFineAndOverdue() {
        Film film = new Film(3, "Epic", "Adventure", "Director", 2024, 120, "PG-13");
        assertEquals(0, film.daysOverdue(2, false));
        assertFalse(film.isOverdue(2, false));
        assertEquals(2, film.daysOverdue(4, false));
        assertTrue(film.isOverdue(4, false));
        assertEquals(10.0, film.computeFine(4, false), 1e-9);
    }

    @Test
    void borrowAndReturnTracksFine() {
        Library library = new Library();
        Book book = new Book(42, "Guardians", "A. Author", "Sci-Fi", "Starlight");
        library.addItem(book);
        User student = new User("Alex", UserRole.STUDENT, true);
        Library.LoanRecord loanRecord = library.borrowItem(42, student, 10);
        assertEquals(24, loanRecord.getDueDay());
        Library.LoanRecord returned = library.returnItem(42, 26);
        assertTrue(returned.wasOverdue());
        assertEquals(2, returned.getOverdueDays());
        assertEquals(1.0, returned.getFine(), 1e-9);
        assertThrows(IllegalStateException.class, () -> library.returnItem(42, 27));
    }
}
