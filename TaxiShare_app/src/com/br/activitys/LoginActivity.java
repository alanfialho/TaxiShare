/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.br.activitys;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.br.entidades.WSTaxiShare;


public class LoginActivity extends Activity {
	Button btnLogin;
	Button btnLinkToRegister;
	EditText loginLogin;
	EditText loginSenha;
	TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		//Pegando os campos da tela
		loginLogin = (EditText) findViewById(R.id.loginLogin);
		loginSenha = (EditText) findViewById(R.id.loginSenha);

		//Botoes e erro
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);

		// Evento do botao de click
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				//Pegando o email e a senha da tela
				String email = loginLogin.getText().toString();
				String password = loginSenha.getText().toString();
				Log.d("Button", "Login");

				WSTaxiShare ws = new WSTaxiShare();
				try 
				{	
					String strJson = ws.login(email, password);
					JSONObject resposta = new JSONObject(strJson);
					Log.i("MEU JSON FODIDAO", resposta + "");

					if (resposta.getInt("errorCode") == 0) {
						loginErrorMsg.setText("Logou!");
						gerarToast("Resposta " + resposta.getString("descricao"));

						// Vai para dashboard
						Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);

						// fecha todas as views antes de ir para dashboard
						dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(dashboard);

						// Fecha a tela de logn
						finish();
					}else{
						// Erro de login
						loginErrorMsg.setText("Usuario ou senha incorreta");
						gerarToast("Resposta " + resposta.getString("descricao"));

					}

				} 
				catch (Exception e) {
					e.printStackTrace();
					gerarToast(e.getMessage());
				}
			}
		});

		// Link para tela de cadastro
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}
}
