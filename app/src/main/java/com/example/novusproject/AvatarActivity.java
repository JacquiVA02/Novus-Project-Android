package com.example.novusproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

import java.util.Map;

public class AvatarActivity extends AppCompatActivity {

    Button btn_map, btn_avatar, btn_shop, btn_edit;
    ImageView ActualHead, ActualFace, ActualFeet, ActualNeck;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avatar);

        btn_map = findViewById(R.id.buttonMapFIrst);
        btn_avatar = findViewById(R.id.buttonAvatarFirst);
        btn_shop = findViewById(R.id.buttonShopFirst);
        btn_edit = findViewById(R.id.buttonEdit);

        ActualFace = findViewById(R.id.face);
        ActualFeet = findViewById(R.id.feet);
        ActualHead = findViewById(R.id.head);
        ActualNeck = findViewById(R.id.neck);

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
            startActivity(new Intent(AvatarActivity.this, SesionActivity.class));
            finish();
            return;
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvatarActivity.this, EditarActivity.class);
                startActivity(intent);
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvatarActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        /*
        btn_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvatarActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });
        */

        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvatarActivity.this, TiendaActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        verificarVestimenta();
    }

    private void verificarVestimenta() {
        // Obtener al usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referenciar al documento de UsuarioAvatar
            DocumentReference userDocRef = db.collection("UsuarioAvatar").document(userId);

            // Obtener el documento una vez
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // El documento existe, obtener sus datos
                        Map<String, Object> userData = document.getData();
                        if (userData != null) {
                            // Obtener valores actuales
                            String actualFace = (String) userData.get("actualFace");
                            String actualFeet = (String) userData.get("actualFeet");
                            String actualHead = (String) userData.get("actualHead");
                            String actualNeck = (String) userData.get("actualNeck");

                            // Configurar la interfaz de usuario según los datos
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
        } else {
            Log.d(TAG, "Recurso no encontrado para: " + imageName);
        }
    }
}