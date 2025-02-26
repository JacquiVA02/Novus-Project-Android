package com.tecMonterrey.novusproject;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.AlertDialog;
import android.widget.TextView;

import android.media.MediaPlayer;


public class PreguntaActivity extends AppCompatActivity {

    ImageView btn_back, question, response1, response2, response3, response4, emotions;
    private List<ImageView> responseViews;
    String correctText;
    TextView coins, categoria;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    // Declarar las variables imgCorrect e imgIncorrect como variables de clase
    private String imgCorrect;
    private String imgIncorrect;
    private MediaPlayer mediaPlayer;
    private MediaPlayer incorrectMediaPlayer;

    Double puntosPregunta, monedasPregunta;
    String param1, param2, param3;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregunta);
        // Inicializar MediaPlayer con el sonido correcto
        mediaPlayer = MediaPlayer.create(this, R.raw.correct);
        incorrectMediaPlayer = MediaPlayer.create(this, R.raw.wrong);
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
        categoria = findViewById(R.id.textCategoria);
        emotions = findViewById(R.id.duckEmotion);
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
        // Obtener el índice del comienzo de 'I' y 'L'
        int indexI = param2.indexOf("I");
        int indexL = param2.indexOf("L");
// Verificar que ambos índices son válidos
        if (indexI != -1 && indexL != -1 && indexI < indexL) {
            // Extraer la parte entre 'I' y 'L'
            String partIY = param2.substring(indexI, indexL);
            // Comparar la parte extraída
            if (partIY.equals("I1")) {
                categoria.setText("Fracciones Numéricas");
            } else if (partIY.equals("I2")) {
                categoria.setText("Jerarquía de Operaciones");
            } else if (partIY.equals("I3")) {
                categoria.setText("Leyes de Exponentes");
            } else if (partIY.equals("I4")) {
                categoria.setText("Expresiones Algebraicas");
            } else if (partIY.equals("I5")) {
                categoria.setText("Productos Notables");
            } else if (partIY.equals("I6")) {
                categoria.setText("Factorización");
            } else if (partIY.equals("I7")) {
                categoria.setText("Fracciones Algebraicas");
            } else if (partIY.equals("I81") || partIY.equals("I82") || partIY.equals("I83")) {
                categoria.setText("Solución de Ecuaciones y Desigualdades");
            } else if (partIY.equals("I91") || partIY.equals("I92")) {
                categoria.setText("Sistemas de Ecuaciones Lineales");
            } else if (partIY.equals("I101") || partIY.equals("I102")) {
                categoria.setText("Logaritmos: Definición y Propiedades");
            }
        } else {
            // Manejar el caso donde no se encuentran 'I' o 'L' de forma válida
            throw new IllegalArgumentException("El formato de param1 no es válido: " + param1);
        }
        // Aplicar márgenes de borde a la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.levelname), (v, insets) -> {
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
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                actualizarMenosOpc3();
                finish();
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
                        //Log.d(TAG, "Current data: " + value.getData());
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
    private void checkAnswer(View view, String correctText, String video) {
        String selectedAnswer = (String) view.getTag();
        ImageView imageView = (ImageView) view;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userDocRef = db.collection("Usuario").document(userId);
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Long opc2 = document.getLong("opc2");
                        if (selectedAnswer != null && selectedAnswer.equals(correctText)) {
                            view.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_correct));
                            Glide.with(getApplicationContext())
                                    .load(imgCorrect)
                                    .fitCenter()
                                    .centerInside()
                                    .into(imageView);
                            updateQuestionState();
                            updatePoints();
//                            updateOpC2();
                            emotions.setImageResource(R.drawable.happy);
                            // Reproducir sonido de respuesta correcta
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                            }
                            finish();
                        } else {
                            view.setBackground(ContextCompat.getDrawable(this, R.drawable.incorrect));
                            Glide.with(getApplicationContext())
                                    .load(imgIncorrect)
                                    .fitCenter()
                                    .centerInside()
                                    .into(imageView);
//                            updateWrongOpC2();
//                            actualizarMasOpc3();
                            emotions.setImageResource(R.drawable.sad);
                            // Reproducir sonido de respuesta incorrecta
                            if (incorrectMediaPlayer != null) {
                                incorrectMediaPlayer.start();
                            }
                            showIncorrectAnswerDialog(correctText, video);
                        }
                        // Desactivar los otros botones
                        response1.setOnClickListener(null);
                        response2.setOnClickListener(null);
                        response3.setOnClickListener(null);
                        response4.setOnClickListener(null);
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

    public interface OnParam2CheckedListener {
        void onResult(boolean isParam2False);
    }


    private void checkParam2(String param2, String param3, OnParam2CheckedListener listener) {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);

            // Referencia al documento del usuario en la colección param3
            DocumentReference userDocRef = db.collection(param3).document(userId);

            // Obtener el dato param2 del documento del usuario
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Verificar si el campo param2 es falso
                        Boolean isParam2False = document.getBoolean(param2);
                        if (isParam2False != null && !isParam2False) {
                            // El campo param2 es falso
                            Log.d(TAG, "El campo " + param2 + " es falso.");
                            listener.onResult(true);
                        } else {
                            // El campo param2 no es falso o no existe
                            Log.d(TAG, "El campo " + param2 + " no es falso o no existe.");
                            listener.onResult(false);
                        }
                    } else {
                        Log.d(TAG, "El documento no existe.");
                        listener.onResult(false);
                    }
                } else {
                    Log.d(TAG, "Error al obtener el documento.", task.getException());
                    listener.onResult(false);
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(PreguntaActivity.this, SesionActivity.class));
            finish();
            listener.onResult(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar MediaPlayer cuando la actividad se destruye
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (incorrectMediaPlayer != null) {
            incorrectMediaPlayer.release();
            incorrectMediaPlayer = null;
        }
    }

    private void showIncorrectAnswerDialog(String correctText, String video) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Respuesta incorrecta")
                .setNegativeButton("Reintentar", (dialog, which) -> {
                    resetResponseButtons(correctText, video);
                })

                .show();
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
                        } else {
                            Log.d("Firestore", "No hay datos actuales (snapshot es null o no existe)");
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

    private void resetResponseButtons(String correctText, String video) {
        emotions.setImageDrawable(null);

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

    private void getElements(){
        db.collection("Elementos").document("elementos")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if(value != null && value.exists()) {
                        //Log.d(TAG, "Current data: " + value.getData());
                        // Asignar los valores a las variables de clase
                        imgCorrect = value.getString("correct");
                        imgIncorrect = value.getString("incorrect");
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                });
    }
}