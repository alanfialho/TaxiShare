package com.br.activitys;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.br.network.WSTaxiShare;
import com.br.resources.AESCrypt;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;

public class LoginActivity extends Activity {

	//Atributos
	Context context;
	Button btnLogin, btnLinkToRegister, btnLinkToForgetPassword;
	EditText loginLogin, loginSenha;
	SessionManagement session;
	private ImageView img;

	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Seta o login layout
		setContentView(R.layout.login);
		//Inicializa os atributos
		setAtributes();
		//Define as ações dos botões
		setBtnActions();
		//Define o contexto como this
		context = this;
	}

	private void setBtnActions() {
		// Evento do botao login
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				boolean checkLogin = loginLogin.getText().toString().isEmpty();
				boolean checkSenha = loginLogin.getText().toString().isEmpty();
				//Checa se login e senha foram informados
				if(!checkLogin && !checkSenha){
					LoginTask task = new LoginTask();
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
		loginSenha = (EditText) findViewById(R.id.login_txt_senha);
		loginLogin.requestFocus();
		

		//Botoes e erro
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		btnLinkToRegister = (Button) findViewById(R.id.login_btn_register);
		btnLinkToForgetPassword = (Button) findViewById(R.id.login_btn_forget);
		img = (ImageView) findViewById(R.id.imageView1);
		
		img.setImageResource(R.drawable.logopretoamarelo);
	}

	private class LoginTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		String login, password, encriptedPassword;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, context, "Efetuando Login", "Aguarde...");
			//Pegando o email e a senha da tela
			login = loginLogin.getText().toString();
			password = loginSenha.getText().toString();


			try {
				AESCrypt senhaEncripatada = new AESCrypt(password);
				encriptedPassword = senhaEncripatada.encrypt(password);

			} catch (Exception e) {
				Utils.logException("RegisteActivity", "RegisterTask", "onPreExecute", e);
				encriptedPassword = password;
			}	
		}

		@Override
		protected String doInBackground(String... urls) {

			//Inicializa o WS
			WSTaxiShare ws = new WSTaxiShare();
			//Recebe a resposta do login
			String response = ws.login(login, encriptedPassword);

			return response;
		}

		@Override
		protected void onPostExecute(String response) {

			try {
				//Transforma a resposta em json
				JSONObject json = new JSONObject(response);

				//Verifica se deu erro
				if (json.getInt("errorCode") == 0) {
					Utils.gerarToast(context, json.getString("descricao"));

					//Pega os dados da resposta
					JSONObject pessoa = json.getJSONObject("data").getJSONObject("pessoa");
					String pessoaId = json.getJSONObject("data").getString("id");
					String nome = pessoa.getString("nome");
					String email = pessoa.getString("email");
					String ddd = pessoa.getString("ddd");
					String celular = pessoa.getString("celular");
					String sexo = pessoa.getString("sexo");
					String dataNasc = pessoa.getString("dataNascimento");
					String login = loginLogin.getText().toString();

					//Coloca informações na sessão
					session.createLoginSession(pessoaId, nome,  email,  sexo,  dataNasc,  ddd,  celular, login);

					//Inicia a main activity
					Intent intent = new Intent(getApplicationContext(),	MainActitity.class);
					startActivity(intent);
					finish();

				}else{
					// Erro de login
					Utils.gerarToast(context, json.getString("descricao"));
				}
			} catch (JSONException e) {
				Utils.logException("LoginActivity", "LoginTask", "onPostExecute", e);
			}
			progress.dismiss();
		}
	}
}
