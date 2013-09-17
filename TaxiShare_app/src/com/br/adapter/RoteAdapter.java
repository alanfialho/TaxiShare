package com.br.adapter;

import com.br.activitys.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoteAdapter extends BaseAdapter {
	private String[] options;
	private LayoutInflater mInflater;
	private ViewHolder holder;


	static class ViewHolder{
		private TextView roteInfo;
	}


	public RoteAdapter(Context context, String[] options) {
		mInflater = LayoutInflater.from(context);
		this.options= options; 
	}

	@Override
	public int getCount() {
		return options.length;
	}

	@Override
	public Object getItem(int index) {
		return options[index];
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

			holder.roteInfo = (TextView) convertView.findViewById(R.id.txtRoteInfoList);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.roteInfo.setText(options[posicao]);

		return convertView;
	}

}