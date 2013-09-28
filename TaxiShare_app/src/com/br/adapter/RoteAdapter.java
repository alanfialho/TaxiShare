package com.br.adapter;

import java.util.List;

import com.br.activitys.R;
import com.br.entidades.EnderecoApp;
import com.br.entidades.RotaApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoteAdapter extends BaseAdapter {
	private List<RotaApp> rotas;
	private LayoutInflater mInflater;
	private ViewHolder holder;

	public static final Integer[] images = { R.drawable.icon_search,
		R.drawable.icon_profile, R.drawable.icon_password, R.drawable.icon_locate, R.drawable.icon_logout};


	static class ViewHolder{
		private TextView option1, option2;
		private ImageView img;
	}


	public RoteAdapter(Context context, List<RotaApp> rotas) {
		mInflater = LayoutInflater.from(context);
		this.rotas = rotas;
	}

	@Override
	public int getCount() {
		return rotas.size();
	}

	@Override
	public Object getItem(int index) {
		return rotas.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int posicao, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.rote_list_item, null);
			holder = new ViewHolder();

			holder.option1 = (TextView) convertView.findViewById(R.id.rote_list_item_txt_1);
			holder.option2 = (TextView) convertView.findViewById(R.id.rote_list_item_txt_2);

			holder.img = (ImageView) convertView.findViewById(R.id.rote_list_item_img_icon);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		RotaApp rotinha = rotas.get(posicao);

		holder.option1.setText("teste");
		holder.option2.setText("Esperando os enderecos certos");

		holder.img.setImageResource(images[0]);

		return convertView;
	}

}