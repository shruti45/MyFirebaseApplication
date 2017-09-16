package shrutzz.com.myfirebaseapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shrutzz.com.myfirebaseapplication.R;
import shrutzz.com.myfirebaseapplication.model.User;
import shrutzz.com.myfirebaseapplication.utils.Internet;

public class LoginActivity extends AppCompatActivity {

    Activity activtiy;

    private static final String TAG = "stringgg";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean firstTimeAccess;
    String username,password;

    @Bind(R.id.loader_lay)
    RelativeLayout loaderlay;
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.register_loginscreen)
    View register_loginscreen;
    @Bind(R.id.loginpage)
    View login_screen;
    @Bind(R.id.reg_username)
    EditText reg_username;
    @Bind(R.id.reg_password)
    EditText reg_password;
    @Bind(R.id.reg_repeatpassword)
    EditText reg_repeatpassword;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activtiy=LoginActivity.this;
        ButterKnife.bind(activtiy);

        firstTimeAccess=true;
        initFirebase();

    }

    @OnClick(R.id.actmain_singbtn)
    void Login(){

         username = et_username.getText().toString();
         password = et_password.getText().toString();
        if (!username.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
            loaderlay.setVisibility(View.VISIBLE);
            if (Internet.isConnectedToInternet(activtiy)) {
                signIn(username,password);
            }
        }

    }

    @OnClick(R.id.actmain_singupbtn)
    public void clickRegister() {
        try {
            String regusername = reg_username.getText().toString();
            String regpassword = reg_password.getText().toString();
            String repeatPassword = reg_repeatpassword.getText().toString();
//            String phoneno = reg_PhoneNo.getText().toString();
            if (!regusername.equalsIgnoreCase("") && (regpassword.equalsIgnoreCase(repeatPassword))) {
                loaderlay.setVisibility(View.VISIBLE);
                createNewUser(regusername,regpassword);
            } else {
                Toast.makeText(this, "Invalid email or not match password or Phone Number", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.signup_Txt)
    void goTosignUpScren(){

        // setVisibleShowHide(register_screen,login_screen);
        nextVisibleShowHide(register_loginscreen, login_screen);
    }


    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();
                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    String uid = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid() +" " +name + " " + email + " " + photoUrl + " " + uid);

                    if (firstTimeAccess) {
                        startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                        LoginActivity.this.finish();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                firstTimeAccess = false;
            }
        };
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            saveUserInfo();
                            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                            LoginActivity.this.finish();
                            loaderlay.setVisibility(View.GONE);
                        }

                    }
                });
    }

    private void saveUserInfo() {
        if (Internet.isConnectedToInternet(activtiy)){

        }else {
            Toast.makeText(getApplicationContext(), Internet.interNetMsg,Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            initNewUSer(email);
                            Toast.makeText(LoginActivity.this, "Register and Login success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                            LoginActivity.this.finish();
                            loaderlay.setVisibility(View.GONE);
                        }

                        // ...
                    }
                });
    }

    private void initNewUSer(String email) {
        if(Internet.isConnectedToInternet(activtiy)){
            User newUser=new User();
            newUser.name=user.getEmail().substring(0, user.getEmail().indexOf("@"));
            newUser.email=user.getEmail();

            FirebaseDatabase.getInstance().getReference().child("user/"+user.getUid()).setValue(newUser);

        }else {
            Toast.makeText(getApplicationContext(), Internet.interNetMsg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        if (register_loginscreen.getVisibility() == View.VISIBLE) {
            setVisibleShowHide(login_screen, register_loginscreen);
        } else {
            super.onBackPressed();
            setResult(RESULT_CANCELED, null);
            finish();
        }
    }

    private void nextVisibleShowHide(View register_loginscreen, View login_screen) {
        register_loginscreen.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.right_to_left);
        register_loginscreen.startAnimation(animation);
        login_screen.setVisibility(View.GONE);
        Animation animation1 = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.left_to_right);
        login_screen.startAnimation(animation1);

    }


    private void setVisibleShowHide(View login_screen, View register_loginscreen) {
        login_screen.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.move_left_in_activity);
        login_screen.startAnimation(animation);
        register_loginscreen.setVisibility(View.GONE);
        Animation animation1 = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.move_right_out_activity);
        register_loginscreen.startAnimation(animation1);
    }


}
