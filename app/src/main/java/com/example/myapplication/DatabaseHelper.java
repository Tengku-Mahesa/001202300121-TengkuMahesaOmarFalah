package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SummaryActivity {
    // Jika SummaryActivity kosong, Anda bisa tinggalkan seperti ini.
}

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "student_enrollment.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_SUBJECTS = "subjects";
    private static final String TABLE_ENROLLMENTS = "enrollments";

    // Students Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    // Subjects Table Columns
    private static final String KEY_SUBJECT_ID = "subject_id";
    private static final String KEY_SUBJECT_NAME = "subject_name";
    private static final String KEY_CREDITS = "credits";

    // Enrollments Table Columns
    private static final String KEY_ENROLL_ID = "enroll_id";
    private static final String KEY_STUDENT_ID = "student_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create students table
        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_STUDENTS_TABLE);

        // Create subjects table
        String CREATE_SUBJECTS_TABLE = "CREATE TABLE " + TABLE_SUBJECTS + "("
                + KEY_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBJECT_NAME + " TEXT,"
                + KEY_CREDITS + " INTEGER" + ")";
        db.execSQL(CREATE_SUBJECTS_TABLE);

        // Create enrollments table
        String CREATE_ENROLLMENTS_TABLE = "CREATE TABLE " + TABLE_ENROLLMENTS + "("
                + KEY_ENROLL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_STUDENT_ID + " INTEGER,"
                + KEY_SUBJECT_ID + " INTEGER" + ")";
        db.execSQL(CREATE_ENROLLMENTS_TABLE);

        // Insert sample subjects
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, credits) VALUES ('Mathematics', 3)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, credits) VALUES ('Physics', 4)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, credits) VALUES ('Chemistry', 5)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        onCreate(db);
    }

    // Add a new student
    public boolean registerStudent(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);

        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1; // Return true if insert succeeds
    }

    // Authenticate student
    public boolean loginStudent(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, KEY_EMAIL + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        return cursor.getCount() > 0;
    }

    // Enroll a subject
    public boolean enrollSubject(int studentId, int subjectId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check total credits
        Cursor cursor = db.rawQuery("SELECT SUM(s.credits) FROM " + TABLE_ENROLLMENTS + " e JOIN "
                        + TABLE_SUBJECTS + " s ON e.subject_id = s.subject_id WHERE e.student_id = ?",
                new String[]{String.valueOf(studentId)});
        cursor.moveToFirst();
        int currentCredits = cursor.getInt(0);

        // Fetch subject credits
        Cursor subjectCursor = db.rawQuery("SELECT credits FROM " + TABLE_SUBJECTS + " WHERE subject_id = ?",
                new String[]{String.valueOf(subjectId)});
        subjectCursor.moveToFirst();
        int subjectCredits = subjectCursor.getInt(0);

        if (currentCredits + subjectCredits > 24) {
            return false; // Exceeds credit limit
        }

        ContentValues values = new ContentValues();
        values.put(KEY_STUDENT_ID, studentId);
        values.put(KEY_SUBJECT_ID, subjectId);
        db.insert(TABLE_ENROLLMENTS, null, values);
        return true;
    }
}
