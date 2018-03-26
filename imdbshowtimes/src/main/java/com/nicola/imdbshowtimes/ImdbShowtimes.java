package com.nicola.imdbshowtimes;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Classe per fare richieste al sito della IBDM
 * Created by Nicola on 19/03/2018.
 */
public class ImdbShowtimes {
    private static final int CINEMA_MAX_RESULT = 8;
    private static final String BASE_URL = "http://www.imdb.com";
    private static final String BASE_FILM_DETAILS_URL = "http://www.imdb.com/showtimes/title/";
    private static final String BASE_SEARCH_URL = "http://www.imdb.com/find?ref_=nv_sr_fn&q=";
    private String zipCode;
    private static String cinemaUrl;

    /**
     * Costruttore parametrico
     * @param zip codice postale italiano
     */
    public ImdbShowtimes(String zip) {
        zipCode = zip;
        cinemaUrl = BASE_URL + "/showtimes/IT/" + zip;
    }

    /**
     * Funzione che chiama il processo asincrono per caricare
     * i cinema vicini alla tua zona
     * @param mCallback funzione da eseguire quando è finito il caricamento
     */
    public void getCinemas(OnCinemasResult mCallback) {
        new GetCinemas(mCallback).execute();
    }

    /**
     * Funzione che chiama il processo asincrono per caricare tutti i film in
     * programmazione di uno specificato cinema
     * @param mCallback funzione da eseguire quando è finito il caricamento
     * @param cinema cinema da interrogare
     */
    public void getFilmsInCinema(OnFilmsOfCinemaResult mCallback, Cinema cinema) {
        new GetFilmsInCinema(mCallback, cinema).execute();
    }

    /**
     * Funzione che chiama il processo asincrono per caricare tutte le informazioni
     * su uno specifico film
     * @param mCallback funzione da eseguire quando è finito il caricamento
     * @param film film da interrogare
     */
    public void getFilmDetails(OnFilmDetailsResult mCallback, Film film) {
        new GetFilmDetails(zipCode, film, mCallback).execute();
    }

    /**
     * Funzione che chiama il processo asincrono per effetuare una ricerca data una
     * query (nome di un film)
     * @param mCallback funzione da eseguire quando è finito il caricamento
     * @param filmToSearch stringa contenente il film da cercare
     */
    public void getFilmsByQuery(OnSearchFilmsResult mCallback, String filmToSearch) {
        new SearchFilms(filmToSearch, mCallback).execute();
    }

    /**
     * Classe che esegue una chiamata Asincrona e prende le info dei cinema vicini
     * alla zona del codice postale
     */
    static class GetCinemas extends AsyncTask<Void, Void, ArrayList<Cinema>> {
        private Document doc;
        private OnCinemasResult mCalllback;

        GetCinemas(OnCinemasResult mCalllback) {
            this.mCalllback = mCalllback;
        }

        @Override
        protected ArrayList<Cinema> doInBackground(Void... args) {
            try {
                doc = Jsoup.connect(cinemaUrl).get();

                Elements kmCount = doc.getElementsByClass("li_group");
                HashMap<Integer, Integer> kmAndCountMap = new HashMap<>();
                for (Element kmItem : kmCount) {
                    int km = Integer.parseInt(kmCount.get(0)
                                    .text()
                                    .replaceAll("[^\\d.]", ""));
                    int count = Integer.parseInt(kmCount.get(0)
                            .getElementsByTag("span")
                            .text()
                            .replace("(", "")
                            .replace(")", "")
                            .trim());
                    kmAndCountMap.put(km, count);
                }

                Log.d("Mappa", kmAndCountMap.toString());

                Elements eOdd = doc.getElementById("cinemas-at-list")
                        .getElementsByClass("list_item odd");
                Elements eEven = doc.getElementById("cinemas-at-list")
                        .getElementsByClass("list_item even");
                int iOdd = 0, iEven = 0;
                ArrayList<Cinema> cinemas = new ArrayList<>();
                for (int i = 0; i < CINEMA_MAX_RESULT; i++) {
                    Element temp;

                    if(i%2 == 0) {
                        // pari
                        temp = eOdd.get(iOdd);
                        iOdd++;
                    }
                    else {
                        // dispari
                        temp = eEven.get(iEven);
                        iEven++;
                    }
                    Cinema c = new Cinema();

                    // info sul cinema
                    c.setNome(temp.getElementsByClass("fav_box")
                            .text());
                    c.setIndirizzo(temp.getElementsByAttributeValue("itemprop", "streetAddress").text() +
                            ", " + temp.getElementsByAttributeValue("itemprop", "addressLocality").text() +
                            " " + temp.getElementsByAttributeValue("itemprop", "addressRegion").text());
                    c.setLink(BASE_URL +
                            temp.getElementsByClass("fav_box")
                            .get(0)
                            .getElementsByAttributeValue("itemprop", "url")
                            .attr("href"));
                    c.setTelefono(temp.getElementsByAttributeValue("itemprop", "telephone").text());
                    cinemas.add(c);
                }

                return cinemas;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Cinema> cinemas) {
            super.onPostExecute(cinemas);
            if(mCalllback != null)
                mCalllback.doStuff(cinemas);
        }
    }

