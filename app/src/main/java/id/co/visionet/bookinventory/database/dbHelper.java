package id.co.visionet.bookinventory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {
    private static dbHelper dbInstance = null;

    public dbHelper(Context context) {
        super(context, Reusable.DATABASE_NAME, null, Reusable.DATABASE_VERSION);
    }

    public static synchronized dbHelper getDbInstance(Context context) {
        if (dbInstance == null)
            dbInstance = new dbHelper(context.getApplicationContext());
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(Reusable.DATABASE_CREATE_BOOK);
        db.execSQL(Reusable.DATABASE_CREATE_CATEGORY);
        db.execSQL(Reusable.DATABASE_INSERT_CATEGORY);
        db.execSQL(Reusable.DATABASE_INSERT_BOOK);
    }

    public void clearAndInitDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Reusable.TABLE_BOOK);
        db.execSQL(Reusable.DATABASE_INSERT_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(dbHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion);
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + Reusable.TABLE_BOOK + " ADD COLUMN " + Reusable.COL_BOOK_ID + " INTEGER;");
        }
    }
}
