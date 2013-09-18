package com.br.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.br.activitys.R;
import com.br.adapter.RoteAdapter;
import com.br.resources.Utils;

public class ListRoteFragment extends Fragment {

	private static View view;
	private ListView RoteList;
	private String endereco1, endereco2;
	public TextView txtEndereco1, txtEndereco2;
	private Button btnCriarRota;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rote_list, container, false);

				
		fillSearchList(view);

		return view;
	}

	/* The click listner for ListView in the navigation drawer */
	private class RoteListItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Utils.gerarToast(view.getContext(),  "Particiapando da rota");
		}
	}

	private void fillSearchList(View view){
		String[] rotes = getResources().getStringArray(R.array.enderecos) ;
		btnCriarRota = (Button) view.findViewById(R.id.btnListCriarRota);
		
		btnCriarRota.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				
				
				String origem = txtEndereco1.getText().toString();
				String destino = txtEndereco2.getText().toString();

				Bundle args = new Bundle();
				args.putCharSequence("origem", origem);
				args.putCharSequence("destino", destino);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ftransaction = fragmentManager.beginTransaction();
				Fragment fragment = new CreateRoteFragment();
				fragment.setArguments(args);
				ftransaction.replace(R.id.content_frame, fragment);
				ftransaction.addToBackStack(null);
				ftransaction.commit();
			
			}
		});	
		
		Bundle data = getArguments();
		String origem = (String) data.get("origem");
		String destino = (String) data.get("destino");
		
		
		txtEndereco1 = (TextView) view.findViewById(R.id.textView1);
		txtEndereco2 = (TextView) view.findViewById(R.id.textView2);
		
		
		txtEndereco1.setText(origem);
		txtEndereco2.setText(destino);
		
		for(int i = 0; i< rotes.length; i++){
			Log.i("CAGUEI", rotes[i]);	
		}
		
		
		
		RoteAdapter roteAdapter = new RoteAdapter(view.getContext(), rotes);

		RoteList = (ListView) view.findViewById(R.id.listViewRoteList);
		RoteList.setAdapter(roteAdapter);
		RoteList.setOnItemClickListener(new RoteListItemClickListener());
		
		
		

	}
	
	public void setEnderecos(String endereco1, String endereco2){
		this.endereco1 = endereco1;
		this.endereco2 = endereco2;
	}

}
