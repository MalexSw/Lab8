package main;

public class Journal extends LibraryItem {
    private final String eIssn;
    private final String publisher;
    private final String latestIssue;
    private final String journalUrl;

    public Journal(int libraryId, String title, String eIssn, String publisher, String latestIssue, String journalUrl) {
        super(libraryId, title, 2.0, 3, 7);
        this.eIssn = eIssn;
        this.publisher = publisher;
        this.latestIssue = latestIssue;
        this.journalUrl = journalUrl;
    }

    public String getEIssn() {
        return eIssn;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getLatestIssue() {
        return latestIssue;
    }

    public String getJournalUrl() {
        return journalUrl;
    }

    @Override
    public ItemType getType() {
        return ItemType.JOURNAL;
    }
}
