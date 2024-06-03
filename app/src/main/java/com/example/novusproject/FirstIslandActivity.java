package com.example.novusproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstIslandActivity extends AppCompatActivity {

    ImageView btn_back, pregunta1, pregunta2;

    Button btn_map, btn_avatar, btn_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_island);

        btn_back = findViewById(R.id.buttonBackQuestion);

        pregunta1 = findViewById(R.id.R1I1);
        pregunta2 = findViewById(R.id.R2I1);

        /*
        btn_map = findViewById(R.id.buttonMapFIrst);
        btn_avatar = findViewById(R.id.buttonAvatarFirst);
        btn_shop = findViewById(R.id.buttonShopFirst);
         */

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        pregunta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Primera");
                intent.putExtra("pregunta", "R1I1");
                startActivity(intent);
            }
        });

        pregunta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R2I1");
                startActivity(intent);
            }
        });

        /*
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        btn_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });

        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandActivity.this, TiendaActivity.class);
                startActivity(intent);
            }
        });
        */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
