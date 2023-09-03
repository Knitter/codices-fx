# Codices-fx

Desktop version of **Codices** application, developed in JavaFX.

**Codices** was a pet project used to test programming languages, libraries, frameworks, etc., and often
in various courses I've taught. It was never a usable product and the project repository was full of
temporary code.

**Codices-fx** is a Desktop based application, being developed in JavaFX, using SQLite database for storage,
and will contain all the features that were intended for the original **Codices** project.

The desktop version is one of 3 that will be developed (the other two are WEB based using PHP and Yii2 & Yii3,
and using Scala and the Play framework, [Codices-yii](https://github.com/Knitter/codices-yii) and
[Codices-play](https://github.com/Knitter/codices-play) respectively).

> The ```main``` branch will be the stable branch after the first release, until a version 1.0 is released,
> the branch is considered unstable and will often break (code may not compile).

# Features
List of features that  will be available in Codices-fx:

* Manage paper books, e-books and audiobooks
* For each of the managed items, store:
    * Publisher {name, URL, short description}
    * Series {name, number of books in series, flag marking the series as finished/completed}
    * Language, ISBN (always 13), URL (for any user provided site, e.g. "goodreads" or "amazon")
    * Added date, edition, plot, review, Genres, volume
    * Format (paperback, hard cover, etc.; depends on item type, need a list of common types)
    * Publish date and Publish year, for when no date is available
    * No. in series (or just order even if no series was explicitly created)
    * Info related to translation  (if translated, original title)
    * Flag to mark the book as read, number of owned copies, subtitle
    * Page count, duration (audio, in minutes), size in KB (both ebook and audio)
    * Audio specific info (e.g. bitrate, narrator), filename for ebooks and audio files
    * Location/path, manually set by user, can be anything from a file path to "calibre library"
    * Store/Seller name (simple text), e.g. Humble Bundle
    * Item type to identify the 3 types of "books" being managed
    * Rating, cover (front for now, back cover later)
    * Authors (multiple authors per book)
* Manage related data for Publishers, Series, Authors and Collections
* Export library as CSV, JSON, HTML pages based on template, or single HTML page based on template
* Integrate with the API from codices-yii and codices-play
* Import from CSV, BookBuddy CSV and HTML export files
* Import from Calibre
* Connect to https://openlibrary.org

# Credits

Currently used icons are provided by https://tabler-icons.io/.

# License

**Codices-fx** is licensed under GNU GPL v3 license. Please check the
[LICENSE](https://raw.githubusercontent.com/Knitter/codices-fx/main/LICENSE) file for more information.

Copyright, 2023 Sérgio Lopes 
