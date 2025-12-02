package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Library {
    private final Map<Integer, LibraryItem> catalog = new HashMap<>();
    private final Map<Integer, LoanRecord> activeLoans = new HashMap<>();

    public void addItem(LibraryItem item) {
        catalog.put(item.getLibraryId(), item);
    }

    public List<LibraryItem> getAvailableItems(ItemType type) {
        List<LibraryItem> available = new ArrayList<>();
        for (LibraryItem item : catalog.values()) {
            if (!item.isOnLoan() && item.getType() == type) {
                available.add(item);
            }
        }
        return available;
    }

    public Optional<LibraryItem> findRandomAvailable(ItemType type, Random random) {
        List<LibraryItem> available = getAvailableItems(type);
        if (available.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(available.get(random.nextInt(available.size())));
    }

    public Optional<LibraryItem> findItem(int libraryId) {
        return Optional.ofNullable(catalog.get(libraryId));
    }

    public Collection<LibraryItem> getCatalog() {
        return Collections.unmodifiableCollection(catalog.values());
    }

    public LoanRecord borrowItem(int libraryId, User borrower, int currentDay) {
        LibraryItem item = catalog.get(libraryId);
        if (item == null) {
            throw new IllegalArgumentException("Unknown item " + libraryId);
        }
        if (item.isOnLoan()) {
            throw new IllegalStateException("Item " + libraryId + " is already on loan");
        }
        if (!borrower.borrow(item.getType())) {
            throw new IllegalStateException("Borrower has reached the limit for " + item.getType());
        }
        item.checkOut();
        int loanPeriod = item.getLoanPeriod(borrower.isFaculty());
        LoanRecord record = new LoanRecord(item, borrower, currentDay, currentDay + loanPeriod);
        activeLoans.put(libraryId, record);
        return record;
    }

    public LoanRecord returnItem(int libraryId, int returnDay) {
        LoanRecord record = activeLoans.get(libraryId);
        if (record == null) {
            throw new IllegalStateException("Item " + libraryId + " is not on loan");
        }
        record.markReturned(returnDay);
        record.getItem().checkIn();
        record.getBorrower().returnItem(record.getItem().getType());
        activeLoans.remove(libraryId);
        return record;
    }

    public Optional<LoanRecord> findLoanRecord(int libraryId) {
        return Optional.ofNullable(activeLoans.get(libraryId));
    }

    public List<LoanRecord> getActiveLoansFor(User borrower) {
        List<LoanRecord> matches = new ArrayList<>();
        for (LoanRecord record : activeLoans.values()) {
            if (record.getBorrower() == borrower) {
                matches.add(record);
            }
        }
        return matches;
    }

    public static class LoanRecord {
        private final LibraryItem item;
        private final User borrower;
        private final int checkoutDay;
        private final int dueDay;
        private Integer returnDay;

        LoanRecord(LibraryItem item, User borrower, int checkoutDay, int dueDay) {
            this.item = item;
            this.borrower = borrower;
            this.checkoutDay = checkoutDay;
            this.dueDay = dueDay;
        }

        public LibraryItem getItem() {
            return item;
        }

        public User getBorrower() {
            return borrower;
        }

        public int getCheckoutDay() {
            return checkoutDay;
        }

        public int getDueDay() {
            return dueDay;
        }

        public boolean isReturned() {
            return returnDay != null;
        }

        void markReturned(int returnDay) {
            if (this.returnDay != null) {
                throw new IllegalStateException("Item already returned");
            }
            if (returnDay < checkoutDay) {
                throw new IllegalArgumentException("Return day cannot be before checkout");
            }
            this.returnDay = returnDay;
        }

        public int getReturnDay() {
            if (returnDay == null) {
                throw new IllegalStateException("Item has not been returned yet");
            }
            return returnDay;
        }

        public int getDaysBorrowed() {
            if (returnDay == null) {
                throw new IllegalStateException("Item has not been returned yet");
            }
            return Math.max(1, returnDay - checkoutDay);
        }

        public int getOverdueDays() {
            return Math.max(0, item.daysOverdue(getDaysBorrowed(), borrower.isFaculty()));
        }

        public double getFine() {
            return item.computeFine(getDaysBorrowed(), borrower.isFaculty());
        }

        public boolean wasOverdue() {
            return getOverdueDays() > 0;
        }
    }
}
