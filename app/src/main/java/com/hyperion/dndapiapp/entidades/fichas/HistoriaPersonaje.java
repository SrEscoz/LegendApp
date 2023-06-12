package com.hyperion.dndapiapp.entidades.fichas;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class HistoriaPersonaje implements Parcelable {

    @SerializedName("idHistoria")
    private long idHistoria;
    @SerializedName("trasfondo")
    private String trasfondo;
    @SerializedName("idiomas")
    private String idiomas;
    @SerializedName("competencias")
    private String competencias;

    /* =============== CONSTRUCTORES =============== */

    /* =============== METODOS =============== */

    protected HistoriaPersonaje(Parcel in) {
        idHistoria = in.readLong();
        trasfondo = in.readString();
        idiomas = in.readString();
        competencias = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idHistoria);
        dest.writeString(trasfondo);
        dest.writeString(idiomas);
        dest.writeString(competencias);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HistoriaPersonaje> CREATOR = new Creator<HistoriaPersonaje>() {
        @Override
        public HistoriaPersonaje createFromParcel(Parcel in) {
            return new HistoriaPersonaje(in);
        }

        @Override
        public HistoriaPersonaje[] newArray(int size) {
            return new HistoriaPersonaje[size];
        }
    };

    /* =============== GETTERS & SETTERS =============== */

    public long getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(long idHistoria) {
        this.idHistoria = idHistoria;
    }

    public String getTrasfondo() {
        return trasfondo;
    }

    public void setTrasfondo(String trasfondo) {
        this.trasfondo = trasfondo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public String getCompetencias() {
        return competencias;
    }

    public void setCompetencias(String competencias) {
        this.competencias = competencias;
    }
}
