package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    TextView new_user,new_user_password,passwordCheck;
    Button registrar;
    AlertDialogManager alert = new AlertDialogManager();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        new_user = findViewById(R.id.new_user);
        new_user_password = findViewById(R.id.new_user_password);
        passwordCheck = findViewById(R.id.passwordCheck);
        registrar = findViewById(R.id.registrar);
        registrar.setOnClickListener(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View v) {
        sharedPreferences = getSharedPreferences(LoginActivity.S_PREFERENCES, Context.MODE_PRIVATE);
        switch (v.getId()){
            case R.id.registrar:
                if(verifyNewUser()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(new_user.getText().toString(),new_user_password.getText().toString());
                    editor.apply();
                    finish();
                    break;
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
                User newUser = PPCDatabase.getInstance(this).userDao().findByName(username,user_password);
                if(newUser==null){
                    newUser = new User(username,user_password);
                    PPCDatabase.getInstance(this).userDao().insertAll(newUser);
                    Toast.makeText(this, "Se registro con exito al usuario "+username,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }else{
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


