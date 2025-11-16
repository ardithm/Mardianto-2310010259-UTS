package model;

/**
 * Agenda
 * Class entity yang mewarisi BaseAgenda (INHERITANCE).
 * Menggunakan ENCAPSULATION, POLYMORPHISM.
 */

public class Agenda extends BaseAgenda {
    private String judul;
    private String tanggal;
    private String waktu;
    private String deskripsi;
    private String status;


    public Agenda(String judul, String tanggal, String waktu, String deskripsi) {
        this.judul = judul;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
    }
    
    public Agenda(String judul, String tanggal, String waktu, String deskripsi, String status) {
        this.judul = judul;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.deskripsi = deskripsi;
        this.status = status;
    }


    // Getter & Setter (ENCAPSULATION)
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getWaktu() { return waktu; }
    public void setWaktu(String waktu) { this.waktu = waktu; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }


    /**
     * POLYMORPHISM
     * Override method abstract getRingkasan
     */
    @Override
    public String getRingkasan() {
        return tanggal + " | " + waktu + " | " + judul;
    }
}
