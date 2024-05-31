package com.example.novusproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MapaActivity extends AppCompatActivity {

    private static final String TAG = "MapaActivity";

    ImageView btn_profile, btn_back;
    Button btn_map, btn_avatar, btn_shop;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);
        } else {
            Log.d(TAG, "Usuario no autenticado");
            // Redirigir a la pantalla de inicio de sesión si el usuario no está autenticado
            startActivity(new Intent(MapaActivity.this, SesionActivity.class));
            finish();
            return;
        }

        btn_profile = findViewById(R.id.buttonProfile);
        btn_back = findViewById(R.id.buttonBackProfile);
        btn_map = findViewById(R.id.buttonMapProfile);
        btn_avatar = findViewById(R.id.buttonAvatarProfile);
        btn_shop = findViewById(R.id.buttonShopProfile);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, TiendaActivity.class);
                startActivity(intent);
            }
        });

        btn_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
