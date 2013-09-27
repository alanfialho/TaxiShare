package com.br.adapter;

import com.br.activitys.R;
import com.br.entidades.RotaApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoteAdapter extends ArrayAdapter<RotaApp> {

	Context contexto;
    int layoutResourceId;
    RotaApp data[] = null;



	public RoteAdapter(Context contexto, int layoutResourceId, RotaApp[] data) {

        super(contexto, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.contexto = contexto;
        this.data = data;
    }
	

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the listview_item_row.xml parent
            LayoutInflater inflater = LayoutInflater.from(contexto);
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        
        // get the elements in the layout
       
        TextView origem = (TextView) convertView.findViewById(R.id.listItemOrigemInfo);
        TextView destino = (TextView) convertView.findViewById(R.id.listItemDestinoInfo);

        /*
         * Set the data for the list item. You can also set tags here if you
         * want.
         */
        RotaApp rota = data[position];

        
        origem.setText(rota.getEnderecos().get(0).getRua());
        destino.setText(rota.getEnderecos().get(1).getRua());

        return convertView;
    }

	

}