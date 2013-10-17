package com.br.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.activitys.R;

import com.br.entidades.LoginApp;
import com.br.entidades.RotaApp;
import com.br.sessions.SessionManagement;


public class UserRoteAdapterAdm extends BaseAdapter {
	
	private List<RotaApp> rotas;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	private SessionManagement session;
	private List<RotaApp> rotasAdm;
	private int id;
	

	public static final Integer[] images = { R.drawable.icon_search,
		R.drawable.icon_profile, R.drawable.icon_password, R.drawable.icon_locate, R.drawable.icon_logout, R.drawable.icon_navigation, R.drawable.icon_passageiros};


	static class ViewHolder{
		private TextView option1, option2, option3, option4;
		private ImageView img1;
	}


	public UserRoteAdapterAdm (Context context, LoginApp login) {
		mInflater = LayoutInflater.from(context);
		this.rotas = login.getRotas();
		this.rotasAdm = login.getRotasAdm();
		this.rotas.addAll(rotasAdm);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int posicao, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.rote_users_list_item, null);
			holder = new ViewHolder();

			holder.option1 = (TextView) convertView.findViewById(R.id.rote_users_list_item_lbl_origem_info);
			holder.option2 = (TextView) convertView.findViewById(R.id.rote_users_list_item_lbl_destino_info);
			//holder.option3 = (TextView) convertView.findViewById(R.id.rote_users_list_item_lbl_passageiros);
			holder.option4 = (TextView) convertView.findViewById(R.id.rote_users_list_item_tipo);

			holder.img1 = (ImageView) convertView.findViewById(R.id.rote_users_list_item_icon);
			

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//HashMap<String, String> user = session.getUserDetails();
		id = 1;
		String tipo = "";
		RotaApp r = rotas.get(posicao);
		String origem = r.getEnderecos().get(0).getRua() + ", " + r.getEnderecos().get(0).getNumero() + " - " + r.getEnderecos().get(0).getBairro();
		String destino = r.getEnderecos().get(1).getRua() + ", " + r.getEnderecos().get(1).getNumero() + " - " + r.getEnderecos().get(1).getBairro();
		if (posicao > rotas.size()){
			tipo = "Administrador";
			holder.option4.setTextColor(Color.RED);
		}
		else{
			tipo = "Participante";
			holder.option4.setTextColor(Color.GREEN);
		}
		
		//String passageiros = login.getRotasAdm().get(0).getPassExistentes() + " / 4";
		
		holder.option1.setText(origem);
		holder.option2.setText(destino);
		//holder.option3.setText(passageiros);
		holder.option4.setText(tipo);

		holder.img1.setImageResource(images[5]);
		
		return convertView;
	}





	@Override
	public int getCount() {
		return rotas.size();
	}





	@Override
	public Object getItem(int index) {
		return rotas.get(index);
	}

}
