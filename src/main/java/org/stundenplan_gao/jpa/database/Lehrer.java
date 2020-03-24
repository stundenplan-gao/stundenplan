package org.stundenplan_gao.jpa.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "lehrer")
public class Lehrer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String nachname;
    private String anrede;
    private String kuerzel;

    @ManyToMany(mappedBy = "lehrer", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Fach> faecher;

    @OneToMany(mappedBy = "lehrer", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Kurs> kurse;

    public Lehrer() {
    }

    public Lehrer(String nachname, String anrede, String kuerzel) {
        this.nachname = nachname;
        this.anrede = anrede;
        this.kuerzel = kuerzel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public Set<Fach> getFaecher() {
        return faecher;
    }

    public void setFaecher(Set<Fach> faecher) {
        this.faecher = faecher;
    }

    public Set<Kurs> getKurse() {
        return kurse;
    }

    public void setKurse(Set<Kurs> kurse) {
        this.kurse = kurse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lehrer lehrer = (Lehrer) o;
        return id == lehrer.id &&
                Objects.equals(anrede, lehrer.anrede) &&
                Objects.equals(nachname, lehrer.nachname) &&
                Objects.equals(kuerzel, lehrer.kuerzel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nachname, anrede, kuerzel);
    }

    @Override
    public String toString() {
        return anrede + " " + nachname;
    }
}
