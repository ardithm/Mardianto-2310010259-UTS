package model;

import java.util.ArrayList;

/**
 * AgendaModel
 * Menyimpan list agenda menggunakan ArrayList.
 * Bagian dari MVC → Model.
 */

public class AgendaModel {
    private ArrayList<Agenda> data = new ArrayList<>();

    public void tambah(Agenda a) { data.add(a); }

    public void hapus(int index) { data.remove(index); }

    public void update(int index, Agenda a) { data.set(index, a); }

    public ArrayList<Agenda> getAll() { return data; }
    
    public void sortByTanggal() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

        data.sort((a, b) -> {
            try {
                java.util.Date d1 = sdf.parse(a.getTanggal());
                java.util.Date d2 = sdf.parse(b.getTanggal());
                return d1.compareTo(d2);
            } catch (Exception e) {
                return 0;
            }
        });
    }

}
