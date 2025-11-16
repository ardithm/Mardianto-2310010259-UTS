# 📝 CatatKu – Aplikasi Agenda Pribadi  
Aplikasi desktop berbasis **Java Swing** untuk mencatat, mengelola, dan memantau agenda harian, mingguan, dan bulanan.  
Dibuat sebagai tugas **Ujian Tengah Semester (UTS) – Pemrograman Berorientasi Objek 2 (PBO)**.

---

## 🎯 Fitur Utama

### 🔹 CRUD Lengkap  
- Tambah agenda  
- Edit agenda  
- Hapus agenda  
- Tampil otomatis di tabel  

### 🔹 Pencarian Real-Time  
Filter judul agenda secara langsung saat pengguna mengetik.

### 🔹 Filter Tanggal  
- Semua  
- Hari Ini  
- Minggu Ini  
- Bulan Ini  

### 🔹 Status Agenda  
- **Belum**  
- **Selesai**  

### 🔹 Statistik Agenda  
- Total agenda  
- Agenda hari ini  
- Agenda minggu ini  

### 🔹 Import & Export TXT (BONUS FITUR UTS)  
Backup agenda ke file TXT, dan bisa diimpor kembali.

### 🔹 Fitur Tambahan  
- Auto resize row untuk deskripsi panjang  
- Wrap text pada kolom Deskripsi  
- Form otomatis reset setelah tambah/update  
- Konfirmasi sebelum hapus  
- Double-click row untuk edit  
- Sort data berdasarkan tanggal  
- Menggunakan **JDateChooser** dan **TimePicker (JSpinner)**

---

## 🏗️ Arsitektur Aplikasi (MVC)

Aplikasi ini menerapkan pola **Model – View – Controller** agar lebih rapi, terstruktur, dan mudah dikembangkan.

### 📁 Model  
Menyimpan dan mengelola data agenda.  
Class penting:  
- `BaseAgenda.java`  
- `Agenda.java`  
- `AgendaModel.java`

### 🖥️ View  
Menangani tampilan GUI menggunakan Java Swing.  
Class:  
- `AgendaView.java`

### 🎮 Controller  
Menangani logika aplikasi, event tombol, sinkronisasi View ↔ Model.  
Class:  
- `AgendaController.java`

---

## 📦 Struktur Folder
```
src/
├── model/
│   ├── BaseAgenda.java
│   ├── Agenda.java
│   └── AgendaModel.java
├── view/
│   └── AgendaView.java
└── controller/
    └── AgendaController.java
```
## 🛠️ Teknologi yang Digunakan

- **Java Swing** (GUI)
- **OOP (Object-Oriented Programming)**
- **JDateChooser (JCalendar 1.4)** untuk pemilihan tanggal  
- **JSpinner TimePicker** untuk pemilihan waktu  
- **ArrayList** sebagai penyimpanan data  
- **TXT Processing** (import & export)

---

## 📂 Format File Export/Import

Contoh format file TXT yang dihasilkan aplikasi:

```
2025-01-19 | 10:30 | Meeting Proyek | Belum
Diskusi modul dan timeline.
-----------------------------------

2025-01-25 | 14:00 | Kuliah PBO | Selesai
Materi interface dan collections.
-----------------------------------
```

**Keterangan format:**  
- Baris 1 : `Tanggal | Waktu | Judul | Status`  
- Baris 2 : Deskripsi  
- Garis pemisah sebagai pembatas antar agenda

---

## ▶️ Cara Menjalankan

1. Clone repository atau download source code  
2. Buka project di **NetBeans / IntelliJ / Eclipse**  
3. Tambahkan library **JCalendar-1.4.jar**  
4. Jalankan `AgendaView.java`  
5. Aplikasi siap digunakan  

---

## ✨ Kelebihan Aplikasi CatatKu

- ✔ Menggunakan arsitektur **MVC**  
- ✔ Fitur lebih lengkap dari standar UTS  
- ✔ Statistik agenda otomatis  
- ✔ UI rapi dan user-friendly  
- ✔ Text area wrap dan auto-row resize  
- ✔ Import–Export TXT (bonus nilai)  
- ✔ Validasi input & dialog konfirmasi
- ✔ Fitur filter tanggal & pencarian real-time
- ✔ Komponen modern (DatePicker & TimePicker)

---

## 🧩 Konsep PBO yang Diterapkan

- **Class & Object**  
- **Encapsulation** (getter/setter)
- **Inheritance**  
- **Abstraction (MVC)**  
- **Polymorphism (opsional)**
- **ArrayList & Data Structure**
- **Event Handling Controller**

---

## 👤 Pengembang

**Nama:** Mardianto  
**NPM:** 2310010259  
**Kelas:** 5B  
**Mata Kuliah:** Pemrograman Berbasis Objek 2  

---
