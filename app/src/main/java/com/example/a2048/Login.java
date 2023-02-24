package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.a2048.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private boolean login;
    private SqlData sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        login=true;

        sql = new SqlData(this);
        Intent intent = new Intent(Login.this,MainActivity.class);

        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login) {
                    String usuario=binding.e1.getText().toString();
                    String contra=binding.e2.getText().toString();

                    Usuario u =sql.getUsuario(usuario);
                    if (u.getUsuario().equals(usuario) && u.getContra().equals(contra)) {
                        intent.putExtra("usuario",usuario);
                        startActivity(intent);
                    }

                }else{
                    login=true;
                    binding.t3.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login) {
                    login=false;
                    binding.t3.setVisibility(View.VISIBLE);
                }else{
                    String usuario=binding.e1.getText().toString();
                    String contra=binding.e2.getText().toString();
                    String contra2=binding.e3.getText().toString();

                    if (contra.equals(contra2)) {
                        if (sql.getUsuario(usuario).getUsuario()==null) {
                            sql.addUsuario(usuario,contra);
                            intent.putExtra("usuario",usuario);
                            startActivity(intent);
                        }else {
                            Toast.makeText(Login.this,"Ya existe el usuario",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        binding.t3.setHelperText("Error de contraseña");
                        binding.t3.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    }

                }
            }
        });
    }
}