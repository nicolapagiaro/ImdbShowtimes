package com.nicola.showtimestester;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nicola.imdbshowtimes.Cinema;
import com.nicola.imdbshowtimes.ImdbShowtimes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Cinema> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView l = findViewById(R.id.list);

        final ImdbShowtimes imdbShowtimes = new ImdbShowtimes("35020");
        imdbShowtimes.getCinemas(new ImdbShowtimes.OnCinemasResult() {
            @Override
            public void doStuff(ArrayList<Cinema> results) {
                list = results;
                Adapter a = new Adapter(MainActivity.this, R.layout.cinema_item, list);
                l.setAdapter(a);
            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showFilm = new Intent(getApplicationContext(), ShowFilmsActivity.class);
                showFilm.putExtra("cinema", list.get(position));
                startActivity(showFilm);
            }
        });
    }
    class Adapter extends ArrayAdapter<Cinema> {
        private ArrayList<Cinema> l;

        Adapter(@NonNull Context context, int resource, @NonNull List<Cinema> objects) {
            super(context, resource, objects);
            l = (ArrayList<Cinema>) objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(getContext())
                    .inflate(R.layout.cinema_item, null);

            TextView tvNome = v.findViewById(R.id.nome);
            TextView tvIndirizzo = v.findViewById(R.id.indirizzo);
            TextView tvDistanza = v.findViewById(R.id.distanza);

            tvNome.setText(l.get(position).getNome());
            tvIndirizzo.setText(l.get(position).getIndirizzo());
            tvDistanza.setText(l.get(position).getDistanceInKm() + "");

            return v;
        }
    }
}
