package controller;

import model.*;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Scanner;
import java.io.File;

public class AgendaController {

    private AgendaModel model;
    private AgendaView view;

    public AgendaController(AgendaModel model, AgendaView view) {
        this.model = model;
        this.view = view;

        loadTable();
        autoResizeRowHeight();

        // Event tombol
        view.btnTambah.addActionListener(e -> tambahAgenda());
        view.btnUpdate.addActionListener(e -> updateAgenda());
        view.btnClear.addActionListener(e -> resetForm());
        view.btnHapus.addActionListener(e -> hapusAgenda());
        view.cmbFilter.addActionListener(e -> filterTanggal());
        view.btnExport.addActionListener(e -> exportTXT());
        view.btnImport.addActionListener(e -> importTXT());

        // Default: nonaktifkan update & hapus jika belum pilih row
        view.btnUpdate.setEnabled(false);
        view.btnHapus.setEnabled(false);

        // Klik baris tabel → tampilkan ke form
        view.tblAgenda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) tampilkanKeForm();
        });

        // Search filter
        view.txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        // Double-click row to edit
        view.tblAgenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) tampilkanKeForm();
            }
        });
    }


    // ===================== HELPER METHOD ========================

    private String getTanggalDariDateChooser() {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(view.dateTanggal.getDate());
        } catch (Exception e) {
            return "";
        }
    }

    private String getWaktuDariSpinner() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        return sdf.format(view.spinnerWaktu.getValue());
    }



    // ========================= CRUD ============================

    private void tambahAgenda() {

        if (!validasiInput()) return;
        String status = view.rbSelesai.isSelected() ? "Selesai" : "Belum";

        Agenda a = new Agenda(
            view.txtJudul.getText(),
            getTanggalDariDateChooser(),
            getWaktuDariSpinner(),
            view.txtDeskripsi.getText(),
            status
        );

        model.tambah(a);
        loadTable();
        autoResizeRowHeight();
        updateStatistik();

        // auto scroll ke baris paling bawah
        int lastRow = view.tblAgenda.getRowCount() - 1;
        view.tblAgenda.setRowSelectionInterval(lastRow, lastRow);

        resetForm();
        JOptionPane.showMessageDialog(view, "Agenda berhasil ditambahkan!");
    }


    private void updateAgenda() {

        if (!validasiInput()) return;

        String status = view.rbSelesai.isSelected() ? "Selesai" : "Belum";
        int row = view.tblAgenda.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Yakin ingin mengubah data ini?",
                "Konfirmasi Update",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        Agenda a = new Agenda(
            view.txtJudul.getText(),
            getTanggalDariDateChooser(),
            getWaktuDariSpinner(),
            view.txtDeskripsi.getText(),
            status
        );

        model.update(row, a);
        loadTable();
        autoResizeRowHeight();
        updateStatistik();

        // tetap highlight baris yang diupdate
        view.tblAgenda.setRowSelectionInterval(row, row);

        resetForm();
        JOptionPane.showMessageDialog(view, "Agenda berhasil diupdate!");
    }


    private void hapusAgenda() {
        int row = view.tblAgenda.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        model.hapus(row);
        loadTable();
        autoResizeRowHeight();
        updateStatistik();
        resetForm();

        JOptionPane.showMessageDialog(view, "Agenda berhasil dihapus!");
    }


    // ======================== RESET FORM ========================

    private void resetForm() {
        view.txtJudul.setText("");
        view.txtDeskripsi.setText("");
        view.dateTanggal.setDate(null);

        try {
            java.util.Date defaultTime =
                    new java.text.SimpleDateFormat("HH:mm").parse("00:00");
            view.spinnerWaktu.setValue(defaultTime);
        } catch (Exception e) {}

        // Reset radio button
        view.rbBelum.setSelected(true);

        // Reset combobox filter ke default ("Semua")
        view.cmbFilter.setSelectedIndex(0);
        
        view.tblAgenda.clearSelection();
        view.btnUpdate.setEnabled(false);
        view.btnHapus.setEnabled(false);
        view.rbBelum.setSelected(true);

    }



    // ======================= VALIDASI INPUT =====================

    private boolean validasiInput() {

        if (view.txtJudul.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Judul tidak boleh kosong!");
            return false;
        }

        if (view.dateTanggal.getDate() == null) {
            JOptionPane.showMessageDialog(view, "Tanggal harus dipilih!");
            return false;
        }

        if (view.spinnerWaktu.getValue() == null) {
            JOptionPane.showMessageDialog(view, "Waktu harus dipilih!");
            return false;
        }

        return true;
    }



    // ===================== TABLE DISPLAY ========================

    private void loadTable() {

        model.sortByTanggal();

        DefaultTableModel dtm = new DefaultTableModel(
            new String[]{"Judul", "Tanggal", "Waktu", "Deskripsi", "Status"}, 0
        );

        for (Agenda a : model.getAll()) {
            dtm.addRow(new Object[]{
                a.getJudul(),
                a.getTanggal(),
                a.getWaktu(),
                a.getDeskripsi(),
                a.getStatus()
            });
        }
        
        view.tblAgenda.setModel(dtm);
        setColumnWidth();
        // Kolom deskripsi wrap text
        view.tblAgenda.getColumnModel().getColumn(3)
                .setCellRenderer(new MultiLineTableCellRenderer());
    }


    private void tampilkanKeForm() {
        int row = view.tblAgenda.getSelectedRow();
        if (row == -1) {
            view.btnUpdate.setEnabled(false);
            view.btnHapus.setEnabled(false);
            return;
        }

        view.btnUpdate.setEnabled(true);
        view.btnHapus.setEnabled(true);

        Agenda a = model.getAll().get(row);

        view.txtJudul.setText(a.getJudul());

        try {
            java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .parse(a.getTanggal());
            view.dateTanggal.setDate(date);
        } catch (Exception e) {}

        try {
            java.util.Date time = new java.text.SimpleDateFormat("HH:mm")
                    .parse(a.getWaktu());
            view.spinnerWaktu.setValue(time);
        } catch (Exception e) {}

        view.txtDeskripsi.setText(a.getDeskripsi());
        
        if (a.getStatus().equals("Selesai")) {
            view.rbSelesai.setSelected(true);
        } else {
            view.rbBelum.setSelected(true);
        }

    }

    
    private void setColumnWidth() {
        view.tblAgenda.getColumnModel().getColumn(0).setPreferredWidth(180); // Judul
        view.tblAgenda.getColumnModel().getColumn(1).setPreferredWidth(90);  // Tanggal
        view.tblAgenda.getColumnModel().getColumn(2).setPreferredWidth(60);  // Waktu
        view.tblAgenda.getColumnModel().getColumn(3).setPreferredWidth(300); // Deskripsi
    }

    
    // Renderer untuk membuat kolom bisa multiline (wrap text)
    class MultiLineTableCellRenderer extends JTextArea implements javax.swing.table.TableCellRenderer {

        public MultiLineTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            setText(value == null ? "" : value.toString());

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }

            return this;
        }
    }



    // ===================== FILTER TABLE ==========================

    private void filterTable() {
        String keyword = view.txtSearch.getText().toLowerCase();
        String filter = view.cmbFilter.getSelectedItem().toString();

        DefaultTableModel dtm = new DefaultTableModel(
                new String[]{"Judul", "Tanggal", "Waktu", "Deskripsi", "Status"}, 0
        );

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date now = new java.util.Date();
        String today = sdf.format(now);

        // Hitung minggu ini
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String startWeek = sdf.format(cal.getTime());
        cal.add(java.util.Calendar.DATE, 6);
        String endWeek = sdf.format(cal.getTime());

        // Hitung bulan ini
        java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat("yyyy-MM");
        String thisMonth = monthFormat.format(now);

        for (Agenda a : model.getAll()) {

            boolean cocokFilterTanggal = false;

            switch (filter) {
                case "Semua":
                    cocokFilterTanggal = true;
                    break;

                case "Hari Ini":
                    cocokFilterTanggal = a.getTanggal().equals(today);
                    break;

                case "Minggu Ini":
                    cocokFilterTanggal =
                        a.getTanggal().compareTo(startWeek) >= 0 &&
                        a.getTanggal().compareTo(endWeek) <= 0;
                    break;

                case "Bulan Ini":
                    cocokFilterTanggal = a.getTanggal().startsWith(thisMonth);
                    break;
            }

            // filter judul
            boolean cocokKeyword = a.getJudul().toLowerCase().contains(keyword);

            if (cocokFilterTanggal && cocokKeyword) {
                dtm.addRow(new Object[]{
                    a.getJudul(),
                    a.getTanggal(),
                    a.getWaktu(),
                    a.getDeskripsi(),
                    a.getStatus()
                });
            }
        }

        // PASANG MODEL BARU
        view.tblAgenda.setModel(dtm);

        // PASANG ULANG LEBAR KOLOM
        setColumnWidth();

        // PASANG ULANG MULTILINE RENDERER
        view.tblAgenda.getColumnModel().getColumn(3)
            .setCellRenderer(new MultiLineTableCellRenderer());

        // AUTO RESIZE TINGGI BARIS
        autoResizeRowHeight();
    }


    // ================= AUTO RESIZE ROW ===========================

    private void autoResizeRowHeight() {
        for (int row = 0; row < view.tblAgenda.getRowCount(); row++) {
            int rowHeight = view.tblAgenda.getRowHeight();

            for (int column = 0; column < view.tblAgenda.getColumnCount(); column++) {
                java.awt.Component comp =
                        view.tblAgenda.prepareRenderer(
                            view.tblAgenda.getCellRenderer(row, column),
                            row, column);

                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            view.tblAgenda.setRowHeight(row, rowHeight);
        }
    }

    // ======================== UPDATE STATISTIK =============================
    
    private void updateStatistik() {
        int total = model.getAll().size();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new java.util.Date());

        int hariIni = 0;
        int mingguIni = 0;

        // Range minggu ini
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String startWeek = sdf.format(cal.getTime());
        cal.add(java.util.Calendar.DATE, 6);
        String endWeek = sdf.format(cal.getTime());

        for (Agenda a : model.getAll()) {

            // Hitung agenda hari ini
            if (a.getTanggal().equals(today)) {
                hariIni++;
            }

            // Hitung agenda minggu ini
            if (a.getTanggal().compareTo(startWeek) >= 0 &&
                a.getTanggal().compareTo(endWeek) <= 0) {
                mingguIni++;
            }
        }

        view.lblTotalAgenda.setText("Total: " + total);
        view.lblAgendaHariIni.setText("Hari ini: " + hariIni);
        view.lblAgendaMingguIni.setText("Minggu ini: " + mingguIni);
    }

    
    // ======================== FILTER TANGGAL =============================
    
     private void filterTanggal() {

        String filter = view.cmbFilter.getSelectedItem().toString();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date now = new java.util.Date();
        String today = sdf.format(now);

        // Minggu ini
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String startWeek = sdf.format(cal.getTime());
        cal.add(java.util.Calendar.DATE, 6);
        String endWeek = sdf.format(cal.getTime());

        // Bulan ini
        java.text.SimpleDateFormat monthFormat = new java.text.SimpleDateFormat("yyyy-MM");
        String thisMonth = monthFormat.format(now);

        DefaultTableModel dtm = new DefaultTableModel(
            new String[]{"Judul", "Tanggal", "Waktu", "Deskripsi", "status"}, 0
        );

        for (Agenda a : model.getAll()) {

            boolean cocok = false;

            switch (filter) {

                case "Semua":
                    cocok = true;
                    break;

                case "Hari Ini":
                    cocok = a.getTanggal().equals(today);
                    break;

                case "Minggu Ini":
                    cocok = a.getTanggal().compareTo(startWeek) >= 0 &&
                             a.getTanggal().compareTo(endWeek) <= 0;
                    break;

                case "Bulan Ini":
                    cocok = a.getTanggal().startsWith(thisMonth);
                    break;
            }

            if (cocok) {
                dtm.addRow(new Object[]{
                    a.getJudul(),
                    a.getTanggal(),
                    a.getWaktu(),
                    a.getDeskripsi(),
                    a.getStatus()
                });
            }
        }

        // PASANG MODEL
        view.tblAgenda.setModel(dtm);

        // PASANG ULANG LEBAR KOLOM
        setColumnWidth();

        // PASANG ULANG MULTILINE WRAP RENDERER
        view.tblAgenda.getColumnModel().getColumn(3)
                .setCellRenderer(new MultiLineTableCellRenderer());

        // AUTO RESIZE ROW HEIGHT
        autoResizeRowHeight();
    }


     // ======================== EXPORT =============================
    private void exportTXT() {
        if (model.getAll().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tidak ada data untuk diexport!");
            return;
        }

        try {
            java.io.PrintWriter out = new java.io.PrintWriter("catatku_export.txt");

            for (Agenda a : model.getAll()) {

                // Baris pertama (HEADER): sudah termasuk STATUS
                out.println(
                    a.getTanggal() + " | " +
                    a.getWaktu() + " | " +
                    a.getJudul() + " | " +
                    a.getStatus()
                );

                // Baris kedua: DESKRIPSI
                out.println(a.getDeskripsi());

                // Baris ketiga: pemisah
                out.println("-----------------------------------");
            }

            out.close();
            JOptionPane.showMessageDialog(view, "Export TXT berhasil!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Export gagal!");
            e.printStackTrace();
        }
    }



 // ======================== IMPORT =============================
    private void importTXT() {
        try {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(view);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();
                Scanner sc = new Scanner(file);

                // Bersihkan data lama
                model.getAll().clear();

                while (sc.hasNextLine()) {

                    String header = sc.nextLine().trim();

                    // Skip baris kosong atau pemisah
                    if (header.isEmpty() || header.startsWith("---")) continue;

                    // Format harus: tanggal | waktu | judul | status
                    String[] parts = header.split("\\|");

                    if (parts.length < 3) continue;

                    String tanggal = parts[0].trim();
                    String waktu   = parts[1].trim();
                    String judul   = parts[2].trim();

                    // Jika status tidak ada → default Belum
                    String status  = (parts.length >= 4)
                            ? parts[3].trim()
                            : "Belum";

                    // Baca deskripsi baris berikutnya
                    if (!sc.hasNextLine()) break;
                    String deskripsi = sc.nextLine().trim();

                    // Skip garis pemisah jika ada
                    if (sc.hasNextLine()) sc.nextLine();

                    // Tambahkan data ke model
                    Agenda a = new Agenda(judul, tanggal, waktu, deskripsi, status);
                    model.tambah(a);
                }

                sc.close();

                loadTable();
                autoResizeRowHeight();
                updateStatistik();

                JOptionPane.showMessageDialog(view, "Import TXT berhasil!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal import TXT!");
            e.printStackTrace();
        }
    }

}