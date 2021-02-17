package com.example.mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class LoginScreen extends AppCompatActivity {
    ImageView btn_login;

    private ConstantKeys Ck;

    SharedPreferences sp;
    SharedPreferences.Editor spedit;

    private EditText et_login_email;
    /**
     * Password
     */
    private EditText et_login_pass;

    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.120:11619/");
        } catch (URISyntaxException e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);
        mSocket.connect();

        initClasses();
        
        et_login_email=findViewById(R.id.et_login_email);
        et_login_pass=findViewById(R.id.et_login_pass);

        btn_login=findViewById(R.id.btn_login);
        
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= et_login_email.getText().toString().trim();
                String pass= et_login_pass.getText().toString().trim();
                userSession.getInstance().setUsrname(email);

                if(email.equals("") && pass.equals("") ){
                    Toast.makeText(LoginScreen.this, "Please enter Username and Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONObject json = new JSONObject();
                    try {
                        json.put("uname", email);
                        json.put("passw", pass);
                    } catch (JSONException e) {
                        Toast.makeText(LoginScreen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    mSocket.emit("login",json);


                    mSocket.on("logsuccess", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            startActivity(new Intent(LoginScreen.this,MainActivity.class));
                        }
                    });
                }
            }
        });

    }

    private void initClasses() {
        Ck = new ConstantKeys();

    }

}