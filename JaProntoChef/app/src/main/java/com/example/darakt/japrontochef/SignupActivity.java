package com.example.darakt.japrontochef;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    public Client client = new Client();

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_surname)
    EditText _surname;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.pseudo)
    EditText _pseudo;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.cpf)
    EditText _cpf;
    @BindView(R.id.phone)
    EditText _phone;
    @BindView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

        _nameText.setText("bob");
        _surname.setText("zbla");
        _emailText.setText("ntm@tamere.com");
        _passwordText.setText("1234");
        _phone.setText("1234567890");
        _cpf.setText("blc");
        _pseudo.setText("vald");
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String surname = _surname.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String cpf = _cpf.getText().toString();
        String pseudo = _pseudo.getText().toString();
        String phone = _phone.getText().toString();

        this.client = new Client(pseudo, password, name, surname, phone, email, cpf);

        ApiService apiService = ApiManager.createService(ApiService.class);

        Call<Client> call = apiService.createUser(client);

        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                Log.d(TAG, "inside the thread, id of the new client");
                SignupActivity.this.client.setId(response.body().getId());
                Log.d(TAG, Integer.toString(SignupActivity.this.client.getId()));
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent data = new Intent();
        data.putExtra("client", this.client);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String surname = _surname.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String cpf = _cpf.getText().toString();
        String pseudo = _pseudo.getText().toString();
        String phone = _phone.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (surname.isEmpty() || surname.length() < 3) {
            _surname.setError("at least 3 characters");
            valid = false;
        } else {
            _surname.setError(null);
        }

        if (pseudo.isEmpty() || pseudo.length() < 3) {
            _pseudo.setError("at least 3 characters");
            valid = false;
        } else {
            _pseudo.setError(null);
        }

        if (cpf.isEmpty() || cpf.length() < 4) {
            _cpf.setError("at least 4 characters");
        } else {
            _cpf.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10) {
            _phone.setError("a phone number is 10 digits");
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
