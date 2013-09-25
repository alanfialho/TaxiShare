package com.br.fragments;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
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
import com.br.validation.Rule;
import com.br.validation.Validator;
import com.br.validation.Validator.ValidationListener;
import com.br.validation.annotation.ConfirmPassword;
import com.br.validation.annotation.Password;
import com.br.validation.annotation.Required;
import com.br.validation.annotation.TextRule;

public class EditPasswordFragment extends Fragment {

	Context context;
	Validator validator;

	//botoes
	Button btnForgotAlterarSenha;

	//dados pessoa

	@Required(order = 1, message="Campo obrigatorio")
	@TextRule(order=3, minLength=6, message="Deve conter no minimo 6 caracteres")
	EditText forgotSenhaAntiga;	

	@Required(order = 4, message="Campo obrigatorio")
	@Password(order=5)
	@TextRule(order=6, minLength=6, message="Deve conter no minimo 6 caracteres")
	EditText forgotNovaSenha;

	@Required(order = 7, message="Campo obrigatorio")
	@ConfirmPassword(order=8, message="Senhas precisam ser iguais")
	EditText forgotNovaSenha2;

	SessionManagement session;

	String sessionedPessoaID, sessionedLogin, respostaCheckPassword, novaSenha, senhaAntiga;
	boolean checkEmpty, checkPassword, checkOldAndNew;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.edit_password, container, false);
		context = getActivity();

		setAtributes(rootView);
		setBtnActions();
		//Checa se o usuario esta logado

		//Criando listner
		ValidationListner validationListner = new ValidationListner();

		//Instanciando Validation
		validator = new Validator(this);
		validator.setValidationListener(validationListner);

		session.checkLogin();

		Log.i("Login Sessao taxi", "Login -> " + sessionedLogin + " Pessoa ->" + sessionedPessoaID);

		return rootView;
	}

	private void setBtnActions() {
		//Acao do botao cadastrar
		btnForgotAlterarSenha.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				context = view.getContext();				
				validator.validate();
			}
		});	
	}

	private void setAtributes(View rootView) {
		session = new SessionManagement(rootView.getContext());

		//Importando os campos da pessoa
		forgotSenhaAntiga = (EditText) rootView.findViewById(R.id.edit_pass_senha_antiga);
		forgotNovaSenha = (EditText) rootView.findViewById(R.id.edit_pass_nova_senha);
		forgotNovaSenha2 = (EditText) rootView.findViewById(R.id.edit_pass_nova_senha_2);

		//Importando botões
		btnForgotAlterarSenha = (Button) rootView.findViewById(R.id.edit_pass_btn_alterar);

		//Recupera os dados do usuario na sessão
		HashMap<String, String> user = session.getUserDetails();

		//Setando id
		sessionedPessoaID = user.get(SessionManagement.KEY_PESSOAID);
		sessionedLogin = user.get(SessionManagement.KEY_LOGIN);
	}

	private class CheckPassWordTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, context, "Carregando", "Aguarde...");

			checkEmpty = checkPassword = checkOldAndNew = true;

			Log.i("onPreExecute Edit Password taxi", "onPreExecute Edit Password taxi");
			//Pegando as informações de pessoas
			senhaAntiga = forgotSenhaAntiga.getText().toString().trim();
			novaSenha = forgotNovaSenha.getText().toString();

			if(senhaAntiga.isEmpty() || senhaAntiga.equals(" ")){
				checkEmpty = false;
			}

			if(novaSenha.equals(senhaAntiga)){
				checkOldAndNew = false;
			}

		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			try{
				Log.i("doInBackground checkPassWord taxi", "URL -> " + urls);

				if(checkEmpty && checkOldAndNew){

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
		protected void onPostExecute(String response) {

			respostaCheckPassword = response;
			Log.i("respostaCheckPassword onPostExecute check", response);

			if(!checkEmpty || !checkPassword || !checkOldAndNew)
			{
				if(!checkEmpty){
					forgotSenhaAntiga.setError("Campo Obrigatório");
					forgotSenhaAntiga.requestFocus();
				}
					
				if(!checkOldAndNew)
					forgotNovaSenha.setError("Nova senha deve ser diferente da senha antiga");
			}
			else{
				Log.i("onPostExecute CheckPassword taxi", response);
				JSONObject resposta;
				try {
					resposta = new JSONObject(respostaCheckPassword);
					if (resposta.getInt("errorCode") == 0) {

						EditPasswordTask task = new EditPasswordTask();
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

		protected void onPreExecute() {
			//Inica a popup de load
			progress = 	Utils.setProgreesDialog(progress, context, "Salvando Alterações", "Aguarde...");
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
		protected void onPostExecute(String response) {
			Log.i("onPostExecute Edit Password taxi", response);


			try {
				JSONObject resposta2 = new JSONObject(response);
				if(resposta2.getInt("errorCode")== 0){
					Utils.gerarToast( context, resposta2.getString("descricao"));

					Fragment fragment = new SearchRoteFragment();
					Bundle args = new Bundle();
					fragment.setArguments(args);

					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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

	private class ValidationListner implements ValidationListener {

		public void onValidationSucceeded() {
			CheckPassWordTask task = new CheckPassWordTask();
			task.execute();
		}

		public void onValidationFailed(View failedView, Rule<?> failedRule) {

			String message = failedRule.getFailureMessage();

			if (failedView instanceof EditText) {
				failedView.requestFocus();
				((EditText) failedView).setError(message);
			} else {
				Utils.gerarToast(failedView.getContext(), message);
			}
		}
	}




}
