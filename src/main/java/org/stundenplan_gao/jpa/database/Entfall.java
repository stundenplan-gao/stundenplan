package org.stundenplan_gao.jpa.database;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ausfallender_kurs")
public class Entfall {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kursId")
    private Kurs kurs;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stundenId")
    private Stunde stunde;

    Date datum;

    public Entfall() {
    }

    public Entfall(Kurs kurs, Stunde stunde, Date datum) {
        this.kurs = kurs;
        this.stunde = stunde;
        this.datum = datum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Kurs getKurs() {
        return kurs;
    }

    public void setKurs(Kurs entfallender_kurs) {
        this.kurs = entfallender_kurs;
    }

    public Stunde getStunde() {
        return stunde;
    }

    public void setStunde(Stunde stunde) {
        this.stunde = stunde;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entfall entfall = (Entfall) o;
        return Objects.equals(kurs, entfall.kurs) &&
                Objects.equals(stunde, entfall.stunde) &&
                Objects.equals(datum, entfall.datum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kurs, stunde, datum);
    }

    @Override
    public String toString() {
        return "Der Kurs " + kurs.getBezeichnung() + " in der " + stunde.getStunde() + ". Stunde entfällt am " + DateFormat.getDateInstance().format(datum);
    }
}
