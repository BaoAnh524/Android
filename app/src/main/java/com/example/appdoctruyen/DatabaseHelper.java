package com.example.appdoctruyen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.appdoctruyen.Account;
import com.example.appdoctruyen.Chapter;
import com.example.appdoctruyen.Comic;
import com.example.appdoctruyen.ComicCategory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "COMIC.db";
    private static final int DATABASE_VERSION = 2;

    // Table: Truyen
    private static final String TABLE_TRUYEN = "Truyen";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_AUTHOR = "Author";
    private static final String COLUMN_IDCATEGORY = "IdCategory";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_ISFAVORITE = "isFavorite";
    private static final String COLUMN_IMAGELINK = "imageLink";
    private static final String COLUMN_NUMOFCHAPTER = "numberOfChapter";

    // Table: TheLoai
    private static final String TABLE_THELOAI = "TheLoai";
    private static final String COLUMN_NAMECATEGORY = "NameCategory";
    private static final String COLUMN_DESCRIPTION_CATEGORY = "Description";

    // Table: LinkTruyen
    private static final String TABLE_LINKTRUYEN = "LinkTruyen";
    private static final String COLUMN_CHAP = "Chap";
    private static final String COLUMN_LINK = "Link";

    // Table: Accounts
    private static final String TABLE_ACCOUNTS = "accounts";
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
        // Create Truyen table
        String CREATE_TRUYEN_TABLE = "CREATE TABLE " + TABLE_TRUYEN + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_AUTHOR + " TEXT NOT NULL," +
                COLUMN_IDCATEGORY + " INTEGER NOT NULL," +
                COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                COLUMN_ISFAVORITE + " INTEGER NOT NULL," +
                COLUMN_IMAGELINK + " TEXT," +
                COLUMN_NUMOFCHAPTER + " INTEGER)";
        db.execSQL(CREATE_TRUYEN_TABLE);

        // Create TheLoai table
        String CREATE_THELOAI_TABLE = "CREATE TABLE " + TABLE_THELOAI + " (" +
                COLUMN_IDCATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAMECATEGORY + " TEXT NOT NULL," +
                COLUMN_DESCRIPTION_CATEGORY + " TEXT NOT NULL)";
        db.execSQL(CREATE_THELOAI_TABLE);

        // Create LinkTruyen table
        String CREATE_LINKTRUYEN_TABLE = "CREATE TABLE " + TABLE_LINKTRUYEN + " (" +
                COLUMN_ID + " INTEGER NOT NULL," +
                COLUMN_CHAP + " INTEGER NOT NULL," +
                COLUMN_LINK + " TEXT NOT NULL)";
        db.execSQL(CREATE_LINKTRUYEN_TABLE);

        // Create Accounts table
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT NOT NULL UNIQUE," +
                COLUMN_PASSWORD + " TEXT NOT NULL," +
                COLUMN_PHONE + " TEXT," +
                COLUMN_BIRTHYEAR + " TEXT," +
                COLUMN_GENDER + " TEXT," +
                COLUMN_EMAIL + " TEXT)";
        db.execSQL(CREATE_ACCOUNTS_TABLE);

        // Insert sample data for TheLoai
        insertTheLoai(db, "Action", "Thể loại này thường có nội dung về đánh nhau, bạo lực, hỗn loạn, với diễn biến nhanh...");
        insertTheLoai(db, "Adventure", "Thể loại phiêu lưu, mạo hiểm, thường là hành trình của các nhân vật");
        insertTheLoai(db, "Comedy", "Thể loại có nội dung trong sáng và cảm động, thường có các tình tiết gây cười, các xung đột nhẹ nhàng");
        insertTheLoai(db, "Detective", "Các truyện có nội dung về các vụ án, các thám tử cảnh sát điều tra...");
        insertTheLoai(db, "Romance", "Truyện có nội dung theo hướng tình cảm nam nữ.");
        insertTheLoai(db, "Fantasy", "Truyện có nội đung gải tưởng, phép thuật thường được trẻ em yêu thích.");

        // Insert sample data for Truyen
        insertTruyen(db, "Doraemon", "Fujiko Fujio", 3, "Bộ truyện kể về một chú mèo máy tên là Doraemon đến từ thế kỷ XXII trở thành người bạn thân thiết của cậu bé Nobita, một cậu học sinh lớp 4 hết sức hậu đậu và không bao giờ đạt được thành tích tốt.", 0, "https://upload.wikimedia.org/wikipedia/en/b/bd/Doraemon_character.png", 1);
        insertTruyen(db, "One Piece", "Oda Eiichiro", 2, "One Piece xoay quanh 1 nhóm cướp biển được gọi là Băng Hải tặc Mũ Rơm - Straw Hat Pirates - được thành lập và lãnh đạo bởi thuyền trưởng Monkey D. Luffy", 0, "https://discover.garmin.com/vi-VN/instinct/instinct-onepiece/images/onepiece-kv-luffy.png", 2);
        insertTruyen(db, "Conan", "Gosho Aoyama", 4, "Mở đầu câu truyện, cậu học sinh trung học 16 tuổi Shinichi Kudo bị biến thành cậu bé Conan Edogawa.", 0, "https://phunuvietnam.mediacdn.vn/179072216278405120/2022/11/4/edogawa-conan--166754179290680712885.jpg", 4);
        insertTruyen(db, "Kimetsu no Yaiba", "Gotouge Koyoharu", 1, "Truyện kể về hành trình trở thành kiếm sĩ diệt quỷ của thiếu niên Kamado Tanjirō sau khi gia đình cậu bị quỷ sát hại và em gái Nezuko của cậu bị biến thành quỷ.", 0, "https://static.wikia.nocookie.net/kimetsunoyaiba/images/0/01/Kimetsu_no_Yaiba_Key_Visual_4.png/revision/latest?cb=20191016145643&path-prefix=vi", 3);
        insertTruyen(db, "My hero academia", "HORIKOSHI Kouhei", 1, "Vào tương lai, lúc mà con người với những sức mạnh siêu nhiên là điều thường thấy quanh thế giới. Đây là câu chuyện về Izuku Midoriya, từ một kẻ bất tài trở thành một siêu anh hùng. Tất cả ta cần là mơ ước.", 0, "https://upload.wikimedia.org/wikipedia/vi/5/5a/Boku_no_Hero_Academia_Volume_1.png", 2);
        insertTruyen(db, "SPYxFamily", "Endo Tatsuya", 3, "Siêu điệp viên ngầu lòi mật danh Twilight nhận được nhiệm vụ tiếp cận một mục tiêu có ảnh hưởng hết sức nghiêm trọng đến nền hòa bình thế giới. Để làm được điều đó, trước tiên anh phải xây dựng cho mình một vỏ bọc là một gia đình hạnh phúc và đứa con học trong ngôi trường danh giá.", 0, "https://storage-bravo.cuutruyen.net/file/cuutruyen/uploads/manga/152/panorama/processed-623be082de7371c6943f1128c4c7187d.jpg", 1);
        insertTruyen(db, "Dragon Ball", "Toriyama Akira", 1, "Dragon Ball là bộ truyện nổi tiếng và phổ biến rộng rãi bậc nhất trên toàn thế giới, là một trong những bộ manga được tiêu thụ nhiều nhất mọi thời đại.", 0, "https://meocuchay.com/wp-content/uploads/2022/04/pasted-image-0-14.png", 2);
        insertTruyen(db, "Inuyasha", "Takahashi Rumiko", 5, "Câu truyện bắt đầu tại Feudal Nhật Bản, khi một tên bán yêu Inuyasha trộm Ngọc tứ hồn – viên ngọc mang lại sức mạnh hoàn toàn cho quỷ dữ. Trong thời hiện đại tại Tokyo, một học sinh trung học tên Kagome Higurashi đang trên đường đến trường. Cô dừng tại giếng cạn ở nhà cô và tìm con mèo, Buyo. Lúc ấy cô bị một con yêu quái rệp kéo xuống giếng và rơi vào thời Sengoku của Nhật Bản.", 0, "https://m.media-amazon.com/images/M/MV5BMGNmMWI4MGUtYmU0ZS00ZDkxLTgzMTAtZDU4YmQ1MDM3Y2IyXkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_UY1200_CR106,0,630,1200_AL_.jpg", 2);
        insertTruyen(db, "Cardcaptor Sakura", "CLAMP", 6, "Truyện tranh Card Captor Sakura xoay quanh Kinomoto Sakura, một học sinh tiểu học tình cờ phát hiện ra tiềm năng phép thuật của mình sau khi tình cờ mở một cuốn sách chứa bộ bài ma thuật bị phong ấn trong nhiều năm. Sau đó cô được giao nhiệm vụ thu phục tất cả các thẻ bài rải rác để ngăn chúng phá hủy thế giới.", 0, "https://i.pinimg.com/originals/10/25/59/10255931d4ac7dc2d499f6e173998f5f.jpg", 2);
        insertTruyen(db, "Tensei-shitara Slime datta ken", "Fuse", 6, "Một manga khác chuyển thể từ light novel đang hot ở nhật. Một anh chàng bị tên cướp đâm chết khi đang gặp vợ chưa cưới của đồng nghiệp. Khi đang thoi thóp trước khi chết, người đầy máu, anh ta nghe được một tiếng nói kỳ lạ. Giọng nói ấy chuyển thể sự tiếc nuối của anh chàng vì vẫn còn tân trước khi đi và ban cho anh ta chiêu thức đặc biệt. Liệu đây có phải là trò đùa?", 0, "https://rainobe.com/wp-content/uploads/2021/08/rainobe-ve-chuyen-toi-chuyen-sinh-thanh-slime-post.png", 2);
        // Insert sample data for LinkTruyen
        insertLinkTruyen(db, 3, 1, "https://docs.google.com/document/d/e/2PACX-1vQ_V1yBHZNBIQ_3nQBYxH2tUnidv7HlQ6xy7RHExIZnzsrizV5kwFz4t4lWWuPWq4JBgrc96mzZK42x/pub");
        insertLinkTruyen(db, 3, 2, "https://docs.google.com/document/d/e/2PACX-1vTrro4Z-BU3SbLhxrs1We2BZVnAvO9LhFOid4qBWoFEABFWVEcBJHzgOAP4CLnRXXg1rDU9eFPKYfFZ/pub?embedded=true");
        insertLinkTruyen(db, 1, 1, "https://docs.google.com/document/d/e/2PACX-1vQyEA042LaeRxHRhK3rsAOWOxCDF-lb4gWh9w5UNd4REEnMrj3mkbsxCPsdTuOf9MgJJdEyuM76pBqx/pub?embedded=true");
        insertLinkTruyen(db, 1, 2, "https://docs.google.com/document/d/e/2PACX-1vQNVjxts1LigT6IDmml20LMb_tJqDfhCKe3m0lzSMHSmadvoibtad76G5CcTDyeZOEf6O60HDmqm5nF/pub?embedded=true");
        insertLinkTruyen(db, 1, 3, "https://docs.google.com/document/d/e/2PACX-1vRTywl_ZSow83xu-jJ3Fw__9Cc5KAOS2FD3xk3jLHsJNJ8joOAp35uM13401X5rj5aP9Ovr6hTX47ao/pub?embedded=true");
        insertLinkTruyen(db, 2, 1, "https://docs.google.com/document/d/e/2PACX-1vRLJxZT1sq44hTzLl22M0d26dDgL1p5bw0C9Q5Vf8tCRCDENgpKDnaDKW3RDePCCHBqbA69ajDTu6B7/pub?embedded=true");
        insertLinkTruyen(db, 3, 3, "https://docs.google.com/document/d/e/2PACX-1vSfN6Xzm8tNW_qqd8-SCcB9O_EkX0MQPJH8wScKYdHX8gXUMJXk_AZjUdiflqDuCdqGkfTI6bXfCCe-/pub?embedded=true");
        insertLinkTruyen(db, 3, 4, "https://docs.google.com/document/d/e/2PACX-1vREv5HjTHsJPoW19Erbc4N0qu4O1dqhWY08ht1sv-z1KSLVKntMrLiukKFCjtGS-VPOHQJAqHJ0o4Tz/pub?embedded=true");
        insertLinkTruyen(db, 3, 5, "https://docs.google.com/document/d/e/2PACX-1vSwDjvWFDT-N7wzuOvoD-DTH9XDBVEXw5kmdskpCshi5azPSFckX0EfGKW16khnhABw_AtQvT43mQCb/pub?embedded=true");
        insertLinkTruyen(db, 3, 6, "https://docs.google.com/document/d/e/2PACX-1vTurAEKpYLLr64Yji1lSymq3GhaZ4IRotFJ4w0FetYJJ506a2B6G7xgZ4O8frjgf2Ql0B1tCeV8Nryv/pub?embedded=true");

        // Insert sample data for Accounts
        insertAccount(db, "user", "pass", "123456789", "1990", "Nam", "user@example.com");
        insertAccount(db, "admin", "admin123", "987654321", "1985", "Nữ", "admin@example.com");
        insertAccount(db, "mc4981", "123", "232323232", "2004", "Nam", "mc4981@example.com");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUYEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THELOAI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINKTRUYEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        onCreate(db);
    }

    // Insert methods
    private void insertTheLoai(SQLiteDatabase db, String nameCategory, String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMECATEGORY, nameCategory);
        values.put(COLUMN_DESCRIPTION_CATEGORY, description);
        db.insert(TABLE_THELOAI, null, values);
    }

    private void insertTruyen(SQLiteDatabase db, String name, String author, int idCategory,
                              String description, int isFavorite, String imageLink, int numOfChapter) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_IDCATEGORY, idCategory);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_ISFAVORITE, isFavorite);
        values.put(COLUMN_IMAGELINK, imageLink);
        values.put(COLUMN_NUMOFCHAPTER, numOfChapter);
        db.insert(TABLE_TRUYEN, null, values);
    }

    private void insertLinkTruyen(SQLiteDatabase db, int id, int chap, String link) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_CHAP, chap);
        values.put(COLUMN_LINK, link);
        db.insert(TABLE_LINKTRUYEN, null, values);
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

    // Get methods
    public ArrayList<Comic> getAllComic(){
        ArrayList<Comic> tmp = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRUYEN, null, null, null, null, null, null);

        while (cursor.moveToNext()){
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
        cursor.close();
        return tmp;
    }

    public ArrayList<ComicCategory> getAllCategory(){
        ArrayList<ComicCategory> tmp = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_THELOAI, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            tmp.add(new ComicCategory(id, name));
        }
        cursor.close();
        return tmp;
    }

    public String getCategoryOfComic(String id){
        String tmp = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT NameCategory FROM TheLoai WHERE IdCategory=?", new String[]{id});
        if (c.moveToFirst()) {
            tmp = c.getString(0);
        }
        c.close();
        return tmp;
    }

    public int updateFavorite(String id, int isFavorite){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isFavorite", isFavorite);
        int tmp = db.update(TABLE_TRUYEN, contentValues, "Id=?", new String[]{String.valueOf(id)});
        return tmp;
    }

    public ArrayList<Chapter> getAllUrl(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Chapter> tmp = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT Chap, Link FROM LinkTruyen WHERE Id = ? GROUP BY chap", new String[]{id});
        while (c.moveToNext()){
            String chap = c.getString(0);
            String url = c.getString(1);
            tmp.add(new Chapter(Integer.parseInt(chap), url));
        }
        c.close();
        return tmp;
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