package com.example.testesamel;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.testesamel.dao.UsuarioDao;
import com.example.testesamel.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String NOME_APPBAR = "Lista de Usuarios";
    UsuarioDao dao = new UsuarioDao();
    private static final String URL = "https://jsonplaceholder.typicode.com/posts";
    FloatingActionButton fabScan;
    RequestQueue QUEUE;
    private SearchView searchView;
    private ListView listaUsuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(NOME_APPBAR);

        configuraFab();
        buscaTodos();

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        searchView = findViewById(R.id.campo_de_busca);

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    listaUsuarios.clearTextFilter();
                }
                else {
                    listaUsuarios.setFilterText(s.toString());
                }
                return true;
            }
        });

    }

    private void configuraFab() {
        QUEUE = Volley.newRequestQueue(this);
        fabScan = findViewById(R.id.BotaoScan);
        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QRCodeActivity.class));

            }
        });
    }


    private void configuraLista() {
        listaUsuarios = findViewById(R.id.listaUsuarios);
        listaUsuarios.setTextFilterEnabled(true);
        listaUsuarios.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dao.todos()));
    }


    private void buscaTodos() {

        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(MainActivity.this, "Não pôde achar produtos, verifique sua conexão com internet", Toast.LENGTH_LONG).show();
                            return;
                        }

                        //conversão de JSON para Objeto Usuario
                        List<Usuario> items = new Gson().fromJson(response.toString(), new TypeToken<List<Usuario>>() {
                        }.getType());

                        //salva a lista
                        dao.salva(items);

                        //Ppopula Lisra
                        configuraLista();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        QUEUE.add(request);
    }
}



