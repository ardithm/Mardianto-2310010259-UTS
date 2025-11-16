package model;

/**
 * BaseAgenda
 * Kelas abstrak yang menjadi dasar agenda.
 * Menggunakan Abstraction dan Polymorphism (abstract method).
 */

public abstract class BaseAgenda {
    // Method abstrak, akan dioverride oleh class Agenda
    public abstract String getRingkasan();
}
