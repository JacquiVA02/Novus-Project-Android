
package com.tecMonterrey.novusproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MapaActivity extends AppCompatActivity {

    private static final String TAG = "MapaActivity";

    ImageView btn_profile, btn_back;
    TextView coins, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10;
    Button btn_map, btn_avatar, btn_shop, btnIsland1, btnIsland2, btnIsland3, btnIsland4, btnIsland5, btnIsland6, btnIsland7, btnIsland8, btnIsland9, btnIsland10, btnIsland82, btnIsland83, btnIsland92, btnIsland102, btnExtras;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    LinearLayout nivelesExtra;
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
        btnExtras = findViewById(R.id.extraLevels);

        btnIsland1 = findViewById(R.id.buttonIsland1);
        btnIsland2 = findViewById(R.id.buttonIsland2);
        btnIsland3 = findViewById(R.id.buttonIsland3);
        btnIsland4 = findViewById(R.id.buttonIsland4);
        btnIsland5 = findViewById(R.id.buttonIsland5);
        btnIsland6 = findViewById(R.id.buttonIsland6);
        btnIsland7 = findViewById(R.id.buttonIsland7);
        btnIsland8 = findViewById(R.id.buttonIsland8);
        btnIsland9 = findViewById(R.id.buttonIsland9);
        btnIsland10 = findViewById(R.id.buttonIsland10);

        // Niveles Extra
        btnIsland82 = findViewById(R.id.buttonIsland82);
        btnIsland83 = findViewById(R.id.buttonIsland83);
        btnIsland92 = findViewById(R.id.buttonIsland92);
        btnIsland102 = findViewById(R.id.buttonIsland102);

        nivelesExtra = findViewById(R.id.nuevosNiveles);

        coins = findViewById(R.id.CoinQuestion);

        N1 = findViewById(R.id.n1);
        N2 = findViewById(R.id.n2);
        N3 = findViewById(R.id.n3);
        N4 = findViewById(R.id.n4);
        N5 = findViewById(R.id.n5);
        N6 = findViewById(R.id.n6);
        N7 = findViewById(R.id.n7);
        N8 = findViewById(R.id.n8);
        N9 = findViewById(R.id.n9);
        N10 = findViewById(R.id.n10);

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
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I1");
                intent.putExtra("base", "UsuarioPrimera");
                intent.putExtra("baseP", "Primera");
                startActivity(intent);
            }
        });

        btnIsland2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I2");
                intent.putExtra("base", "UsuarioSegunda");
                intent.putExtra("baseP", "Segunda");
                startActivity(intent);
            }
        });

        btnIsland3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I3");
                intent.putExtra("base", "UsuarioTercera");
                intent.putExtra("baseP", "Tercera");
                startActivity(intent);
            }
        });

        btnIsland4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I4");
                intent.putExtra("base", "UsuarioCuarta");
                intent.putExtra("baseP", "Cuarta");
                startActivity(intent);
            }
        });

        btnIsland5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I5");
                intent.putExtra("base", "UsuarioQuinta");
                intent.putExtra("baseP", "Quinta");
                startActivity(intent);
            }
        });

        btnIsland6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I6");
                intent.putExtra("base", "UsuarioSexta");
                intent.putExtra("baseP", "Sexta");
                startActivity(intent);
            }
        });

        btnIsland7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I7");
                intent.putExtra("base", "UsuarioSeptima");
                intent.putExtra("baseP", "Septima");
                startActivity(intent);
            }
        });

        btnIsland8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I81");
                intent.putExtra("base", "UsuarioOctava");
                intent.putExtra("baseP", "Octava1");
                startActivity(intent);
            }
        });

        btnIsland9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I91");
                intent.putExtra("base", "UsuarioNovena");
                intent.putExtra("baseP", "Novena1");
                startActivity(intent);
            }
        });

        btnIsland10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I101");
                intent.putExtra("base", "UsuarioDecima");
                intent.putExtra("baseP", "Decima1");
                startActivity(intent);
            }
        });

        btnIsland82.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I82");
                intent.putExtra("base", "UsuarioOctava2");
                intent.putExtra("baseP", "Octava2");
                startActivity(intent);
            }
        });

        btnIsland83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I83");
                intent.putExtra("base", "UsuarioOctava3");
                intent.putExtra("baseP", "Octava3");
                startActivity(intent);
            }
        });

        btnIsland92.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I92");
                intent.putExtra("base", "UsuarioNovena2");
                intent.putExtra("baseP", "Novena2");
                startActivity(intent);
            }
        });

        btnIsland102.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapaActivity.this, LevelsActivity.class);
                // Agregar parámetros al Intent
                intent.putExtra("isla", "I102");
                intent.putExtra("base", "UsuarioDecima2");
                intent.putExtra("baseP", "Decima2");
                startActivity(intent);
            }
        });

        // Listener para btnExtras
        btnExtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nivelesExtra.getVisibility() == View.VISIBLE) {
                    // Si el layout de niveles extra está visible, lo ocultamos y mostramos los botones de las islas
                    nivelesExtra.setVisibility(View.GONE);
                    btnIsland1.setVisibility(View.VISIBLE);
                    btnIsland2.setVisibility(View.VISIBLE);
                    btnIsland3.setVisibility(View.VISIBLE);
                    btnIsland4.setVisibility(View.VISIBLE);
                    btnIsland5.setVisibility(View.VISIBLE);
                    btnIsland6.setVisibility(View.VISIBLE);
                    btnIsland7.setVisibility(View.VISIBLE);
                    btnIsland8.setVisibility(View.VISIBLE);
                    btnIsland9.setVisibility(View.VISIBLE);
                    btnIsland10.setVisibility(View.VISIBLE);
                    N1.setVisibility(View.VISIBLE);
                    N2.setVisibility(View.VISIBLE);
                    N3.setVisibility(View.VISIBLE);
                    N4.setVisibility(View.VISIBLE);
                    N5.setVisibility(View.VISIBLE);
                    N6.setVisibility(View.VISIBLE);
                    N7.setVisibility(View.VISIBLE);
                    N8.setVisibility(View.VISIBLE);
                    N9.setVisibility(View.VISIBLE);
                    N10.setVisibility(View.VISIBLE);
                    btnExtras.setText("Niveles Extra");
                } else {
                    // Si el layout de niveles extra está oculto, lo mostramos y ocultamos los botones de las islas
                    nivelesExtra.setVisibility(View.VISIBLE);
                    btnIsland1.setVisibility(View.GONE);
                    btnIsland2.setVisibility(View.GONE);
                    btnIsland3.setVisibility(View.GONE);
                    btnIsland4.setVisibility(View.GONE);
                    btnIsland5.setVisibility(View.GONE);
                    btnIsland6.setVisibility(View.GONE);
                    btnIsland7.setVisibility(View.GONE);
                    btnIsland8.setVisibility(View.GONE);
                    btnIsland9.setVisibility(View.GONE);
                    btnIsland10.setVisibility(View.GONE);
                    N1.setVisibility(View.GONE);
                    N2.setVisibility(View.GONE);
                    N3.setVisibility(View.GONE);
                    N4.setVisibility(View.GONE);
                    N5.setVisibility(View.GONE);
                    N6.setVisibility(View.GONE);
                    N7.setVisibility(View.GONE);
                    N8.setVisibility(View.GONE);
                    N9.setVisibility(View.GONE);
                    N10.setVisibility(View.GONE);
                    btnExtras.setText("Volver");
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.levelname), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_avatar.setBackgroundColor(ContextCompat.getColor(this, R.color.boton));
        btn_map.setBackgroundColor(ContextCompat.getColor(this, R.color.fondoBoton));
        btn_shop.setBackgroundColor(ContextCompat.getColor(this, R.color.boton));

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

                            // Muestra los valores numéricos en los TextView
                            if (coinsValue instanceof Number) {
                                coins.setText(String.valueOf(((Number) coinsValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'monedas' no es numérico");
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
