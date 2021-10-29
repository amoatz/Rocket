package com.example.rocket.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.auth0.android.Auth0;
////import com.auth0.android.Auth0Exception;
//import com.auth0.android.authentication.AuthenticationException;
//import com.auth0.android.callback.Callback;
//import com.auth0.android.provider.WebAuthProvider;
//import com.auth0.android.result.Credentials;

//import com.auth0.android.Auth0;
//import com.auth0.android.Auth0Exception;
//import com.auth0.android.authentication.AuthenticationException;
//import com.auth0.android.callback.Callback;
//import com.auth0.android.provider.WebAuthProvider;
//import com.auth0.android.result.Credentials;
//
//import com.auth0.android.Auth0;
//import com.auth0.android.authentication.AuthenticationException;
//import com.auth0.android.jwt.JWT;
//import com.auth0.android.provider.AuthCallback;
////import com.auth0.android.provider.ResponseType;
//import com.auth0.android.provider.WebAuthProvider;
//import com.auth0.android.result.Credentials;
//import com.auth0.samples.R;
//import com.auth0.samples.models.User;
//import com.auth0.samples.utils.CredentialsManager;
//import com.auth0.samples.utils.UserProfileManager;

import com.example.rocket.MainActivity;
import com.example.rocket.R;

public class SignInActivity extends AppCompatActivity {

//    private Auth0 auth0;

    public static final String EXTRA_CLEAR_CREDENTIALS = "com.auth0.CLEAR_CREDENTIALS";
    public static final String EXTRA_ACCESS_TOKEN = "com.auth0.ACCESS_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        auth0 = new Auth0(,"");
//        auth0 = new Auth0(this);

        //Check if the activity was launched to log the user out
        if (getIntent().getBooleanExtra(EXTRA_CLEAR_CREDENTIALS, false)) {
            logout();
        }

        Button signButton = (Button) findViewById(R.id.signin_login_button);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {

//        AccountManager am = AccountManager.get(this);
//        Bundle options = new Bundle();
//
//        am.getAuthToken(
//                myAccount_,                     // Account retrieved using getAccountsByType()
//                "Manage your tasks",            // Auth scope
//                options,                        // Authenticator-specific options
//                this,                           // Your activity
//                new OnTokenAcquired(),          // Callback called when a token is successfully acquired
//                new Handler(new OnError()));    // Callback called if an error occurs

//        auth0.setOIDCConformant(true);
//
//        WebAuthProvider.init(auth0)
//                .withScheme("demo")
//                .withAudience("https://api.exampleco.com/timesheets")
//                .withResponseType(ResponseType.CODE)
//                .withScope("create:timesheets read:timesheets openid profile email offline_access")
//                .start(
//                        // ...
//                );

//        Callback callback = new Callback() {
//            @Override
//            public void onFailure(Auth0Exception e) {
//
//            }
//
//            @Override
//            public void onSuccess(Object o) {
//
//            }
//
//        };
//
//        WebAuthProvider.login(auth0)
//                .withScheme("drink")
//                .start(this, callback);


//        WebAuthProvider.login(auth0)
//                .withScope("openid profile email read:users")
//                .start(this, callback);


//        WebAuthProvider.login(auth0)
//                .withScheme("demo")
//                .withScope("openid profile email read:current_user update:current_user_metadata")
//                .withAudience("https://${getString(R.string.com_auth0_domain)}/api/v2/")
//                .start(this, new Callback<Credentials, AuthenticationException>() {
//
//                    @Override
//                    public void onFailure(final AuthenticationException exception) {
//                        Toast.makeText(SignInActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSuccess(final Credentials credentials) {
//                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                        intent.putExtra(EXTRA_ACCESS_TOKEN, credentials.getAccessToken());
//                        startActivity(intent);
//                        finish();
//                    }
//                });


//                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
//                .start(this, new Callback<Credentials, AuthenticationException>() {
//
//                    @Override
//                    public void onFailure(final AuthenticationException exception) {
//                        Toast.makeText(SignInActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSuccess(final Credentials credentials) {
//                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                        intent.putExtra(EXTRA_ACCESS_TOKEN, credentials.getAccessToken());
//                        startActivity(intent);
//                        finish();
//                    }
//                });
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            // Get the result of the operation from the AccountManagerFuture.
//            Bundle bundle = result.getResult();

            // The token is a named value in the bundle. The name of the value
            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
//            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

        }
    }

    private void logout() {
//        WebAuthProvider.logout(auth0)
//                .withScheme("demo")
//                .start(this, new Callback<Void, AuthenticationException>() {
//                    @Override
//                    public void onSuccess(Void payload) {
//
//                    }
//
//                    @Override
//                    public void onFailure(AuthenticationException error) {
//                        //Log out canceled, keep the user logged in
//                        showNextActivity();
//                    }
//                });
    }

    private void showNextActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}