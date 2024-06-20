package com.example.novusproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    ImageView btn_hat, btn_glasses, btn_shirt, btn_shoes, btn_back;
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
            }
        });

        btn_glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        btn_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        btn_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_hat.setBackgroundColor(getResources().getColor(R.color.white));
                btn_glasses.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shirt.setBackgroundColor(getResources().getColor(R.color.white));
                btn_shoes.setBackgroundColor(getResources().getColor(R.color.fondoIcon));
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
        // Se obtiene al usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referenciar al documento de UsuarioIsla
            DocumentReference userDocRef = db.collection("UsuarioAvatar").document(userId);

            // Escuchar cambios en el documento en tiempo real
            userDocRef.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.d(TAG, "Error al obtener el documento", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // El documento existe, obtener sus datos
                    Map<String, Object> userData = documentSnapshot.getData();

                } else {
                    Log.d(TAG, "El documento no existe.");
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
    }

}