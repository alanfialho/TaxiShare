package com.br.fragments;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.br.activitys.R;
import com.br.entidades.LoginApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;

public class EditPasswordFragment extends Fragment {

	Context context;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.edit_password, container, false);

		setAtributes(rootView);
		setBtnActions();
		//Checa se o usuario esta logado
		session.checkLogin();

		Log.i("Login Sessao taxi", "Login -> " + sessionedLogin + " Pessoa ->" + sessionedPessoaID);

		return rootView;
	}

	private void setBtnActions() {
		//Acao do botao cadastrar
		btnForgotAlterarSenha.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				context = view.getContext();
				CheckPassWordTask task = new CheckPassWordTask();
				task.fillContext = view.getContext();
				task.execute();
			}
		});	
	}

	private void setAtributes(View rootView) {
		session = new SessionManagement(rootView.getContext());

		//Importando os campos da pessoa
		forgotSenhaAntiga = (EditText) rootView.findViewById(R.id.forgotSenhaAntiga);
		forgotNovaSenha = (EditText) rootView.findViewById(R.id.forgotNovaSenha);
		forgotNovaSenha2 = (EditText) rootView.findViewById(R.id.forgotNovaSenha2);

		//Importando botões
		btnForgotAlterarSenha = (Button) rootView.findViewById(R.id.btnForgotAlterarSenha);

		//Recupera os dados do usuario na sessão
		HashMap<String, String> user = session.getUserDetails();

		//Setando id
		sessionedPessoaID = user.get(SessionManagement.KEY_PESSOAID);
		sessionedLogin = user.get(SessionManagement.KEY_LOGIN);
	}




	private class CheckPassWordTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		Context fillContext;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, fillContext, "Carregando", "Aguarde...");

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
				Utils.gerarToast(context,"Erro ao alterar!");
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
					Utils.gerarToast( context, "Todos os campos são obrigatórios");
				if(!checkPassword)
					Utils.gerarToast( context, "Senhas precisam ser iguais");
				if(!checkOldAndNew)
					Utils.gerarToast( context, "Digite uma senha diferente da antiga");
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
						Utils.gerarToast( context, resposta.getString("descricao"));

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
			progress = 	Utils.setProgreesDialog(progress, fillContext, "Salvando Alterações", "Aguarde...");
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

					loginApp.setId(resposta.getJSONObject("data").getInt("id"));

					loginApp.setLogin(sessionedLogin);
					loginApp.setSenha(novaSenha);

					Log.i("Dados do login taxi", "ID -> " + loginApp.getId() + " LOGIN -> " + loginApp.getLogin() + " NOVA SENHA -> " + loginApp.getSenha() );

					response = ws.editarSenha(loginApp);
					JSONObject resposta2 = new JSONObject(response);

					if(resposta2.getInt("errorCode")== 0){
						Log.i("Resposta da alteracao taxi", resposta.toString());
						Utils.gerarToast( context, resposta2.getString("descricao"));
					}
					else{
						Utils.gerarToast( context, resposta2.getString("descricao"));
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

					//					//Transfere para a pagina de dashboard
					//					Intent i = new Intent(getApplicationContext(),
					//							DashboardActivity.class);
					//					startActivity(i);
					//					finish();
				}
				else{
					Utils.gerarToast( context, resposta2.getString("descricao"));
				}

			} catch (JSONException e) {
				Log.i("Excetion onPostExecute edit password taxi", "Exception -> " + e + " Message -> " + e.getMessage());

			}
			progress.dismiss();
		}


	}




}
