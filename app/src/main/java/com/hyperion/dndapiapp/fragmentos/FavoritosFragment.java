package com.hyperion.dndapiapp.fragmentos;

import static com.hyperion.dndapiapp.utilidades.Constantes.ACTIVIDAD_FAVORITO;
import static com.hyperion.dndapiapp.utilidades.Constantes.FAVORITO_BUNDLE;
import static com.hyperion.dndapiapp.utilidades.Constantes.HECHIZOS_BUNDLE;
import static com.hyperion.dndapiapp.utilidades.Constantes.IS_FAVORITO;
import static com.hyperion.dndapiapp.utilidades.Constantes.IS_FAVORITO_RESULT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperion.dndapiapp.actividades.MainActivity;
import com.hyperion.dndapiapp.actividades.fichas.FichaHechizoActivity;
import com.hyperion.dndapiapp.adaptadores.recyclerView.RecyclerViewClick;
import com.hyperion.dndapiapp.adaptadores.recyclerView.adaptadores.AdaptadorFavoritos;
import com.hyperion.dndapiapp.databinding.FragmentFavoritosBinding;
import com.hyperion.dndapiapp.dialogos.LoadingDialog;
import com.hyperion.dndapiapp.entidades.equipamiento.Hechizo;
import com.hyperion.dndapiapp.servicioRest.callbacks.CallbackCustom;
import com.hyperion.dndapiapp.servicioRest.servicios.ServicioClases;
import com.hyperion.dndapiapp.servicioRest.servicios.ServicioCompetencias;
import com.hyperion.dndapiapp.servicioRest.servicios.ServicioEnemigos;
import com.hyperion.dndapiapp.servicioRest.servicios.ServicioEquipamiento;
import com.hyperion.dndapiapp.servicioRest.servicios.ServicioRazas;
import com.hyperion.dndapiapp.servicioRest.servicios.ServicioTrasfondos;
import com.hyperion.dndapiapp.sqlite.Favorito;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class FavoritosFragment extends Fragment implements RecyclerViewClick {

    private FragmentFavoritosBinding binding;
    private AdaptadorFavoritos adaptador;
    private List<Favorito> favoritos;

    public FavoritosFragment() {
    }

    public static FavoritosFragment newInstance() {
        return new FavoritosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritos = ((MainActivity) getActivity()).getFavoritos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFavoritosBinding.inflate(inflater, container, false);
        iniciarFragmento();
        return binding.getRoot();
    }

    private void iniciarFragmento() {
        RecyclerView recyclerView = binding.listaFavoritos;
        adaptador = new AdaptadorFavoritos(favoritos, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptador);
    }

    @Override
    public void onCosaCliked(int posicion) {
        LoadingDialog dialog = new LoadingDialog(getContext());
        Favorito favorito = adaptador.getFavorito(posicion);

        dialog.show("Obteniendo favorito");

        switch (favorito.getTipo()) {
            case "Hechizo":
                ServicioEquipamiento.getInstance().getAllHechizo(new CallbackCustom<Hechizo>() {
                    @Override
                    public void exito(Hechizo resultado) {
                        dialog.dismiss();

                        Intent intent = new Intent(getContext(), FichaHechizoActivity.class);
                        intent.putExtra(HECHIZOS_BUNDLE, resultado);
                        intent.putExtra(IS_FAVORITO, true);
                        startActivityForResult(intent, ACTIVIDAD_FAVORITO);
                    }

                    @Override
                    public void fallo(String mensaje) {
                        dialog.dismiss();
                    }

                }, favorito.getNombre());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVIDAD_FAVORITO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    boolean isFavorito = data.getBooleanExtra(IS_FAVORITO_RESULT, false);
                    Favorito favorito = data.getParcelableExtra(FAVORITO_BUNDLE);

                    if (isFavorito)
                        ((MainActivity) getActivity()).guardaFavorito(favorito);
                    else {
                        ((MainActivity) getActivity()).eliminaFavorito(favorito);
                        adaptador.removeItem(favorito);
                    }
                }
            }
        }
    }
}