
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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.br.entidades.LoginApp;
import com.br.network.WSTaxiShare;
import com.br.resources.AESCrypt;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;
import com.br.validation.Rule;
import com.br.validation.Validator;
import com.br.validation.Validator.ValidationListener;
import com.br.validation.annotation.ConfirmPassword;
import com.br.validation.annotation.Password;
import com.br.validation.annotation.Required;
import com.br.validation.annotation.TextRule;


public class LoginToResetActivity extends Activity {
	Context context;
	AQuery aQuery;
	Validator validator;

	EditText txtLogin;
	EditText txtResposta;

	@Required(order = 1, message="Campo obrigatorio")
	@Password(order=2)
	@TextRule(order=3, minLength=6, message="Deve conter no minimo 6 caracteres")
	EditText txtNovasenha; 

	@Required(order = 4, message="Campo obrigatorio")
	@ConfirmPassword(order=5, message="Senhas precisam ser iguais")
	EditText txtNovasenha2;

	TextView lblPergunta;

	Button btnRecuperar, btnAlterar, btnCheckAnswer;

	LoginApp loginApp;

	SessionManagement session;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_to_reset);
		context = this;		
		//Criando listner
		ValidationListner validationListner = new ValidationListner();

		//Instanciando Validation
		validator = new Validator(this);
		validator.setValidationListener(validationListner);

		setAtributes();
		setBtnActions();	
		setInvisiblePart("other");
	}

	private void setBtnActions() {
		// Evento do botao de click
		btnRecuperar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				CheckLoginTask task = new CheckLoginTask();
				task.execute();
			}
		});

		// Link para tela de cadastro
		btnAlterar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				validator.validate();
			}
		});

		btnCheckAnswer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String resposta = txtResposta.getText().toString();
				if(resposta.equalsIgnoreCase(loginApp.getResposta())){
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
		txtLogin = (EditText) findViewById(R.id.reset_pass_txt_login);
		txtResposta = (EditText) findViewById(R.id.reset_pass_txt_resposta);
		txtNovasenha = (EditText) findViewById(R.id.reset_pass_nova_senha);
		txtNovasenha2 = (EditText) findViewById(R.id.reset_pass_nova_senha2);
		lblPergunta = (TextView) findViewById(R.id.reset_pass_lbl_pergunta);

		//Botoes e erro
		btnRecuperar = (Button) findViewById(R.id.reset_pass_btn_recuperar);
		btnAlterar = (Button) findViewById(R.id.reset_pass_btn_alterar2);
		btnCheckAnswer = (Button) findViewById(R.id.reset_pass_btn_checar);

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

	private class ValidationListner implements ValidationListener {

		public void onValidationSucceeded() {
			EditPasswordTask editTask = new EditPasswordTask();			
			editTask.execute();		
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

	private class CheckLoginTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		String login;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, context, "Verificando Login", "Aguarde...");
			login = txtLogin.getText().toString(); 
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			if(login.length() >3){
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.checkLogin(login);
			}
			return response;
		}

		@Override
		protected void onPostExecute(String response) {

			if(login.length() >3){
				try {
					JSONObject jsonResposta = new JSONObject(response);

					if (jsonResposta.getInt("errorCode") == 1) {
						setInvisiblePart("login");

						JSONObject objetoResposta= jsonResposta.getJSONObject("data");

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
					Utils.logException("LoginToResetActivity", "CheckLoginTask", "onPostExecute", e);
				}

			}else{
				txtLogin.setError("Deve conter no minimo 4 digitos!");
			}
			progress.dismiss();
		}
	}

	private class EditPasswordTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		String resposta;
		boolean checkResposta, checkEquals,checkEmpty;

		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, context, "Alterando", "Aguarde...");
			String senha = txtNovasenha.getText().toString();
			
			try {
				AESCrypt senhaEncripatada = new AESCrypt(senha);
				loginApp.setSenha(senhaEncripatada.encrypt(senha));

			} catch (Exception e) {
				Utils.logException("RegisteActivity", "RegisterTask", "onPreExecute", e);
				loginApp.setSenha(senha);

			}
			
			resposta = txtResposta.getText().toString();

			String resposta2 = txtResposta.getText().toString();
			String checkNovaSenha = txtNovasenha.getText().toString();
			String checkNovaSenha2 = txtNovasenha2.getText().toString();

			checkResposta = checkEquals = checkEmpty = false;

			if(resposta.equalsIgnoreCase(loginApp.getResposta()))
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

			} catch (Exception e) {
				Utils.logException("LoginToResetActivity", "EditPasswordTask", "onPostExecute", e);
				Utils.gerarToast( context, "Não Foi possível logar");
			}

			return response;
		}

		@Override
		protected void onPostExecute(String response) {

			try {

				JSONObject resposta = new JSONObject(response);

				if (resposta.getInt("errorCode") == 0) {
					Utils.gerarToast( context, resposta.getString("descricao"));
					Intent intent = new Intent(getApplicationContext(),	LoginActivity.class);
					startActivity(intent);
					finish();

				}else{
					// Erro de login
					Utils.gerarToast( context, resposta.getString("descricao"));
				}
			} catch (JSONException e) {
				Utils.logException("LoginToResetActivity", "EditPasswordTask", "onPostExecute", e);
			}
			progress.dismiss();
		}
	}
}
