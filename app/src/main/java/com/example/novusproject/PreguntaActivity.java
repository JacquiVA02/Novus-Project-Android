package com.example.novusproject;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.AlertDialog;
import android.widget.TextView;

import android.media.MediaPlayer;


public class PreguntaActivity extends AppCompatActivity {

    ImageView btn_back, question, response1, response2, response3, response4, coffee, eraser, candy, emotions;
    private List<ImageView> responseViews;
    String correctText;
    TextView coins, coffeeUser, eraserUser, candyUser;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    // Declarar las variables imgCorrect e imgIncorrect como variables de clase
    private String imgCorrect;
    private String imgIncorrect;
    // Declarar las variables para los valores c1, c2 y c3 como campos de clase
    private Double c1 = 0.0;
    private Double c2 = 0.0;
    private Double c3 = 0.0;

    private MediaPlayer mediaPlayer;
    private MediaPlayer incorrectMediaPlayer;

    Double puntosPregunta, monedasPregunta;
    String param1, param2, param3;

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
        coffee = findViewById(R.id.CoffeeComodin);
        eraser = findViewById(R.id.EraserComodin);
        candy = findViewById(R.id.CandyComodin);

        coffeeUser = findViewById(R.id.coffeeUser);
        eraserUser = findViewById(R.id.eraserUser);
        candyUser = findViewById(R.id.candyUser);

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
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c2 != 0){
                    removeRandomIncorrectResponses();
                    updateRemoveOpC2();
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

        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1 != 0){
                    // Ocultar todas las respuestas incorrectas
                    for (ImageView view : responseViews) {
                        String tag = (String) view.getTag();
                        if (tag != null && !tag.equals(correctText)) {
                            view.setVisibility(View.GONE);
                        }
                    }

                    // Asegurarse de que la respuesta correcta esté visible
                    for (ImageView view : responseViews) {
                        String tag = (String) view.getTag();
                        if (tag != null && tag.equals(correctText)) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                    updateRemoveOpC1();
                } else {
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

        candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c3 != 0){
                    removeRandomIncorrectResponse1();
                    updateRemoveOpC3();
                } else {
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

    // Método para eliminar una respuesta incorrecta aleatoria
    private void removeRandomIncorrectResponse1() {
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

            // Eliminar la primera respuesta incorrecta aleatoria
            ImageView responseToRemove1 = incorrectResponses.get(0);

            // Ocultar la respuesta eliminada
            responseToRemove1.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "No hay suficientes respuestas incorrectas para eliminar.");
        }
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
            updateOpC2();

            emotions.setImageResource(R.drawable.happy);

            // Reproducir sonido de respuesta correcta
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }

            // Comodin de Café
            if (param2.startsWith("R10")) {
                checkParam2(param2, param3, new OnParam2CheckedListener() {
                    @Override
                    public void onResult(boolean isParam2False) {
                        // Verificar si el resultado es falso
                        if (isParam2False) {
                            incrementC1();
                            // El resultado de checkParam2 es verdadero (es decir, el campo es falso)
                            // Lógica específica cuando param2 es falso
                            //Log.d(TAG, "El campo param2 es falso. Ejecutar lógica específica.");
                        }
                        //else {
                            // El resultado de checkParam2 es falso (es decir, el campo no es falso)
                            // Lógica específica cuando param2 no es falso
                           // Log.d(TAG, "El campo param2 no es falso. Ejecutar lógica alternativa.");
                        //}
                    }
                });
            }

            // Comodin de Dulce
            verificarOpc3(opc3 -> {
                if (opc3 != null && opc3 == 0) {
                    checkParam2(param2, param3, new OnParam2CheckedListener() {
                        @Override
                        public void onResult(boolean isParam2False) {
                            if (isParam2False) {
                                incrementC3();
                                // Lógica específica cuando param2 es falso
                                // Log.d(TAG, "El campo param2 es falso. Ejecutar lógica específica.");
                            } else {
                                // Lógica específica cuando param2 no es falso
                                // Log.d(TAG, "El campo param2 no es falso. Ejecutar lógica alternativa.");
                            }
                        }
                    });
                } else {
                    // Opcional: Manejar el caso en que opc3 no sea 0
                    // Log.d(TAG, "El valor de opc3 no es 0.");
                }
            });


            // Finalizar la actividad actual
            updateMenosOpC3();
            finish();

        } else {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_incorrect));
            Glide.with(getApplicationContext())
                    .load(imgIncorrect)
                    .fitCenter()
                    .centerInside()
                    .into(imageView);
            updateWrongOpC2();

            emotions.setImageResource(R.drawable.sad);

            // Reproducir sonido de respuesta incorrecta
            if (incorrectMediaPlayer != null) {
                incorrectMediaPlayer.start();
            }

            updateMasOpC3();
            showIncorrectAnswerDialog(correctText, video);
        }

        // Desactivar los otros botones
        response1.setOnClickListener(null);
        response2.setOnClickListener(null);
        response3.setOnClickListener(null);
        response4.setOnClickListener(null);
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

    private void incrementC1() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);

            // Referencia al documento del usuario en la colección "Usuario"
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Incrementar el campo "c1" en el documento
            userDocRef.update("c1", FieldValue.increment(1))
                    .addOnSuccessListener(aVoid -> {
                        // Operación exitosa
                        Log.d(TAG, "Campo c1 incrementado exitosamente.");
                    })
                    .addOnFailureListener(e -> {
                        // Manejo del error
                        Log.d(TAG, "Error al incrementar el campo c1.", e);
                    });
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(PreguntaActivity.this, SesionActivity.class));
            finish();
        }
    }

    // Comodin del dulce
    private void incrementC3() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);

            // Referencia al documento del usuario en la colección "Usuario"
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Incrementar el campo "c1" en el documento
            userDocRef.update("c3", FieldValue.increment(1))
                    .addOnSuccessListener(aVoid -> {
                        // Operación exitosa
                        Log.d(TAG, "Campo c3 incrementado exitosamente.");
                    })
                    .addOnFailureListener(e -> {
                        // Manejo del error
                        Log.d(TAG, "Error al incrementar el campo c3.", e);
                    });
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(PreguntaActivity.this, SesionActivity.class));
            finish();
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

    private void updateOpC2() {
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
                        Double currentC1 = document.getDouble("c2");
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
                            userDocRef.update("c2", newC1, "opc1", 0.0)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "c2 y opc1 actualizados correctamente"))
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

    private void updateRemoveOpC2() {
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
                        Double currentC1 = document.getDouble("c2");
                        if (currentC1 == null) {
                            currentC1 = 0.0;
                        }

                        Double newC1 = currentC1 - 1;

                        // Actualizar el campo 'c1'
                        userDocRef.update("c2", newC1)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "c2 actualizado correctamente"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar c2", e));
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

    private void updateRemoveOpC3() {
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
                        Double currentC1 = document.getDouble("c3");
                        if (currentC1 == null) {
                            currentC1 = 0.0;
                        }

                        Double newC1 = currentC1 - 1;

                        // Actualizar el campo 'c1'
                        userDocRef.update("c3", newC1)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "c3 actualizado correctamente"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar c3", e));
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

    private void updateWrongOpC2() {
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
                        Double currentC1 = document.getDouble("c2");
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
                            userDocRef.update("c2", newC1, "opc1", 0.0)
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

    public interface Opc3Callback {
        void onCallback(Double opc3);
    }

    private void verificarOpc3(Opc3Callback callback) {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener los valores actuales de opc3
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double opc3 = document.getDouble("opc3");
                        if (callback != null) {
                            callback.onCallback(opc3);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                        if (callback != null) {
                            callback.onCallback(null);
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    if (callback != null) {
                        callback.onCallback(null);
                    }
                }
            });
        } else {
            Log.d(TAG, "Usuario no autenticado");
            if (callback != null) {
                callback.onCallback(null);
            }
        }
    }



    private void updateMenosOpC3() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener los valores actuales de opc2 y actualizar a 0
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double currentOpC2 = document.getDouble("opc2");
                        if (currentOpC2 == null) {
                            currentOpC2 = 0.0;
                        }

                        // Poner a opc2 en 0
                        Double newOpC2 = 0.0;
                        userDocRef.update("opc2", newOpC2);

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

    private void updateMasOpC3() {
        // Obtener el usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Referencia al documento del usuario
            DocumentReference userDocRef = db.collection("Usuario").document(userId);

            // Obtener los valores actuales de opc2 y actualizar a 0
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double currentOpC2 = document.getDouble("opc2");
                        if (currentOpC2 == null) {
                            currentOpC2 = 0.0;
                        }

                        // Poner a opc2 en 0
                        Double newOpC2 = 1.0;
                        userDocRef.update("opc2", newOpC2);

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
                            Object coffeeValue = snapshot.get("c1");
                            Object eraserValue = snapshot.get("c2");
                            Object candyValue = snapshot.get("c3");

                            if (coinsValue instanceof Number) {
                                coins.setText(String.valueOf(((Number) coinsValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'monedas' no es numérico");
                            }

                            if (coffeeValue instanceof Number) {
                                coffeeUser.setText(String.valueOf(((Number) coffeeValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'c1' no es numérico");
                            }

                            if (eraserValue instanceof Number) {
                                eraserUser.setText(String.valueOf(((Number) eraserValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'c2' no es numérico");
                            }

                            if (candyValue instanceof Number) {
                                candyUser.setText(String.valueOf(((Number) candyValue).intValue()));
                            } else {
                                Log.d("Firestore", "Valor de 'c3' no es numérico");
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
                        Log.d(TAG, "Current data: " + value.getData());
                        // Asignar los valores a las variables de clase
                        imgCorrect = value.getString("correct");
                        imgIncorrect = value.getString("incorrect");
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                });
    }
}