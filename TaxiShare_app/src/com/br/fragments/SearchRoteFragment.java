package com.br.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.br.activitys.R;
import com.br.adapter.RoteAdapter;
import com.br.resources.Utils;

public class SearchRoteFragment extends Fragment {

	private static View view;
	private ListView RoteList;
	private Button searchButton;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rote_search, container, false);

		searchButton = (Button) view.findViewById(R.id.btnListarRotas);
		searchButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				fillSearchList();
			}
		});

		return view;
	}

	/* The click listner for ListView in the navigation drawer */
	private class RoteListItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Utils.gerarToast(view.getContext(),  "Particiapando da rota");
		}
	}

	private void fillSearchList(){
		String[] rotes = getResources().getStringArray(R.array.enderecos) ;

		RoteAdapter roteAdapter = new RoteAdapter(view.getContext(), rotes);

		RoteList = (ListView) view.findViewById(R.id.listViewRoteList);
		RoteList.setAdapter(roteAdapter);
		RoteList.setOnItemClickListener(new RoteListItemClickListener());

	}

}
