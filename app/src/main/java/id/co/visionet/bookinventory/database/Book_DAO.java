package id.co.visionet.bookinventory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import id.co.visionet.bookinventory.core.CoreApplication;
import id.co.visionet.bookinventory.model.Book;

public class Book_DAO {
    private SQLiteDatabase catalogDB;
    private dbHelper helper;
    private String[] allColumns = {Reusable.COL_DB_ID, Reusable.COL_BOOK_ID, Reusable.COL_ISBN, Reusable.COL_TITLE, Reusable.COL_AUTHOR, Reusable.COL_CATEGORY, Reusable.COL_YEAR,
            Reusable.COL_SYNOPSIS, Reusable.COL_IMG_COVER};

    public Book_DAO(Context context) {
        helper = CoreApplication.getInstance().getDatabase();
    }

    public void open() throws SQLiteException {
        catalogDB = helper.getWritableDatabase();
    }

    public void close() {
        if (catalogDB != null && catalogDB.isOpen())
            catalogDB.close();
    }

    public List<Book> getAllBooks() {
        List<Book> listBooks = new ArrayList<Book>();
        Cursor cursor = null;
        try {
            cursor = catalogDB.query(Reusable.TABLE_BOOK, allColumns, null, null, null, null, null);
            if (!cursor.moveToFirst()) {
                return listBooks;
            } else {
                listBooks = new ArrayList<Book>(cursor.getCount());
                do {
                    listBooks.add(new Book(cursor));
                } while (cursor.moveToNext());
                return listBooks;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listBooks;
    }


    public long insertBook(Book book) {
        ContentValues valuesToDB = new ContentValues();
        valuesToDB.put(Reusable.COL_BOOK_ID, book.getBook_id());
        valuesToDB.put(Reusable.COL_ISBN, book.getISBN());
        valuesToDB.put(Reusable.COL_TITLE, book.getBook_title());
        valuesToDB.put(Reusable.COL_AUTHOR, book.getBook_author());
        valuesToDB.put(Reusable.COL_YEAR, book.getPublished_year());
        valuesToDB.put(Reusable.COL_CATEGORY, book.getBook_category());
        valuesToDB.put(Reusable.COL_SYNOPSIS, book.getBook_synopsis());
        valuesToDB.put(Reusable.COL_IMG_COVER, book.getBook_cover());

        long rowID = catalogDB.insertOrThrow(Reusable.TABLE_BOOK, null, valuesToDB);

        return rowID;
    }

    public boolean updateBook(Book book) {
        ContentValues valuesToDB = new ContentValues();

        valuesToDB.put(Reusable.COL_BOOK_ID, book.getBook_id());
        valuesToDB.put(Reusable.COL_ISBN, book.getISBN());
        valuesToDB.put(Reusable.COL_TITLE, book.getBook_title());
        valuesToDB.put(Reusable.COL_AUTHOR, book.getBook_author());
        valuesToDB.put(Reusable.COL_YEAR, book.getPublished_year());
        valuesToDB.put(Reusable.COL_CATEGORY, book.getBook_category());
        valuesToDB.put(Reusable.COL_SYNOPSIS, book.getBook_synopsis());
        valuesToDB.put(Reusable.COL_IMG_COVER, book.getBook_cover());

        long rowID = catalogDB.update(Reusable.TABLE_BOOK,
                valuesToDB, Reusable.COL_DB_ID + " = '" + book.getIdx() + "'", null);

        return rowID != -1 ? true : false;
    }

    public void updateBookId(int bookId, long idx) {
        String query = "UPDATE " + Reusable.TABLE_BOOK + " SET " + Reusable.COL_BOOK_ID + " = " + bookId +
                " where " + Reusable.COL_DB_ID + " = " + idx;
        catalogDB.execSQL(query);
    }

    public Book getBook(String ISBN) {
        Book book = null;
        String query = "SELECT * FROM " + Reusable.TABLE_BOOK + " where " + Reusable.COL_ISBN + " = '" + ISBN + "'";
        Cursor cursor = catalogDB.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            book = new Book();
            book.setIdx(cursor.getInt(cursor.getColumnIndex(Reusable.COL_DB_ID)));
            book.setBook_id(cursor.getInt(cursor.getColumnIndex(Reusable.COL_BOOK_ID)));
            book.setISBN(cursor.getString(cursor.getColumnIndex(Reusable.COL_ISBN)));
            book.setBook_title(cursor.getString(cursor.getColumnIndex(Reusable.COL_TITLE)));
            book.setBook_author(cursor.getString(cursor.getColumnIndex(Reusable.COL_AUTHOR)));
            book.setPublished_year(cursor.getInt(cursor.getColumnIndex(Reusable.COL_YEAR)));
            book.setBook_category(cursor.getInt(cursor.getColumnIndex(Reusable.COL_CATEGORY)));
            book.setBook_synopsis(cursor.getString(cursor.getColumnIndex(Reusable.COL_SYNOPSIS)));
            book.setBook_cover(cursor.getString(cursor.getColumnIndex(Reusable.COL_IMG_COVER)));
        }

        return book;
    }

    public boolean deleteBook(String ISBN) {
        return catalogDB.delete(Reusable.TABLE_BOOK, Reusable.COL_ISBN + "='" + ISBN + "'", null) > 0;
    }
}
