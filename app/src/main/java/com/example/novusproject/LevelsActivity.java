package com.example.novusproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LevelsActivity extends AppCompatActivity {

    Button btn_nivel1, btn_nivel2, btn_nivel3;
    ImageView btn_back;
    TextView coins, Ac1, Ac2, Ac3;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_levels);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
        } else {
            // Redirigir a la pantalla de inicio de sesión si el usuario no está autenticado
            startActivity(new Intent(LevelsActivity.this, SesionActivity.class));
            finish();
            return;
        }

        btn_back = findViewById(R.id.buttonBackQuestion);

        coins = findViewById(R.id.CoinQuestion);
        Ac1 = findViewById(R.id.Ac1FI);
        Ac2 = findViewById(R.id.Ac2FI);
        Ac3 = findViewById(R.id.Ac3FI);

        btn_nivel1 = findViewById(R.id.nivel1);
        btn_nivel2 = findViewById(R.id.nivel2);
        btn_nivel3 = findViewById(R.id.nivel3);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recuperar los parámetros del Intent
        Intent intent = getIntent();
        String parametro1 = intent.getStringExtra("level1");
        String parametro2 = intent.getStringExtra("level2");
        String parametro3 = intent.getStringExtra("level3");

        // Configurar OnClickListeners para los botones de nivel
        setLevelButtonOnClickListener(btn_nivel1, parametro1);
        setLevelButtonOnClickListener(btn_nivel2, parametro2);
        setLevelButtonOnClickListener(btn_nivel3, parametro3);
    }

    private void setLevelButtonOnClickListener(Button button, String className) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Intent intent = new Intent(LevelsActivity.this, clazz);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Log.e("LevelsActivity", "Class not found: " + className, e);
                }
            }
        });
    }
}
