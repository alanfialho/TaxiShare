package com.br.resources;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapUtils {

	private Context context;
	private GoogleMap googleMap;

	public MapUtils(Context context, GoogleMap googleMap){
		this.context = context;
		this.googleMap = googleMap;
	}

	public void execute(double destinoLatitude, double destinoLongitude, double origemLatitude, double origemLongitude){
		String url = makeURL(origemLatitude, origemLongitude, destinoLatitude, destinoLongitude);
		connectAsyncTask task = new connectAsyncTask(url);
		task.execute();
		
	}

	public Marker setMarker(double latitude, double longitude, String title, String snippet, boolean zoom) {
		//		googleMap.addMarker(new MarkerOptions().position(new LatLng(-23.489839, -46.410520)).title("Marker"));
		Marker mark = googleMap.addMarker(new MarkerOptions()
		.position(new LatLng(latitude, longitude))
		.title(title)
		.snippet(snippet)
		.draggable(true));

		if(zoom)
			setaZoom(latitude, longitude);

		return mark;
	}

	private void setaZoom(double latitude, double longitude){
		//Location myLocation = googleMap.getMyLocation();
		LatLng myLatLng = new LatLng(latitude, longitude);
		googleMap.getProjection();
		//Adiciona a latitude e longitude da minha localização a um objeto LatLng

		//Move a camera do mapa para a minha localização de acordo com o objeto LatLng gerado
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

	}

	private String makeURL (double origemLatitude, double origemLongitude, double destinoLatitude, double destinoLongitude ){
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(origemLatitude));
		urlString.append(",");
		urlString.append(Double.toString( origemLongitude));
		urlString.append("&destination=");// to
		urlString.append(Double.toString( destinoLatitude));
		urlString.append(",");
		urlString.append(Double.toString( destinoLongitude));
		urlString.append("&sensor=false&mode=driving&alternatives=true");

		googleMap.clear();

		return urlString.toString();
	}

	public void drawPath(String  result) {

		try {

			//Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);

			for(int z = 0; z<list.size()-1;z++){
				LatLng src= list.get(z);
				LatLng dest= list.get(z+1);
				googleMap.addPolyline(new PolylineOptions()
				.add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
				.width(4)
				.color(Color.BLUE)
				.geodesic(true));
			}
			
			JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);
			String distance = legs.getJSONObject("distance").getString("text");
			String duration = legs.getJSONObject("duration").getString("text");
			
			double destLat = legs.getJSONObject("end_location").getDouble("lat");
			double destLng = legs.getJSONObject("end_location").getDouble("lng");
			
			double oriLat = legs.getJSONObject("start_location").getDouble("lat");
			double oriLng = legs.getJSONObject("start_location").getDouble("lng");
			
			String via = routes.getString("summary");

			setMarker(oriLat, oriLng, "Destino", distance + "\n" + duration + "\n via: " + via ,true);
			setMarker(destLat, destLng, "Destino", distance + "\n" + duration + "\n via: " + via ,true);

		} 
		catch (JSONException e) {
			Utils.logException("SearchRoteFragment", "drawPath", "", e);
		}
	} 

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng( (((double) lat / 1E5)),
					(((double) lng / 1E5) ));
			poly.add(p);
		}

		return poly;
	}

	private class connectAsyncTask extends AsyncTask<Void, Void, String>{
		private ProgressDialog progressDialog;
		String url;

		connectAsyncTask(String urlPass){
			url = urlPass;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = Utils.setProgreesDialog(progressDialog, context, "Traçando rota", "Aguarde...");
		}
		@Override
		protected String doInBackground(Void... params) {
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromUrl(url);
			return json;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);   
			progressDialog.dismiss();        
			if(result!=null){
				drawPath(result);
			}

		}
	}

}
