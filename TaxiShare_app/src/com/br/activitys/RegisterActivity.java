package com.br.activitys;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.br.entidades.LoginApp;
import com.br.entidades.PessoaApp;
import com.br.entidades.PerguntaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.Utils;
import com.br.validation.Rule;
import com.br.validation.Validator;
import com.br.validation.Validator.ValidationListener;
import com.br.validation.annotation.ConfirmPassword;
import com.br.validation.annotation.Email;
import com.br.validation.annotation.Password;
import com.br.validation.annotation.Regex;
import com.br.validation.annotation.Required;
import com.br.validation.annotation.TextRule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;

public class RegisterActivity extends Activity {
	Context context;
	// botoes
	Button btnCadastrar, btnLinkToLogin;

	Validator validator;

	// Campos
	@Required(order = 1, message="Campo obrigatorio")
	@Regex(order=2, pattern="[a-z A-Z]+", message="Deve conter apenas letras")
	EditText textNome;

	@Required(order = 3, message="Campo obrigatorio")
	@Email(order =4, message="E-mail Inválido")
	EditText textEmail;

	@Required(order = 5, message="Campo obrigatorio")
	@Password(order=6)
	@TextRule(order=7, minLength=6, message="Deve conter no minimo 6 caracteres")
	EditText textSenha; 

	@Required(order = 8, message="Campo obrigatorio")
	@ConfirmPassword(order=9, message="Senhas precisam ser iguais")
	EditText textSenha2;

	@Required(order = 10, message="Campo obrigatorio")
	@TextRule(order=11, minLength=8, message="Deve conter no minimo 8 digitos")
	EditText textCelular; 

	@Required(order = 12, message="Campo obrigatorio")
	@TextRule(order=13, minLength=2, message="Deve conter 2 digitos")
	EditText textDDD; 

	@Required(order = 14, message="Campo obrigatorio")
	@TextRule(order=15, minLength=4, message="Deve conter no minimo 4 caracteres")
	@Regex(order=16, pattern="[A-Za-z0-9]+", message="Deve conter apenas letras e numeros")
	EditText textLogin; 

	@Required(order = 17, message="Campo obrigatorio")
	@TextRule(order=18, minLength=4, message="Deve conter no minimo 4 caracteres")
	@Regex(order=19, pattern="[A-Za-z0-9 ]+", message="Deve conter apenas letras e numeros")
	EditText textResposta;

	Spinner spinnerSexo;
	DatePicker dateDataNascimento;
	Spinner spinnerPergunta;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		//Criando listner
		ValidationListner validationListner = new ValidationListner();
		validationListner.validationContext = this;
		
		//Instanciando Validation
		validator = new Validator(this);
		validator.setValidationListener(validationListner);
	
