package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{// implements View.OnClickListener{
    //Button botonCerrarSesion,listarObras;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //Toolbar toolBar;
    Gson gson;
    RequestQueue cola;
    String url = "http://ppc.edit.com.ar:8080/resources/datos/obras/-34.581727/-60.931513";
    List<Obra> obras;
    AlertDialogManager alert;
    RecyclerView listado_obras;
    ObrasAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(LoginActivity.S_PREFERENCES, Context.MODE_PRIVATE);
        listado_obras = findViewById(R.id.recycler);
        //alert.showAlertDialog(MainActivity.this, "Bienvenido", "Bienvenido "+sharedPreferences.getString("user","invitado")+" al sistema de gestion de obras", false);
        cola = Volley.newRequestQueue(this);
        gson = new Gson();
        cargar_obras();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Esta seguro de que desea cerrar la sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargar_obras(){
        cola = Volley.newRequestQueue(this);
        JsonArrayRequest json_request = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        llenar_lista(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: hubo un error al conectar con el Web Service en [ "+url+" ]");
            }
        });

        cola.add(json_request);
    }
    private void llenar_lista(JSONArray source){

        obras = Arrays.asList(gson.fromJson(source.toString(),Obra[].class));
        if(obras != null && !obras.isEmpty()) {
            adapter = new ObrasAdapter(obras);
            listado_obras.setAdapter(adapter);
        }else{
            alert.showAlertDialog(MainActivity.this, "Error!", "No hay obras para mostrar", false);
        }
    }
/*    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonCerrarSesion:
                //session.logoutUser();
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Esta seguro de que desea cerrar la sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;

        }
    }*/

    @Override
    public void onBackPressed(){
        finish();
    }
}
