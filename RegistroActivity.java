package com.silvia_penacoba.easytimes.Vista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.silvia_penacoba.easytimes.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistroActivity extends AppCompatActivity {
    private EditText edtdni,edtnombre,edtapellido,edtcontra,edtrol,edttelefono;
    private Button btncrear;
    private Toolbar toolbar;
    private final String serverUrl = "http://192.168.1.43/";
    protected String enteredUserdni;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        edtdni=findViewById(R.id.edtDni);
        edtnombre=findViewById(R.id.edtNombre);
        edtapellido=findViewById(R.id.edtApellido);
        edtcontra=findViewById(R.id.edtContra);
        edtrol=findViewById(R.id.edtRol);
        edttelefono=findViewById(R.id.edtTelefono);
        btncrear=findViewById(R.id.btnCrear);


        btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
            }
            });

    }

    private void cargarWebService() {

        enteredUserdni = edtdni.getText().toString();

        String enteredPassword = edtcontra.getText().toString();

        String enteredName = edtnombre.getText().toString();
        String enteredApellido = edtapellido.getText().toString();
        String enteredRol = edtrol.getText().toString();
        String enteredTelefono = edttelefono.getText().toString();
        if (enteredUserdni.equals("") || enteredPassword.equals("") || enteredName.equals("") || enteredApellido.equals("") || enteredRol.equals("") || enteredTelefono.equals("")) {

            Toast.makeText(RegistroActivity.this, "Deben estar todos los datos completos", Toast.LENGTH_LONG).show();

            return;

        }

        if (enteredUserdni.length() <= 1 || enteredPassword.length() <= 1) {

            Toast.makeText(RegistroActivity.this, "El dni y la contraseña deben ser mas largas que uno", Toast.LENGTH_LONG).show();

            return;

        }
        // request authentication with remote server4

        AsyncDataClass asyncRequestObject = new AsyncDataClass();

        asyncRequestObject.execute(serverUrl, enteredUserdni, enteredPassword, enteredName,enteredApellido,enteredRol);

    }





@SuppressLint("StaticFieldLeak")
private class AsyncDataClass extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);

        HttpConnectionParams.setSoTimeout(httpParameters, 5000);

        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        HttpPost httpPost = new HttpPost(params[0]);

        String jsonResult = "";

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("username", params[1]));

            nameValuePairs.add(new BasicNameValuePair("password", params[2]));

            nameValuePairs.add(new BasicNameValuePair("email", params[3]));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);

            jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

            System.out.println("Returned Json object " + jsonResult.toString());

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return jsonResult;

    }
    @Override

    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override

    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        System.out.println("Resulted Value: " + result);

        if(result.equals("") || result == null){

            Toast.makeText(RegistroActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();

            return;

        }

        int jsonResult = returnParsedJsonObject(result);

        if(jsonResult == 0){

            Toast.makeText(RegistroActivity.this, "Invalid username or password or email", Toast.LENGTH_LONG).show();

            return;

        }

        if(jsonResult == 1){

            Intent intent = new Intent(RegistroActivity.this, PasswordActivity.class);

            intent.putExtra("DNI", enteredUserdni);

            intent.putExtra("MESSAGE", "You have been successfully Registered");

            startActivity(intent);

        }

    }
}
    private StringBuilder inputStreamToString(InputStream is) {

        String rLine = "";

        StringBuilder answer = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {

            while ((rLine = br.readLine()) != null) {

                answer.append(rLine);

            }

        } catch (IOException e) {

// TODO Auto-generated catch block

            e.printStackTrace();

        }

        return answer;

    }
    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;

        int returnedResult = 0;

        try {

            resultObject = new JSONObject(result);

            returnedResult = resultObject.getInt("success");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return returnedResult;

    }




  /**  @Override
    public void onErrorResponse(VolleyError error) {
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                // Now you can use any deserializer to make sense of data
                JSONObject obj = new JSONObject(res);
            } catch (UnsupportedEncodingException e1) {
                // Couldn't properly decode data to string
                e1.printStackTrace();
            } catch (JSONException e2) {
                // returned data is not JSONObject?
                e2.printStackTrace();
            }
        }
       // Toast.makeText(RegistroActivity.this,"No se pudo registrar"+error.toString(),Toast.LENGTH_SHORT).show();
       // Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(RegistroActivity.this,"Se ha registrado exitosamente",Toast.LENGTH_SHORT).show();

        edtdni.setText("");
        edtnombre.setText("");
        edtapellido.setText("");
        edtrol.setText("");
        edtcontra.setText("");
        edttelefono.setText("");

    }*/


}
