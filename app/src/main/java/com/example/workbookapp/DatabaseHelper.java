package com.example.workbookapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String dbName = "workbook_app";

    public DatabaseHelper(@Nullable Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, fullName TEXT NOT NULL, username TEXT NOT NULL," +
                    "password TEXT NOT NULL, avatar TEXT, birthDay INTEGER, createdAt INTEGER NOT NULL, updatedAt INTEGER NOT NULL)");
            db.execSQL("CREATE TABLE notifies(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, message TEXT NOT NULL, isRead INTEGER NOT NULL DEFAULT 0," +
                    "userId INTEGER NOT NULL, createdAt INTEGER NOT NULL, updatedAt INTEGER NOT NULL, FOREIGN KEY(userId) REFERENCES users(id))");
            db.execSQL("CREATE TABLE tables(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, themeId INTEGER, userId INTEGER NOT NULL," +
                    "createdAt INTEGER NOT NULL, updatedAt INTEGER NOT NULL, FOREIGN KEY(themeId) REFERENCES themes(id), FOREIGN KEY(userId) REFERENCES users(id))");
            db.execSQL("CREATE TABLE cards(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, description TEXT, status INTEGER NOT NULL DEFAULT 0, startDate INTEGER NOT NULL," +
                    "endDate INTEGER, tableId INTEGER NOT NULL, createdAt INTEGER NOT NULL, updatedAt INTEGER NOT NULL, FOREIGN KEY(tableId) REFERENCES tables(id))");
            db.execSQL("CREATE TABLE themes(id INTEGER PRIMARY KEY AUTOINCREMENT, color TEXT NOT NULL, createdAt INTEGER NOT NULL, updatedAt INTEGER NOT NULL)");

            // insert theme
            long currentDateTime = System.currentTimeMillis() ; // Convert milliseconds to seconds
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#ec407a', " + currentDateTime + ", " + currentDateTime + ")");
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#f44336', " + currentDateTime + ", " + currentDateTime + ")");
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#9c27b0', " + currentDateTime + ", " + currentDateTime + ")");
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#3f51b5', " + currentDateTime + ", " + currentDateTime + ")");
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#009688', " + currentDateTime + ", " + currentDateTime + ")");
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#ffc107', " + currentDateTime + ", " + currentDateTime + ")");
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#795548', " + currentDateTime + ", " + currentDateTime + ")");
            db.execSQL("INSERT INTO themes(color, createdAt, updatedAt) VALUES ('#8bc34a', " + currentDateTime + ", " + currentDateTime + ")");

            Log.d("DB_CREATE_SUCCESS", "Database created successfully.");
        }catch (SQLException e){
            Log.e("DB_CREATE_ERROR", "Error creating database: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS tables");
            db.execSQL("DROP TABLE IF EXISTS cards");
            db.execSQL("DROP TABLE IF EXISTS themes");
            onCreate(db);
        }catch (SQLException e){
            Log.e("DB_Upgrade_ERROR", "Error upgrading database: " + e.getMessage());
        }
    }

    public boolean checkUsername(String username){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
            boolean isCheck = cursor.getCount() > 0;
            cursor.close();

            return isCheck;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return false;
        }
    }

    @SuppressLint("Range")
    public User getUserById(int id){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(id)});

            User user = null;

            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));

                // Chuyển đổi chuỗi ngày sinh từ cơ sở dữ liệu sang kiểu Date trong Java
                long birthDayMillis = cursor.getLong(cursor.getColumnIndex("birthDay"));
                user.setBirthDay(new Date(birthDayMillis));

                long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                user.setCreatedAt(new Date(createdAtMillis));

                long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                user.setUpdatedAt(new Date(updatedAtMillis));
            }

            cursor.close();
            return user;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public User getUserByUsernameAndPassword(String username, String password){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

            User user = null;

            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));

                // Chuyển đổi chuỗi ngày sinh từ cơ sở dữ liệu sang kiểu Date trong Java
                long birthDayMillis = cursor.getLong(cursor.getColumnIndex("birthDay"));
                user.setBirthDay(new Date(birthDayMillis));

                long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                user.setCreatedAt(new Date(createdAtMillis));

                long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                user.setUpdatedAt(new Date(updatedAtMillis));
            }

            cursor.close();
            return user;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    public int addUser(String fullName, String username, String password){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("fullName", fullName);
            contentValues.put("username", username);
            contentValues.put("password", password);

            long currentDateTime = System.currentTimeMillis() ;
            contentValues.put("createdAt", currentDateTime);
            contentValues.put("updatedAt", currentDateTime);

            long newUserId = db.insert("users", null, contentValues);
            return Integer.parseInt(String.valueOf(newUserId));
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return -1;
        }
    }

    public boolean updateUser(int id, String avatar, String fullName, Date birthDay){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues userValues = new ContentValues();
            userValues.put("fullName", fullName);
            userValues.put("avatar", avatar);
            userValues.put("birthDay", birthDay == null ? null : birthDay.getTime());
            long currentDateTime = System.currentTimeMillis() ;
            userValues.put("updatedAt", currentDateTime);

            // Câu lệnh SQL UPDATE
            int affectedRows = db.update("users", userValues, "id = ?", new String[]{String.valueOf(id)});

            // Kiểm tra số lượng hàng đã được cập nhật
            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return false;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Theme> getAllTheme(){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM themes", null);

            ArrayList<Theme> themes = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Theme theme = new Theme();
                    theme.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    theme.setColor(cursor.getString(cursor.getColumnIndex("color")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    theme.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    theme.setUpdatedAt(new Date(updatedAtMillis));

                    themes.add(theme);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return themes;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public Theme getThemeById(int id){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM themes WHERE id = ?", new String[]{String.valueOf(id)});

            Theme theme = new Theme();
            if (cursor.moveToFirst()) {
                theme.setId(cursor.getInt(cursor.getColumnIndex("id")));
                theme.setColor(cursor.getString(cursor.getColumnIndex("color")));

                // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                theme.setCreatedAt(new Date(createdAtMillis));

                long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                theme.setUpdatedAt(new Date(updatedAtMillis));
            }

            cursor.close();
            return theme;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Table> getTables(int userId){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM tables WHERE userId = ? ORDER BY createdAt DESC", new String[]{String.valueOf(userId)});

            ArrayList<Table> tables = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Table table = new Table();
                    table.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    table.setName(cursor.getString(cursor.getColumnIndex("name")));
                    table.setThemeId(cursor.getInt(cursor.getColumnIndex("themeId")));
                    table.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    table.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    table.setUpdatedAt(new Date(updatedAtMillis));

                    tables.add(table);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return tables;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Table> getTablesBySearchTerm(int userId, String searchTerm){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM tables WHERE userId = ? AND LOWER(name) LIKE '%' || LOWER(?) || '%' ORDER BY createdAt DESC", new String[]{String.valueOf(userId), searchTerm});

            ArrayList<Table> tables = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Table table = new Table();
                    table.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    table.setName(cursor.getString(cursor.getColumnIndex("name")));
                    table.setThemeId(cursor.getInt(cursor.getColumnIndex("themeId")));
                    table.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    table.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    table.setUpdatedAt(new Date(updatedAtMillis));

                    tables.add(table);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return tables;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public Table getTableById(int id){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM tables WHERE id = ? ORDER BY createdAT DESC", new String[]{String.valueOf(id)});

            Table table = new Table();

            if (cursor.moveToFirst()) {
                table.setId(cursor.getInt(cursor.getColumnIndex("id")));
                table.setName(cursor.getString(cursor.getColumnIndex("name")));
                table.setThemeId(cursor.getInt(cursor.getColumnIndex("themeId")));
                table.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));

                // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                table.setCreatedAt(new Date(createdAtMillis));

                long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                table.setUpdatedAt(new Date(updatedAtMillis));
            }

            cursor.close();
            return table;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    public int addTable(String name, int themeId, int userId){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues tableValues = new ContentValues();
            tableValues.put("name", name);
            tableValues.put("themeId", themeId == 0 ? null : themeId);
            tableValues.put("userId", userId);
            long currentDateTime = System.currentTimeMillis() ;
            tableValues.put("createdAt", currentDateTime);
            tableValues.put("updatedAt", currentDateTime);

            // Thêm dữ liệu vào bảng 'tables'
            long newTableId = db.insert("tables", null, tableValues);
            return Integer.parseInt(String.valueOf(newTableId));
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return -1;
        }
    }

    public boolean updateTable(int id, String name, int themeId){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues tableValues = new ContentValues();
            tableValues.put("name", name);
            tableValues.put("themeId", themeId == 0 ? null : themeId);
            long currentDateTime = System.currentTimeMillis() ;
            tableValues.put("updatedAt", currentDateTime);

            // Câu lệnh SQL UPDATE
            int affectedRows = db.update("tables", tableValues, "id = ?", new String[]{String.valueOf(id)});

            // Kiểm tra số lượng hàng đã được cập nhật
            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return false;
        }
    }

    public boolean removeTable(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("cards", "tableId = ?", new String[]{String.valueOf(id)});

            // Câu lệnh SQL UPDATE
            int affectedRows = db.delete("tables", "id = ?", new String[]{String.valueOf(id)});

            // Kiểm tra số lượng hàng đã được cập nhật
            if (affectedRows > 0) {
                db.setTransactionSuccessful();
                return true;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
        }finally {
            db.endTransaction();
        }
        return false;
    }

    @SuppressLint("Range")
    public ArrayList<Card> getCardExpireLessByUserId(int userId, int timeAlmost){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            long timeAlmostDateTime = System.currentTimeMillis() + timeAlmost;
            Cursor cursor = db.rawQuery("SELECT c.*\n" +
                    "FROM users u\n" +
                    "INNER JOIN tables t ON u.id = t.userId\n" +
                    "INNER JOIN cards c ON t.id = c.tableId\n" +
                    "WHERE u.id = ? AND c.endDate <= ? AND c.status = 0", new String[]{String.valueOf(userId), String.valueOf(timeAlmostDateTime)});

            ArrayList<Card> cards = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Card card = new Card();
                    card.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    card.setName(cursor.getString(cursor.getColumnIndex("name")));
                    card.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    card.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                    card.setTableId(cursor.getInt(cursor.getColumnIndex("tableId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long startDateMillis = cursor.getLong(cursor.getColumnIndex("startDate"));
                    card.setStartDate(new Date(startDateMillis));

                    long endDateMillis = cursor.getLong(cursor.getColumnIndex("endDate"));
                    card.setEndDate(new Date(endDateMillis));

                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    card.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    card.setUpdatedAt(new Date(updatedAtMillis));

                    cards.add(card);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return cards;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Card> getCardAlmostExpiredByUserId(int userId, int timeAlmost){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            long timeAlmostDateTime = System.currentTimeMillis() + timeAlmost;
            long oneMinute = 1 * 60 * 1000;
            long timeAlmostDateTimeBeforeOneMinute = timeAlmostDateTime - oneMinute;
            Cursor cursor = db.rawQuery("SELECT c.*\n" +
                    "FROM users u\n" +
                    "INNER JOIN tables t ON u.id = t.userId\n" +
                    "INNER JOIN cards c ON t.id = c.tableId\n" +
                    "WHERE u.id = ? AND (c.endDate BETWEEN ? AND ?) AND c.status = 0", new String[]{String.valueOf(userId), String.valueOf(timeAlmostDateTimeBeforeOneMinute), String.valueOf(timeAlmostDateTime)});

            ArrayList<Card> cards = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Card card = new Card();
                    card.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    card.setName(cursor.getString(cursor.getColumnIndex("name")));
                    card.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    card.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                    card.setTableId(cursor.getInt(cursor.getColumnIndex("tableId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long startDateMillis = cursor.getLong(cursor.getColumnIndex("startDate"));
                    card.setStartDate(new Date(startDateMillis));

                    long endDateMillis = cursor.getLong(cursor.getColumnIndex("endDate"));
                    card.setEndDate(new Date(endDateMillis));

                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    card.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    card.setUpdatedAt(new Date(updatedAtMillis));

                    cards.add(card);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return cards;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Card> getCardExpiredByUserId(int userId){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            long currentDateTime = System.currentTimeMillis();
            Cursor cursor = db.rawQuery("SELECT c.*\n" +
                    "FROM users u\n" +
                    "INNER JOIN tables t ON u.id = t.userId\n" +
                    "INNER JOIN cards c ON t.id = c.tableId\n" +
                    "WHERE u.id = ? AND c.endDate <= ? AND c.status = 0", new String[]{String.valueOf(userId), String.valueOf(currentDateTime)});

            ArrayList<Card> cards = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Card card = new Card();
                    card.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    card.setName(cursor.getString(cursor.getColumnIndex("name")));
                    card.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    card.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                    card.setTableId(cursor.getInt(cursor.getColumnIndex("tableId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long startDateMillis = cursor.getLong(cursor.getColumnIndex("startDate"));
                    card.setStartDate(new Date(startDateMillis));

                    long endDateMillis = cursor.getLong(cursor.getColumnIndex("endDate"));
                    card.setEndDate(new Date(endDateMillis));

                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    card.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    card.setUpdatedAt(new Date(updatedAtMillis));

                    cards.add(card);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return cards;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Card> getCards(int tableId){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM cards WHERE tableId = ? ORDER BY createdAt DESC", new String[]{String.valueOf(tableId)});

            ArrayList<Card> cards = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Card card = new Card();
                    card.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    card.setName(cursor.getString(cursor.getColumnIndex("name")));
                    card.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    card.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                    card.setTableId(cursor.getInt(cursor.getColumnIndex("tableId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long startDateMillis = cursor.getLong(cursor.getColumnIndex("startDate"));
                    card.setStartDate(new Date(startDateMillis));

                    long endDateMillis = cursor.getLong(cursor.getColumnIndex("endDate"));
                    card.setEndDate(new Date(endDateMillis));

                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    card.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    card.setUpdatedAt(new Date(updatedAtMillis));

                    cards.add(card);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return cards;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Card> getCardsBySearchTerm(int tableId, String searchTerm){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM cards WHERE tableId = ? AND LOWER(name) LIKE '%' || LOWER(?) || '%' ORDER BY createdAt DESC", new String[]{String.valueOf(tableId), searchTerm});

            ArrayList<Card> cards = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Card card = new Card();
                    card.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    card.setName(cursor.getString(cursor.getColumnIndex("name")));
                    card.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    card.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                    card.setTableId(cursor.getInt(cursor.getColumnIndex("tableId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long startDateMillis = cursor.getLong(cursor.getColumnIndex("startDate"));
                    card.setStartDate(new Date(startDateMillis));

                    long endDateMillis = cursor.getLong(cursor.getColumnIndex("endDate"));
                    card.setEndDate(new Date(endDateMillis));

                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    card.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    card.setUpdatedAt(new Date(updatedAtMillis));

                    cards.add(card);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return cards;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public Card getCardById(int id){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM cards WHERE id = ? ORDER BY createdAT DESC", new String[]{String.valueOf(id)});

            Card card = new Card();

            if (cursor.moveToFirst()) {
                card.setId(cursor.getInt(cursor.getColumnIndex("id")));
                card.setName(cursor.getString(cursor.getColumnIndex("name")));
                card.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                card.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                card.setTableId(cursor.getInt(cursor.getColumnIndex("tableId")));

                // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                long startDateMillis = cursor.getLong(cursor.getColumnIndex("startDate"));
                card.setStartDate(new Date(startDateMillis));

                long endDateMillis = cursor.getLong(cursor.getColumnIndex("endDate"));
                card.setEndDate(new Date(endDateMillis));

                long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                card.setCreatedAt(new Date(createdAtMillis));

                long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                card.setUpdatedAt(new Date(updatedAtMillis));
            }

            cursor.close();
            return card;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    public int addCard(String name, String description, int status, int tableId, Date startDate, Date endDate){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cardValues = new ContentValues();
            cardValues.put("name", name);
            cardValues.put("description", description.equals("") ? null : description);
            cardValues.put("status", status);
            cardValues.put("tableId", tableId);
            cardValues.put("startDate", startDate.getTime());
            cardValues.put("endDate", endDate.getTime());
            long currentDateTime = System.currentTimeMillis() ;
            cardValues.put("status", endDate.getTime() <= currentDateTime ? 2 : 0);
            cardValues.put("createdAt", currentDateTime);
            cardValues.put("updatedAt", currentDateTime);

            long newCardId = db.insert("cards", null, cardValues);
            return Integer.parseInt(String.valueOf(newCardId));
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return -1;
        }
    }

    public boolean updateCard(int id, String name, String description, Date startDate, Date endDate){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cardValues = new ContentValues();
            cardValues.put("name", name);
            cardValues.put("description", description.equals("") ? null : description);
            cardValues.put("startDate", startDate.getTime());
            cardValues.put("endDate", endDate.getTime());
            long currentDateTime = System.currentTimeMillis() ;
            cardValues.put("status", endDate.getTime() <= currentDateTime ? 2 : 0);
            cardValues.put("updatedAt", currentDateTime);

            // Câu lệnh SQL UPDATE
            int affectedRows = db.update("cards", cardValues, "id = ?", new String[]{String.valueOf(id)});

            // Kiểm tra số lượng hàng đã được cập nhật
            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return false;
        }
    }

    public boolean finishCard(int id, int status){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cardValues = new ContentValues();
            cardValues.put("status", status);
            long currentDateTime = System.currentTimeMillis() ;
            cardValues.put("updatedAt", currentDateTime);

            // Câu lệnh SQL UPDATE
            int affectedRows = db.update("cards", cardValues, "id = ?", new String[]{String.valueOf(id)});

            // Kiểm tra số lượng hàng đã được cập nhật
            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return false;
        }
    }

    public boolean expiredCard(int id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cardValues = new ContentValues();
            cardValues.put("status", 2);
            long currentDateTime = System.currentTimeMillis() ;
            cardValues.put("updatedAt", currentDateTime);

            // Câu lệnh SQL UPDATE
            int affectedRows = db.update("cards", cardValues, "id = ?", new String[]{String.valueOf(id)});

            // Kiểm tra số lượng hàng đã được cập nhật
            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return false;
        }
    }

    public boolean removeCard(int id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            // Câu lệnh SQL UPDATE
            int affectedRows = db.delete("cards", "id = ?", new String[]{String.valueOf(id)});

            // Kiểm tra số lượng hàng đã được cập nhật
            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return false;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Notify> getNotifies(int userId){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM notifies WHERE userId = ? ORDER BY createdAt DESC", new String[]{String.valueOf(userId)});

            ArrayList<Notify> notifies = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    Notify notify = new Notify();
                    notify.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    notify.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    notify.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                    notify.setIsRead(cursor.getInt(cursor.getColumnIndex("isRead")));
                    notify.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));

                    // Chuyển đổi chuỗi ngày tạo và cập nhật từ cơ sở dữ liệu sang kiểu Date trong Java
                    long createdAtMillis = cursor.getLong(cursor.getColumnIndex("createdAt"));
                    notify.setCreatedAt(new Date(createdAtMillis));

                    long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updatedAt"));
                    notify.setUpdatedAt(new Date(updatedAtMillis));

                    notifies.add(notify);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return notifies;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return null;
        }
    }

    @SuppressLint("Range")
    public int notifyCount(int userId){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) count FROM notifies WHERE userId = ? AND isRead = 0", new String[]{String.valueOf(userId)});

            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

            cursor.close();
            return count;
        }catch (SQLException e){
            Log.e("DB_ERROR", "Error database: " + e.getMessage());
            return 0;
        }
    }

    public int addNotify(String title, String message, int userId){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues notifyValues = new ContentValues();
            notifyValues.put("title", title);
            notifyValues.put("message", message);
            notifyValues.put("isRead", 0);
            notifyValues.put("userId", userId);
            long currentDateTime = System.currentTimeMillis() ;
            notifyValues.put("createdAt", currentDateTime);
            notifyValues.put("updatedAt", currentDateTime);

            long newNotifyId = db.insert("notifies", null, notifyValues);
            return Integer.parseInt(String.valueOf(newNotifyId));
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return -1;
        }
    }

    public int readAllNotify(int userId){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues notifyValues = new ContentValues();
            notifyValues.put("isRead", 1);
            long currentDateTime = System.currentTimeMillis() ;
            notifyValues.put("updatedAt", currentDateTime);

            long newNotifyId = db.update("notifies", notifyValues, "userId = ? AND isRead = 0", new String[]{String.valueOf(userId)});
            return Integer.parseInt(String.valueOf(newNotifyId));
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error inserting data: " + e.getMessage());
            return -1;
        }
    }
}
