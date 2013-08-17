/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.br.activitys;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.br.network.WSTaxiShare;
import com.br.sessions.SessionManagement;


public class LoginActivity extends Activity {
	Button btnLogin;
	Button btnLinkToRegister;
	EditText loginLogin;
	EditText loginSenha;
	TextView loginErrorMsg;
	SessionManagement session;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Session Manager
		session = new SessionManagement(getApplicationContext()); 

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
				loginTask task = new loginTask();
				task.execute(new String[] { "" });

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

	private class loginTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String response = "";


			try {
				//Pegando o email e a senha da tela
				String login = loginLogin.getText().toString();
				String password = loginSenha.getText().toString();

				WSTaxiShare ws = new WSTaxiShare();

				Log.i("inciando login taxi", "Login -> " + login + " Senha -> " + password);
				response = ws.login(login, password);
				Log.i("String resposta taxi", response + "");


			} catch (Exception e) {
				Log.i("Exception Login taxi", e + "");
				gerarToast("Não Foi possível logar");
				e.printStackTrace();
			}


			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {
			btnLogin.setText("Login");

			try {
				JSONObject resposta = new JSONObject(strJson);
				Log.i("JSON resposta taxi", resposta + "");

				if (resposta.getInt("errorCode") == 0) {
					loginErrorMsg.setText("Logou!");
					gerarToast(resposta.getString("descricao"));

					JSONObject pessoa = resposta.getJSONObject("data").getJSONObject("pessoa");
					Log.i("Testando as paradas aqui", pessoa.toString());
					String pessoaId = resposta.getJSONObject("data").getString("id");
					String nome = pessoa.getString("nome");
					String nick = pessoa.getString("nick");
					String email = pessoa.getString("email");
					String ddd = pessoa.getString("ddd");
					String celular = pessoa.getString("celular");
					String sexo = pessoa.getString("sexo");
					String dataNasc = pessoa.getString("dataNascimento");
					String aaaaaaaaaa = loginLogin.getText().toString();

					Log.i("taxi pessoaid no login", pessoaId);
					Log.i("taxi login no login", aaaaaaaaaa);
					session.createLoginSession(pessoaId, nome,  email,  sexo,  dataNasc,  nick,  ddd,  celular, aaaaaaaaaa);

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
					gerarToast(resposta.getString("descricao"));

				}
			} catch (JSONException e) {
				Log.i("Exception on post execute taxi", "Exception -> " + e + " Message->" +e.getMessage());
				e.printStackTrace();
			}

		}

		protected void onPreExecute() {
			btnLogin.setText("Aguarde...");
		}

	}

}
