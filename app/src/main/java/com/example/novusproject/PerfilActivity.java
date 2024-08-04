package com.example.novusproject;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

public class PerfilActivity extends AppCompatActivity {

    ImageView btn_back, btn_editPhoto, profile;
    TextView nombreUsuario, points, title, level;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private StorageReference mStorage;
    private static final int GALLEY_INTENT = 1;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        profile = findViewById(R.id.imageViewProfileProfile);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(PerfilActivity.this, SesionActivity.class));
            finish();
            return;
        }

        nombreUsuario = findViewById(R.id.nameUser);
        points = findViewById(R.id.puntosPerfil);
        title = findViewById(R.id.tituloPerfil);
        level = findViewById(R.id.nivelPerfil);

        btn_back = findViewById(R.id.buttonBackQuestion);
        btn_editPhoto = findViewById(R.id.imageViewEditProfile);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        btn_editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLEY_INTENT);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getUserData();
    }

    private void getUserData() {
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
                                String value = snapshot.getString("nombre");
                                Object pointsValue = snapshot.get("puntos");

                                nombreUsuario.setText(value);
                                int pointsInt = ((Number) pointsValue).intValue();
                                points.setText(String.valueOf(pointsInt));

                                if (pointsInt <= 500) {
                                    level.setText("1");
                                    title.setText("Principiante");
                                } else if (pointsInt > 501 && pointsInt <= 1000) {
                                    level.setText("2");
                                    title.setText("Aprendiz");
                                } else if (pointsInt > 1001 && pointsInt <= 2000) {
                                    level.setText("3");
                                    title.setText("Intermedio");
                                } else if (pointsInt > 2001 && pointsInt <= 3500) {
                                    level.setText("4");
                                    title.setText("Avanzado");
                                } else if (pointsInt > 3501 && pointsInt <= 5000) {
                                    level.setText("5");
                                    title.setText("Experto");
                                } else if (pointsInt > 5001 && pointsInt <= 7000) {
                                    level.setText("6");
                                    title.setText("Maestro");
                                } else if (pointsInt > 7001 && pointsInt <= 9000) {
                                    level.setText("7");
                                    title.setText("Gran Maestro");
                                } else if (pointsInt > 9001 && pointsInt <= 12000) {
                                    level.setText("8");
                                    title.setText("Sabio");
                                } else if (pointsInt > 12001 && pointsInt <= 15000) {
                                    level.setText("9");
                                    title.setText("Gurú de las Matemáticas");
                                } else {
                                    level.setText("10");
                                    title.setText("Leyenda Matemática");
                                }

                                String profileImageUrl = snapshot.getString("profileImageUrl");
                                if (profileImageUrl != null) {
                                    Glide.with(PerfilActivity.this)
                                            .load(profileImageUrl)
                                            .fitCenter()
                                            .centerInside()
                                            .circleCrop() // Esta línea hace que la imagen sea redonda
                                            .into(profile);
                                }
                            } else {
                                Log.d("Firestore", "Current data: null");
                            }
                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLEY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mProgressDialog.setTitle("Subiendo...");
            mProgressDialog.setMessage("Subiendo foto de perfil");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            Uri uri = data.getData();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                StorageReference filePath = mStorage.child("fotos").child(userId);

                // Eliminar la imagen existente
                filePath.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Subir la nueva imagen
                        uploadNewImage(uri, filePath, userId);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar el error si no se pudo eliminar la imagen anterior
                        Log.e(TAG, "Error al eliminar la imagen anterior", e);
                        // Intentar subir la nueva imagen de todos modos
                        uploadNewImage(uri, filePath, userId);
                    }
                });
            } else {
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        } else {
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadNewImage(Uri uri, StorageReference filePath, String userId) {
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        // Guardar la URL en Firestore
                        db.collection("Usuario").document(userId)
                                .update("profileImageUrl", downloadUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Glide.with(PerfilActivity.this)
                                                .load(downloadUrl)
                                                .fitCenter()
                                                .centerCrop()
                                                .circleCrop() // Esta línea hace que la imagen sea redonda
                                                .into(profile);

                                        Toast.makeText(PerfilActivity.this, "Foto subida exitosamente", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PerfilActivity.this, "Error al guardar la URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Error al guardar la URL", e);
                                        mProgressDialog.dismiss();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PerfilActivity.this, "Error al obtener la URL de descarga: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al obtener la URL de descarga", e);
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PerfilActivity.this, "Error al subir la foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al subir la foto", e);
                mProgressDialog.dismiss();
            }
        });
    }




}