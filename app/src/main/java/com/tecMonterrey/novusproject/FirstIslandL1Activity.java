package com.tecMonterrey.novusproject;

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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirstIslandL1Activity extends AppCompatActivity {

    ImageView btn_back, pregunta1, pregunta2, pregunta3, pregunta4, pregunta5, pregunta6, pregunta7, pregunta8, pregunta9, pregunta10, btn_profileI;
    TextView coins, islaB, nivelB;
    Button btn_map, btn_avatar, btn_shop;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    View mainView;

    // Mapa para asociar los IDs de los botones con sus correspondientes ImageViews
    private Map<String, Integer> buttonToImageViewMap;

    String isla, base, baseP, nivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_island_l1);

        //Fondo de la interfaz
        mainView = findViewById(R.id.levelname);

        btn_back = findViewById(R.id.buttonBackQuestion);

        // Recuperar los parámetros del Intent
        Intent intent = getIntent();
        isla = intent.getStringExtra("isla");
        base = intent.getStringExtra("base");
        baseP = intent.getStringExtra("baseP");
        nivel = intent.getStringExtra("nivel");

        coins = findViewById(R.id.CoinQuestion);

        islaB = findViewById(R.id.islaButtons);
        nivelB = findViewById(R.id.nivelButtons);

        pregunta1 = findViewById(R.id.R1I1L1);
        pregunta2 = findViewById(R.id.R2I1L1);
        pregunta3 = findViewById(R.id.R3I1L1);
        pregunta4 = findViewById(R.id.R4I1L1);
        pregunta5 = findViewById(R.id.R5I1L1);
        pregunta6 = findViewById(R.id.R6I1L1);
        pregunta7 = findViewById(R.id.R7I1L1);
        pregunta8 = findViewById(R.id.R8I1L1);
        pregunta9 = findViewById(R.id.R9I1L1);
        pregunta10 = findViewById(R.id.R10I1L1);

        btn_profileI = findViewById(R.id.buttonProfileIsland);

        // Inicializar el mapa de asociaciones
        buttonToImageViewMap = new HashMap<>();
        initializeButtonToImageViewMap();

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
            startActivity(new Intent(FirstIslandL1Activity.this, SesionActivity.class));
            finish();
            return;
        }

        if (nivel.equals("L1")){
            nivelB.setText("Nivel 1");
        } else if (nivel.equals("L2")){
            nivelB.setText("Nivel 2");
        } else{
            nivelB.setText("Nivel 3");
        }




        if (isla.equals("I1")) {
            islaB.setText("Isla 1");
        } else if (isla.equals("I2")) {
            islaB.setText("Isla 2");
        } else if (isla.equals("I3")) {
            islaB.setText("Isla 3");
        } else if (isla.equals("I4")) {
            islaB.setText("Isla 4");
        } else if (isla.equals("I5")) {
            islaB.setText("Isla 5");
        } else if (isla.equals("I6")) {
            islaB.setText("Isla 6");
        } else if (isla.equals("I7")) {
            islaB.setText("Isla 7");
        } else if (isla.equals("I81")) {
            islaB.setText("Isla 8");
        } else if (isla.equals("I82")) {
            islaB.setText("Isla 8.2");
        } else if (isla.equals("I83")) {
            islaB.setText("Isla 8.3");
        } else if (isla.equals("I91")) {
            islaB.setText("Isla 9");
        } else if (isla.equals("I92")) {
            islaB.setText("Isla 9.2");
        } else if (isla.equals("I101")) {
            islaB.setText("Isla 10");
        } else if (isla.equals("I102")){
            islaB.setText("Isla 10.2");
        }



        /*
        btn_map = findViewById(R.id.buttonMapFIrst);
        btn_avatar = findViewById(R.id.buttonAvatarFirst);
        btn_shop = findViewById(R.id.buttonShopFirst);
         */

        btn_profileI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y vuelve a la actividad anterior
            }
        });


        pregunta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R1" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R2" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R3" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R4" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R5" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R6" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R7" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R8" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R9" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        pregunta10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIslandL1Activity.this, PreguntaActivity.class);
                intent.putExtra("isla", baseP + nivel);
                intent.putExtra("pregunta", "R10" + isla + nivel);
                intent.putExtra("estado", base);
                startActivity(intent);
            }
        });

        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //    return insets;
        //});

        verificarAvance();
        getData();
        Log.d("NivelTag", nivel);
        setBackground(nivel);
    }

    private void initializeButtonToImageViewMap() {
        // Agregar las asociaciones entre los botones e ImageViews
        buttonToImageViewMap.put("R1" + isla + nivel, R.id.R1I1L1);
        buttonToImageViewMap.put("R2" + isla + nivel, R.id.R2I1L1);
        buttonToImageViewMap.put("R3" + isla + nivel, R.id.R3I1L1);
        buttonToImageViewMap.put("R4" + isla + nivel, R.id.R4I1L1);
        buttonToImageViewMap.put("R5" + isla + nivel, R.id.R5I1L1);
        buttonToImageViewMap.put("R6" + isla + nivel, R.id.R6I1L1);
        buttonToImageViewMap.put("R7" + isla + nivel, R.id.R7I1L1);
        buttonToImageViewMap.put("R8" + isla + nivel, R.id.R8I1L1);
        buttonToImageViewMap.put("R9" + isla + nivel, R.id.R9I1L1);
        buttonToImageViewMap.put("R10" + isla + nivel, R.id.R10I1L1);
        // Agrega todas las asociaciones necesarias aquí
        // buttonToImageViewMap.put("botonKey", R.id.imageViewId);
    }

    private void actualizarEstadoBotones(Map<String, Object> userData) {
        boolean estadoAnterior = true; // Se inicializa como true para habilitar la primera pregunta

        for (int i = 1; i <= 10; i++) {
            String preguntaKey = "R" + i + isla + nivel;
            //Log.d(TAG, "Verificando preguntaKey: " + preguntaKey);

            if (buttonToImageViewMap.containsKey(preguntaKey)) {
                int preguntaId = buttonToImageViewMap.get(preguntaKey);
                ImageView pregunta = findViewById(preguntaId);

                if (pregunta != null) {
                    if (userData.containsKey(preguntaKey) && userData.get(preguntaKey) instanceof Boolean) {
                        boolean estado = (Boolean) userData.get(preguntaKey);
                        //Log.d(TAG, "Estado de " + preguntaKey + ": " + estado);
                        pregunta.setEnabled(estadoAnterior); // Habilitar/deshabilitar el botón según el estado anterior
                        estadoAnterior = estado; // Actualizar el estado anterior
                    } else {
                        pregunta.setEnabled(false); // Deshabilitar el botón si no hay datos
                    }
                } else {
                    Log.d(TAG, "ImageView no encontrado para preguntaKey: " + preguntaKey);
                }
            } else {
                Log.d(TAG, "preguntaKey no encontrada en buttonToImageViewMap: " + preguntaKey);
            }
        }
    }


    private void verificarAvance() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            DocumentReference userDocRef = db.collection(base).document(userId);

            userDocRef.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.d(TAG, "Error al obtener el documento", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Map<String, Object> userData = documentSnapshot.getData();
                    if (userData != null) {
                        //Log.d(TAG, "Datos del usuario: " + userData);
                        actualizarEstadoBotones(userData);

                        for (Map.Entry<String, Object> entry : userData.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();

                            if (value instanceof Boolean && (Boolean) value) {
                                if (buttonToImageViewMap.containsKey(key)) {
                                    int imageViewId = buttonToImageViewMap.get(key);
                                    ImageView imageView = findViewById(imageViewId);
                                    if (imageView != null) {
                                        imageView.setImageResource(R.drawable.green);
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
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Log.w("Firestore", "Listen failed", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Obtiene el valor del campo y lo muestra en el TextView
                            Object coinsValue = snapshot.get("monedas");

                            if (coinsValue instanceof Number) {
                                coins.setText(String.valueOf(((Number) coinsValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'monedas' no es numérico");
                            }

                            // Cargar la imagen de perfil redonda si está disponible
                            String profileImageUrl = snapshot.getString("profileImageUrl");
                            if (profileImageUrl != null) {
                                Glide.with(FirstIslandL1Activity.this)
                                        .load(profileImageUrl)
                                        .fitCenter()
                                        .centerInside()
                                        .circleCrop() // Esta línea hace que la imagen sea redonda
                                        .into(btn_profileI);
                            }



                        } else {
                            Log.d("Firestore", "No hay datos actuales (snapshot es null o no existe)");
                        }
                    });
        }
    }


    private void setBackground(String nivel) {
        if (nivel.equals("L1")) {
            mainView.setBackgroundResource(R.drawable.backl1);
        } else if (nivel.equals("L2")) {
            mainView.setBackgroundResource(R.drawable.backl2);
        } else {
            mainView.setBackgroundResource(R.drawable.backl3);
        }
    }

}
