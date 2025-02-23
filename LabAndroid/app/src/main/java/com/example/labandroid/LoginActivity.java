package com.example.labandroid;

import static com.example.labandroid.helpers.Constants.VALIDATE_USER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.labandroid.helpers.RESTController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validateUser(View view) {
        TextView loginField = findViewById(R.id.usernameField);
        TextView passwordField = findViewById(R.id.passwordField);

        String login = loginField.getText().toString();
        String password = passwordField.getText().toString();

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("login", login);
        jsonObject.addProperty("password", password);
        String json = gson.toJson(jsonObject);
        System.out.println("Request JSON: " + json);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                String response = RESTController.sendPost(VALIDATE_USER, json);
                handler.post(() -> {
                    try {
                        System.out.println("Server Response: " + response);

                        if (!response.equals("error") && !response.isEmpty()) {
                            JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                            String id = jsonResponse.get("id").getAsString();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userId", id);
                            startActivity(intent);
                        } else {
                            System.out.println("Invalid response or error occurred.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}