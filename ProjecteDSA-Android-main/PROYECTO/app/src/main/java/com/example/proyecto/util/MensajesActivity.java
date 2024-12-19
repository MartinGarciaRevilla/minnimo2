package com.example.proyecto.util;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto.R;
import com.example.proyecto.models.Mensaje;
import com.example.proyecto.services.MensajeService;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MensajesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    public static final String BASE_URI = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandeja);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarMensajes();
    }

    private void cargarMensajes() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MensajeService mensajeService = retrofit.create(MensajeService.class);
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Mensaje>> call = mensajeService.getMensajes();

        call.enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    MensajeAdapter adapter = new MensajeAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(MensajesActivity.this, "Messages loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MensajesActivity.this, "Error loading messages: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("MensajesActivity", "Error loading messages: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MensajesActivity.this, "Connection failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MensajesActivity", "Connection failure: " + t.getMessage());
            }
        });
    }

    public void volverMenuOnClick(View view) {
        Intent intent = new Intent(this, MenuUsuario.class);
        startActivity(intent);
    }
}