    /**
     * Classe che esegue una chiamata Asincrona e prende le info sui film in programmazione
     * in un determinato cinema del giorno seguente
     */
    static class GetFilmsInCinema extends AsyncTask<Void, Void, Cinema> {
        private Document doc;
        private Cinema cinema;
        private OnFilmsOfCinemaResult mCalllback;

        GetFilmsInCinema(OnFilmsOfCinemaResult mCalllback, Cinema cinema) {
            this.mCalllback = mCalllback;
            this.cinema = cinema;
        }

        @Override
        protected Cinema doInBackground(Void... args) {
            try {
                doc = Jsoup.connect(cinema.getLink()).get();

                Elements list = new Elements();
                list.addAll(doc.getElementsByClass("list_item odd"));
                list.addAll(doc.getElementsByClass("list_item even"));

                for (Element e : list) {
                    Film t = new Film();
                    Element el = e.getElementsByClass("info").get(0);

                    String[] links = el.getElementsByAttributeValue("itemprop", "url").attr("href").split("/");
                    for (String l : links) {
                        if(l.contains("tt")) {
                            t.setLink(l);
                            break;
                        }
                    }
                    t.setImageUrl(e.getElementsByAttributeValue("itemprop", "image").attr("src"));
                    t.setNome(el.getElementsByTag("h3").text());
                    t.setDurata(el.getElementsByAttributeValue("itemprop", "duration").text());

                    String[] orari = DateTimeUtils.formatDatetime(el.getElementsByClass("showtimes")
                            .text()
                            .split(Pattern.quote("|")));
                    t.addProgrammazione(new Programmazione(DateTimeUtils.getCurrentDate(), orari));

                    cinema.addFilm(t);
                }

                return cinema;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cinema cinema) {
            super.onPostExecute(cinema);
            if(mCalllback != null)
                mCalllback.doStuff(cinema);
        }
    }

    /**
     * Classe che esegue una chiamata Asincrona e prende le info di un determinato film
     */
    static class GetFilmDetails extends AsyncTask<Void, Void, Film> {
        private Document doc;
        private String zipCode;
        private Film film;
        private OnFilmDetailsResult mCallback;

        GetFilmDetails(String zipCode, Film film, OnFilmDetailsResult mCallback) {
            this.zipCode = zipCode;
            this.film = film;
            this.mCallback = mCallback;
        }

        @Override
        protected Film doInBackground(Void... voids) {
            try {
                doc = Jsoup.connect(getFilmDetailsWithZip(film.getLink(), zipCode)).get();

                film.setDescrizione(doc.getElementsByAttributeValue("itemprop", "description").text());
                Elements eDirectors = doc.getElementsByAttributeValue("itemprop", "director");
                String[] directors = new String[eDirectors.size()];
                for(int i =0; i<directors.length; i++) {
                    directors[i] = eDirectors.get(i).text();
                }
                film.setDirettori(directors);
                Elements eActors = doc.getElementsByAttributeValue("itemprop", "actors");
                String[] actors = new String[eActors.size()];
                for(int i =0; i<actors.length; i++) {
                    actors[i] = eActors.get(i).text();
                }
                film.setAttori(actors);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return film;
        }

        @Override
        protected void onPostExecute(Film film) {
            super.onPostExecute(film);
            if(mCallback != null)
                mCallback.doStuff(film);
        }
    }

    /**
     * Classe che esegue una chiamata Asincrona ed esegue una ricerca di film
     * tramite delle parole chiave
     */
    static class SearchFilms extends AsyncTask<Void, Void, ArrayList<Film>> {
        private Document doc;
        private String searchKey;
        private OnSearchFilmsResult mCallback;

        SearchFilms(String searchKey, OnSearchFilmsResult mCallback) {
            this.searchKey = searchKey.replace(" ", "+").trim().toLowerCase();
            this.mCallback = mCallback;
        }

        @Override
        protected ArrayList<Film> doInBackground(Void... voids) {
            try {
                doc = Jsoup.connect(BASE_SEARCH_URL + searchKey).get();

                ArrayList<Film> f = new ArrayList<>();
                Elements article = doc.getElementsByClass("findSection")
                        .get(0)
                        .getElementsByClass("findResult");
                for (Element e : article) {
                    Film t = new Film();

                    String[] links = e.getElementsByTag("a").attr("href").split("/");
                    for (String l : links) {
                        if (l.contains("tt")) {
                            t.setLink(l);
                            break;
                        }
                    }

                    t.setNome(e.getElementsByClass("result_text").text());
                    f.add(t);
                }

                return f;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> films) {
            super.onPostExecute(films);
            if(mCallback != null) {
                mCallback.doStuff(films);
            }
        }
    }

    /**
     * Metodo che restituisce il link completo del film
     * @param filmUrl url/codice del film
     * @return il link completo
     */
    private static String getFilmDetailsWithZip(String filmUrl, String zipCode) {
        return BASE_FILM_DETAILS_URL + filmUrl + "/IT/" + zipCode;
    }

    public interface OnCinemasResult {
        void doStuff(ArrayList<Cinema> results);
    }

    public interface OnFilmsOfCinemaResult {
        void doStuff(Cinema results);
    }

    public interface OnFilmDetailsResult {
        void doStuff(Film film);
    }

    public interface OnSearchFilmsResult {
        void doStuff(ArrayList<Film> films);
    }
}
