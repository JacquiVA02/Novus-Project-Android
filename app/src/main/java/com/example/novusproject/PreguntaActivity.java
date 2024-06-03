package com.example.novusproject;

import static android.content.ContentValues.TAG;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.AlertDialog;
import android.widget.Toast;


public class PreguntaActivity extends AppCompatActivity {

    ImageView btn_back, question, response1, response2, response3, response4;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregunta);

        btn_back = findViewById(R.id.buttonBackQuestion);
        question = findViewById(R.id.imageQuestion);
        response1 = findViewById(R.id.imageResponse1);
        response2 = findViewById(R.id.imageResponse2);
        response3 = findViewById(R.id.imageResponse3);
        response4 = findViewById(R.id.imageResponse4);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Log.d(TAG, "Usuario ID: " + userId);
        } else {
            Log.d(TAG, "Usuario no autenticado");
            startActivity(new Intent(PreguntaActivity.this, SesionActivity.class));
            finish();
            return;
        }

        String param1 = getIntent().getStringExtra("isla");
        String param2 = getIntent().getStringExtra("pregunta");

        if (param1 != null && param2 != null) {
            Log.d("isla", param1);
            Log.d("pregunta", param2);
        } else {
            Log.d("PreguntaActivity", "No se recibieron los parámetros esperados.");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_back.setOnClickListener(v -> finish());

        getQuestion(param1, param2);
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
                        String correctText = value.getString("correcta");
                        String incorrect1Text = value.getString("incorrecta1");
                        String incorrect2Text = value.getString("incorrecta2");
                        String incorrect3Text = value.getString("incorrecta3");
                        String video = value.getString("link");

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
        if (selectedAnswer != null && selectedAnswer.equals(correctText)) {
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.RED);
            showIncorrectAnswerDialog(video);

        }

        // Desactivar los otros botones
        response1.setOnClickListener(null);
        response2.setOnClickListener(null);
        response3.setOnClickListener(null);
        response4.setOnClickListener(null);

        // Borrar la imagen dentro del ImageView
        //((ImageView)view).setImageDrawable(null);
    }

    private void showIncorrectAnswerDialog(String video) {
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
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + video));
                        startActivity(webIntent);
                    }
                })
                .setNegativeButton("Reintentar", (dialog, which) -> {
                    // Código para reintentar
                })
                .show();
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
