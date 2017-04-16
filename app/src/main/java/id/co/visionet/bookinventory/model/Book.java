package id.co.visionet.bookinventory.model;

import android.database.Cursor;

import java.io.Serializable;

import id.co.visionet.bookinventory.database.Reusable;

/**
 * Created by jessica.susanto on 07/04/2016.
 */
public class Book implements Serializable {
    private int idx;
    private String ISBN;
    private int book_id;
    private String book_title;
    private String book_author;
    private int published_year;
    private int book_category;
    private String book_synopsis;
    private String book_cover;

    /*public Book(String pISBN, String book_title, String pBookAuthor, int pPublishedYear, String book_genre, String synopsis, String cover) {
        ISBN = pISBN;
        this.book_title = book_title;
        book_author = pBookAuthor;
        published_year = pPublishedYear;
        this.book_genre = book_genre;
        book_synopsis = synopsis;
        book_cover = cover;
    }*/

    public Book(Cursor cursor) {
        this.idx = cursor.getInt(cursor.getColumnIndex(Reusable.COL_DB_ID));
        this.book_id = cursor.getInt(cursor.getColumnIndex(Reusable.COL_BOOK_ID));
        this.ISBN = cursor.getString(cursor.getColumnIndex(Reusable.COL_ISBN));
        this.book_title = cursor.getString(cursor
                .getColumnIndex(Reusable.COL_TITLE));
        this.book_author = cursor.getString(cursor
                .getColumnIndex(Reusable.COL_AUTHOR));
        this.published_year = cursor.getInt(cursor.getColumnIndex(Reusable.COL_YEAR));
        this.book_category = cursor.getInt(cursor
                .getColumnIndex(Reusable.COL_CATEGORY));
        this.book_synopsis = cursor.getString(cursor
                .getColumnIndex(Reusable.COL_SYNOPSIS));
        this.book_cover = cursor.getString(cursor
                .getColumnIndex(Reusable.COL_IMG_COVER));
    }

    public Book() {

    }

    public int getBook_category() {
        return book_category;
    }

    public void setBook_category(int book_category) {
        this.book_category = book_category;
    }

    public int getPublished_year() {
        return published_year;
    }

    public void setPublished_year(int published_year) {
        this.published_year = published_year;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getBook_cover() {
        return book_cover;
    }

    public void setBook_cover(String book_cover) {
        this.book_cover = book_cover;
    }

    public String getBook_synopsis() {
        return book_synopsis;
    }

    public void setBook_synopsis(String book_synopsis) {
        this.book_synopsis = book_synopsis;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
