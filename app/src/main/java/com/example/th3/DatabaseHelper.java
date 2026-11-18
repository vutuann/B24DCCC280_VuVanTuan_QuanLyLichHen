package com.example.th3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "appointments.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        db.execSQL("CREATE TABLE users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL)");

        // Tạo bảng appointments
        db.execSQL("CREATE TABLE appointments ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL, "
                + "location TEXT NOT NULL, "
                + "date TEXT NOT NULL, "
                + "time TEXT NOT NULL, "
                + "reminder INTEGER NOT NULL)");

        ContentValues user = new ContentValues();
        user.put("username", "admin");
        user.put("password", "123456");
        db.insert("users", null, user);

        // Chèn 3 lịch hẹn mẫu
        db.insert("appointments", null, createValues("Họp nhóm đồ án", "Phòng B203", "2026-10-25", "09:00", 1));
        db.insert("appointments", null, createValues("Khám răng", "Nha khoa Minh Tâm", "2026-10-26", "14:30", 1));
        db.insert("appointments", null, createValues("Đi cà phê với bạn", "Highland Coffee", "2026-10-27", "19:00", 0));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS appointments");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    private ContentValues createValues(String title, String location, String date, String time, int reminder) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("location", location);
        values.put("date", date);
        values.put("time", time);
        values.put("reminder", reminder);
        return values;
    }

    // ============ BẢNG USERS ============

    // Trả về true nếu có tài khoản khớp
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Thêm tài khoản mới
    public long insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        return db.insert("users", null, values);
    }

    // ============ BẢNG APPOINTMENTS ============

    // Thêm lịch hẹn, trả về id vừa thêm
    public long insertAppointment(Appointment a) {
        SQLiteDatabase db = this.getWritableDatabase();
        int reminder = 0;
        if (a.isReminder()) {
            reminder = 1;
        }
        ContentValues values = createValues(a.getTitle(), a.getLocation(), a.getDate(), a.getTime(), reminder);
        return db.insert("appointments", null, values);
    }

    // Lấy tất cả lịch hẹn, sắp xếp theo ngày giờ tăng dần
    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appointments ORDER BY date ASC, time ASC", null);
        if (cursor.moveToFirst()) {
            do {
                // Thứ tự cột: 0 id, 1 title, 2 location, 3 date, 4 time, 5 reminder
                Appointment a = new Appointment();
                a.setId(cursor.getInt(0));
                a.setTitle(cursor.getString(1));
                a.setLocation(cursor.getString(2));
                a.setDate(cursor.getString(3));
                a.setTime(cursor.getString(4));
                a.setReminder(cursor.getInt(5) == 1);
                list.add(a);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy một lịch hẹn theo id
    public Appointment getAppointmentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appointments WHERE id = ?",
                new String[]{String.valueOf(id)});
        Appointment a = null;
        if (cursor.moveToFirst()) {
            a = new Appointment();
            a.setId(cursor.getInt(0));
            a.setTitle(cursor.getString(1));
            a.setLocation(cursor.getString(2));
            a.setDate(cursor.getString(3));
            a.setTime(cursor.getString(4));
            a.setReminder(cursor.getInt(5) == 1);
        }
        cursor.close();
        return a;
    }

    // Cập nhật trạng thái nhắc nhở
    public int updateReminder(int id, boolean on) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (on) {
            values.put("reminder", 1);
        } else {
            values.put("reminder", 0);
        }
        return db.update("appointments", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Xóa lịch hẹn theo id
    public int deleteAppointment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("appointments", "id = ?", new String[]{String.valueOf(id)});
    }

    // Đếm tổng số lịch hẹn
    public int countAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM appointments", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Đếm số lịch hẹn đang bật nhắc nhở
    public int countReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM appointments WHERE reminder = ?",
                new String[]{"1"});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
