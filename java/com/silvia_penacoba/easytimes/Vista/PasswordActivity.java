package com.silvia_penacoba.easytimes.Vista;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.silvia_penacoba.easytimes.R;

import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class PasswordActivity extends AppCompatActivity {

    private EditText user, pass;
    private Button mSubmit, mRegister;

    private ProgressBar pBar;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        context=this;
        user=findViewById(R.id.dni);
        pass=findViewById(R.id.password);
        mSubmit=findViewById(R.id.email_sign_in_button);
        mRegister=findViewById(R.id.email_registrer);
        pBar=findViewById(R.id.progressBar);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PasswordActivity.this,RegistroActivity.class);
                PasswordActivity.this.startActivity(intent);
            }
        });


    }
    public void login(){
        StringRequest request=new StringRequest(Request.Method.POST, "http://192.168.1.43/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

        };
    }









}

