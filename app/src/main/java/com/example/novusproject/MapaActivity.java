package com.example.novusproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MapaActivity extends AppCompatActivity {

    private static final String TAG = "MapaActivity";

    ImageView btn_profile, btn_back;
    TextView coins, Ac1, Ac2, Ac3;
    Button btn_map, btn_avatar, btn_shop, btnIsland1, btnIsland2, btnIsland3, btnIsland4;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa);

        db = FirebaseFirestore.getInstance();
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
        btn_back = findViewById(R.id.buttonBackQuestion);
        btn_map = findViewById(R.id.buttonMapFIrst);
        btn_avatar = findViewById(R.id.buttonAvatarFirst);
        btn_shop = findViewById(R.id.buttonShopFirst);

        btnIsland1 = findViewById(R.id.buttonIsland1);
        btnIsland2 = findViewById(R.id.buttonIsland2);
        btnIsland3 = findViewById(R.id.buttonIsland3);
        btnIsland4 = findViewById(R.id.buttonIsland4);

        coins = findViewById(R.id.CoinQuestion);
        Ac1 = findViewById(R.id.Ac1FI);
        Ac2 = findViewById(R.id.Ac2FI);
        Ac3 = findViewById(R.id.Ac3FI);

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

        btnIsland1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, FirstIslandActivity.class);
                startActivity(intent);
            }
        });

        btnIsland2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, SecondIslandActivity.class);
                startActivity(intent);
            }
        });

        btnIsland3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, ThirdIslandActivity.class);
                startActivity(intent);
            }
        });

        btnIsland4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, FourthIslandActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
                            // Obtiene los valores de los campos numéricos
                            Object coinsValue = snapshot.get("monedas");
                            Object c1Value = snapshot.get("c1");
                            Object c2Value = snapshot.get("c2");
                            Object c3Value = snapshot.get("c3");

                            // Muestra los valores numéricos en los TextView
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

                            // Cargar la imagen de perfil redonda si está disponible
                            String profileImageUrl = snapshot.getString("profileImageUrl");
                            if (profileImageUrl != null) {
                                Glide.with(MapaActivity.this)
                                        .load(profileImageUrl)
                                        .fitCenter()
                                        .centerInside()
                                        .circleCrop() // Esta línea hace que la imagen sea redonda
                                        .into(btn_profile);
                            }

                        } else {
                            Log.d("Firestore", "No hay datos actuales (snapshot es null o no existe)");
                        }
                    });
        }
    }


}
