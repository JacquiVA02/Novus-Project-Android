package com.example.novusproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;


public class PreguntaActivity extends AppCompatActivity {

    ImageView btn_back, question, response1, response2, response3, response4, coffee, eraser, candy;
    private List<ImageView> responseViews;
    String correctText;
    TextView coins;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    // Declarar las variables imgCorrect e imgIncorrect como variables de clase
    private String imgCorrect;
    private String imgIncorrect;
    // Declarar las variables para los valores c1, c2 y c3 como campos de clase
    private Double c1 = 0.0;
    private Double c2 = 0.0;
    private Double c3 = 0.0;

    Double puntosPregunta, monedasPregunta;
    String param1, param2, param3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregunta);

        // Asignación de vistas
        btn_back = findViewById(R.id.buttonBackQuestion);
        question = findViewById(R.id.imageQuestion);
        response1 = findViewById(R.id.imageResponse1);
        response2 = findViewById(R.id.imageResponse2);
        response3 = findViewById(R.id.imageResponse3);
        response4 = findViewById(R.id.imageResponse4);

        // Inicialización de responseViews y añadir las respuestas
        responseViews = new ArrayList<>();
        responseViews.add(response1);
        responseViews.add(response2);
        responseViews.add(response3);
        responseViews.add(response4);

        coins = findViewById(R.id.CoinQuestion);
        coffee = findViewById(R.id.CoffeeComodin);
        eraser = findViewById(R.id.EraserComodin);
        candy = findViewById(R.id.CandyComodin);



        // Inicialización de Firebase
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        // Verificar el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        // Dentro del método onCreate o donde sea necesario obtener los valores de c1, c2 y c3
        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener los datos del documento Usuario
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Obtener los valores de c1, c2 y c3
                        c1 = document.getDouble("c1");
                        c2 = document.getDouble("c2");
                        c3 = document.getDouble("c3");

                        // Verificar si alguno de los valores es null y manejarlo si es necesario
                        if (c1 == null) {
                            c1 = 0.0;  // Valor por defecto o manejo apropiado
                        }
                        if (c2 == null) {
                            c2 = 0.0;  // Valor por defecto o manejo apropiado
                        }
                        if (c3 == null) {
                            c3 = 0.0;  // Valor por defecto o manejo apropiado
                        }

                        // Aquí puedes utilizar c1, c2, c3 según tus necesidades
                        Log.d(TAG, "c1: " + c1);
                        Log.d(TAG, "c2: " + c2);
                        Log.d(TAG, "c3: " + c3);

                        // Puedes almacenar estos valores en variables globales si necesitas usarlos fuera de este bloque
                    } else {
                        Log.d(TAG, "El documento Usuario no existe");
                    }
                } else {
                    Log.d(TAG, "Error al obtener el documento Usuario", task.getException());
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(PreguntaActivity.this, SesionActivity.class));
            finish();
            return;
        }


        // Obtener los parámetros
        param1 = getIntent().getStringExtra("isla");
        param2 = getIntent().getStringExtra("pregunta");
        param3 = getIntent().getStringExtra("estado");

        if (param1 != null && param2 != null && param3 != null ){
            Log.d("isla", param1);
            Log.d("pregunta", param2);
            Log.d("estado", param3);
        } else {
            Log.d("PreguntaActivity", "No se recibieron los parámetros esperados.");
        }

        // Aplicar márgenes de borde a la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db.collection(param1).document(param2)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (value != null && value.exists()) {
                        puntosPregunta = value.getDouble("puntos");
                        monedasPregunta = value.getDouble("monedas");
                        Log.d("puntos pregunta de inicio", String.valueOf(puntosPregunta));

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                    });

        // Configurar el botón de retroceso
        btn_back.setOnClickListener(v -> finish());

        // USAR COMODIN DE CAFE
        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1 != 0){
                    removeRandomIncorrectResponses();
                    updateRemoveOpC1();
                }else {
                    // Mostrar un AlertDialog indicando que el usuario no tiene el comodín disponible
                    AlertDialog.Builder builder = new AlertDialog.Builder(PreguntaActivity.this);
                    builder.setMessage("No tienes disponible el comodín.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); // Cierra el diálogo
                                }
                            })
                            .show();
                }
            }
        });

        // Obtener la pregunta y los elementos solo una vez al crear la actividad
        getData();
        getQuestion(param1, param2);
        getElements();
    }


    private void getQuestion(String param1, String param2) {
        db.collection(param1).document(param2)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (value != null && value.exists()) {
                        Log.d(TAG, "Current data: " + value.getData());
                        String questionText = value.getString("pregunta");
                        correctText = value.getString("correcta");
                        String incorrect1Text = value.getString("incorrecta1");
                        String incorrect2Text = value.getString("incorrecta2");
                        String incorrect3Text = value.getString("incorrecta3");
                        String video = value.getString("link");
                        Double puntosDouble = value.getDouble("puntos");
                        Log.d("puntos pregunta", String.valueOf(puntosDouble));

                        List<String> responses = new ArrayList<>();
                        responses.add(correctText);
                        responses.add(incorrect1Text);
                        responses.add(incorrect2Text);
                        responses.add(incorrect3Text);

                        Collections.shuffle(responses); // Mezclar la lista de respuestas

                        Glide.with(PreguntaActivity.this)
                                .load(questionText)
                                .fitCenter()
                                .centerInside()
                                .into(question);


                        loadImageIntoView(responses.get(0), response1, responses.get(0));
                        loadImageIntoView(responses.get(1), response2, responses.get(1));
                        loadImageIntoView(responses.get(2), response3, responses.get(2));
                        loadImageIntoView(responses.get(3), response4, responses.get(3));


                        response1.setOnClickListener(view -> checkAnswer(view, correctText, video));
                        response2.setOnClickListener(view -> checkAnswer(view, correctText, video));
                        response3.setOnClickListener(view -> checkAnswer(view, correctText, video));
                        response4.setOnClickListener(view -> checkAnswer(view, correctText, video));

                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                });
    }

    // Método para eliminar dos respuestas incorrectas aleatorias
    private void removeRandomIncorrectResponses() {
        List<ImageView> incorrectResponses = new ArrayList<>();

        // Identificar y almacenar las respuestas incorrectas
        for (ImageView view : responseViews) {
            String tag = (String) view.getTag();
            if (tag != null && !tag.equals(correctText)) {
                incorrectResponses.add(view);
            }
        }

        // Verificar si hay al menos dos respuestas incorrectas para eliminar
        if (incorrectResponses.size() >= 2) {
            // Mezclar la lista de respuestas incorrectas para seleccionar dos aleatoriamente
            Collections.shuffle(incorrectResponses);

            // Eliminar las dos primeras respuestas incorrectas aleatorias
            ImageView responseToRemove1 = incorrectResponses.get(0);
            ImageView responseToRemove2 = incorrectResponses.get(1);

            // Ocultar las respuestas eliminadas (puedes cambiar la visibilidad según tus necesidades)
            responseToRemove1.setVisibility(View.GONE);
            responseToRemove2.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "No hay suficientes respuestas incorrectas para eliminar.");
        }
    }

    private void getElements(){
        db.collection("Elementos").document("elementos")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if(value != null && value.exists()) {
                        Log.d(TAG, "Current data: " + value.getData());
                        // Asignar los valores a las variables de clase
                        imgCorrect = value.getString("correct");
                        imgIncorrect = value.getString("incorrect");
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                });
    }

    private void checkAnswer(View view, String correctText, String video) {
        String selectedAnswer = (String) view.getTag();
        ImageView imageView = (ImageView) view;

        if (selectedAnswer != null && selectedAnswer.equals(correctText)) {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_correct));
            Glide.with(getApplicationContext())
                    .load(imgCorrect)
                    .fitCenter()
                    .centerInside()
                    .into(imageView);
            updateQuestionState();
            updatePoints();
            updateOpC1();

            // Finalizar la actividad actual
            finish();

        } else {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_incorrect));
            Glide.with(getApplicationContext())
                    .load(imgIncorrect)
                    .fitCenter()
                    .centerInside()
                    .into(imageView);
            updateWrongOpC1();
            showIncorrectAnswerDialog(correctText, video);
        }

        // Desactivar los otros botones
        response1.setOnClickListener(null);
        response2.setOnClickListener(null);
        response3.setOnClickListener(null);
        response4.setOnClickListener(null);
    }



    private void showIncorrectAnswerDialog(String correctText, String video) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Respuesta incorrecta ¿Necesitas ayuda?")
                .setPositiveButton("Ver video", (dialog, which) -> {
                    // Crear un Intent para abrir el enlace de YouTube
                    Uri youtubeUri = Uri.parse(video);
                    Intent intent = new Intent(Intent.ACTION_VIEW, youtubeUri);

                    // Comprobar si hay alguna aplicación que pueda manejar este Intent
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Si hay una aplicación disponible, iniciarla
                        startActivity(intent);
                    } else {
                        // Si no hay una aplicación disponible, abrir el enlace en un navegador web
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video));
                        startActivity(webIntent);
                        resetResponseButtons(correctText, video);
                    }
                })
                .setNegativeButton("Reintentar", (dialog, which) -> {
                    resetResponseButtons(correctText, video);
                })

                .show();
    }

    private void resetResponseButtons(String correctText, String video) {
        // Restaurar el fondo original de los botones de respuesta
        response1.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_answer));
        response2.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_answer));
        response3.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_answer));
        response4.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_answer));

        // Cargar la imagen original en cada ImageView correspondiente
        loadImageIntoView(response1.getTag().toString(), response1, response1.getTag().toString());
        loadImageIntoView(response2.getTag().toString(), response2, response2.getTag().toString());
        loadImageIntoView(response3.getTag().toString(), response3, response3.getTag().toString());
        loadImageIntoView(response4.getTag().toString(), response4, response4.getTag().toString());

        response1.setOnClickListener(view -> checkAnswer(view, correctText, video));
        response2.setOnClickListener(view -> checkAnswer(view, correctText, video));
        response3.setOnClickListener(view -> checkAnswer(view, correctText, video));
        response4.setOnClickListener(view -> checkAnswer(view, correctText, video));
    }

    private void updateQuestionState(){
        // Se obtiene al usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            String userId = user.getUid();

            // Referenciar al documento de UsuarioIsla
            DocumentReference userDocRef = db.collection(param3).document(userId);

            // Actualizar a true la pregunta
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Actualizar el campo de la pregunta
                        userDocRef.update(param2, true)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Valor actualizado correctamente"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar valor", e));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
    }

    private void updateOpC1() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener los valores actuales de opc1 y c1, y actualizar
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double currentC1 = document.getDouble("c1");
                        if (currentC1 == null) {
                            currentC1 = 0.0;
                        }

                        Double currentOpC1 = document.getDouble("opc1");
                        if (currentOpC1 == null) {
                            currentOpC1 = 0.0;
                        }

                        // Verificar si opc1 alcanzó el valor de 3
                        if (currentOpC1 >= 3) {
                            // Incrementar c1 en 1 y reiniciar opc1 a 0
                            Double newC1 = currentC1 + 1;
                            userDocRef.update("c1", newC1, "opc1", 0.0)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "c1 y opc1 actualizados correctamente"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar c1 y opc1", e));
                        } else {
                            // Incrementar opc1 en 1
                            Double newOpC1 = currentOpC1 + 1;
                            userDocRef.update("opc1", newOpC1)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "opc1 actualizado correctamente"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar opc1", e));
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
    }

    private void updateRemoveOpC1() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener el c1 actual y actualizar valor
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double currentC1 = document.getDouble("c1");
                        if (currentC1 == null) {
                            currentC1 = 0.0;
                        }

                        Double newC1 = currentC1 - 1;

                        // Actualizar el campo 'c1'
                        userDocRef.update("c1", newC1)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "c1 actualizado correctamente"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar c1", e));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
    }

    private void updateWrongOpC1() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener los valores actuales de opc1 y actualizar a 0
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double currentC1 = document.getDouble("c1");
                        if (currentC1 == null) {
                            currentC1 = 0.0;
                        }

                        Double currentOpC1 = document.getDouble("opc1");
                        if (currentOpC1 == null) {
                            currentOpC1 = 0.0;
                        }

                        // Verificar si opc1 alcanzó el valor de 3
                        if (currentOpC1 >= 3) {
                            // Incrementar c1 en 1 y reiniciar opc1 a 0
                            Double newC1 = currentC1 + 1;
                            userDocRef.update("c1", newC1, "opc1", 0.0)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "c1 y opc1 actualizados correctamente"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar c1 y opc1", e));
                        } else {
                            // Incrementar opc1 en 1
                            Double newOpC1 = 0.0;
                            userDocRef.update("opc1", newOpC1)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "opc1 actualizado correctamente"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar opc1", e));
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
        }
    }


    private void updatePoints() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener los puntos actuales y actualizar el valor
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double currentPoints = document.getDouble("puntos");
                        Double currentCoins = document.getDouble("monedas");
                        if (currentPoints == null && currentCoins == null) {
                            currentPoints = 0.0;
                            currentCoins = 0.0;
                        }

                        Double newPoints = currentPoints + puntosPregunta;
                        Double newCoins = currentCoins + monedasPregunta;

                        // Actualizar el campo 'puntos'
                        userDocRef.update("puntos", newPoints, "monedas", newCoins)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Puntos actualizados correctamente"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar puntos", e));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
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
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("Firestore", "Listen failed", e);
                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {
                                // Obtiene el valor del campo y lo muestra en el TextView
                                Object value = snapshot.get("monedas");
                                Object value1 = snapshot.get("c1");
                                Object value2 = snapshot.get("c2");
                                Object value3 = snapshot.get("c3");
                                if (value != null) {
                                    coins.setText(String.valueOf(value));
                                    //Ac1.setText(String.valueOf(value1));
                                    //Ac2.setText(String.valueOf(value2));
                                    //Ac3.setText(String.valueOf(value3));

                                } else {
                                    Log.d("Firestore", "Campo 'monedas' no encontrado");
                                }
                            } else {
                                Log.d("Firestore", "Current data: null");
                            }

                        }
                    });
        }
    }

    private void loadImageIntoView(String url, ImageView imageView, String tag) {
        if (url != null) {
            Glide.with(PreguntaActivity.this)
                    .load(url)
                    .fitCenter()
                    .centerInside()
                    .into(imageView);
            imageView.setTag(tag);
        }
    }
}
