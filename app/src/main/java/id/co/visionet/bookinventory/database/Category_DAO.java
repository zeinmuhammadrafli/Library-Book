package id.co.visionet.bookinventory.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import id.co.visionet.bookinventory.core.CoreApplication;
import id.co.visionet.bookinventory.helper.SpinnerObject;
import id.co.visionet.bookinventory.model.Category;

public class Category_DAO {
    private SQLiteDatabase catalogDB;
    private dbHelper helper;
    private String[] allColumns = {Reusable.COL_CATEGORY_ID, Reusable.COL_CATEGORY_NAME, Reusable.COL_CATEGORY_DESC};

    public Category_DAO(Context context) {
        helper = CoreApplication.getInstance().getDatabase();
    }

    public void open() throws SQLiteException {
        catalogDB = helper.getWritableDatabase();
    }

    public void close() {
        catalogDB.close();
    }

    public List<SpinnerObject> getGenres() {
        List<SpinnerObject> listGenre = new ArrayList<SpinnerObject>();
        Cursor cursor = null;
        try {
            cursor = catalogDB.query(Reusable.TABLE_CATEGORY, allColumns, null, null, null, null, null);
            if (!cursor.moveToFirst()) {
                return listGenre;
            } else {
                do {
                    Category category = new Category(cursor);
                    SpinnerObject obj = new SpinnerObject(category.getCategory_id(), category.getCategory_name());
                    listGenre.add(obj);
                } while (cursor.moveToNext());
                return listGenre;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listGenre;
    }
}
