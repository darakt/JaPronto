package com.example.darakt.japrontochef;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darakt.japronto.REST.ApiManager;
import com.example.darakt.japronto.REST.ApiService;
import com.example.darakt.japronto.REST.models.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    Client client = new Client();
    final Context context = this;
    ProgressDialog pd;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
        pd = new ProgressDialog(context);
        pd.setIndeterminate(true);
        pd.setMessage("Authenticating...");
        pd.show();

        String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        ApiService apiService = ApiManager.createService(ApiService.class, email, password);
        Call<Client> call = apiService.connect(email);

        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, final Response<Client> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show();
                    Log.d(TAG,Boolean.toString(response.isSuccessful()));
                    Client it = response.body();
                    it.setPassword(password);
                    }
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                pd.dismiss();
                                if (response.isSuccessful()) {
                                    LoginActivity.this.client = response.body();
                                    Log.d(TAG,"loging successful");
                                    Log.d(TAG, client.getName());
                                    Log.d(TAG, client.getPseudo());
                                    onLoginSuccess();
                                }else{
                                    Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
                                    _emailText.setText("");
                                    _passwordText.setText("");
                                }
                            }
                        }, 3000);
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: "+t.getMessage()+"     "+t.getStackTrace());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.client = (Client) data.getSerializableExtra("client");
                Intent toReturn = new Intent();
                toReturn.putExtra("client", this.client);
                setResult(RESULT_OK, toReturn);
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent toReturn = new Intent();
        toReturn.putExtra("client", this.client);
        setResult(RESULT_OK, toReturn);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
