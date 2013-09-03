package com.br.network;

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
			Log.i("WSClient Get taxi", "Url -> " + url);
			response = HttpClientSingleton.getHttpClientInstace().execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result[0] = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream instream = entity.getContent();
				result[1] = toString(instream);
				instream.close();
				Log.i("WSClient if entity not null taxi", "Respsta 0: " + result[0]);
				Log.i("WSClient if entity not null taxi", "Respsta 1: " + result[1]);
				
				if(!result[0].equals("200"))
					result[1] = "{errorCode: 1, descricao: Falha de rede!}";

			}
		} catch (Exception e) {
			Log.i("Exception WSClient get taxi", "Exception ->" + e);
			result[0] = "0";
			result[1] = "{errorCode: 1, descricao: Falha de rede!}";
		}
		return result;
	}

	public final String[] post(String url, String json) {
		String[] result = new String[2];
		try {
			Log.i("WSClient post taxi", json);
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
				Log.i("WSClient Post taxi", "JsonPost : " + result[0] + " : " + result[1]);
			}

		} catch (Exception e) {
			result[0] = "0";
			result[1] = "{errorCode: 1, Descricao: Falha de rede!}";
			Log.i("WSCliente Exception post", "Exception -> " + e + " | Message -> " + e.getMessage());
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