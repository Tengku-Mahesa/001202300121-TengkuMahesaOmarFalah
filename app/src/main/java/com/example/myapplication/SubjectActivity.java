package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SubjectActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject); // Pastikan layout yang benar

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);
    }

    // Metode untuk mendaftar mata kuliah
    private void enrollSubject(int studentId, int subjectId) {
        if (dbHelper.enrollSubject(studentId, subjectId)) {
            Toast.makeText(this, "Subject Enrolled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Credit Limit Exceeded!", Toast.LENGTH_SHORT).show();
        }
    }
}
