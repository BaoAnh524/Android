package com.example.appdoctruyen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "COMIC.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TRUYEN = "Truyen";
    private static final String TABLE_THELOAI = "TheLoai";
    private static final String TABLE_LINKTRUYEN = "LinkTruyen";

    // Columns for Truyen
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_AUTHOR = "Author";
    private static final String COLUMN_IDCATEGORY = "IdCategory";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_ISFAVORITE = "isFavorite";
    private static final String COLUMN_IMAGELINK = "ImageLink";
    private static final String COLUMN_NUMBOFCHAPTERS = "NumberOfChapters";

    // Columns for TheLoai
    private static final String COLUMN_NAMECATEGORY = "NameCategory";

    // Columns for LinkTruyen
    private static final String COLUMN_CHAP = "Chap";
    private static final String COLUMN_LINK = "Link";

    private final Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        // Copy database from assets to app's database directory
        copyDatabaseFromAssets();
    }

    private void copyDatabaseFromAssets() {
        File databasePath = context.getDatabasePath(DATABASE_NAME);
        // Check if database already exists
        if (!databasePath.exists()) {
            try {
                // Ensure the databases directory exists
                databasePath.getParentFile().mkdirs();
                // Open input stream from assets
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                // Open output stream to app's database path
                OutputStream outputStream = new FileOutputStream(databasePath);
                // Copy file
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                Log.d("DbHelper", "Database copied from assets to " + databasePath.getAbsolutePath());
            } catch (IOException e) {
                Log.e("DbHelper", "Error copying database: " + e.getMessage());
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create tables since we're using a pre-existing database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
        Log.d("DbHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
    }

    public ArrayList<Comic> getAllComic() {
        ArrayList<Comic> tmp = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_TRUYEN, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String author = cursor.getString(2);
                int idCategory = cursor.getInt(3);
                String des = cursor.getString(4);
                int isFavorite = cursor.getInt(5);
                String imageLink = cursor.getString(6);
                int numbOfChap = cursor.getInt(7);
                tmp.add(new Comic(id, name, author, idCategory, des, isFavorite, imageLink, numbOfChap));
            }
        } catch (Exception e) {
            Log.e("DbHelper", "Error querying Truyen: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return tmp;
    }

    public ArrayList<ComicCategory> getAllCategory() {
        ArrayList<ComicCategory> tmp = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_THELOAI, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                tmp.add(new ComicCategory(id, name));
            }
        } catch (Exception e) {
            Log.e("DbHelper", "Error querying TheLoai: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return tmp;
    }

    public String getCategoryOfComic(String id) {
        String tmp = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_NAMECATEGORY + " FROM " + TABLE_THELOAI + " WHERE " + COLUMN_IDCATEGORY + "=?", new String[]{id});
            if (cursor.moveToFirst()) {
                tmp = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e("DbHelper", "Error querying category: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return tmp;
    }

    public int updateFavorite(String id, int isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ISFAVORITE, isFavorite);
        int tmp = db.update(TABLE_TRUYEN, contentValues, COLUMN_ID + "=?", new String[]{id});
        db.close();
        return tmp;
    }

    public ArrayList<Chapter> getAllUrl(String id) {
        ArrayList<Chapter> tmp = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_CHAP + ", " + COLUMN_LINK + " FROM " + TABLE_LINKTRUYEN + " WHERE " + COLUMN_ID + " = ? GROUP BY " + COLUMN_CHAP, new String[]{id});
            while (cursor.moveToNext()) {
                String chap = cursor.getString(0);
                String url = cursor.getString(1);
                tmp.add(new Chapter(Integer.parseInt(chap), url));
            }
        } catch (Exception e) {
            Log.e("DbHelper", "Error querying LinkTruyen: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return tmp;
    }
}