package com.tecMonterrey.novusproject;

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
    TextView coins, Ac1, Ac2, Ac3, islaLevel, categoria;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String param1, param2, param3;

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
        islaLevel = findViewById(R.id.islaLevels);
        categoria = findViewById(R.id.textCategoriaLevel);

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
        String isla = intent.getStringExtra("isla");
        String base = intent.getStringExtra("base");
        String baseP = intent.getStringExtra("baseP");

        if (isla.equals("I1")) {
            categoria.setText("Fracciones Numéricas");
        } else if (isla.equals("I2")) {
            categoria.setText("Jerarquía de Operaciones");
        } else if (isla.equals("I3")) {
            categoria.setText("Leyes de Exponentes");
        } else if (isla.equals("I4")) {
            categoria.setText("Expresiones Algebraicas");
        } else if (isla.equals("I5")) {
            categoria.setText("Productos Notables");
        } else if (isla.equals("I6")) {
            categoria.setText("Factorización");
        } else if (isla.equals("I7")) {
            categoria.setText("Fracciones Algebraicas");
        } else if (isla.equals("I81") || isla.equals("I82") || isla.equals("I83")) {
            categoria.setText("Solución de Ecuaciones y Desigualdades");
        } else if (isla.equals("I91") || isla.equals("I92")) {
            categoria.setText("Sistemas de Ecuaciones Lineales");
        } else if (isla.equals("I101") || isla.equals("I102")) {
            categoria.setText("Logaritmos: Definición y Propiedades");
        }



        btn_nivel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelsActivity.this, FirstIslandL1Activity.class);
                intent.putExtra("isla", isla);
                intent.putExtra("base", base);
                intent.putExtra("baseP", baseP);
                intent.putExtra("nivel", "L1");
                startActivity(intent);
            }
        });

        btn_nivel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelsActivity.this, FirstIslandL1Activity.class);
                intent.putExtra("isla", isla);
                intent.putExtra("base", base);
                intent.putExtra("baseP", baseP);
                intent.putExtra("nivel", "L2");
                startActivity(intent);
            }
        });

        if (isla.equals("I1")){
            btn_nivel3.setVisibility(View.GONE);
        }

        if (isla.equals("I91")){
            btn_nivel3.setVisibility(View.GONE);
        }

        btn_nivel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelsActivity.this, FirstIslandL1Activity.class);
                intent.putExtra("isla", isla);
                intent.putExtra("base", base);
                intent.putExtra("baseP", baseP);
                intent.putExtra("nivel", "L3");
                startActivity(intent);
            }
        });

        // Configurar OnClickListeners para los botones de nivel
        /*
        setLevelButtonOnClickListener(btn_nivel1, parametro1);
        setLevelButtonOnClickListener(btn_nivel2, parametro2);
        setLevelButtonOnClickListener(btn_nivel3, parametro3);

         */



        getData();
    }

    /*
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
    */

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
