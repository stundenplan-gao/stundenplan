package org.stundenplan_gao.jpa.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "stunde")
public class Stunde {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private short tag;
    private short stunde;

    @ManyToMany(mappedBy = "stunden")
    @JsonIgnore
    private Set<Kurs> kurse;

    public Stunde() {
    }

    public Stunde(short tag, short stunde) {
        this.tag = tag;
        this.stunde = stunde;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getTag() {
        return tag;
    }

    public void setTag(short tag) {
        this.tag = tag;
    }

    public int getStunde() {
        return stunde;
    }

    public void setStunde(short stunde) {
        this.stunde = stunde;
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
        Stunde stunde1 = (Stunde) o;
        return tag == stunde1.tag &&
                stunde == stunde1.stunde &&
                Objects.equals(id, stunde1.id) &&
                Objects.equals(kurse, stunde1.kurse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, stunde, kurse);
    }

    @Override
    public String toString() {
        return "" + tagFromInt(tag) + ", " + stunde + ". Stunde";
    }

    private static String tagFromInt(short tag) {
        switch (tag) {
        case 1:
            return "Mo";
        case 2:
            return "Di";
        case 3:
            return "Mi";
        case 4:
            return "Do";
        case 5:
            return "Fr";
        case 6:
            return "Sa";
        case 7:
            return "So";
        default:
            return "##";
        }
    }
}
