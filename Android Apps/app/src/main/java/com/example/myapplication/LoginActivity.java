package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity<filters> extends AppCompatActivity {
    public static final String IP_ADDRESS = "com.example.myfirstapp.IP";
    public EditText username_editText, password_editText, editText;

    private static final Pattern IP_ADDRESS_PATTERN
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_editText = (EditText) findViewById(R.id.username);
        password_editText = (EditText) findViewById(R.id.password);
        editText = (EditText) findViewById(R.id.editText);

    }



    public void sendMessage(View view) {
        checkLogin();
    }

    public void checkLogin(){
        String username = username_editText.getText().toString();
        String password = password_editText.getText().toString();
        String ip = editText.getText().toString();
        Matcher matcher = IP_ADDRESS_PATTERN.matcher(ip);
        System.out.println(matcher.matches());
        if (!(matcher.matches())){
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Enter MATLAB API IP address");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if(!(username.equals("gunapriya@gmail.com") && password.equals("8807130337"))){ // Password : 8807130337
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Invalid Credentials");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            username_editText.setText("");
            password_editText.setText("");
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(IP_ADDRESS, ip);
            startActivity(intent);
        }
    }


}
