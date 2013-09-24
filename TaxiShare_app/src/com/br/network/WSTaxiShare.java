package com.br.network;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.JsonParser;

import com.br.entidades.*;
import com.br.entidades.LoginApp;
import com.br.entidades.PerguntaApp;
import com.br.entidades.PessoaApp;
import com.br.entidades.ResponseApp;


public class WSTaxiShare {

	private static final String URL_WS = "http://192.168.56.1:8080/WS_TaxiShare/";

	public String login(String email, String password) throws Exception {
		Log.i("WSTaxiShare login taxi", "email ->" + email + " || senha -> " + password);
		
		String[] resposta = new WSClient().get(URL_WS + "login/login/?login="+ email +"&password="+ password);

		return checkError(resposta);
	}

	public List<PerguntaApp> getPerguntas() throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pergunta/findAll");
		
		Log.i("WSTaxiShare getPerguntas taxi", "resposta 0 -> " + resposta[0] + " || resposta 1 -> " + resposta[1]);
		
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<PerguntaApp> listaPerguntas = new ArrayList<PerguntaApp>(); 
			JSONObject json = new JSONObject(resposta[1]);
			json = json.getJSONObject("data");
			JSONArray array = json.getJSONArray("perguntas");

			for (int i = 0; i < array.length(); i++) {
				listaPerguntas.add(gson.fromJson(array.get(i).toString(), PerguntaApp.class));
			}

			return listaPerguntas;
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public List<PessoaApp> getListaPessoa() throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pessoa/findAll");
		
		Log.i("WSTaxiShare getListaPessoa taxi", "resposta 0 -> " + resposta[0] + " || resposta 1 -> " + resposta[1]);

		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<PessoaApp> listaPessoa = new ArrayList<PessoaApp>();
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(resposta[1]).getAsJsonArray();

			for (int i = 0; i < array.size(); i++) {
				listaPessoa.add(gson.fromJson(array.get(i), PessoaApp.class));
			}
			return listaPessoa;
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public String cadastrarLogin(LoginApp login) throws Exception {
		String[] resposta = {"", "errorCode:1, descricao: Problema no servidor"};
		try
		{
			Gson gson = new Gson();
			String loginJSON = gson.toJson(login);
			resposta = new WSClient().post(URL_WS + "login/create", loginJSON);
			Log.i("WSTaxishare cadastrarLogin taxi", "URL -> " + URL_WS + " || Resposta -> " + resposta );

		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception cadastrarLogin taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return checkError(resposta);
	}

	public String editarCadastro(PessoaApp novaPessoa) throws Exception {
		String[] resposta = {"", "errorCode:1, descricao: Problema no servidor"};
		try
		{
			Gson gson = new Gson();
			String pessoaJSON = gson.toJson(novaPessoa);
			resposta = new WSClient().post(URL_WS + "pessoa/edit", pessoaJSON);
			Log.i("WSTaxishare editarCadastro taxi", "ErrorCode: " + resposta[0] + " || Resposta: " +resposta[1]);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception editarCadastro taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return checkError(resposta);
	}

	public String editarSenha(LoginApp loginApp) throws Exception {
		String[] resposta = {"", "errorCode:1, descricao: Problema no servidor"};
		try
		{
			Gson gson = new Gson();
			String loginJSON = gson.toJson(loginApp);
			resposta = new WSClient().post(URL_WS + "login/editPassword", loginJSON);
			Log.i("WSTaxishare editarSenha taxi", "ErrorCode: " + resposta[0] + " || Resposta: " +resposta[1]);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception editarSenha taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return checkError(resposta);
	}

	public String checkLogin(String login) throws Exception {
		String[] resposta = {"", "errorCode:1, descricao: Problema no servidor"};
		try
		{
			resposta = new WSClient().get(URL_WS + "login/checkLogin?login="+ login);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception checkLogin taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return checkError(resposta);
	}
	
	public String criarRota(RotaApp rota) throws Exception {
		String[] resposta = {};
		try
		{
			Gson gson = new Gson();
			String rotaJson = gson.toJson(rota) ;
			resposta = new WSClient().post(URL_WS + "rota/create", rotaJson);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception criarRota taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return checkError(resposta);
	}
	
	public String participarRota(int idRota, int idUsuario) throws Exception {
		
		String[] resposta = {"", "errorCode:1, descricao: Problema no servidor"};
			
		try
		{
			resposta = new WSClient().put(URL_WS + "rota/joinIn/" + idRota + "/" + idUsuario);
			Log.i("WSTaxishare participarRota taxi", "ErrorCode: " + resposta[0] + " || Resposta: " +resposta[1]);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception participarRota taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return checkError(resposta);
		
	}
	
	private String checkError(String[] resposta){
		if(resposta[0].equals("200"))
			return resposta[1];
		else{
			ResponseApp resp = new ResponseApp("Erro no servidor", 2, "Servidor indisponivel, tente novamente mais tarde", null);
			Gson gson = new Gson();
			return gson.toJson(resp).toString();
		}
	}

}

