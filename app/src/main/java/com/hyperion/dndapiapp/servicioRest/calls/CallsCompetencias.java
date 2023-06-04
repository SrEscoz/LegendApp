package com.hyperion.dndapiapp.servicioRest.calls;

import com.hyperion.dndapiapp.entidades.competencias.Competencia;
import com.hyperion.dndapiapp.servicioRest.RespuestaApi;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CallsCompetencias {

    @GET("competencias")
    Call<RespuestaApi<Competencia>> getCompetencias();
}
