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
    Button btn_map, btn_avatar, btn_shop;
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
        btn_back = findViewById(R.id.buttonBackProfile);
        btn_map = findViewById(R.id.buttonMapShop);
        btn_avatar = findViewById(R.id.buttonAvatarShop);
        btn_shop = findViewById(R.id.buttonShopShop);

        coins = findViewById(R.id.textViewCoin);
        Ac1 = findViewById(R.id.textViewAc1);
        Ac2 = findViewById(R.id.textViewAc2);
        Ac3 = findViewById(R.id.textViewAc3);

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

        getData();
    }

    private void getData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("Usuario").document(userId)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("Firestore", "Listen failed", e);
                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {
                                // Obtiene el valor del campo y lo muestra en el TextView
                                Object value = snapshot.get("monedas");
                                Object value1 = snapshot.get("c1");
                                Object value2 = snapshot.get("c2");
                                Object value3 = snapshot.get("c3");
                                if (value != null) {
                                    coins.setText(String.valueOf(value));
                                    Ac1.setText(String.valueOf(value1));
                                    Ac2.setText(String.valueOf(value2));
                                    Ac3.setText(String.valueOf(value3));
                                } else {
                                    Log.d("Firestore", "Campo 'monedas' no encontrado");
                                }
                            } else {
                                Log.d("Firestore", "Current data: null");
                            }
                        }
                    });
        }
    }

}
