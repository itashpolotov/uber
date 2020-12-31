package com.example.ubertaxi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class MainActivity extends AppCompatActivity {
    Button signup, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.signin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


    }

    private void signup() {


        AlertDialog.Builder dialogSignUp = new AlertDialog.Builder(this);
        dialogSignUp.setTitle("SIGN UP");

        View layout = LayoutInflater.from(this).inflate(R.layout.signup_layout, null);


        dialogSignUp.setView(layout);

        dialogSignUp.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {

            MaterialEditText usernameEd = layout.findViewById(R.id.username);
            MaterialEditText emailEd = layout.findViewById(R.id.email);
            MaterialEditText phoneEd = layout.findViewById(R.id.phone);
            MaterialEditText passwordEd = layout.findViewById(R.id.password);
            Spinner spinner = layout.findViewById(R.id.spinner);

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(usernameEd.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(emailEd.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordEd.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneEd.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the phone", Toast.LENGTH_SHORT).show();
                    return;
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://parseapi.back4app.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Api api = retrofit.create(Api.class);
                String username = usernameEd.getText().toString();
                String email = emailEd.getText().toString();
                String usertype = spinner.getSelectedItem().toString();
                String password = passwordEd.getText().toString();


                Call<RegisterResponse> call = api.signUpUser(new User(username, email, usertype, password, phoneEd.getText().toString()));

                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "user has been created", Toast.LENGTH_LONG).show();
                            //       Log.i("ikbol",registerResponse.sessionToken);
                        } else {
                            Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                            Log.i("ikbol", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Log.i("ikbol", t.getMessage());
                    }
                });

            }


        });
        dialogSignUp.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogSignUp.show();
    }

    private void login() {


        AlertDialog.Builder dialogLogin = new AlertDialog.Builder(this);

        dialogLogin.setTitle("SIGN IN");

        View layout_login = LayoutInflater.from(this).inflate(R.layout.login_layout, null);


        dialogLogin.setView(layout_login);

        dialogLogin.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {

            MaterialEditText username_login = layout_login.findViewById(R.id.username_login);

            MaterialEditText password_login = layout_login.findViewById(R.id.password_login);

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(username_login.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password_login.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://parseapi.back4app.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Api api = retrofit.create(Api.class);


                Call<LoginResponse> call = api.loginUser(username_login.getText().toString(), password_login.getText().toString());

                call.enqueue(new Callback<LoginResponse>() {


                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            String token=loginResponse.getSessionToken();
                            if(loginResponse.getUserType().equals("Customer")) {
                                Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                                String username = loginResponse.getUsername();
                                String phone = loginResponse.getPhone();

                                intent.putExtra("username", (String) username);
                                intent.putExtra("phone", (String) phone);


                                Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_LONG).show();
                                Log.i("ikbol", loginResponse.sessionToken);
                                Log.i("ikbol", String.valueOf(response.code()));
                                startActivity(intent);
                            } else if (loginResponse.getUserType().equals("Driver")){
                                Intent intent = new Intent(MainActivity.this, Orders.class);

                                String username = loginResponse.getUsername();
                                String phone = loginResponse.getPhone();

                                intent.putExtra("username", (String) username);
                                intent.putExtra("phone", (String) phone);


                                Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_LONG).show();
                                Log.i("ikbol", loginResponse.sessionToken);
                                Log.i("ikbol", String.valueOf(response.code()));
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                            Log.i("ikbol", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                    }
                });
            }
        });
        dialogLogin.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogLogin.show();
    }
}
