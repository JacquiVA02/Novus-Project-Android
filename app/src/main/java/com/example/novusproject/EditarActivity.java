package com.example.novusproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditarActivity extends AppCompatActivity {

    ImageView btn_hat, btn_glasses, btn_shirt, btn_shoes, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);

        btn_back = findViewById(R.id.buttonBackQuestion);
        btn_hat = findViewById(R.id.imageViewHat);
        btn_glasses = findViewById(R.id.imageViewGlasses);
        btn_shirt = findViewById(R.id.imageViewShirt);
        btn_shoes = findViewById(R.id.imageViewShoe);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });

        btn_hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        btn_glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        btn_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        btn_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}