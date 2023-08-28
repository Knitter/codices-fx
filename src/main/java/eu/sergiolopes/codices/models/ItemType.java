package eu.sergiolopes.codices.models;

/**
 * Simple enumeration to hide the item type values used in the database.
 */
public enum ItemType {
    EBOOK("ebook"),
    AUDIO_BOOK("audio"),
    PAPER_BOOK("paper");

    private String type;

    private ItemType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public String getType() {
        return type;
    }

    public static ItemType fromString(String type) {
        switch (type) {
            case "ebook":
                return ItemType.EBOOK;
            case "audio":
                return ItemType.AUDIO_BOOK;
            case "paper":
                return ItemType.PAPER_BOOK;
        }

        return null;
    }
}
