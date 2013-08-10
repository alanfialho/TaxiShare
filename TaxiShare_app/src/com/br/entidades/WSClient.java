package com.br.entidades;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import android.util.Log;

public class WSClient {

	public final String[] get(String url) {

		String[] result = new String[2];
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;

		try {
			response = HttpClientSingleton.getHttpClientInstace().execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result[0] = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream instream = entity.getContent();
				result[1] = toString(instream);
				instream.close();
				Log.i("get", "Result from post JsonPost : " + result[0] + " : " + result[1]);
			}
		} catch (Exception e) {
			Log.e("NGVL", "Falha ao acessar Web service", e);
			result[0] = "0";
			result[1] = "Falha de rede!";
		}
		return result;
	}

	public final String[] post(String url, String json) {
		String[] result = new String[2];
		try {
			Log.i("POST taxi", json);
			HttpPost httpPost = new HttpPost(new URI(url));
			httpPost.setHeader("Content-type", "application/json");
			StringEntity sEntity = new StringEntity(json, "UTF-8");
			httpPost.setEntity(sEntity);

			HttpResponse response;
			response = HttpClientSingleton.getHttpClientInstace().execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result[0] = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream instream = entity.getContent();
				result[1] = toString(instream);
				instream.close();
				Log.d("Resultado do Post taxi", "JsonPost : " + result[0] + " : " + result[1]);
			}
			else{
				
			}

		} catch (Exception e) {
			Log.e("NGVL", "Falha ao acessar Web service", e);
			result[0] = "0";
			result[1] = "Falha de rede!";
			e.printStackTrace();
		}
		return result;
	}

	private String toString(InputStream is) throws IOException {

		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int lidos;
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		return new String(baos.toByteArray());
	}
}


