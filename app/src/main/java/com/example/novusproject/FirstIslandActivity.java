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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class FirstIslandActivity extends AppCompatActivity {

    ImageView btn_back, pregunta1, pregunta2;
    TextView coins, Ac1, Ac2, Ac3;
    Button btn_map, btn_avatar, btn_shop;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_island);

        btn_back = findViewById(R.id.buttonBackQuestion);

        coins = findViewById(R.id.CoinQuestion);
        Ac1 = findViewById(R.id.Ac1FI);
        Ac2 = findViewById(R.id.Ac2FI);
        Ac3 = findViewById(R.id.Ac3FI);

        pregunta1 = findViewById(R.id.R1I1);
        pregunta2 = findViewById(R.id.R2I1);

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
            startActivity(new Intent(FirstIslandActivity.this, SesionActivity.class));
            finish();
            return;
        }

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
                intent.putExtra("estado", "UsuarioPrimera");
                startActivity(intent);
            }
        });

        pregunta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandActivity.this, PreguntaActivity.class);
                intent.putExtra("isla", "Segunda");
                intent.putExtra("pregunta", "R2I1");
                intent.putExtra("estado", "UsuarioPrimera");
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

        verificarAvance();
        getData();
    }

    private void verificarAvance() {
        // Se obtiene al usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referenciar al documento de UsuarioIsla
            DocumentReference userDocRef = db.collection("UsuarioPrimera").document(userId);

            // Escuchar cambios en el documento en tiempo real
            userDocRef.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.d(TAG, "Error al obtener el documento", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // El documento existe, obtener sus datos
                    Map<String, Object> userData = documentSnapshot.getData();
                    if (userData != null) {
                        for (Map.Entry<String, Object> entry : userData.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            //Log.d(TAG, "Key: " + key + " Value: " + value);

                            // Verificar si el valor es true
                            if (value instanceof Boolean && (Boolean) value) {
                                // Obtener el id del ImageView usando el nombre de la clave
                                int imageViewId = getResources().getIdentifier(key, "id", getPackageName());
                                if (imageViewId != 0) {
                                    ImageView imageView = findViewById(imageViewId);
                                    if (imageView != null) {
                                        // Cambiar la imagen del ImageView
                                        imageView.setImageResource(R.drawable.green); // new_image es el nombre de la nueva imagen
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "El documento no existe.");
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
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
