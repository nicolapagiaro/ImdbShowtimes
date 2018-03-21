package com.nicola.showtimestester;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nicola.imdbshowtimes.Cinema;
import com.nicola.imdbshowtimes.Film;
import com.nicola.imdbshowtimes.ImdbShowtimes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowFilmsActivity extends AppCompatActivity {
    private ArrayList<Film> films;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_films);

        final ListView filmsList = findViewById(R.id.films_list);

        Cinema c = (Cinema) getIntent().getSerializableExtra("cinema");
        final ImdbShowtimes showtimes = new ImdbShowtimes("35020");
        showtimes.getFilmsInCinema(new ImdbShowtimes.OnFilmsOfCinemaResult() {
            @Override
            public void doStuff(Cinema results) {
                films = results.getFilms();
                Adapter a = new Adapter(ShowFilmsActivity.this, R.layout.film_item, results.getFilms());
                filmsList.setAdapter(a);
            }
        }, c);

        filmsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showtimes.getFilmDetails(new ImdbShowtimes.OnFilmDetailsResult() {
                    @Override
                    public void doStuff(Film film) {
                        Log.d("Film specs", film.toString());
                    }
                }, films.get(position));
            }
        });
    }

    class Adapter extends ArrayAdapter<Film> {
        private ArrayList<Film> l;

        public Adapter(@NonNull Context context, int resource, @NonNull ArrayList<Film> objects) {
            super(context, resource, objects);
            l = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(getContext())
                    .inflate(R.layout.film_item, null);

            TextView tvNome = v.findViewById(R.id.tvNome);
            TextView tvDurata = v.findViewById(R.id.tvDurata);
            TextView tvOrari = v.findViewById(R.id.tvOrari);
            ImageView iv = v.findViewById(R.id.copertina);

            Film f = l.get(position);

            tvDurata.setText(f.getDurata());
            tvNome.setText(f.getNome());
            tvOrari.setText(Arrays.toString(f.getProgrammazione(0).getOrari()));
            Picasso.get()
                    .load(f.getImageUrl())
                    .into(iv);

            return v;
        }
    }
}