package com.shashank.platform.schoolcollegeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    TextView txt_back;
    EditText txt_email;
    EditText txt_password;
    EditText txt_email_link;
    EditText txt_send_code;
    EditText txt_verify_code;

//    Button btn_goApp;
    Button btn_sign_anonymous;
    Button btn_sign_in_with;
    Button btn_register_with;
    Button btn_forgot_pass;
    Button btn_sign_in_link;
    Button btn_google_sign;
    Button btn_facebook_sign;
    Button btn_github_sign;
//    Button btn_twitter_sign;
    Button btn_send_code;
    Button btn_verify_code;

    AlertDialog.Builder builder;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    String TAG="Main2Activity";
    private String mVerificationId="";
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    GoogleSignInClient client;
    CallbackManager callbackManager;

    OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");

//    private FirebaseFirestore db = FirebaseFirestore.getInstance();

//    OAuthProvider.Builder providerTwiter =  OAuthProvider.newBuilder("twitter.com");




    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);

        callbackManager = CallbackManager.Factory.create();

        // Button back
        txt_back = findViewById(R.id.back);
        txt_back.setOnClickListener(view -> finish());

        //Button GoApp
        /*btn_goApp = findViewById(R.id.btn_goApp);
        btn_goApp.setOnClickListener(view -> {
            goApp();
        });*/

        //Button Anonymous
        btn_sign_anonymous = findViewById(R.id.btn_sign_anonymous);
        btn_sign_anonymous.setOnClickListener(view -> {
            signAnonymous();
        });

        // Button sign in with email&pass
        btn_sign_in_with = findViewById(R.id.btn_sign_in_with);
        btn_sign_in_with.setOnClickListener(view -> {
            signInWithEmailAndPassword();
        });

        // Button register in with email&pass
        btn_register_with = findViewById(R.id.btn_register_with);
        btn_register_with.setOnClickListener(view -> {
            createWithEmailAndPassword();
        });

        //Button forgot password
        btn_forgot_pass = findViewById(R.id.btn_forgot_password);
        btn_forgot_pass.setOnClickListener(view -> {
            restorePassword();
        });

        // Button sign in with link
        /*btn_sign_in_link =findViewById(R.id.btn_sign_in_link);
        btn_sign_in_link.setOnClickListener(view -> {
            signInWithLink();
        });*/

        // Button sign in with Google BEGIN
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this,options);
        client.signOut();
        btn_google_sign =findViewById(R.id.btn_google_sign);
        btn_google_sign.setOnClickListener(view -> {
            Intent i = client.getSignInIntent();
            startActivityForResult(i, 1234);
        });

        // Button sign in with Google END

        // Facebook BEGIN
        btn_facebook_sign = findViewById(R.id.btn_facebook_sign);
        //btn_facebook_sign.setPermissions("email");

        LoginManager fbLoginManager = com.facebook.login.LoginManager.getInstance();
        fbLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Sig in with Facebook");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                showAlert("Cancel");
            }

            @Override
            public void onError(@NotNull FacebookException e) {
                showAlert(e.toString());
            }
        });

        btn_facebook_sign.setOnClickListener(view -> fbLoginManager.logInWithReadPermissions(
            this, Arrays.asList("email", "public_profile", "user_birthday")
        ));

        // Facebook END

        // Github BEGIN

        btn_github_sign = findViewById(R.id.btn_github_sign);
        btn_github_sign.setOnClickListener(view -> {
            signInGithub();
        });

        // Twitter
        /*btn_twitter_sign = findViewById(R.id.btn_twitter_sign);
        btn_twitter_sign.setOnClickListener(view -> {
            signInTwitter();
        });*/

        // Github BEGIN


        // Button send code
        btn_send_code = findViewById(R.id.btn_send_code);
        txt_send_code = findViewById(R.id.txt_send_code);
        btn_send_code.setOnClickListener(view -> {
            fnSendCode();
        });

        // Button verify code
        btn_verify_code = findViewById(R.id.btn_verify_code);
        txt_verify_code = findViewById(R.id.txt_verify_code);
        btn_verify_code.setOnClickListener(view -> {
            fnVerifyCode();
        });


    }


    // Twitter Sign
    /*private void signInTwitter(){
        Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showAlert("Error: "+e.toString());
                                    Log.d(TAG,"Error: "+e.toString());
                                }
                            });
        } else {
            flowTwitter();
        }
        flowTwitter();
    }

    private  void  flowTwitter(){
        auth.startActivityForSignInWithProvider(this, providerTwiter.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.d(TAG, "signInTwitter:success");
                                goApp();
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showAlert("Error: "+e.toString());
                                Log.d(TAG,"Error: "+e.toString());
                            }
                        }
                );
    }*/


    // Anonymous
    private void signAnonymous(){
        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInAnonymously:success");
                            goApp();
                        } else{
                            Log.w(TAG, "signInAnonymously:failure"+ task.getException().toString());
                            showAlert("signInAnonymously:failure");
                        }
                    }
                });
    }

    // Github BEGIN
    private void signInGithub(){
        auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                goApp();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showAlert("ERROR: "+ e.toString());
                                System.out.println("ERROR: "+e.toString());
                                Log.d(TAG,"Error: "+e.toString());
                            }
                        });
    }
    // Github END

    // Forgot PASS BEGIN
    private void restorePassword(){
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        String email = txt_email.getText().toString().trim();
        if (email.length()>0){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                showAlert("We have sent you an email to change your password. =)");
                            }else {
                                showAlert("Error: Link didn't send. =(");
                            }
                        }
                    });
        }else {
            showAlert("You have to write something");
        }
    }
    // Forgot PASS END

    // With Link BEGIN

    /*private void signInWithLink(){
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        .setUrl("localhost")
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.shashank.platform.schoolcollegeapp",
                                true,
                                "12")
                        .build();
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        String email = txt_email.getText().toString().trim();
        auth.sendSignInLinkToEmail(email,actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            showAlert("We have sent you an email with a Link. =)");
                        }else {
                            showAlert("Error: Email didn't send. =(");
                        }
                    }
                });
    }*/

    // With Link END

    // Facebook sign BEGIN
    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            goApp();
                        } else {
                            // If sign in fails, display a message to the user.

                            showAlert("Wrong credentials");
                            Log.d(TAG,"Error Facebook sign in: "+task.getException().getMessage());

                        }
                    }
                });
    }

    // Facebook sign END

    // Send Code BEGIN
    private void fnSendCode(){
        String phone = "+52"+txt_send_code.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        auth.setLanguageCode("es");
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

//            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            //mResendToken = token;
        }
    };

    private void fnVerifyCode(){
           String code = txt_verify_code.getText().toString();
           PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
           auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goApp();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredentialCode:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.w(TAG, "signInWithCredentialCode:failure"+ task.getException().getMessage());
                            }
                            showAlert("Error with code my friend");
                        }
                    }
                });
    }

    // Send Code END

    // WithGoogle BEGIN
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Facebook BEGIN
        callbackManager.onActivityResult(requestCode,resultCode,data);
        //Facebook END


        if (requestCode==1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);


                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    System.out.println("Sig in with Google");
                                    goApp();
                                }else {
                                    showAlert(task.getException().getMessage());
                                    Log.d(TAG,"Error Google sign in: "+task.getException().getMessage());
                                }
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
                showAlert(e.toString());
            }
        }
    }
    // WithGoogle END

    // WithEmailAndPassword BEGIN
    private void createWithEmailAndPassword(){
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        if (txt_email.getText().length()>0 && txt_password.getText().length()>0){
            FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(txt_email.getText().toString(),
                                    txt_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                System.out.println("User created");
                                showAlert("User created");
                            }else{
                                // ShowAlert if error
                                showAlert(task.getException().toString());
                            }
                        }
                    });
        } else{
            showAlert("You have to write something");
        };
    }

    private void signInWithEmailAndPassword(){
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        if (txt_email.getText().length()>0 && txt_password.getText().length()>0){
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(txt_email.getText().toString(),
                            txt_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                System.out.println("User correct");
                                goApp();
                            }else{
                                // ShowAlert if error
                                showAlert(task.getException().toString());
                            }
                        }
                    });
        } else{
            showAlert("You have to write something");
        };
    }
    // WithEmailAndPassword END

    private void showAlert(String text){
        Toast.makeText(Main2Activity.this, text, Toast.LENGTH_SHORT).show();
        /*builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(text)
                .setPositiveButton("Aceptar",null)
                .show();*/
    }

    private void goApp(){
        // Ir a otra ventana
//        String text = db.collection("users").document().get().toString();
//        System.out.println("TEXT: - "+text);
        Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
        startActivity(intent);
    }

}
