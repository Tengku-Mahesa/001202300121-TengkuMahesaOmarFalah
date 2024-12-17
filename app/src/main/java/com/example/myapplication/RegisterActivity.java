package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  // Pastikan menggunakan layout yang benar

        // Inisialisasi EditText
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);
    }

    public void registerStudent(View view) {
        // Mendapatkan input dari pengguna
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        // Validasi input sebelum registrasi
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Melakukan registrasi
        if (dbHelper.registerStudent(name, email, password)) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            // Pindah ke LoginActivity setelah registrasi berhasil
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Optional: untuk menutup RegisterActivity setelah pindah
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
