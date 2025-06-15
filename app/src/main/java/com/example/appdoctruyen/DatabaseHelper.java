package com.example.appdoctruyen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppDocTruyen.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_BIRTHYEAR = "birthYear";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_EMAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT NOT NULL UNIQUE," +
                COLUMN_PASSWORD + " TEXT NOT NULL," +
                COLUMN_PHONE + " TEXT," +
                COLUMN_BIRTHYEAR + " TEXT," +
                COLUMN_GENDER + " TEXT," +
                COLUMN_EMAIL + " TEXT)";
        db.execSQL(CREATE_ACCOUNTS_TABLE);

        insertDefaultAccounts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        onCreate(db);
    }

    private void insertDefaultAccounts(SQLiteDatabase db) {
        insertAccount(db, "user", "pass", "123456789", "1990", "Nam", "user@example.com");
        insertAccount(db, "admin", "admin123", "987654321", "1985", "Nữ", "admin@example.com");
        insertAccount(db, "mc4981", "123", "232323232", "2004", "Nam", "mc4981@example.com");
        Log.d("DatabaseHelper", "Default accounts inserted");
    }

    private void insertAccount(SQLiteDatabase db, String username, String password, String phone, String birthYear, String gender, String email) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_BIRTHYEAR, birthYear);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_EMAIL, email);
        long result = db.insert(TABLE_ACCOUNTS, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert account: " + username);
        } else {
            Log.d("DatabaseHelper", "Successfully inserted account: " + username);
        }
    }

    public boolean addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, account.getUsername());
        values.put(COLUMN_PASSWORD, account.getPassword());
        values.put(COLUMN_PHONE, account.getPhone());
        values.put(COLUMN_BIRTHYEAR, account.getBirthYear());
        values.put(COLUMN_GENDER, account.getGender());
        values.put(COLUMN_EMAIL, account.getEmail());

        long result = db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, account.getPassword());
        values.put(COLUMN_PHONE, account.getPhone());
        values.put(COLUMN_BIRTHYEAR, account.getBirthYear());
        values.put(COLUMN_GENDER, account.getGender());
        values.put(COLUMN_EMAIL, account.getEmail());

        int result = db.update(TABLE_ACCOUNTS, values, COLUMN_USERNAME + " = ?", new String[]{account.getUsername()});
        db.close();
        return result > 0;
    }

    public Account getAccountByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Account account = null;
        Cursor cursor = db.query(TABLE_ACCOUNTS, new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_PHONE, COLUMN_BIRTHYEAR, COLUMN_GENDER, COLUMN_EMAIL},
                COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);

        if (cursor.moveToFirst()) {
            account = new Account(
                    cursor.getString(1), // username
                    cursor.getString(2), // password
                    cursor.getString(3), // phone
                    cursor.getString(4), // birthYear
                    cursor.getString(5), // gender
                    cursor.getString(6)  // email
            );
        }
        cursor.close();
        db.close();
        return account;
    }

    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(1), // username
                        cursor.getString(2), // password
                        cursor.getString(3), // phone
                        cursor.getString(4), // birthYear
                        cursor.getString(5), // gender
                        cursor.getString(6)  // email
                );
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountList;
    }
}