package com.example.myapplication;

import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivitySummary extends AppCompatActivity {

    private SQLiteDatabase db;
    private ListView listView;
    private TextView textViewCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize ListView and TextView
        listView = findViewById(R.id.listViewSubjects);
        textViewCredits = findViewById(R.id.textViewCredits);

        // Open database (make sure to adjust the database path if needed)
        db = openOrCreateDatabase("StudentDatabase", MODE_PRIVATE, null);

        // Get studentId (you may want to retrieve this dynamically)
        int studentId = 1; // Example studentId

        // Query the database
        Cursor cursor = db.rawQuery("SELECT s.subject_name, s.credits FROM enrollments e JOIN subjects s " +
                "ON e.subject_id = s.subject_id WHERE e.student_id = ?", new String[]{String.valueOf(studentId)});

        // Prepare an ArrayList to hold the subject names and credits
        ArrayList<String> subjects = new ArrayList<>();
        int totalCredits = 0;

        // Check if the cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            int subjectNameColumnIndex = cursor.getColumnIndex("subject_name");
            int creditsColumnIndex = cursor.getColumnIndex("credits");

            if (subjectNameColumnIndex == -1 || creditsColumnIndex == -1) {
                Log.e("Database", "Column not found");
            } else {
                do {
                    String subjectName = cursor.getString(subjectNameColumnIndex);
                    int credits = cursor.getInt(creditsColumnIndex);

                    // Add subject to list
                    subjects.add(subjectName + " - " + credits + " Credits");

                    // Calculate total credits
                    totalCredits += credits;
                } while (cursor.moveToNext());
            }
        }

        // Set the adapter to display subjects in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);
        listView.setAdapter(adapter);

        // Display total credits in the TextView
        textViewCredits.setText("Total Credits: " + totalCredits);

        // Close the cursor
        cursor.close();
    }
}
