package com.example.novusproject;

import static android.content.ContentValues.TAG;

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

public class SecondIslandActivity extends AppCompatActivity {

    ImageView btn_back, pregunta1, pregunta2, pregunta3, pregunta4, pregunta5, pregunta6, pregunta7, pregunta8, pregunta9, pregunta10;
    TextView coins, Ac1, Ac2, Ac3;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second_island);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btn_back = findViewById(R.id.buttonBackQuestion);

        coins = findViewById(R.id.CoinQuestion);
        Ac1 = findViewById(R.id.Ac1FI);
        Ac2 = findViewById(R.id.Ac2FI);
        Ac3 = findViewById(R.id.Ac3FI);

        pregunta1 = findViewById(R.id.R1I2);
        pregunta2 = findViewById(R.id.R2I2);
        pregunta3 = findViewById(R.id.R3I2);
        pregunta4 = findViewById(R.id.R4I2);
        pregunta5 = findViewById(R.id.R5I2);
        pregunta6 = findViewById(R.id.R6I2);
        pregunta7 = findViewById(R.id.R7I2);
        pregunta8 = findViewById(R.id.R8I2);
        pregunta9 = findViewById(R.id.R9I2);
        pregunta10 = findViewById(R.id.R10I2);

        // Inicialización de Firebase
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        // Verificar el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(SecondIslandActivity.this, SesionActivity.class));
            finish();
            return;
        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        pregunta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R1I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R2I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R3I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R4I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R5I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R6I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R7I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R8I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R9I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });

        pregunta10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R10I2");
                intent.putExtra("estado", "UsuarioSegunda");
                startActivity(intent);
            }
        });


        getData();
    }

    private void getData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("Usuario").document(userId)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Log.w("Firestore", "Listen failed", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Obtiene el valor del campo y lo muestra en el TextView
                            Object coinsValue = snapshot.get("monedas");
                            Object c1Value = snapshot.get("c1");
                            Object c2Value = snapshot.get("c2");
                            Object c3Value = snapshot.get("c3");

                            if (coinsValue instanceof Number) {
                                coins.setText(String.valueOf(((Number) coinsValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'monedas' no es numérico");
                            }

                            if (c1Value instanceof Number) {
                                Ac1.setText(String.valueOf(((Number) c1Value).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'c1' no es numérico");
                            }

                            if (c2Value instanceof Number) {
                                Ac2.setText(String.valueOf(((Number) c2Value).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'c2' no es numérico");
                            }

                            if (c3Value instanceof Number) {
                                Ac3.setText(String.valueOf(((Number) c3Value).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'c3' no es numérico");
                            }

                        } else {
                            Log.d("Firestore", "No hay datos actuales (snapshot es null o no existe)");
                        }
                    });
        }
    }

}