
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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.br.entidades.LoginApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;


public class LoginToResetActivity extends Activity {
	Context context;
	AQuery aQuery;

	EditText txtLogin;
	EditText txtResposta;
	EditText txtNovasenha;
	EditText txtNovasenha2;

	TextView lblPergunta;

	Button btnRecuperar;
	Button btnAlterar;
	Button btnCheckAnswer;

	LoginApp loginApp;

	SessionManagement session;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_to_reset); 	

		setAtributes();
		setBtnActions();	
		setInvisiblePart("other");
	}

	private void setBtnActions() {
		// Evento do botao de click
		btnRecuperar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				context = view.getContext();
				CheckLoginTask task = new CheckLoginTask();
				task.fillContext = view.getContext();
				task.execute();
			}
		});


		// Link para tela de cadastro
		btnAlterar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String novaSenha = txtNovasenha.getText().toString();
				String novaSenha2 = txtNovasenha2.getText().toString();
				Log.i("Respota Retornada Taxi", loginApp.getResposta());

				if(novaSenha.equals(novaSenha2)){
					context = view.getContext();
					EditPasswordTask editTask = new EditPasswordTask();
					editTask.fillContext = view.getContext();
					editTask.execute();
				}
				else
					Utils.gerarToast(context, "Senhas precisam ser iguais");
			}
		});



		btnCheckAnswer.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String resposta = txtResposta.getText().toString();
				Log.i("Respota Retornada Taxi", loginApp.getResposta());
				if(resposta.equals(loginApp.getResposta())){
					aQuery.id(R.id.resetPasswordLayout).visible();	
				}
				else
					Utils.gerarToast(context, "Resposta Inválida");
			}
		});
	}

	private void setAtributes(){
		// Session Manager
		session = new SessionManagement(getApplicationContext());

		//Pegando os campos da tela
		txtLogin = (EditText) findViewById(R.id.resetLogin);
		txtResposta = (EditText) findViewById(R.id.resetResposta);
		txtNovasenha = (EditText) findViewById(R.id.resetNovaSenha);
		txtNovasenha2 = (EditText) findViewById(R.id.resetNovaSenha2);
		lblPergunta = (TextView) findViewById(R.id.lblPergunta);

		//Botoes e erro
		btnRecuperar = (Button) findViewById(R.id.btnResetRecuperarSenha);
		btnAlterar = (Button) findViewById(R.id.btnResetAlterarSenha);
		btnCheckAnswer = (Button) findViewById(R.id.resetCheckAnswerBtn);

		aQuery = new AQuery(this);	
	}

	public void setInvisiblePart(String part){
		if(part.equals("login")){
			aQuery.id(R.id.resetLoginLayout).visibility(View.GONE);
			aQuery.id(R.id.resetDataLayout).visible();

		}
		else{
			aQuery.id(R.id.resetLoginLayout).visible();
			aQuery.id(R.id.resetDataLayout).visibility(View.GONE);
		}
	}

	private class CheckLoginTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		Context fillContext;
		String login;


		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, fillContext, "Verificando Login", "Aguarde...");
			login = txtLogin.getText().toString(); 

		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			if(login.length() >3){
				try {
					//Pegando o email e a senha da tela

					WSTaxiShare ws = new WSTaxiShare();

					Log.i("CheckLoginTask  doInBackground taxi", "Login -> " + login);
					response = ws.checkLogin(login);
					Log.i("CheckLoginTask  doInBackground taxi response", response + "");


				} catch (Exception e) {
					Log.i("CheckLoginTask Exception doInBackground taxi", e + "");
					Utils.gerarToast( context, "Não foi possível checar login");
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {

			if(login.length() >3){
				try {

					JSONObject jsonResposta = new JSONObject(strJson);
					Log.i("CheckLoginTask Exception onPostExecute taxi",  "Json resposta -> " + jsonResposta);


					if (jsonResposta.getInt("errorCode") == 1) {
						setInvisiblePart("login");

						JSONObject objetoResposta= jsonResposta.getJSONObject("data");
						Log.i("CheckLoginTask  doInBackground taxi response data", objetoResposta + "");
						Log.i("CheckLoginTask  doInBackground taxi response resposta", objetoResposta.getString("resposta"));

						lblPergunta.setText(objetoResposta.getJSONObject("pergunta").getString("pergunta"));
						loginApp = new LoginApp();
						loginApp.setId(objetoResposta.getInt("id"));
						loginApp.setLogin(objetoResposta.getString("login"));	
						loginApp.setResposta(objetoResposta.getString("resposta"));

					}else{
						// Erro de login
						Utils.gerarToast( context, jsonResposta.getString("descricao"));
					}
				} catch (Exception e) {
					Log.i("Exception on post execute taxi", "Exception -> " + e + " Message->" +e.getMessage());
				}
				
			}else{
				txtLogin.setError("Deve conter no minimo 4 digitos!");
			}
			progress.dismiss();
		}
	}

	private class EditPasswordTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		Context fillContext;
		String resposta;
		boolean checkResposta;
		boolean checkEquals;
		boolean checkEmpty;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, fillContext, "Alterando", "Aguarde...");
			loginApp.setSenha(txtNovasenha.getText().toString());
			resposta = txtResposta.getText().toString();

			String resposta2 = txtResposta.getText().toString();
			String checkNovaSenha = txtNovasenha.getText().toString();
			String checkNovaSenha2 = txtNovasenha2.getText().toString();

			checkResposta = checkEquals = checkEmpty = false;

			if(resposta.equals(loginApp.getResposta()))
				checkResposta = true;
			else
				txtResposta.setError("Resposta Inválida");


			if(!resposta2.isEmpty() && !checkNovaSenha.isEmpty() && !checkNovaSenha2.isEmpty()){
				checkEmpty = true;
			}
			else{
				txtResposta.setError("Campo obrigatório");
				txtNovasenha.setError("Campo obrigatório");
				txtNovasenha2.setError("Campo obrigatório");
			}
			
			if(checkNovaSenha.equals(checkNovaSenha2)){
				checkEquals = true;
			}else{
				txtNovasenha2.setError("Senhas devem ser iguais!");
			}
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			try {

				WSTaxiShare ws = new WSTaxiShare();

				Log.i("EditPasswordTask doInBackground taxi", "Login -> " +  loginApp.getLogin()); 
				Log.i("EditPasswordTask doInBackground taxi", "resposta1 -> " +  loginApp.getResposta() + " resposta2 -> " + resposta);


				if(checkEmpty && checkEquals && checkResposta)
					response = ws.editarSenha(loginApp);

				else{

					if(!checkEquals)
						response = "{errorCode:1, descricao:Senhas precisam ser iguais}";

					if(!checkEmpty)
						response = "{errorCode:1, descricao:Preencha todos os campos}";

					if(!checkResposta)
						response = "{errorCode:1, descricao:Resposta Invalida}";
				}


				Log.i("EditPasswordTask doInBackground taxi response", response + "");


			} catch (Exception e) {
				Log.i("EditPasswordTask doInBackground taxi Exception", e + "");
				Utils.gerarToast( context, "Não Foi possível logar");
			}

			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {

			try {

				JSONObject resposta = new JSONObject(strJson);
				Log.i("EditPasswordTask doInBackground taxi resposta", resposta + "");

				if (resposta.getInt("errorCode") == 0) {
					Utils.gerarToast( context, resposta.getString("descricao"));
					Intent intent = new Intent(getApplicationContext(),
							LoginActivity.class);
					startActivity(intent);
					finish();

				}else{
					// Erro de login
					Utils.gerarToast( context, resposta.getString("descricao"));
				}
			} catch (JSONException e) {
				Log.i("EditPasswordTask onPostExecute taxi Exception", "Exception -> " + e + " Message->" +e.getMessage());
			}
			progress.dismiss();
		}
	}

}
