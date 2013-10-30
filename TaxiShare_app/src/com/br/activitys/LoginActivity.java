package com.br.activitys;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.br.entidades.PerimetroApp;
import com.br.entidades.RotaApp;
import com.br.network.WSTaxiShare;
import com.br.resources.AESCrypt;
import com.br.resources.Utils;
import com.br.sessions.SessionManagement;
import com.br.validation.Rule;
import com.br.validation.Validator;
import com.br.validation.Validator.ValidationListener;
import com.br.validation.annotation.Password;
import com.br.validation.annotation.Regex;
import com.br.validation.annotation.Required;
import com.br.validation.annotation.TextRule;

public class LoginActivity extends Activity {

	//Atributos
	Context context;
	Button btnLogin, btnLinkToRegister, btnLinkToForgetPassword;
	Validator validator;
	
	@Required(order = 1, message="Informe o seu login")
	@TextRule(order=2, minLength=4, message="Deve conter no minimo 4 caracteres")
	@Regex(order=3, pattern="[A-Za-z0-9]+", message="Deve conter apenas letras e numeros")
	EditText loginLogin;
	
	@Required(order = 4, message="Digite a sua senha")
	@Password(order=5)
	@TextRule(order=6, minLength=6, message="Senha invalida")
	EditText loginSenha;
	
	SessionManagement session;
	private ImageView img;
	private boolean doubleBackToExitPressedOnce;

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
		
		//Criando listner
		ValidationListner validationListner = new ValidationListner();

		//Instanciando Validation
		validator = new Validator(this);
		validator.setValidationListener(validationListner);
	}
	
	//Se pressionado o botão "back" 2x, o app encerra
	@Override
    public void onBackPressed() {
        
		if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.gerarToast(this, "Pressione novamente para sair do app");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
             doubleBackToExitPressedOnce=false;   

            }
        }, 2000);
    } 

	private void setBtnActions() {
		// Evento do botao login
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				validator.validate();
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
		img = (ImageView) findViewById(R.id.rote_users_list_item_icon);
		
		img.setImageResource(R.drawable.logopretoamarelo);
	}

	//subclasse que define a acao de validacao
		private class ValidationListner implements ValidationListener {

			//quando a validação estiver correta;
			public void onValidationSucceeded() {
				LoginTask task = new LoginTask();
				task.execute();
			}

			public void onValidationFailed(View failedView, Rule<?> failedRule) {

				//recupera a mensagem de validação
				String message = failedRule.getFailureMessage();

				//Se o erro esteja em um editText
				if (failedView instanceof EditText) {
					//coloca o cursor no campo com erro
					failedView.requestFocus();
					//seta a mensagem de erro
					((EditText) failedView).setError(message);
				} else {
					//Se não, gera um toast com a mensagem
					Utils.gerarToast(failedView.getContext(), message);
				}
			}
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
			//List<PerimetroApp> perimetros = new ArrayList();
			PerimetroApp origem = new PerimetroApp(-22.0000000,-19.000000,-37.000000,-36.00000);
			//PerimetroApp destino = new PerimetroApp(-46.000000,-45.00000,-66.000000,-64.0000000);
			//perimetros.add(origem);
			//perimetros.add(destino);
			
			//try
			//{
			//	List<RotaApp> rotas = ws.getRotasPerimetro(perimetros);
			//}
			//catch(Exception ex){}
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
