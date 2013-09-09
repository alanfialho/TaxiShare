package com.br.network;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.br.entidades.*;

public class WSTaxiShare {

	private static final String URL_WS = "http://10.0.2.2:8080/WS_TaxiShare/";

	public PessoaApp getPessoa(int id) throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pessoa/findById/" + id);

		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			PessoaApp pessoa = gson.fromJson(resposta[1], PessoaApp.class);
			return pessoa;
		} else {
			throw new Exception(resposta[1]);
		}
	}	

	public String login(String email, String password) throws Exception {
		String[] resposta = new WSClient().get(URL_WS + "login/login/?login="+ email +"&password="+ password);

		return resposta[1];
	}

	public List<PerguntaApp> getPerguntas() throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pergunta/findAll");
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<PerguntaApp> listaPessoa = new ArrayList<PerguntaApp>();
			JSONObject json = new JSONObject(resposta[1]);
			json = json.getJSONObject("data");
			JSONArray array = json.getJSONArray("perguntas");

			for (int i = 0; i < array.length(); i++) {
				listaPessoa.add(gson.fromJson(array.get(i).toString(), PerguntaApp.class));
			}

			return listaPessoa;
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public List<PessoaApp> getListaPessoa() throws Exception {

		String[] resposta = new WSClient().get(URL_WS + "pessoa/findAll");

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

	public String cadastrarPessoa(PessoaApp pessoa) throws Exception {
		String[] resposta = {};
		try
		{
			Gson gson = new Gson();
			String pessoaJson = gson.toJson(pessoa);
			resposta = new WSClient().post(URL_WS + "pessoa/create", pessoaJson);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception cadastrarPessoa taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return resposta[1];
	} 

	public String cadastrarLogin(LoginApp login) throws Exception {
		String[] resposta = {};
		try
		{
			Gson gson = new Gson();
			String loginJSON = gson.toJson(login);
			resposta = new WSClient().post(URL_WS + "login/create", loginJSON);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception cadastrarLogin taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return resposta[1];
	}

	public String editarCadastro(PessoaApp novaPessoa) throws Exception {
		String[] resposta = {};
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

		return resposta[1];
	}


	public String editarSenha(LoginApp loginApp) throws Exception {
		String[] resposta = {};
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

		return resposta[1];
	}

	public String checkLogin(String login) throws Exception {
		String[] resposta = {};
		try
		{
			resposta = new WSClient().get(URL_WS + "login/checkLogin?login="+ login);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception checkLogin taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return resposta[1];
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

		return resposta[1];
	}
	
	public String participarRota(int idRota, int idUsuario) throws Exception {
		
		String[] resposta = {};
			
		try
		{
			resposta = new WSClient().put(URL_WS + "rota/joinIn/" + idRota + "/" + idUsuario);
			Log.i("WSTaxishare participarRota taxi", "ErrorCode: " + resposta[0] + " || Resposta: " +resposta[1]);
		}
		catch(Exception e)
		{
			Log.i("WSTaxishare Exception participarRota taxi", "Exception -> " + e + " | Message -> " + e.getMessage());
		}

		return resposta[1];
		
	} 


}

