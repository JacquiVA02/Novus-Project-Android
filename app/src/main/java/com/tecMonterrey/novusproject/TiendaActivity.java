package com.tecMonterrey.novusproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class TiendaActivity extends AppCompatActivity {

    Button btn_map, btn_avatar, btn_shop;
    LinearLayout accesorios;
    TextView coins;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tienda);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);
            loadUserAvatarItems(userId);
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(TiendaActivity.this, SesionActivity.class));
            finish();
            return;
        }

        btn_map = findViewById(R.id.buttonMapFIrst);
        btn_avatar = findViewById(R.id.buttonAvatarFirst);
        btn_shop = findViewById(R.id.buttonShopFirst);

        accesorios = findViewById(R.id.accesorios);

        coins = findViewById(R.id.CoinQuestion);

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TiendaActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        btn_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TiendaActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.levelname), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_avatar.setBackgroundColor(ContextCompat.getColor(this, R.color.boton));
        btn_map.setBackgroundColor(ContextCompat.getColor(this, R.color.boton));
        btn_shop.setBackgroundColor(ContextCompat.getColor(this, R.color.fondoBoton));

        getCoins();
    }

    private void getCoins() {
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
                            Object coinsValue = snapshot.get("monedas");
                            if (coinsValue instanceof Number) {
                                coins.setText(String.valueOf(((Number) coinsValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'monedas' no es numérico");
                            }
                        } else {
                            Log.d("Firestore", "No hay datos actuales (snapshot es null o no existe)");
                        }
                    });
        }
    }

    private void loadUserAvatarItems(String userId) {
        db.collection("UsuarioAvatar").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> avatarItems = document.getData();
                    if (avatarItems != null) {
                        createButtonsForAvatarItems(avatarItems);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void createButtonForAvatarItem(String itemName, LinearLayout rowLayout, LinearLayout.LayoutParams layoutParams) {
        db.collection("Avatar").document(itemName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Long price = document.getLong("precio"); // Obtener el precio del accesorio
                    ImageButton imageButton = new ImageButton(this);
                    int imageResource = getResources().getIdentifier(itemName, "drawable", getPackageName());
                    Glide.with(this)
                            .load(imageResource)
                            .into(imageButton);

                    // Ajustar el tamaño del ImageButton
                    int size = getResources().getDimensionPixelSize(R.dimen.image_button_size); // Asegúrate de definir image_button_size en dimens.xml
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(size, size);
                    buttonParams.setMargins(2, 6, 2, 6); // Agregar márgenes entre botones

                    imageButton.setLayoutParams(buttonParams);
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP); // Ajusta esto según sea necesario

                    // Configurar el color de fondo con opacidad
                    int colorWithOpacity = Color.argb(179, 204, 204, 204); // 179 es el valor de opacidad (B3 en hexadecimal)
                    imageButton.setBackgroundColor(colorWithOpacity);

                    imageButton.setOnClickListener(v -> {
                        // Mostrar el popup al hacer clic en el botón
                        String description = document.getString("descripcion");
                        showPurchaseDialog(description != null ? description : itemName, itemName, price != null ? price.intValue() : 0);
                    });

                    rowLayout.addView(imageButton);
                } else {
                    Log.d(TAG, "No existe tal documento en la colección Avatar");
                }
            } else {
                Log.d(TAG, "La obtención falló con ", task.getException());
            }
        });
    }


    private void createButtonsForAvatarItems(Map<String, Object> avatarItems) {
        int count = 0;
        LinearLayout rowLayout = null;

        for (Map.Entry<String, Object> entry : avatarItems.entrySet()) {
            if (entry.getValue() instanceof Boolean && !(Boolean) entry.getValue()) {
                if (count % 2 == 0) {
                    rowLayout = new LinearLayout(this);
                    rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                    accesorios.addView(rowLayout);
                }
                String itemName = entry.getKey();
                createButtonForAvatarItem(itemName, rowLayout, null); // layoutParams ya no es necesario aquí
                count++;
            }
        }
    }


    private void showPurchaseDialog(String description, String itemName, int price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Quieres comprar la prenda " + description + "?");
        builder.setMessage("Precio: " + price + " monedas");

        builder.setPositiveButton("Comprar", (dialog, which) -> {
            // Acción para comprar el accesorio
            purchaseAvatarItem(itemName, price);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void purchaseAvatarItem(String itemName, int price) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("Usuario").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Long currentCoins = document.getLong("monedas");
                        if (currentCoins != null && currentCoins >= price) {
                            // Actualizar las monedas del usuario
                            int newCoins = currentCoins.intValue() - price;
                            db.collection("Usuario").document(userId).update("monedas", newCoins)
                                    .addOnSuccessListener(aVoid -> {
                                        // Actualizar el estado del accesorio del usuario
                                        db.collection("UsuarioAvatar").document(userId)
                                                .update(itemName, true)
                                                .addOnSuccessListener(aVoid1 -> {
                                                    // Mostrar un mensaje de éxito
                                                    Toast.makeText(TiendaActivity.this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show();
                                                    // Actualizar la visualización de monedas
                                                    getCoins();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Mostrar un mensaje de error si no se pudo actualizar el estado del accesorio
                                                    Toast.makeText(TiendaActivity.this, "Error al actualizar el estado del accesorio", Toast.LENGTH_SHORT).show();
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        // Mostrar un mensaje de error si no se pudo actualizar las monedas
                                        Toast.makeText(TiendaActivity.this, "Error al actualizar las monedas", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Mostrar un mensaje de error si no hay suficientes monedas
                            Toast.makeText(TiendaActivity.this, "No tienes suficientes monedas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

}
