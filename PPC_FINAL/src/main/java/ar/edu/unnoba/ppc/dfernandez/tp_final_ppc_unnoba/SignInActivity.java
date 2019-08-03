package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    TextView new_user,new_user_password,passwordCheck;
    Button registrar;
    //String newUsername,newUsernamePassword;
    AlertDialogManager alert = new AlertDialogManager();
    SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor = sharedPreferences.edit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        new_user = (TextView) findViewById(R.id.new_user);
        new_user_password = (TextView) findViewById(R.id.new_user_password);
        passwordCheck = (TextView) findViewById(R.id.passwordCheck);
        registrar = (Button) findViewById(R.id.registrar);
        registrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sharedPreferences = getSharedPreferences(LoginActivity.S_PREFERENCES, Context.MODE_PRIVATE);

        switch (v.getId()){
            case R.id.registrar:
                if(verifyNewUser()){
                    //Intent i = new Intent();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(new_user.getText().toString(),new_user_password.getText().toString());
                    //editor.putString("password",user_password);
                    editor.apply();
                    //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    //startActivity(i);
                    finish();
                    break;

                    //ArrayList<String> newUserData = new ArrayList<>();
                    //newUserData.add(0,new_user.getText().toString());
                    //newUserData.add(1,new_user_password.getText().toString());
                    //returnIntent.putStringArrayListExtra("newUser",newUserData);
                    //setResult(Activity.RESULT_OK,returnIntent);
                    //finish();
                    //break;
                }else{
                    break;
                }


        }
    }

    private boolean verifyNewUser() {
        String username = new_user.getText().toString();
        String user_password = new_user_password.getText().toString();

        if(username.trim().length() > 0 && user_password.trim().length() > 0){
            if(user_password.equals(passwordCheck.getText().toString())){

                if(sharedPreferences.getString(username,null)==null){
                    return true;
                }
                //ArrayList<String> actual_users = getIntent().getStringArrayListExtra("actualUsers");
                //if(!actual_users.contains(username)){
                //    return true;
                else{
                    alert.showAlertDialog(SignInActivity.this, "Atencion!", "Ya existe el usuario especificado", false);
                    return false;
                }
            }else{
                alert.showAlertDialog(SignInActivity.this, "Atencion!", "Las contraseñas no coinciden", false);
                return false;
            }
        }else{
            alert.showAlertDialog(SignInActivity.this, "Fallo el registro..", "Por favor ingrese el nuevo usuario y contraseña", false);
            return false;
        }
    }
}


