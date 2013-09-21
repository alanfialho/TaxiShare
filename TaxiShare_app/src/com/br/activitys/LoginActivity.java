
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
	Button btnLinkToForgetPassword;
	EditText loginLogin;
	EditText loginSenha;

	SessionManagement session;
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setAtributes();
		setBtnActions();
		context = this;
	}

	private void setBtnActions() {
		// Evento do botao login
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				context = view.getContext();

				boolean checkLogin = loginLogin.getText().toString().isEmpty();
				boolean checkSenha = loginLogin.getText().toString().isEmpty();
				if(!checkLogin || !checkSenha){
					loginTask task = new loginTask();
					task.execute();
				}
				else{
					Utils.gerarToast(context, "Preencha Login e Senha!");
				}
			}
		});

		// Link para tela de cadastro
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(intent);
			}
		});

		//Link para tela de recuperação de senha
		btnLinkToForgetPassword.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),LoginToResetActivity.class);
				startActivity(i);
			}
		});
	}

	public void setAtributes(){
		// Session Manager
		session = new SessionManagement(getApplicationContext()); 

		//Pegando os campos da tela
		loginLogin = (EditText) findViewById(R.id.login_txt_login);
		loginSenha = (EditText) findViewById(R.id.login_senha);

		//Botoes e erro
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		btnLinkToRegister = (Button) findViewById(R.id.login_btn_register);
		btnLinkToForgetPassword = (Button) findViewById(R.id.login_btn_forget);
	}

	private class loginTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, context, "Efetuando Login", "Aguarde...");
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
					String email = pessoa.getString("email");
					String ddd = pessoa.getString("ddd");
					String celular = pessoa.getString("celular");
					String sexo = pessoa.getString("sexo");
					String dataNasc = pessoa.getString("dataNascimento");
					String login = loginLogin.getText().toString();

					Log.i("taxi pessoaid no login", pessoaId);
					Log.i("taxi login no login", login);
					session.createLoginSession(pessoaId, nome,  email,  sexo,  dataNasc,  ddd,  celular, login);

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
