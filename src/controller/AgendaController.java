package controller;

import model.*;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Scanner;
import java.io.File;

/**
 * AgendaController
 * Menghubungkan View dan Model (MVC).
 * Menangani event tombol & logika aplikasi.
 */

public class AgendaController {
    
    private AgendaModel model;
    private AgendaView view;

    public AgendaController(AgendaModel model, AgendaView view) {
        this.model = model;
        this.view = view;

        loadTable();

        // Event tombol
        view.btnTambah.addActionListener(e -> tambahAgenda());
        view.btnUpdate.addActionListener(e -> updateAgenda());
        view.btnHapus.addActionListener(e -> hapusAgenda());
        view.btnExport.addActionListener(e -> exportTXT());
        view.btnImport.addActionListener(e -> importTXT());

        view.tblAgenda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) tampilkanKeForm();
        });
    }

    // ----- HELPER METHOD -----

    // Ambil tanggal dari JDateChooser
    private String getTanggalDariDateChooser() {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(view.dateTanggal.getDate());
        } catch (Exception e) {
            return "";
        }
    }

    // Ambil waktu dari JSpinner TimePicker
    private String getWaktuDariSpinner() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        return sdf.format(view.spinnerWaktu.getValue());
    }

    // ----- CRUD -----

    private void tambahAgenda() {
        Agenda a = new Agenda(
                view.txtJudul.getText(),
                getTanggalDariDateChooser(),
                getWaktuDariSpinner(),
                view.txtDeskripsi.getText()
        );

        model.tambah(a);
        loadTable();
        JOptionPane.showMessageDialog(view, "Agenda berhasil ditambahkan!");
    }

    private void updateAgenda() {
        int row = view.tblAgenda.getSelectedRow();
        if (row == -1) return;

        Agenda a = new Agenda(
                view.txtJudul.getText(),
                getTanggalDariDateChooser(),
                getWaktuDariSpinner(),
                view.txtDeskripsi.getText()
        );

        model.update(row, a);
        loadTable();
        JOptionPane.showMessageDialog(view, "Agenda berhasil diupdate!");
    }

    private void hapusAgenda() {
        int row = view.tblAgenda.getSelectedRow();
        if (row == -1) return;

        model.hapus(row);
        loadTable();
        JOptionPane.showMessageDialog(view, "Agenda berhasil dihapus!");
    }

    // ----- TABLE DISPLAY -----

    private void loadTable() {
        DefaultTableModel dtm = new DefaultTableModel(
                new String[]{"Judul", "Tanggal", "Waktu"}, 0
        );

        for (Agenda a : model.getAll()) {
            dtm.addRow(new Object[]{
                    a.getJudul(),
                    a.getTanggal(),
                    a.getWaktu()
            });
        }

        view.tblAgenda.setModel(dtm);
    }

    private void tampilkanKeForm() {
        int row = view.tblAgenda.getSelectedRow();
        if (row == -1) return;

        Agenda a = model.getAll().get(row);

        view.txtJudul.setText(a.getJudul());

        // Set ke JDateChooser
        try {
            java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .parse(a.getTanggal());
            view.dateTanggal.setDate(date);
        } catch (Exception e) {}

        // Set ke JSpinner
        try {
            java.util.Date time = new java.text.SimpleDateFormat("HH:mm")
                    .parse(a.getWaktu());
            view.spinnerWaktu.setValue(time);
        } catch (Exception e) {}

        view.txtDeskripsi.setText(a.getDeskripsi());
    }

    // ----- FILE EXPORT -----

    private void exportTXT() {
        try {
            java.io.PrintWriter out = new java.io.PrintWriter("catatku_export.txt");

            for (Agenda a : model.getAll()) {
                out.println(a.getRingkasan());
                out.println(a.getDeskripsi());
                out.println("-----------------------------------");
            }

            out.close();
            JOptionPane.showMessageDialog(view, "Export TXT berhasil!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Export gagal!");
        }
    }

    // ----- FILE IMPORT -----

    private void importTXT() {
        try {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(view);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();
                Scanner sc = new Scanner(file);

                model.getAll().clear();

                while (sc.hasNextLine()) {

                    String header = sc.nextLine();
                    if (header.trim().isEmpty()) continue;

                    String[] bagian = header.split("\\|");

                    String tanggal = bagian[0].trim();
                    String waktu   = bagian[1].trim();
                    String judul   = bagian[2].trim();

                    String deskripsi = sc.nextLine();

                    if (sc.hasNextLine()) sc.nextLine();

                    Agenda a = new Agenda(judul, tanggal, waktu, deskripsi);
                    model.tambah(a);
                }

                sc.close();
                loadTable();
                JOptionPane.showMessageDialog(view, "Import TXT berhasil!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Import gagal!");
        }
    }
}
