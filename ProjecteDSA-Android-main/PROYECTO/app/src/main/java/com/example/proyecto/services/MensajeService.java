package com.example.proyecto.services;

import com.example.proyecto.models.Mensaje;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MensajeService {
    @GET("game/posts") // Ensure this path is correct
    Call<List<Mensaje>> getMensajes();
}