		setAtributes();
		setBtnActions();
	}

	private void setBtnActions() {
		// Acao do botao cadastrar
		btnCadastrar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {			

				validator.validate();
			}
		});

		// Link para Login
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});		
	}

	private void setAtributes() {
		// Importando os campos da pessoa
		textNome = (EditText) findViewById(R.id.textNome);
		textEmail = (EditText) findViewById(R.id.textEmail);
		textSenha = (EditText) findViewById(R.id.textSenha);
		textSenha2 = (EditText) findViewById(R.id.textSenha2);
		textDDD = (EditText) findViewById(R.id.textDDD);
		spinnerSexo = (Spinner) findViewById(R.id.textSexo);
		textCelular = (EditText) findViewById(R.id.textCelular);
		dateDataNascimento = (DatePicker) findViewById(R.id.dateDataNascimemto);

		// Importando os campos do login
		spinnerPergunta = (Spinner) findViewById(R.id.textPergunta);
		textLogin = (EditText) findViewById(R.id.textLogin);
		textResposta = (EditText) findViewById(R.id.textResposta);

		// Importando botões
		btnCadastrar = (Button) findViewById(R.id.lblPergunta);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


		// Instanciando WS

		try {

			// Definindo Lista de sexos
			List<String> sexos = new ArrayList<String>();
			sexos.add("Masculino");
			sexos.add("Feminino");

			// Colocando lista de sexos no spinner
			ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexos);
			adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerSexo.setAdapter(adapterSexo);		


			FillQuestionSpinner questionTask = new FillQuestionSpinner();
			questionTask.fillContext = this;
			questionTask.execute();

		} catch (Exception e) {
			Log.i("Preenchendo Sppiners Exception taxi", "Exceptiom -> " + e + " || Message -> " + e.getMessage());
		}
	}

	
	private class ValidationListner implements ValidationListener {
		Context validationContext;

		public void onValidationSucceeded() {
			CheckLoginTask task = new CheckLoginTask();
			task.fillContext = validationContext;			
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

	private class CheckLoginTask extends AsyncTask<String, Void, String> {
		Context fillContext;
		ProgressDialog progress;
		String login;

		protected void onPreExecute() {
			progress = Utils.setProgreesDialog(progress, fillContext, "Checando Login", "Aguarde...");
			//Pega o texto do login
			login = textLogin.getText().toString().trim();
		}

		@Override
		protected String doInBackground(String... urls) {

			String response = "";

			try {
				//Inicia o ws e checa se o login esta disponivel
				WSTaxiShare ws = new WSTaxiShare();
				response = ws.checkLogin(login);
				Log.i("Response CheckLoginTask doInBackground taxi", response);

			}catch (Exception e){
				Log.i("Exception CheckLoginTask doInBackground taxi ", "Execption -> " + e + " || Message -> " +e.getMessage());
			}

			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {
			
			try {
				//Cria JSON com a resposta do WS
				JSONObject checkLoginJSON = new JSONObject(strJson);

				//Checa se existe erro
				if (checkLoginJSON.getInt("errorCode") == 0) {
					//Caso esteja tudo certo, cria uma task para efeutar o cadastro.
					RegisterTask registerTask = new RegisterTask();
					registerTask.fillContext = fillContext;
					registerTask.execute();

				}
				else{
					Utils.gerarToast(fillContext, checkLoginJSON.getString("descricao"));
				}

			} catch (JSONException e) {
				Log.i("Exception CheckLoginTask onPostExecute taxi ", "Execption -> " + e + " || Message -> " +e.getMessage());
				Utils.gerarToast(context, "Erro ao checar login!");

			}

			//Fecha o alert de carregando
			progress.dismiss();
		}
	}

	private class RegisterTask extends AsyncTask<String, Void, String> {

		boolean checkSenha = true;
		boolean checkEmpty = true;
		boolean checkEmail = true;
		boolean checkPhoneNumber = true;
		boolean validate = false;
		String message = "";
		LoginApp loginApp; 
		ProgressDialog progress;
		Context fillContext;

		protected void onPreExecute() {

			progress = Utils.setProgreesDialog(progress, fillContext, "Registrando", "Aguarde...");

			// Pegando as informações de pessoas
			String nome = textNome.getText().toString().trim();
			String email = textEmail.getText().toString().trim();
			String ddd = textDDD.getText().toString().trim();
			String celular = textCelular.getText().toString().trim();

			// Montando data de nascimento
			int year = dateDataNascimento.getYear();
			int month = dateDataNascimento.getMonth();
			int day = dateDataNascimento.getDayOfMonth();
			String dataNascimento = day + "/" + month + "/" + year;
			String sexo = spinnerSexo.getSelectedItem().toString().trim();

			// Pegando as informações do login
			int pergunta = spinnerPergunta.getSelectedItemPosition();
			String login = textLogin.getText().toString().trim();
			String resposta = textResposta.getText().toString().trim();
			String senha = textSenha.getText().toString();
			String senha2 = textSenha2.getText().toString();

			//Checa se as senhas são iguais
			if (!senha.equals(senha2) || senha.equals("") || senha2.equals(""))
				checkSenha = false;

			//Checa se não há campos vazios
			if (nome.equals("") || email.equals("") || ddd.equals("") || celular.equals("") || resposta.equals("") || login.equals("") || senha.equals("")) 
				checkEmpty = false;

			//checa se o email é válido
			if(!Utils.isValidEmail(email))
				checkEmail = false;

			if(celular.length() < 8)
				checkPhoneNumber = false;

			//Checa se tudo esta ok
			if(checkSenha && checkEmpty && checkEmail && checkPhoneNumber){
				validate = true;
			}
			else{
				Log.i("Erro Validacao Registro taxi", "Senha-> "+ checkSenha + "|| Empty -> " + checkEmpty + " || Email -> " + checkEmail);

				if(!checkSenha){
					Log.i("Erro Validacao", "Check-Senha");
					message+= "Senhas precisam ser iguais! \n";
				}

				if(!checkEmpty){
					Log.i("Erro Validacao", "Check-Vazio");
					message+= "Preencha os campos vazios! \n";
				}

				if(!checkEmail){
					Log.i("Erro Validacaop", "Check-Email");
					message+= "Email inválido! \n";
				}
				if(!checkPhoneNumber){
					Log.i("Erro Validacao", "Check-Phone");
					message+= "Celular inválido!";
				}

				validate = false;
			}

			if (validate) {
				// Criando objeto pessoa e objeto login
				PessoaApp pessoaApp = new PessoaApp();
				loginApp = new LoginApp();
				PerguntaApp perguntaApp = new PerguntaApp();

				// Definindo as paradas em pessoa
				pessoaApp.setNome(nome);
				pessoaApp.setCelular(celular);
				pessoaApp.setDataNascimento(dataNascimento);
				pessoaApp.setDdd(ddd);
				pessoaApp.setEmail(email);
				pessoaApp.setSexo(sexo);

				// Definindo as paradas em pergunta
				perguntaApp.setId(pergunta);

				// Definindo as paradas em login
				loginApp.setLogin(login);
				loginApp.setSenha(senha);
				loginApp.setResposta(resposta);

				// Setando a pergunta no login
				loginApp.setPergunta(perguntaApp);

				// Setando pessoa no login
				loginApp.setPessoa(pessoaApp);
			}
			else
				Utils.gerarToast(context, message);
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			if(validate){
				try {
					WSTaxiShare ws = new WSTaxiShare();
					response = ws.cadastrarLogin(loginApp);		

				} catch (Exception e) {
					Utils.gerarToast(fillContext, "Erro ao cadastrar!");
					Log.i("Exception RegisterTask doInBackground taxi", "Exception -> " + e + " || Message -> " + e.getMessage());
				}	
			}

			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {
			// Transforma a string em um objeto JSON
			try {
				JSONObject cadastroLoginJSON = new JSONObject(strJson);
				// Checa se o cadastro deu certo
				if (cadastroLoginJSON.getInt("errorCode") == 0) {
					Utils.gerarToast(fillContext, "Cadastro efetuado!");

					// Retorna para tela de login
					Intent i = new Intent(getApplicationContext(), LoginActivity.class);
					startActivity(i);
					finish();
				} else
					Utils.gerarToast(fillContext, cadastroLoginJSON.getString("descricao"));

			} catch (JSONException e) {
				Log.i("Exception RegisterTask onPostExecute taxi", "Exception -> " + e + " || Message -> " + e.getMessage());
				Utils.gerarToast(fillContext, "Erro ao cadastrar!");

			}

			progress.dismiss();
		}
	}

	private class FillQuestionSpinner extends AsyncTask<String, Void, String> {
		Context fillContext;
		List <String> perguntas;
		ProgressDialog progress;

		protected void onPreExecute() {
			perguntas = new ArrayList<String>();
			progress = Utils.setProgreesDialog(progress, fillContext, "Carregando", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {

			String response = "";

			try {
				WSTaxiShare ws = new WSTaxiShare();
				List<PerguntaApp> jsonPerguntas = ws.getPerguntas();

				//Populando lista de Strings de Pertuntas
				for (int i = 0; i < 4; i++) {
					String opcao = jsonPerguntas.get(i).getId() + " - " + jsonPerguntas.get(i).getPergunta();
					perguntas.add(opcao);
				}
			} catch (Exception e) {
				Log.i("Exeception doInBackground fillQuestionSpinner taxi", "Exception -> " + e + "  || Message: -> " + e.getMessage());			
			}

			return response;
		}

		@Override
		protected void onPostExecute(String strJson) {
			// Colocando lista de perguntas no spinner
			try{
				ArrayAdapter<String> adapterPerguntas = new ArrayAdapter<String>(fillContext, android.R.layout.simple_spinner_item, perguntas);
				adapterPerguntas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerPergunta.setAdapter(adapterPerguntas);	
			}
			catch(Exception e){
				Log.i("Exeception onPostExecute fillQuestionSpinner taxi", "Exception -> " + e + "  || Message: -> " + e.getMessage());			
			}			

			progress.dismiss();
		}
	}
}
