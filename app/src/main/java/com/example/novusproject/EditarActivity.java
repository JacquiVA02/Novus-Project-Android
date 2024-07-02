package com.example.novusproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
                loadAvatarItems("head", ActualHead);
            }
        });

        btn_glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAvatarItems("face", ActualFace);
            }
        });

        btn_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAvatarItems("neck", ActualNeck);
            }
        });

        btn_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAvatarItems("feet", ActualFeet);
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
                Intent intent = new Intent(EditarActivity.this, AvatarActivity.class);
                startActivity(intent);
                Toast.makeText(EditarActivity.this, "Avatar editado con Éxito", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        verificarVestimenta();
    }

    private void loadAvatarItems(String category, ImageView actualImageView) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("UsuarioAvatar").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> userData = document.getData();
                        if (userData != null) {
                            List<String> itemNames = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : userData.entrySet()) {
                                if (entry.getValue() instanceof Boolean && (Boolean) entry.getValue() && entry.getKey().startsWith(category)) {
                                    itemNames.add(entry.getKey());
                                }
                            }
                            loadDescriptionsAndCreateButtons(itemNames, actualImageView);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }
    }

    private void loadDescriptionsAndCreateButtons(List<String> itemNames, ImageView actualImageView) {
        things.removeAllViews();
        for (String itemName : itemNames) {
            DocumentReference docRef = db.collection("Avatar").document(itemName);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String description = document.getString("descripcion");
                        Button itemButton = new Button(this);

                        itemButton.setBackground(getResources().getDrawable(R.drawable.ircular_button_with_border));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400, 200); // tamaño en píxeles
                        params.setMargins(10, 10, 10, 10); // margen entre botones
                        itemButton.setLayoutParams(params);

                        itemButton.setText(description);
                        itemButton.setTextColor(getResources().getColor(R.color.white));

                        itemButton.setOnClickListener(v -> {
                            Log.d(TAG, "Item seleccionado: " + itemName);
                            setImageViewResource(itemName, actualImageView);
                        });

                        things.addView(itemButton);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }
    }

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

            userDocRef.update(updates)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Datos actualizados correctamente."))
                    .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar los datos.", e));
        }
    }

    private String getResourceName(ImageView imageView) {
        Object tag = imageView.getTag();
        if (tag instanceof Integer) {
            int resId = (int) tag;
            return getResources().getResourceEntryName(resId);
        }
        return null;
    }
}
