package com.example.novusproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    ImageView botonAtras;
    Button btn_registro;
    EditText nombre, apellidos, email, contrasena;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        botonAtras = findViewById(R.id.buttonBackQuestion);
        btn_registro = findViewById(R.id.buttonIniciar);
        nombre = findViewById(R.id.nombreRegistro);
        apellidos = findViewById(R.id.apellidosRegistro);
        email = findViewById(R.id.emailSesion);
        contrasena = findViewById(R.id.passwordSesion);

        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUser = nombre.getText().toString().trim();
                String usernameUser = apellidos.getText().toString().trim();
                String emailUser = email.getText().toString().trim();
                String passUser = contrasena.getText().toString().trim();

                if (nameUser.isEmpty() && usernameUser.isEmpty() && emailUser.isEmpty() && passUser.isEmpty()){
                    Toast.makeText(RegistroActivity.this, "Porfavor, llene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(nameUser, usernameUser, emailUser, passUser);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser(String nameUser, String usernameUser, String emailUser, String passUser) {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();

                    // Crear el mapa para la colección "Usuario"
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", id);
                    userMap.put("nombre", nameUser);
                    userMap.put("apellidos", usernameUser);
                    userMap.put("correo", emailUser);
                    userMap.put("password", passUser);
                    userMap.put("puntos", 0);
                    userMap.put("monedas", 100);
                    userMap.put("c1", 3);
                    userMap.put("c2", 3);
                    userMap.put("c3", 3);
                    userMap.put("opc1", 0);
                    userMap.put("opc2", 0);

                    // Guardar el documento en la colección "Usuario"
                    mFirestore.collection("Usuario").document(id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Llamar a la función para crear el documento en "UsuarioAvatar"
                            createUserAvatar(id);
                            createUsuarioPrimera(id);
                            createUsuarioSegunda(id);
                            createUsuarioTercera(id);
                            createUsuarioCuarta(id);
                            createUsuarioQuinta(id);
                            createUsuarioSexta(id);
                            createUsuarioSeptima(id);
                            createUsuarioOctava(id);
                            createUsuarioNovena(id);
                            createUsuarioDecima(id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistroActivity.this, "Error al guardar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(RegistroActivity.this, "Error al registrar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioAvatar"
    private void createUserAvatar(String userId) {
        // Crear el mapa para la colección "UsuarioAvatar"
        Map<String, Object> avatarMap = new HashMap<>();
        avatarMap.put("actualFace", "");
        avatarMap.put("actualFeet", "");
        avatarMap.put("actualHead", "");
        avatarMap.put("actualNeck", "");

        avatarMap.put("face1", false);
        avatarMap.put("face2", false);
        avatarMap.put("face3", false);
        avatarMap.put("face4", false);

        avatarMap.put("feet1", false);
        avatarMap.put("feet2", false);
        avatarMap.put("feet3", false);
        avatarMap.put("feet4", false);

        avatarMap.put("head1", false);
        avatarMap.put("head2", false);
        avatarMap.put("head3", false);

        avatarMap.put("neck1", false);
        avatarMap.put("neck2", false);
        avatarMap.put("neck3", false);
        avatarMap.put("neck4", false);

        // Guardar el documento en la colección "UsuarioAvatar"
        mFirestore.collection("UsuarioAvatar").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar avatar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioPrimera"
    private void createUsuarioPrimera(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 1 nivel 1
        avatarMap.put("R1I1L1", false);
        avatarMap.put("R2I1L1", false);
        avatarMap.put("R3I1L1", false);
        avatarMap.put("R4I1L1", false);
        avatarMap.put("R5I1L1", false);
        avatarMap.put("R6I1L1", false);
        avatarMap.put("R7I1L1", false);
        avatarMap.put("R8I1L1", false);
        avatarMap.put("R9I1L1", false);
        avatarMap.put("R10I1L1", false);

        // Isla 1 nivel 2
        avatarMap.put("R1I1L2", false);
        avatarMap.put("R2I1L2", false);
        avatarMap.put("R3I1L2", false);
        avatarMap.put("R4I1L2", false);
        avatarMap.put("R5I1L2", false);
        avatarMap.put("R6I1L2", false);
        avatarMap.put("R7I1L2", false);
        avatarMap.put("R8I1L2", false);
        avatarMap.put("R9I1L2", false);
        avatarMap.put("R10I1L2", false);

        // Guardar el documento en la colección "UsuarioAvatar"
        mFirestore.collection("UsuarioPrimera").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar primera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioSegunda"
    private void createUsuarioSegunda(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 2 nivel 1
        avatarMap.put("R1I2L1", false);
        avatarMap.put("R2I2L1", false);
        avatarMap.put("R3I2L1", false);
        avatarMap.put("R4I2L1", false);
        avatarMap.put("R5I2L1", false);
        avatarMap.put("R6I2L1", false);
        avatarMap.put("R7I2L1", false);
        avatarMap.put("R8I2L1", false);
        avatarMap.put("R9I2L1", false);
        avatarMap.put("R10I2L1", false);

        // Isla 2 nivel 2
        avatarMap.put("R1I2L2", false);
        avatarMap.put("R2I2L2", false);
        avatarMap.put("R3I2L2", false);
        avatarMap.put("R4I2L2", false);
        avatarMap.put("R5I2L2", false);
        avatarMap.put("R6I2L2", false);
        avatarMap.put("R7I2L2", false);
        avatarMap.put("R8I2L2", false);
        avatarMap.put("R9I2L2", false);
        avatarMap.put("R10I2L2", false);

        // Isla 2 nivel 3
        avatarMap.put("R1I2L3", false);
        avatarMap.put("R2I2L3", false);
        avatarMap.put("R3I2L3", false);
        avatarMap.put("R4I2L3", false);
        avatarMap.put("R5I2L3", false);
        avatarMap.put("R6I2L3", false);
        avatarMap.put("R7I2L3", false);
        avatarMap.put("R8I2L3", false);
        avatarMap.put("R9I2L3", false);
        avatarMap.put("R10I2L3", false);

        // Guardar el documento en la colección "UsuarioAvatar"
        mFirestore.collection("UsuarioSegunda").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar segunda: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioTercera"
    private void createUsuarioTercera(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 3 nivel 1
        avatarMap.put("R1I3L1", false);
        avatarMap.put("R2I3L1", false);
        avatarMap.put("R3I3L1", false);
        avatarMap.put("R4I3L1", false);
        avatarMap.put("R5I3L1", false);
        avatarMap.put("R6I3L1", false);
        avatarMap.put("R7I3L1", false);
        avatarMap.put("R8I3L1", false);
        avatarMap.put("R9I3L1", false);
        avatarMap.put("R10I3L1", false);

        // Isla 3 nivel 2
        avatarMap.put("R1I3L2", false);
        avatarMap.put("R2I3L2", false);
        avatarMap.put("R3I3L2", false);
        avatarMap.put("R4I3L2", false);
        avatarMap.put("R5I3L2", false);
        avatarMap.put("R6I3L2", false);
        avatarMap.put("R7I3L2", false);
        avatarMap.put("R8I3L2", false);
        avatarMap.put("R9I3L2", false);
        avatarMap.put("R10I3L2", false);

        // Isla 3 nivel 3
        avatarMap.put("R1I3L3", false);
        avatarMap.put("R2I3L3", false);
        avatarMap.put("R3I3L3", false);
        avatarMap.put("R4I3L3", false);
        avatarMap.put("R5I3L3", false);
        avatarMap.put("R6I3L3", false);
        avatarMap.put("R7I3L3", false);
        avatarMap.put("R8I3L3", false);
        avatarMap.put("R9I3L3", false);
        avatarMap.put("R10I3L3", false);

        // Guardar el documento en la colección "UsuarioAvatar"
        mFirestore.collection("UsuarioTercera").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar tercera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioCuarta"
    private void createUsuarioCuarta(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 4 nivel 1
        avatarMap.put("R1I4L1", false);
        avatarMap.put("R2I4L1", false);
        avatarMap.put("R3I4L1", false);
        avatarMap.put("R4I4L1", false);
        avatarMap.put("R5I4L1", false);
        avatarMap.put("R6I4L1", false);
        avatarMap.put("R7I4L1", false);
        avatarMap.put("R8I4L1", false);
        avatarMap.put("R9I4L1", false);
        avatarMap.put("R10I4L1", false);

        // Isla 4 nivel 2
        avatarMap.put("R1I4L2", false);
        avatarMap.put("R2I4L2", false);
        avatarMap.put("R3I4L2", false);
        avatarMap.put("R4I4L2", false);
        avatarMap.put("R5I4L2", false);
        avatarMap.put("R6I4L2", false);
        avatarMap.put("R7I4L2", false);
        avatarMap.put("R8I4L2", false);
        avatarMap.put("R9I4L2", false);
        avatarMap.put("R10I4L2", false);

        // Isla 4 nivel 3
        avatarMap.put("R1I4L3", false);
        avatarMap.put("R2I4L3", false);
        avatarMap.put("R3I4L3", false);
        avatarMap.put("R4I4L3", false);
        avatarMap.put("R5I4L3", false);
        avatarMap.put("R6I4L3", false);
        avatarMap.put("R7I4L3", false);
        avatarMap.put("R8I4L3", false);
        avatarMap.put("R9I4L3", false);
        avatarMap.put("R10I4L3", false);

        // Guardar el documento en la colección "UsuarioAvatar"
        mFirestore.collection("UsuarioCuarta").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar cuarta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioQuinta"
    private void createUsuarioQuinta(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 5 nivel 1
        avatarMap.put("R1I5L1", false);
        avatarMap.put("R2I5L1", false);
        avatarMap.put("R3I5L1", false);
        avatarMap.put("R4I5L1", false);
        avatarMap.put("R5I5L1", false);
        avatarMap.put("R6I5L1", false);
        avatarMap.put("R7I5L1", false);
        avatarMap.put("R8I5L1", false);
        avatarMap.put("R9I5L1", false);
        avatarMap.put("R10I5L1", false);

        // Isla 5 nivel 2
        avatarMap.put("R1I5L2", false);
        avatarMap.put("R2I5L2", false);
        avatarMap.put("R3I5L2", false);
        avatarMap.put("R4I5L2", false);
        avatarMap.put("R5I5L2", false);
        avatarMap.put("R6I5L2", false);
        avatarMap.put("R7I5L2", false);
        avatarMap.put("R8I5L2", false);
        avatarMap.put("R9I5L2", false);
        avatarMap.put("R10I5L2", false);

        // Isla 5 nivel 3
        avatarMap.put("R1I5L3", false);
        avatarMap.put("R2I5L3", false);
        avatarMap.put("R3I5L3", false);
        avatarMap.put("R4I5L3", false);
        avatarMap.put("R5I5L3", false);
        avatarMap.put("R6I5L3", false);
        avatarMap.put("R7I5L3", false);
        avatarMap.put("R8I5L3", false);
        avatarMap.put("R9I5L3", false);
        avatarMap.put("R10I5L3", false);

        // Guardar el documento en la colección "UsuarioQuinta"
        mFirestore.collection("UsuarioQuinta").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar quinta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioSexta"
    private void createUsuarioSexta(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 6 nivel 1
        avatarMap.put("R1I6L1", false);
        avatarMap.put("R2I6L1", false);
        avatarMap.put("R3I6L1", false);
        avatarMap.put("R4I6L1", false);
        avatarMap.put("R5I6L1", false);
        avatarMap.put("R6I6L1", false);
        avatarMap.put("R7I6L1", false);
        avatarMap.put("R8I6L1", false);
        avatarMap.put("R9I6L1", false);
        avatarMap.put("R10I6L1", false);

        // Isla 6 nivel 2
        avatarMap.put("R1I6L2", false);
        avatarMap.put("R2I6L2", false);
        avatarMap.put("R3I6L2", false);
        avatarMap.put("R4I6L2", false);
        avatarMap.put("R5I6L2", false);
        avatarMap.put("R6I6L2", false);
        avatarMap.put("R7I6L2", false);
        avatarMap.put("R8I6L2", false);
        avatarMap.put("R9I6L2", false);
        avatarMap.put("R10I6L2", false);

        // Isla 6 nivel 3
        avatarMap.put("R1I6L3", false);
        avatarMap.put("R2I6L3", false);
        avatarMap.put("R3I6L3", false);
        avatarMap.put("R4I6L3", false);
        avatarMap.put("R5I6L3", false);
        avatarMap.put("R6I6L3", false);
        avatarMap.put("R7I6L3", false);
        avatarMap.put("R8I6L3", false);
        avatarMap.put("R9I6L3", false);
        avatarMap.put("R10I6L3", false);

        // Guardar el documento en la colección "UsuarioSexta"
        mFirestore.collection("UsuarioSexta").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar sexta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioSeptima"
    private void createUsuarioSeptima(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 7 nivel 1
        avatarMap.put("R1I7L1", false);
        avatarMap.put("R2I7L1", false);
        avatarMap.put("R3I7L1", false);
        avatarMap.put("R4I7L1", false);
        avatarMap.put("R5I7L1", false);
        avatarMap.put("R6I7L1", false);
        avatarMap.put("R7I7L1", false);
        avatarMap.put("R8I7L1", false);
        avatarMap.put("R9I7L1", false);
        avatarMap.put("R10I7L1", false);

        // Isla 7 nivel 2
        avatarMap.put("R1I7L2", false);
        avatarMap.put("R2I7L2", false);
        avatarMap.put("R3I7L2", false);
        avatarMap.put("R4I7L2", false);
        avatarMap.put("R5I7L2", false);
        avatarMap.put("R6I7L2", false);
        avatarMap.put("R7I7L2", false);
        avatarMap.put("R8I7L2", false);
        avatarMap.put("R9I7L2", false);
        avatarMap.put("R10I7L2", false);

        // Isla 7 nivel 3
        avatarMap.put("R1I7L3", false);
        avatarMap.put("R2I7L3", false);
        avatarMap.put("R3I7L3", false);
        avatarMap.put("R4I7L3", false);
        avatarMap.put("R5I7L3", false);
        avatarMap.put("R6I7L3", false);
        avatarMap.put("R7I7L3", false);
        avatarMap.put("R8I7L3", false);
        avatarMap.put("R9I7L3", false);
        avatarMap.put("R10I7L3", false);

        // Guardar el documento en la colección "UsuarioSeptima"
        mFirestore.collection("UsuarioSeptima").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar septima: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioOctava"
    private void createUsuarioOctava(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 8 nivel 1
        avatarMap.put("R1I81L1", false);
        avatarMap.put("R2I81L1", false);
        avatarMap.put("R3I81L1", false);
        avatarMap.put("R4I81L1", false);
        avatarMap.put("R5I81L1", false);
        avatarMap.put("R6I81L1", false);
        avatarMap.put("R7I81L1", false);
        avatarMap.put("R8I81L1", false);
        avatarMap.put("R9I81L1", false);
        avatarMap.put("R10I81L1", false);

        // Isla 8 nivel 2
        avatarMap.put("R1I81L2", false);
        avatarMap.put("R2I81L2", false);
        avatarMap.put("R3I81L2", false);
        avatarMap.put("R4I81L2", false);
        avatarMap.put("R5I81L2", false);
        avatarMap.put("R6I81L2", false);
        avatarMap.put("R7I81L2", false);
        avatarMap.put("R8I81L2", false);
        avatarMap.put("R9I81L2", false);
        avatarMap.put("R10I81L2", false);

        // Isla 8 nivel 3
        avatarMap.put("R1I81L3", false);
        avatarMap.put("R2I81L3", false);
        avatarMap.put("R3I81L3", false);
        avatarMap.put("R4I81L3", false);
        avatarMap.put("R5I81L3", false);
        avatarMap.put("R6I81L3", false);
        avatarMap.put("R7I81L3", false);
        avatarMap.put("R8I81L3", false);
        avatarMap.put("R9I81L3", false);
        avatarMap.put("R10I81L3", false);

        // Guardar el documento en la colección "UsuarioOctava"
        mFirestore.collection("UsuarioOctava").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar octava: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUsuarioOctava2(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 82 nivel 1
        avatarMap.put("R1I82L1", false);
        avatarMap.put("R2I82L1", false);
        avatarMap.put("R3I82L1", false);
        avatarMap.put("R4I82L1", false);
        avatarMap.put("R5I82L1", false);
        avatarMap.put("R6I82L1", false);
        avatarMap.put("R7I82L1", false);
        avatarMap.put("R8I82L1", false);
        avatarMap.put("R9I82L1", false);
        avatarMap.put("R10I82L1", false);

        // Isla 82 nivel 2
        avatarMap.put("R1I82L2", false);
        avatarMap.put("R2I82L2", false);
        avatarMap.put("R3I82L2", false);
        avatarMap.put("R4I82L2", false);
        avatarMap.put("R5I82L2", false);
        avatarMap.put("R6I82L2", false);
        avatarMap.put("R7I82L2", false);
        avatarMap.put("R8I82L2", false);
        avatarMap.put("R9I82L2", false);
        avatarMap.put("R10I82L2", false);

        // Isla 82 nivel 3
        avatarMap.put("R1I82L3", false);
        avatarMap.put("R2I82L3", false);
        avatarMap.put("R3I82L3", false);
        avatarMap.put("R4I82L3", false);
        avatarMap.put("R5I82L3", false);
        avatarMap.put("R6I82L3", false);
        avatarMap.put("R7I82L3", false);
        avatarMap.put("R8I82L3", false);
        avatarMap.put("R9I82L3", false);
        avatarMap.put("R10I82L3", false);

        // Guardar el documento en la colección "UsuarioOctava"
        mFirestore.collection("UsuarioOctava").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar octava: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUsuarioOctava3(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 83 nivel 1
        avatarMap.put("R1I83L1", false);
        avatarMap.put("R2I83L1", false);
        avatarMap.put("R3I83L1", false);
        avatarMap.put("R4I83L1", false);
        avatarMap.put("R5I83L1", false);
        avatarMap.put("R6I83L1", false);
        avatarMap.put("R7I83L1", false);
        avatarMap.put("R8I83L1", false);
        avatarMap.put("R9I83L1", false);
        avatarMap.put("R10I83L1", false);

        // Isla 83 nivel 2
        avatarMap.put("R1I83L2", false);
        avatarMap.put("R2I83L2", false);
        avatarMap.put("R3I83L2", false);
        avatarMap.put("R4I83L2", false);
        avatarMap.put("R5I83L2", false);
        avatarMap.put("R6I83L2", false);
        avatarMap.put("R7I83L2", false);
        avatarMap.put("R8I83L2", false);
        avatarMap.put("R9I83L2", false);
        avatarMap.put("R10I83L2", false);

        // Isla 83 nivel 3
        avatarMap.put("R1I83L3", false);
        avatarMap.put("R2I83L3", false);
        avatarMap.put("R3I83L3", false);
        avatarMap.put("R4I83L3", false);
        avatarMap.put("R5I83L3", false);
        avatarMap.put("R6I83L3", false);
        avatarMap.put("R7I83L3", false);
        avatarMap.put("R8I83L3", false);
        avatarMap.put("R9I83L3", false);
        avatarMap.put("R10I83L3", false);

        // Guardar el documento en la colección "UsuarioOctava"
        mFirestore.collection("UsuarioOctava").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar octava: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioNovena"
    private void createUsuarioNovena(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 9 nivel 1
        avatarMap.put("R1I91L1", false);
        avatarMap.put("R2I91L1", false);
        avatarMap.put("R3I91L1", false);
        avatarMap.put("R4I91L1", false);
        avatarMap.put("R5I91L1", false);
        avatarMap.put("R6I91L1", false);
        avatarMap.put("R7I91L1", false);
        avatarMap.put("R8I91L1", false);
        avatarMap.put("R9I91L1", false);
        avatarMap.put("R10I91L1", false);

        // Isla 9 nivel 2
        avatarMap.put("R1I91L2", false);
        avatarMap.put("R2I91L2", false);
        avatarMap.put("R3I91L2", false);
        avatarMap.put("R4I91L2", false);
        avatarMap.put("R5I91L2", false);
        avatarMap.put("R6I91L2", false);
        avatarMap.put("R7I91L2", false);
        avatarMap.put("R8I91L2", false);
        avatarMap.put("R9I91L2", false);
        avatarMap.put("R10I91L2", false);

        // Isla 9 nivel 3
        avatarMap.put("R1I91L3", false);
        avatarMap.put("R2I91L3", false);
        avatarMap.put("R3I91L3", false);
        avatarMap.put("R4I91L3", false);
        avatarMap.put("R5I91L3", false);
        avatarMap.put("R6I91L3", false);
        avatarMap.put("R7I91L3", false);
        avatarMap.put("R8I91L3", false);
        avatarMap.put("R9I91L3", false);
        avatarMap.put("R10I91L3", false);

        // Guardar el documento en la colección "UsuarioNovena"
        mFirestore.collection("UsuarioNovena").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar novena: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUsuarioNovena2(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 92 nivel 1
        avatarMap.put("R1I92L1", false);
        avatarMap.put("R2I92L1", false);
        avatarMap.put("R3I92L1", false);
        avatarMap.put("R4I92L1", false);
        avatarMap.put("R5I92L1", false);
        avatarMap.put("R6I92L1", false);
        avatarMap.put("R7I92L1", false);
        avatarMap.put("R8I92L1", false);
        avatarMap.put("R9I92L1", false);
        avatarMap.put("R10I92L1", false);

        // Isla 92 nivel 2
        avatarMap.put("R1I92L2", false);
        avatarMap.put("R2I92L2", false);
        avatarMap.put("R3I92L2", false);
        avatarMap.put("R4I92L2", false);
        avatarMap.put("R5I92L2", false);
        avatarMap.put("R6I92L2", false);
        avatarMap.put("R7I92L2", false);
        avatarMap.put("R8I92L2", false);
        avatarMap.put("R9I92L2", false);
        avatarMap.put("R10I92L2", false);

        // Isla 92 nivel 3
        avatarMap.put("R1I92L3", false);
        avatarMap.put("R2I92L3", false);
        avatarMap.put("R3I92L3", false);
        avatarMap.put("R4I92L3", false);
        avatarMap.put("R5I92L3", false);
        avatarMap.put("R6I92L3", false);
        avatarMap.put("R7I92L3", false);
        avatarMap.put("R8I92L3", false);
        avatarMap.put("R9I92L3", false);
        avatarMap.put("R10I92L3", false);

        // Guardar el documento en la colección "UsuarioNovena"
        mFirestore.collection("UsuarioNovena").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar novena: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear documento en "UsuarioDecima"
    private void createUsuarioDecima(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 10 nivel 1
        avatarMap.put("R1I101L1", false);
        avatarMap.put("R2I101L1", false);
        avatarMap.put("R3I101L1", false);
        avatarMap.put("R4I101L1", false);
        avatarMap.put("R5I101L1", false);
        avatarMap.put("R6I101L1", false);
        avatarMap.put("R7I101L1", false);
        avatarMap.put("R8I101L1", false);
        avatarMap.put("R9I101L1", false);
        avatarMap.put("R10I101L1", false);

        // Isla 10 nivel 2
        avatarMap.put("R1I101L2", false);
        avatarMap.put("R2I101L2", false);
        avatarMap.put("R3I101L2", false);
        avatarMap.put("R4I101L2", false);
        avatarMap.put("R5I101L2", false);
        avatarMap.put("R6I101L2", false);
        avatarMap.put("R7I101L2", false);
        avatarMap.put("R8I101L2", false);
        avatarMap.put("R9I101L2", false);
        avatarMap.put("R10I101L2", false);

        // Isla 10 nivel 3
        avatarMap.put("R1I101L3", false);
        avatarMap.put("R2I101L3", false);
        avatarMap.put("R3I101L3", false);
        avatarMap.put("R4I101L3", false);
        avatarMap.put("R5I101L3", false);
        avatarMap.put("R6I101L3", false);
        avatarMap.put("R7I101L3", false);
        avatarMap.put("R8I101L3", false);
        avatarMap.put("R9I101L3", false);
        avatarMap.put("R10I101L3", false);

        // Guardar el documento en la colección "UsuarioDecima"
        mFirestore.collection("UsuarioDecima").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar decima: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUsuarioDecima2(String userId) {

        Map<String, Object> avatarMap = new HashMap<>();
        // Isla 102 nivel 1
        avatarMap.put("R1I102L1", false);
        avatarMap.put("R2I102L1", false);
        avatarMap.put("R3I102L1", false);
        avatarMap.put("R4I102L1", false);
        avatarMap.put("R5I102L1", false);
        avatarMap.put("R6I102L1", false);
        avatarMap.put("R7I102L1", false);
        avatarMap.put("R8I102L1", false);
        avatarMap.put("R9I102L1", false);
        avatarMap.put("R10I102L1", false);

        // Isla 102 nivel 2
        avatarMap.put("R1I102L2", false);
        avatarMap.put("R2I102L2", false);
        avatarMap.put("R3I102L2", false);
        avatarMap.put("R4I102L2", false);
        avatarMap.put("R5I102L2", false);
        avatarMap.put("R6I102L2", false);
        avatarMap.put("R7I102L2", false);
        avatarMap.put("R8I102L2", false);
        avatarMap.put("R9I102L2", false);
        avatarMap.put("R10I102L2", false);

        // Isla 102 nivel 3
        avatarMap.put("R1I102L3", false);
        avatarMap.put("R2I102L3", false);
        avatarMap.put("R3I102L3", false);
        avatarMap.put("R4I102L3", false);
        avatarMap.put("R5I102L3", false);
        avatarMap.put("R6I102L3", false);
        avatarMap.put("R7I102L3", false);
        avatarMap.put("R8I102L3", false);
        avatarMap.put("R9I102L3", false);
        avatarMap.put("R10I102L3", false);

        // Guardar el documento en la colección "UsuarioDecima"
        mFirestore.collection("UsuarioDecima").document(userId).set(avatarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                //Toast.makeText(RegistroActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "Error al guardar decima: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}