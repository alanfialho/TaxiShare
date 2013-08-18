package com.br.activitys;

import java.util.HashMap;

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
import android.widget.Toast;

import com.br.entidades.LoginApp;
import com.br.network.WSTaxiShare;
import com.br.sessions.SessionManagement;

public class EditPasswordActivity extends Activity {

	//botoes
	Button btnForgotAlterarSenha;

	//dados pessoa
	EditText forgotSenhaAntiga;
	EditText forgotNovaSenha;
	EditText forgotNovaSenha2;

	SessionManagement session;

	String sessionedPessoaID;
	String sessionedLogin;

	String respostaCheckPassword;
	String novaSenha;
	String senhaAntiga;


	boolean checkEmpty;
	boolean checkPassword;
	boolean checkOldAndNew;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_password);

		session = new SessionManagement(getApplicationContext());

		//Importando os campos da pessoa
		forgotSenhaAntiga = (EditText) findViewById(R.id.forgotSenhaAntiga);
		forgotNovaSenha = (EditText) findViewById(R.id.forgotNovaSenha);
		forgotNovaSenha2 = (EditText) findViewById(R.id.forgotNovaSenha2);

		//Importando botões
		btnForgotAlterarSenha = (Button) findViewById(R.id.btnForgotAlterarSenha);

		//Checa se o usuario esta logado
		session.checkLogin();

		//Recupera os dados do usuario na sessão
		HashMap<String, String> user = session.getUserDetails();

		//Setando id
		sessionedPessoaID = user.get(SessionManagement.KEY_PESSOAID);
		sessionedLogin = user.get(SessionManagement.KEY_LOGIN);

		Log.i("Login Sessao taxi", sessionedLogin + "");
		Log.i("Login Sessao taxi", sessionedPessoaID + "");

		new WSTaxiShare();		

		//Acao do botao cadastrar
		btnForgotAlterarSenha.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {

				CheckPassWordTask task = new CheckPassWordTask();
				task.fillContext = view.getContext();
				task.execute();
			}
		});
	}




	private class CheckPassWordTask extends AsyncTask<String, Void, String> {
		
		ProgressDialog progress;
		Context fillContext;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = new ProgressDialog(fillContext);
			progress.setTitle("Carregando");
			progress.setMessage("Aguarde...");
			progress.show();

		
			checkEmpty = checkPassword = checkOldAndNew = true;

			Log.i("onPreExecute Edit Password taxi", "onPreExecute Edit Password taxi");
			//Pegando as informações de pessoas
			senhaAntiga = forgotSenhaAntiga.getText().toString();
			novaSenha = forgotNovaSenha.getText().toString();
			String novaSenha2 = forgotNovaSenha2.getText().toString();

			if(senhaAntiga.equals("") || novaSenha.equals("") || novaSenha2.equals(""))
				checkEmpty = false;				

			if(!novaSenha.equals(novaSenha2))
				checkPassword = false;

			if(novaSenha.equals(senhaAntiga))
				checkOldAndNew = false;
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			try{
				Log.i("doInBackground checkPassWord taxi", "URL -> " + urls);

				if(checkPassword && checkEmpty && checkOldAndNew){

					Log.i("Abrindo o WS checkpassword taxi", "");
					WSTaxiShare ws = new WSTaxiShare();
					response = ws.login(sessionedLogin, senhaAntiga);
					Log.i("Retorno do login taxi", response);					

				}
				else
					Log.i("Algum check é falso", "Empty -> " + checkEmpty + " Password -> " + checkPassword + " OldAndNew -> " +checkOldAndNew );

			}catch(Exception e){
				gerarToast("Erro ao alterar!");
				Log.i("Excetion check edit password taxi", "Exception -> " + e + " Message -> " + e.getMessage());
			}

			return response;

		}

		@Override
		protected void onPostExecute(String strJson) {

			respostaCheckPassword = strJson;
			Log.i("respostaCheckPassword onPostExecute check", strJson);

			if(!checkEmpty || !checkPassword || !checkOldAndNew)
			{
				if(!checkEmpty)
					gerarToast("Todos os campos são obrigatórios");
				if(!checkPassword)
					gerarToast("Senhas precisam ser iguais");
				if(!checkOldAndNew)
					gerarToast("Digite uma senha diferente da antiga");
			}
			else{
				Log.i("onPostExecute CheckPassword taxi", strJson);
				JSONObject resposta;
				try {
					resposta = new JSONObject(respostaCheckPassword);
					if (resposta.getInt("errorCode") == 0) {

						EditPasswordTask task = new EditPasswordTask();
						task.fillContext = fillContext;
						task.execute();
					}
					else
						gerarToast(resposta.getString("descricao"));
					
				} catch (JSONException e) {
					Log.i("onPostExecute exception taxi", "Exception -> " + e + "Message -> " + e.getMessage());
				}
			}
			progress.dismiss();
		}
	}

	private class EditPasswordTask extends AsyncTask<String, Void, String> {
		
		ProgressDialog progress;
		Context fillContext;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = new ProgressDialog(fillContext);
			progress.setTitle("Salvando Alterações");
			progress.setMessage("Aguarde...");
			progress.show();
			Log.i("onPreExecute Edit Password taxi", "onPreExecute Edit Password taxi");

		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			try{
				Log.i("doInBackground edit password taxi", "URL -> " + urls.toString());

				LoginApp loginApp = new LoginApp();

				WSTaxiShare ws = new WSTaxiShare();
				JSONObject resposta = new JSONObject(respostaCheckPassword);

				if (resposta.getInt("errorCode") == 0) {
					Log.i("Primeira senha ok taxi", "Resposta -> " + resposta.toString());

					loginApp.setId(resposta.getJSONObject("data").getLong("id"));

					loginApp.setLogin(sessionedLogin);
					loginApp.setSenha(novaSenha);

					Log.i("Dados do login taxi", "ID -> " + loginApp.getId() + " LOGIN -> " + loginApp.getLogin() + " NOVA SENHA -> " + loginApp.getSenha() );

					response = ws.editarSenha(loginApp);
					JSONObject resposta2 = new JSONObject(response);

					if(resposta2.getInt("errorCode")== 0){
						Log.i("Resposta da alteracao taxi", resposta.toString());
						gerarToast(resposta2.getString("descricao"));
					}
					else{
						gerarToast(resposta2.getString("descricao"));
					}
				}
			}catch(Exception e){
				Log.i("Excetion doinBack edit password taxi", "Exception -> " + e + " Message -> " + e.getMessage());
				Log.i("Excetion doinBack edit password taxi", "RESPONSE -> " + response);
			}

			Log.i("doinBack edit password taxi", "RESPONSE -> " + response);

			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {
			Log.i("onPostExecute Edit Password taxi", strJson);

			 
			try {
				JSONObject resposta2 = new JSONObject(strJson);
				if(resposta2.getInt("errorCode")== 0){

					//Transfere para a pagina de dashboard
					Intent i = new Intent(getApplicationContext(),
							DashboardActivity.class);
					startActivity(i);
					finish();
				}
				else{
					gerarToast(resposta2.getString("descricao"));
				}

			} catch (JSONException e) {
				Log.i("Excetion onPostExecute edit password taxi", "Exception -> " + e + " Message -> " + e.getMessage());

			}
			progress.dismiss();
		}


	}


	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}

}
