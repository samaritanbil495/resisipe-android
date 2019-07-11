package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText nicknameText = findViewById(R.id.nicknameText);
        EditText emailText = findViewById(R.id.emailText);
        EditText passwordText = findViewById(R.id.passwordText);
        EditText confirmPasswordText = findViewById(R.id.confirmPasswordText);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener((v) -> {
            if(!confirmPasswordText.getText().toString().equals(passwordText.getText().toString()))
                Toast.makeText(RegisterActivity.this, "Passwords must match!", Toast.LENGTH_SHORT);
            else {
                User user = new User();
                user.setNickname(nicknameText.getText().toString().trim());
                user.setEmail(emailText.getText().toString().trim());
                user.setPassword(passwordText.getText().toString().trim());
            }
        });
    }


}
