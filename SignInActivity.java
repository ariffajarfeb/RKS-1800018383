package com.covid.perpustakaanyusril;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignInActivity extends AppCompatActivity {

    private EditText etEmailSignIn, etPasswordSignIn;
    private Button btnSignIn, btnReset;
    private TextView btnakunn;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    final String URL = "http://192.168.1.9/PERPUSTAKAAN/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmailSignIn = (EditText) findViewById(R.id.etEmailSignIn);
        etPasswordSignIn = (EditText) findViewById(R.id.etPasswordSignIn);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnakunn = (TextView) findViewById(R.id.btnakunn);

        requestQueue = Volley.newRequestQueue(this);
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success")) {
                        String email = jsonObject.getString("email");
                        String password = jsonObject.getString("password");

                        Intent intent = new Intent(SignInActivity.this, AddActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);

                        Toast.makeText(getApplicationContext(), "SUKSES", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "DATA BELUM MASUK", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringRequest = new StringRequest(Request.Method.POST, URL, listener, null) {
                     @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                         HashMap<String, String> hashMap = new HashMap<String, String>();
                         hashMap.put("email", etEmailSignIn.getText().toString());
                         hashMap.put("password", etPasswordSignIn.getText().toString());

                         return hashMap;
                     }
                };
                requestQueue.add(stringRequest);
            }
        });



        btnakunn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regTab = new Intent(SignInActivity.this, SignUp.class);
                startActivity(regTab);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasi(etEmailSignIn.getText().toString(), etPasswordSignIn.getText().toString());
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmailSignIn.setText("");
                etPasswordSignIn.setText("");
            }
        });

    }

    private void validasi(String Email, String Password) {
        if ((Email.equals("admin")) && (Password.equals("admin"))) {
            Intent intent = new Intent(SignInActivity.this, AddActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(SignInActivity.this, "ID / PASSWORD SALAH", Toast.LENGTH_SHORT).show();
        }


    }
}