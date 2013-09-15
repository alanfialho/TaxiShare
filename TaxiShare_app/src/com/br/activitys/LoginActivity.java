
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
					task.fillContext = view.getContext();
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
		loginLogin = (EditText) findViewById(R.id.loginLogin);
		loginSenha = (EditText) findViewById(R.id.loginSenha);

		//Botoes e erro
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		btnLinkToForgetPassword = (Button) findViewById(R.id.btnLinkToForgetPassword);
	}

	private class loginTask extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		Context fillContext;
		protected void onPreExecute() {
			//Inica a popup de load
			progress = Utils.setProgreesDialog(progress, fillContext, "Efetuando Login", "Aguarde...");
		}

		@Override
		protected String doInBackground(String... urls) {
			String response = "";

			try {
				//Pegando o email e a senha da tela
				String login = loginLogin.getText().toString();
				String password = loginSenha.getText().toString();
				WSTaxiShare ws = new WSTaxiShare();
<<<<<<< HEAD
=======
				//************************************************
				//teste rota
				
				//*************************************************
				//Cadastro da rota
				//*************************************************
//				EnderecoApp e1 = new EnderecoApp("rua FW","Sao miguel2", 444,"são paulo", "sao paulo", "sp", "brasil", "049876542", 'O',"02928765","098653098");
//		        EnderecoApp e2 = new EnderecoApp("rua FW","itaquera2", 444,"são paulo", "sao paulo", "sp", "brasil", "098763838", 'D',"0987453782","948764325");
//		        EnderecoApp e3 = new EnderecoApp("rua FW","patriarca2", 444,"são paulo", "sao paulo", "sp", "brasil", "098763566", 'D',"77654339","4362892291");
//		        
//		        List<EnderecoApp> lstEnd  = new ArrayList();
//		        lstEnd.add(e1);
//		        lstEnd.add(e2);
//		        lstEnd.add(e3);
//		        
//		        List<LoginApp> lstUsuarios = new ArrayList();
//		        LoginApp usuario = new LoginApp();
//		        usuario.setId(9);
//		        lstUsuarios.add(usuario);
//				
//				RotaApp rota = new RotaApp("12/04/13",true,Short.parseShort("1"),lstEnd,lstUsuarios);
//				response = ws.criarRota(rota);
				
				//*****************************************************
				//fim cadastro da rota
				//*****************************************************
				//fim teste rota
				//*****************************************************
				
				//*****************************************
				//Participar da rota
				//*****************************************
//				response = ws.participarRota(1, 11);
				
				
				
				//*****************************************
				//Fim participar da rota
				//*****************************************
				
>>>>>>> ca4e16f3b21d22ceb14f13cf44c19e7710f1c721
				Log.i("inciando login taxi", "Login -> " + login + " Senha -> " + password);
				response = ws.login(login, password);
				Log.i("String resposta taxi", response + "");


			} catch (Exception e) {
				Log.i("Exception Login taxi", e + "");
				Utils.gerarToast( fillContext, "Não Foi possível logar");
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
					Utils.gerarToast( fillContext, resposta.getString("descricao"));

					JSONObject pessoa = resposta.getJSONObject("data").getJSONObject("pessoa");
					Log.i("Testando as paradas aqui", pessoa.toString());
					String pessoaId = resposta.getJSONObject("data").getString("id");
					String nome = pessoa.getString("nome");
					String email = pessoa.getString("email");
					String ddd = pessoa.getString("ddd");
					String celular = pessoa.getString("celular");
					String sexo = pessoa.getString("sexo");
					String dataNasc = pessoa.getString("dataNascimento");
					String aaaaaaaaaa = loginLogin.getText().toString();

					Log.i("taxi pessoaid no login", pessoaId);
					Log.i("taxi login no login", aaaaaaaaaa);
					session.createLoginSession(pessoaId, nome,  email,  sexo,  dataNasc,  ddd,  celular, aaaaaaaaaa);

					Intent intent = new Intent(getApplicationContext(),
							MainActitity.class);
					startActivity(intent);
					finish();


				}else{
					// Erro de login
					Utils.gerarToast( fillContext, resposta.getString("descricao"));
				}
			} catch (JSONException e) {
				Log.i("Exception on post execute taxi", "Exception -> " + e + " Message->" +e.getMessage());
			}
			progress.dismiss();
		}
	}
}
