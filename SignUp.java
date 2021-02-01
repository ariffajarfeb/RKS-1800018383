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

public class SignUp extends AppCompatActivity {

    private EditText etNama, etEmailSignUp, etPasswordSignUp;
    private Button btnSignUp, btnReset;
    private RequestQueue requestQueue;
    private TextView btnakunn;
    private StringRequest stringRequest;
    private final String URL = "http://192.168.1.9/PERPUSTAKAAN/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etNama = (EditText) findViewById(R.id.etNama);
        etEmailSignUp = (EditText) findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = (EditText) findViewById(R.id.etPasswordSignUp);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnReset = (Button) findViewById(R.id.btnResett);

        requestQueue = Volley.newRequestQueue(this);

        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("sukses")) {
                        Toast.makeText(getApplicationContext(), "DATA SUDAH MASUK", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "DATA BELUM MASUK", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringRequest = new StringRequest(Request.Method.POST, URL, listener, null) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("nama", etNama.getText().toString());
                        hashMap.put("email", etEmailSignUp.getText().toString());
                        hashMap.put("password", etPasswordSignUp.getText().toString());

                        return hashMap;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNama.setText("");
                etEmailSignUp.setText("");
                etPasswordSignUp.setText("");
            }
        });
    }
}