package bflows;

import blogics.*;
import global.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Guido Pio
 */
public class TicketManagement implements Serializable {

    private String username;
    private int id_film;
    private String data;
    private int id_tabella;

    // film in programmazione
    private FilmDate[] film;
    private String[] week;

    // uso per controllo ticket che possono essere acquistati
    private int ticketCounter;
    private String[] reserved;
    private int subscriptionSeat;

    public TicketManagement() {
    }

    public void index() {
        try {
            // Creo la settimana di programmazione che voglio vedere
            String[] week = new String[7];
            LocalDate day = LocalDate.now();
            for (int i = 0; i < week.length; i++) {
                day = day.plusDays(1);
                week[i] = day.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            this.setWeek(week);
            LocalDate firstDayOfTheWeek = LocalDate.parse(week[0]);
            LocalDate lastDayOfTheWeek = day;

            // recupero film in programmazione
            List<FilmModel> films = FilmManager.getFilms(firstDayOfTheWeek, lastDayOfTheWeek);
            List<FilmDate> filmDate = new ArrayList<>();

            for (FilmModel tmp : films) {
                // recupero le date associate al film
                List<DateTimeModel> date = ShowManager.getDate(tmp, firstDayOfTheWeek, lastDayOfTheWeek);
                FilmDate film = new FilmDate(tmp.getId_film(), tmp.getTitolo(), date);
                filmDate.add(film);
            }
            this.film = filmDate.toArray(new FilmDate[filmDate.size()]);
        } catch (Exception ex) {
            // da gestire
        }
    }

    public void populate() {
        try {
            // controllo che a id_tabella possa corrispondere id_film - data
            FilmTheaterDateModel model = ShowManager.get(this.getId_tabella());
            FilmModel film = model.getFilm();
            DateTimeModel data = model.getDate();
            if (this.getId_film() != film.getId_film()
                    || !this.getData().equals(data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                throw new Exception();
            }

            List<String> userReserved = TicketManager.getReserved(this.getId_tabella(), this.getUsername());
            // diminuisco contatore in base a reserved
            if (userReserved != null) {
                this.setTicketCounter(Constants.MAX_TICKETS - userReserved.size());
            } else {
                this.setTicketCounter(Constants.MAX_TICKETS);
            }
            // recupero abbonamento - null se non esiste
            SubscriptionModel subscription = TicketManager.getSubscription(this.getUsername());
            if (subscription != null) {
                // setto valori per posti disponibili
                this.setSubscriptionSeat(subscription.getIngressi_disp());
            } else {
                this.setSubscriptionSeat(-1);
            }

            // recupero i posti prenotati - formato [FILA_NUM]
            List<String> reserved = TicketManager.getReserved(this.getId_tabella());
            this.setReserved(reserved.toArray(new String[reserved.size()]));
        } catch (Exception ex) {
            /* 
             ritorno alla pagina di scelta di orario-sala con un messaggio di errore
             e passando come parametri id_film e data che ho.
             */
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom FilmDate ">
    /**
     * Recupero la lista di tutti i film in programmazione
     *
     * @return L'array con tutti gli id_film
     */
    public int[] ticketId_film() {
        int[] id_film = new int[this.film.length];
        for (int i = 0; i < id_film.length; i++) {
            id_film[i] = this.film[i].getId_film();
        }
        return id_film;
    }

    /**
     * Recupero il titolo del film in base all'id_film
     *
     * @param id_film Id del film di cui voglio recuperare il titolo
     * @return Il titolo del film
     */
    public String ticketTitolo(int id_film) {
        FilmDate[] film = this.film;
        String titolo = "";
        for (FilmDate tmp : film) {
            if (tmp.getId_film() == id_film) {
                titolo = tmp.getTitolo();
                break;
            }
        }
        return titolo;
    }

    
    public String[] ticketDate(int id_film) {
        String[] date = null;
        for (FilmDate tmp : this.film) {
            if (tmp.getId_film() == id_film) {
                date = tmp.getDate();
                break;
            }
        }
        return date;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the value of id_film
     *
     * @return the value of id_film
     */
    public int getId_film() {
        return id_film;
    }

    /**
     * Set the value of id_film
     *
     * @param id_film new value of id_film
     */
    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    /**
     * Get the value of data
     *
     * @return the value of data
     */
    public String getData() {
        return data;
    }

    /**
     * Set the value of data
     *
     * @param data new value of data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Get the value of id_tabella
     *
     * @return the value of id_tabella
     */
    public int getId_tabella() {
        return id_tabella;
    }

    /**
     * Set the value of id_tabella
     *
     * @param id_tabella new value of id_tabella
     */
    public void setId_tabella(int id_tabella) {
        this.id_tabella = id_tabella;
    }

    /**
     * Get the value of week
     *
     * @return the value of week
     */
    public String[] getWeek() {
        return week;
    }

    /**
     * Set the value of week
     *
     * @param week new value of week
     */
    public void setWeek(String[] week) {
        this.week = week;
    }

    /**
     * Get the value of week at specified index
     *
     * @param index the index of week
     * @return the value of week at specified index
     */
    public String getWeek(int index) {
        return this.week[index];
    }

    /**
     * Set the value of week at specified index.
     *
     * @param index the index of week
     * @param week new value of week at specified index
     */
    public void setWeek(int index, String week) {
        this.week[index] = week;
    }

    /**
     * Get the value of ticketCounter
     *
     * @return the value of ticketCounter
     */
    public int getTicketCounter() {
        return ticketCounter;
    }

    /**
     * Set the value of ticketCounter
     *
     * @param ticketCounter new value of ticketCounter
     */
    public void setTicketCounter(int ticketCounter) {
        this.ticketCounter = ticketCounter;
    }

    /**
     * Get the value of reserved
     *
     * @return the value of reserved
     */
    public String[] getReserved() {
        return reserved;
    }

    /**
     * Set the value of reserved
     *
     * @param reserved new value of reserved
     */
    public void setReserved(String[] reserved) {
        this.reserved = reserved;
    }

    /**
     * Get the value of reserved at specified index
     *
     * @param index the index of reserved
     * @return the value of reserved at specified index
     */
    public String getReserved(int index) {
        return this.reserved[index];
    }

    /**
     * Set the value of reserved at specified index.
     *
     * @param index the index of reserved
     * @param reserved new value of reserved at specified index
     */
    public void setReserved(int index, String reserved) {
        this.reserved[index] = reserved;
    }

    /**
     * Get the value of subscriptionSeat
     *
     * @return the value of subscriptionSeat
     */
    public int getSubscriptionSeat() {
        return subscriptionSeat;
    }

    /**
     * Set the value of subscriptionSeat
     *
     * @param subscriptionSeat new value of subscriptionSeat
     */
    public void setSubscriptionSeat(int subscriptionSeat) {
        this.subscriptionSeat = subscriptionSeat;
    }

    // </editor-fold>
}

class FilmDate {

    private int id_film;
    private String titolo;
    private String[] date;

    public FilmDate(int id_film, String titolo, List<DateTimeModel> date) {
        this.setId_film(id_film);
        this.setTitolo(titolo);

        List<String> model = new ArrayList<>();
        String help = "2000-01-01";
        for (DateTimeModel data : date) {
            String day = data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE);
            // evito di aggiungere date uguali
            if (!day.equals(help)) {
                model.add(day);
                help = day; // new String(day)
            }
        }
        this.setDate(model.toArray(new String[model.size()]));
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_film
     *
     * @return the value of id_film
     */
    public int getId_film() {
        return id_film;
    }

    /**
     * Set the value of id_film
     *
     * @param id_film new value of id_film
     */
    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    /**
     * Get the value of titolo
     *
     * @return the value of titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Set the value of titolo
     *
     * @param titolo new value of titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Get the value of date
     *
     * @return the value of date
     */
    public String[] getDate() {
        return date;
    }

    /**
     * Set the value of date
     *
     * @param date new value of date
     */
    public void setDate(String[] date) {
        this.date = date;
    }

    /**
     * Get the value of date at specified index
     *
     * @param index the index of date
     * @return the value of date at specified index
     */
    public String getDate(int index) {
        return this.date[index];
    }

    /**
     * Set the value of date at specified index.
     *
     * @param index the index of date
     * @param date new value of date at specified index
     */
    public void setDate(int index, String date) {
        this.date[index] = date;
    }

    // </editor-fold>
}
