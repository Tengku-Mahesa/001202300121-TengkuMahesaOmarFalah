package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Pastikan menggunakan layout yang benar

        // Inisialisasi EditText
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);
    }

    public void loginStudent(View view) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        // Validasi input sebelum login
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.loginStudent(email, password)) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SubjectActivity.class));
            finish(); // Optional: menutup LoginActivity setelah login berhasil
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
