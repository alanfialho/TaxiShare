
package com.br.activitys;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;


public class LoginActivity extends Activity {
	Context context;
	
	Button btnLogin;
	Button btnLinkToRegister;
	EditText loginLogin;
	EditText loginSenha;

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

		// Evento do botao de click
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				context = view.getContext();
				loginTask task = new loginTask();
				task.fillContext = view.getContext();
				task.execute();
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



	private class loginTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		Context fillContext;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = new ProgressDialog(fillContext);
			progress.setTitle("Efetuando login");
			progress.setMessage("Aguarde...");
			progress.show();

		}

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
				Utils.gerarToast( context, "Não Foi possível logar");
				e.printStackTrace();
			}


			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {

			try {
				JSONObject resposta = new JSONObject(strJson);
				Log.i("JSON resposta taxi", resposta + "");

				if (resposta.getInt("errorCode") == 0) {
					Utils.gerarToast( context, resposta.getString("descricao"));

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
					
					Intent intent = new Intent(getApplicationContext(),
							MainActitity.class);
					startActivity(intent);
					finish();
				
				
				}else{
					// Erro de login
					Utils.gerarToast( context, resposta.getString("descricao"));
				}
			} catch (JSONException e) {
				Log.i("Exception on post execute taxi", "Exception -> " + e + " Message->" +e.getMessage());
			}
			progress.dismiss();
		}
	}
}
