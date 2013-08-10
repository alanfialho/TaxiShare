package com.br.activitys;

import java.util.HashMap;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.entidades.LoginApp;
import com.br.entidades.WSTaxiShare;
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
				//Pegando as informações de pessoas
				String senhaAntiga = forgotSenhaAntiga.getText().toString();
				String novaSenha = forgotNovaSenha.getText().toString();
				String novaSenha2 = forgotNovaSenha2.getText().toString();

				boolean checkEmpty = true;
				boolean checkPassword = true;
				boolean checkOldAndNew = true;

				if(senhaAntiga.equals("") || novaSenha.equals("") || novaSenha2.equals(""))
					checkEmpty = false;				

				if(!novaSenha.equals(novaSenha2))
					checkPassword = false;

				if(novaSenha.equals(senhaAntiga))
					checkOldAndNew = false;

				if(checkPassword && checkEmpty && checkOldAndNew){
					try 
					{							
						LoginApp loginApp = new LoginApp();

						WSTaxiShare ws = new WSTaxiShare();
						String strJson = ws.login(sessionedLogin, senhaAntiga);
						JSONObject resposta = new JSONObject(strJson);


						if (resposta.getInt("errorCode") == 0) {
							Log.i("Entrou na primeira resposta taxi", resposta.toString());
							loginApp.setId(resposta.getJSONObject("data").getLong("id"));

							Log.i("Resposta aqui taxe", resposta.getJSONObject("data").toString());

							loginApp.setLogin(sessionedLogin);
							loginApp.setSenha(novaSenha);

							Log.i("Entrou na primeira resposta taxi", loginApp.getId() + " --- " + loginApp.getLogin() + " ----" + loginApp.getSenha() );


							String strJson2 = ws.editarSenha(loginApp);
							JSONObject resposta2 = new JSONObject(strJson2);

							if(resposta2.getInt("errorCode")== 0){
								Log.i("Entrou na segunda resposta taxi", resposta.toString());
								gerarToast(resposta2.getString("descricao"));

								//Transfere para a pagina de dashboard
								Intent i = new Intent(getApplicationContext(),
										DashboardActivity.class);
								startActivity(i);
								finish();

							}
							else
								gerarToast(resposta2.getString("descricao"));

						}
						else
							gerarToast(resposta.getString("descricao"));


					} 
					catch (Exception e) {
						gerarToast("Erro ao alterar!");
						Log.i("Exception alterar taxi", e + " -- Message: " +e.getMessage());
					}
				}
				else{
					if(checkEmpty)
						gerarToast("Todos os campos são obrigatórios");
					if(checkPassword)
						gerarToast("Senhas precisam ser iguais");
					if(checkOldAndNew)
						gerarToast("Digite uma senha diferente da antiga");
				}

			}
		});



	}


	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}

}
