package com.hyperion.dndapiapp.actividades;

import static com.hyperion.dndapiapp.utilidades.Constantes.DB_NAME;
import static com.hyperion.dndapiapp.utilidades.Constantes.USUARIO_BUNDLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hyperion.dndapiapp.R;
import com.hyperion.dndapiapp.databinding.ActivityMainBinding;
import com.hyperion.dndapiapp.entidades.usuario.Usuario;
import com.hyperion.dndapiapp.fragmentos.BibliotecaFragment;
import com.hyperion.dndapiapp.fragmentos.FavoritosFragment;
import com.hyperion.dndapiapp.fragmentos.FichasFragment;
import com.hyperion.dndapiapp.sqlite.Favorito;
import com.hyperion.dndapiapp.sqlite.SQLiteHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Usuario usuario;

    /* Utils */
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private SQLiteHelper sqLiteHelper;
    private List<Favorito> favoritos;

    /* Fragmentos */
    private BibliotecaFragment bibliotecaFragment;
    private FavoritosFragment favoritosFragment;
    private FichasFragment fichasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        sqLiteHelper = SQLiteHelper.getInstance(this, DB_NAME, null, 1);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuario = bundle.getParcelable(USUARIO_BUNDLE);
        }

        actualizaFavoritos();
        iniciaActividad();
        inicializaFragmentos();
        aniadeListenerNavBar();

        cambiaFragmento(bibliotecaFragment);
        binding.tituloFragment.setText("Biblioteca");
    }

    private void iniciaActividad() {
        binding.botonUsuario.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra(USUARIO_BUNDLE, usuario);
            startActivity(intent);
        });
    }

    private void inicializaFragmentos() {
        bibliotecaFragment = BibliotecaFragment.newInstance();
        favoritosFragment = new FavoritosFragment();
        fichasFragment = new FichasFragment();
    }

    private void aniadeListenerNavBar() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navBiblioteca) {
                binding.tituloFragment.setText("Biblioteca");
                cambiaFragmento(bibliotecaFragment);

            } else if (itemId == R.id.navFavoritos) {
                binding.tituloFragment.setText("Favoritos");
                cambiaFragmento(favoritosFragment);

            } else if (itemId == R.id.navFichas) {
                binding.tituloFragment.setText("Mis Fichas");
                cambiaFragmento(fichasFragment);
            }
            return true;
        });
    }

    private void cambiaFragmento(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    public void guardaFavorito(Favorito favorito) {
        sqLiteHelper.iniciaConexion();
        sqLiteHelper.insert(favorito);
        favoritos.add(favorito);
        sqLiteHelper.stop();
    }

    public void eliminaFavorito(Favorito favorito) {
        if (checkFavorito(favorito.getNombre())) {
            favoritos.remove(favorito);
            sqLiteHelper.iniciaConexion();
            sqLiteHelper.delete(favorito.getNombre());
            sqLiteHelper.stop();
        }
    }

    private void actualizaFavoritos() {
        sqLiteHelper.iniciaConexion();
        favoritos = sqLiteHelper.selectAll();
        sqLiteHelper.stop();
    }

    public boolean checkFavorito(String nombre) {
        return favoritos.contains(new Favorito(nombre, ""));
    }
}