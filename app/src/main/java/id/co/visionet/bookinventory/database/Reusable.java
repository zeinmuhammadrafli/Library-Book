package id.co.visionet.bookinventory.database;

public class Reusable {
    public static final String DATABASE_NAME = "BooksCatalog";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_BOOK = "Book";
    public static final String TABLE_CATEGORY = "Category";

    public static final String COL_DB_ID = "idx";
    //field baru
    public static final String COL_BOOK_ID = "book_id";
    public static final String COL_ISBN = "ISBN";
    public static final String COL_TITLE = "BookTitle";
    public static final String COL_AUTHOR = "BookAuthor";
    public static final String COL_YEAR = "PublishedYear";
    public static final String COL_CATEGORY = "BookCategory";
    public static final String COL_SYNOPSIS = "BookSynopsis";
    public static final String COL_IMG_COVER = "BookCover";

    public static final String COL_CATEGORY_ID = "CategoryId";
    public static final String COL_CATEGORY_NAME = "CategoryName";
    public static final String COL_CATEGORY_DESC = "CategoryDesc";

    //penambahan COL_BOOK_ID
    public static final String DATABASE_CREATE_BOOK =
            "CREATE TABLE " + TABLE_BOOK + "("
                    + COL_DB_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" + ","
                    + COL_BOOK_ID + " INTEGER " + ","
                    + COL_ISBN + " TEXT NOT NULL UNIQUE" + ","
                    + COL_TITLE + " TEXT NOT NULL " + ","
                    + COL_AUTHOR + " TEXT NOT NULL " + ","
                    + COL_YEAR + " INTEGER " + ","
                    + COL_CATEGORY + " INTEGER " + ","
                    + COL_SYNOPSIS + " TEXT " + ","
                    + COL_IMG_COVER + " TEXT);";
    public static final String DATABASE_CREATE_CATEGORY =
            "CREATE TABLE " + TABLE_CATEGORY + "("
                    + COL_CATEGORY_ID + " INTEGER NOT NULL PRIMARY KEY,"
                    + COL_CATEGORY_NAME + " TEXT NOT NULL " + ","
                    + COL_CATEGORY_DESC + " TEXT);";
    // penambahan kolom kedua untuk book_id
    public static final String DATABASE_INSERT_BOOK =
            "INSERT INTO " + TABLE_BOOK + " SELECT NULL, NULL, '9780439064873' AS ISBN, 'The Hobbit' AS BookTitle, 'J.R.R Tolkien' AS BookAuthor,"
                    + " 1937 as PublishedYear, 1 as BookGenre, 'This is some synopsis' as BookSynopsis, 'http://t3.gstatic.com/images?q=tbn:ANd9GcQatWKiHJ1Rr9gdXiBugSd9FssWkHhryVelawfwfQPkh9olrCam' as BookCover"
                    + " UNION SELECT NULL, NULL, '9780316015844','The Lord of The Rings','J.R.R Tolkien', 1955, 1, 'This is some synopsis', 'http://t2.gstatic.com/images?q=tbn:ANd9GcTtBf6RksT465ZSIfrIllks47VaGTorGG6rh53ex6Ayv5Xa5z3L'"
                    + " UNION SELECT NUll, NULL, '9781484724989','Journey to Star Wars: The Force Awakens Lost Stars','Claudia Gray', 2015, 2, 'This is some synopsis', 'http://ecx.images-amazon.com/images/I/51LjaHPiwPL._SL194_.jpg'"
                    + " UNION SELECT NUll, NULL, '9782949109302','Forrest Gump','Winston Groom', 1986, 2, 'This is some synopsis', 'http://t3.gstatic.com/images?q=tbn:ANd9GcQa_1H6cx4GeWlCsLOsLYCvEcZwdQgi-03Z8tkXG7qdFyad5yWp'"
                    + " UNION SELECT NUll, NULL, '9782949109302','The Hunger Games','Suzanne Collins', 2008, 1, 'This is some synopsis', 'http://t1.gstatic.com/images?q=tbn:ANd9GcR9KpMf9TC1v4Shsccefy_eT7hLpWTggZSVpBCEKvVB_esGIjzh'";
    public static final String DATABASE_INSERT_CATEGORY =
            "INSERT INTO " + TABLE_CATEGORY + " SELECT 1 AS CategoryId, 'Fantasy' AS CategoryName, '-' AS CategoryDesc"
                    + " UNION SELECT 2 AS CategoryId, 'Science Fiction' AS CategoryName, '-' AS CategoryDesc"
                    + " UNION SELECT 3 AS CategoryId, 'Romance' AS CategoryName, '-' AS CategoryDesc";
}