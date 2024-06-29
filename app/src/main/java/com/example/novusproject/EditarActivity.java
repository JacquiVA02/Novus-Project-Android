package com.example.novusproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    ImageView btn_hat, btn_glasses, btn_shirt, btn_shoes, btn_back, ActualHead, ActualFace, ActualFeet, ActualNeck;
    Button btn_guardar;
    LinearLayout things;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);

        btn_back = findViewById(R.id.buttonBackQuestion);
        btn_hat = findViewById(R.id.imageViewHat);
        btn_glasses = findViewById(R.id.imageViewGlasses);
        btn_shirt = findViewById(R.id.imageViewShirt);
        btn_shoes = findViewById(R.id.imageViewShoe);

        btn_guardar = findViewById(R.id.buttonSafe);

        ActualFace = findViewById(R.id.faceEdit);
        ActualFeet = findViewById(R.id.feetEdit);
        ActualHead = findViewById(R.id.headEdit);
        ActualNeck = findViewById(R.id.neckEdit);

        things = findViewById(R.id.things);

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
            startActivity(new Intent(EditarActivity.this, SesionActivity.class));
            finish();
            return;
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });

        btn_hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));

                // Limpiar el LinearLayout antes de agregar nuevos botones
                things.removeAllViews();

                List<String> availableHats = new ArrayList<>();
                for (VestimentaHead item : vestimentaHeads.values()) {
                    if (item.name.startsWith("head") && item.isSelected) {
                        availableHats.add(item.name);
                    }
                }

                for (String hat : availableHats) {
                    // Crear un nuevo botón para cada sombrero disponible
                    Button hatButton = new Button(EditarActivity.this);
                    hatButton.setText(hat);

                    // Establecer un listener de clic para el botón
                    hatButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Sombrero seleccionado: " + hat);
                            // Aquí puedes hacer algo con el sombrero seleccionado
                            // Por ejemplo, actualizar la imagen del sombrero en la vista actual
                            setImageViewResource(hat, ActualHead);
                        }
                    });

                    // Agregar el botón al LinearLayout
                    things.addView(hatButton);
                }
            }
        });

        btn_glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));

                // Limpiar el LinearLayout antes de agregar nuevos botones
                things.removeAllViews();

                List<String> availableFaces = new ArrayList<>();
                for (VestimentaFace item : vestimentaFaces.values()) {
                    if (item.name.startsWith("face") && item.isSelected) {
                        availableFaces.add(item.name);
                    }
                }

                for (String face : availableFaces) {
                    // Crear un nuevo botón para cada sombrero disponible
                    Button faceButton = new Button(EditarActivity.this);
                    faceButton.setText(face);

                    // Establecer un listener de clic para el botón
                    faceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Accesorio seleccionado: " + face);
                            // Aquí puedes hacer algo con el sombrero seleccionado
                            // Por ejemplo, actualizar la imagen del sombrero en la vista actual
                            setImageViewResource(face, ActualFace);
                        }
                    });

                    // Agregar el botón al LinearLayout
                    things.addView(faceButton);
                }
            }
        });

        btn_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));

                // Limpiar el LinearLayout antes de agregar nuevos botones
                things.removeAllViews();

                List<String> availableNecks = new ArrayList<>();
                for (VestimentaNeck item : vestimentaNecks.values()) {
                    if (item.name.startsWith("neck") && item.isSelected) {
                        availableNecks.add(item.name);
                    }
                }

                for (String neck : availableNecks) {
                    // Crear un nuevo botón para cada sombrero disponible
                    Button neckButton = new Button(EditarActivity.this);
                    neckButton.setText(neck);

                    // Establecer un listener de clic para el botón
                    neckButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Cuello seleccionado: " + neck);
                            // Aquí puedes hacer algo con el sombrero seleccionado
                            // Por ejemplo, actualizar la imagen del sombrero en la vista actual
                            setImageViewResource(neck, ActualNeck);
                        }
                    });

                    // Agregar el botón al LinearLayout
                    things.addView(neckButton);
                }
            }
        });

        btn_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.fondoIcon));

                // Limpiar el LinearLayout antes de agregar nuevos botones
                things.removeAllViews();

                List<String> availableFeets = new ArrayList<>();
                for (VestimentaFeet item : vestimentaFeets.values()) {
                    if (item.name.startsWith("feet") && item.isSelected) {
                        availableFeets.add(item.name);
                    }
                }

                for (String feet : availableFeets) {
                    // Crear un nuevo botón para cada sombrero disponible
                    Button feetButton = new Button(EditarActivity.this);
                    feetButton.setText(feet);

                    // Establecer un listener de clic para el botón
                    feetButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Sombrero seleccionado: " + feet);
                            // Aquí puedes hacer algo con el sombrero seleccionado
                            // Por ejemplo, actualizar la imagen del sombrero en la vista actual
                            setImageViewResource(feet, ActualFeet);
                        }
                    });

                    // Agregar el botón al LinearLayout
                    things.addView(feetButton);
                }
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
                Intent intent = new Intent(EditarActivity.this, AvatarActivity.class);
                startActivity(intent);
                Toast.makeText(EditarActivity.this, "Avatar editado con Exito", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        verificarVestimenta();
    }


    private Map<String, VestimentaHead> vestimentaHeads = new HashMap<>();
    private Map<String, VestimentaFace> vestimentaFaces = new HashMap<>();
    private Map<String, VestimentaNeck> vestimentaNecks = new HashMap<>();
    private Map<String, VestimentaFeet> vestimentaFeets = new HashMap<>();

    private void verificarVestimenta() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userDocRef = db.collection("UsuarioAvatar").document(userId);

            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> userData = document.getData();
                        if (userData != null) {
                            String actualFace = (String) userData.get("actualFace");
                            String actualFeet = (String) userData.get("actualFeet");
                            String actualHead = (String) userData.get("actualHead");
                            String actualNeck = (String) userData.get("actualNeck");

                            setImageViewResource(actualFace, ActualFace);
                            setImageViewResource(actualFeet, ActualFeet);
                            setImageViewResource(actualHead, ActualHead);
                            setImageViewResource(actualNeck, ActualNeck);

                            for (Map.Entry<String, Object> entry : userData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (value instanceof Boolean) {
                                    Boolean isSelected = (Boolean) value;
                                    vestimentaHeads.put(key, new VestimentaHead(key, isSelected));
                                    Log.d(TAG, key + ": " + isSelected);
                                }
                            }

                            for (Map.Entry<String, Object> entry : userData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (value instanceof Boolean) {
                                    Boolean isSelected = (Boolean) value;
                                    vestimentaFaces.put(key, new VestimentaFace(key, isSelected));
                                    Log.d(TAG, key + ": " + isSelected);
                                }
                            }

                            for (Map.Entry<String, Object> entry : userData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (value instanceof Boolean) {
                                    Boolean isSelected = (Boolean) value;
                                    vestimentaNecks.put(key, new VestimentaNeck(key, isSelected));
                                    Log.d(TAG, key + ": " + isSelected);
                                }
                            }

                            for (Map.Entry<String, Object> entry : userData.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (value instanceof Boolean) {
                                    Boolean isSelected = (Boolean) value;
                                    vestimentaFeets.put(key, new VestimentaFeet(key, isSelected));
                                    Log.d(TAG, key + ": " + isSelected);
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "El documento no existe.");
                    }
                } else {
                    Log.d(TAG, "Error al obtener el documento: ", task.getException());
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
    }


    private void setImageViewResource(String imageName, ImageView imageView) {
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (resId != 0) {
            imageView.setImageResource(resId);
            imageView.setTag(resId); // Configurar la etiqueta
        } else {
            Log.d(TAG, "Recurso no encontrado para: " + imageName);
        }
    }


    private void guardarCambios() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userDocRef = db.collection("UsuarioAvatar").document(userId);

            Map<String, Object> updates = new HashMap<>();
            updates.put("actualFace", getResourceName(ActualFace));
            updates.put("actualFeet", getResourceName(ActualFeet));
            updates.put("actualHead", getResourceName(ActualHead));
            updates.put("actualNeck", getResourceName(ActualNeck));

            userDocRef.update(updates).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Datos actualizados exitosamente");
            }).addOnFailureListener(e -> {
                Log.d(TAG, "Error al actualizar los datos", e);
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
    }

    private String getResourceName(ImageView imageView) {
        int resId = (int) imageView.getTag();
        return getResources().getResourceEntryName(resId);
    }

}
