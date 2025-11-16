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
}
