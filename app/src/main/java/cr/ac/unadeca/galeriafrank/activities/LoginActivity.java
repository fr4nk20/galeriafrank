package cr.ac.unadeca.galeriafrank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;


import cr.ac.unadeca.galeriafrank.R;
import cr.ac.unadeca.galeriafrank.database.Models.Users;
import cr.ac.unadeca.galeriafrank.database.Models.Users_Table;
import cr.ac.unadeca.galeriafrank.util.Functions;
import cr.ac.unadeca.galeriafrank.util.Session;

/**
 * Created by pc on 26/3/2018.
 */


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Session session;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        image = findViewById(R.id.imageLogin);

        session = new Session(this);
        if(session.isLoggedIn()){
            goToMain();
        }
        Button iniciar = findViewById(R.id.login);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userna = username.getText().toString();
                String passwo = password.getText().toString();
                if (userna.isEmpty() || passwo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginerror), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Login(userna, passwo);
                }
            }
        });


        Button registrar = findViewById(R.id.register);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegistrar();
            }
        });


        Functions.loadImage("http://reconciliasian.com/wp-content/uploads/2018/03/gallery-perfect-7-piece-wide-frame-set-modern-picture-frames-with-prepare.jpg", image, this);
    }

    private  void goToMain(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private  void goToRegistrar(){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
        finish();
    }

    private boolean Login(String username, String password){
        boolean isLoggedIn= SQLite.selectCountOf().from(Users.class).where(Users_Table.username.eq(username)).and(Users_Table.password.eq(Functions.md5(password))).hasData();
        Users user = SQLite.select().from(Users.class).where(Users_Table.username.eq(username)).and(Users_Table.password.eq(Functions.md5(password))).querySingle();

        if (isLoggedIn){
            session.createLoginSession(user.id,user.nombre, user.roll);
            goToMain();
        }else{
            Toast.makeText(this, getResources().getString(R.string.tryregister), Toast.LENGTH_LONG).show();
        }
        return isLoggedIn;
    }

}


