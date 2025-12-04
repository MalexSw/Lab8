package main;

public abstract class LibraryItem {
    private final int libraryId;
    private final String title;
    private final double overdueFeePerDay;
    private final int loanDaysStudent;
    private final int loanDaysFaculty;
    private boolean onLoan;

    protected LibraryItem(int libraryId, String title, double overdueFeePerDay, int loanDaysStudent,
            int loanDaysFaculty) {
        this.libraryId = libraryId;
        this.title = title;
        this.overdueFeePerDay = overdueFeePerDay;
        this.loanDaysStudent = loanDaysStudent;
        this.loanDaysFaculty = loanDaysFaculty;
        this.onLoan = false;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isOnLoan() {
        return onLoan;
    }

    void checkOut() {
        onLoan = true;
    }

    void checkIn() {
        onLoan = false;
    }

    public int getLoanPeriod(boolean isFaculty) {
        return isFaculty ? loanDaysFaculty : loanDaysStudent;
    }

    public int daysOverdue(int daysBorrowed, boolean isFaculty) {
        return daysBorrowed - getLoanPeriod(isFaculty);
    }

    public boolean isOverdue(int daysBorrowed, boolean isFaculty) {
        return daysOverdue(daysBorrowed, isFaculty) > 0;
    }

    public double computeFine(int daysBorrowed, boolean isFaculty) {
        int overdueDays = Math.max(0, daysOverdue(daysBorrowed, isFaculty));
        return overdueDays * overdueFeePerDay;
    }

    public abstract ItemType getType();
}
