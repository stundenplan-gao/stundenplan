package org.jgrosshardt.jpa.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "fach")
public class Fach {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String fach;
    private String kuerzel;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "fach_lehrer",
            joinColumns = @JoinColumn(name = "lehrerId"),
            inverseJoinColumns = @JoinColumn(name = "fachId")
    )
    @JsonIgnore
    private Set<Lehrer> lehrer;

    public Fach() {
    }

    public Fach(String fach, String kuerzel) {
        this.fach = fach;
        this.kuerzel = kuerzel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String shorthand) {
        this.kuerzel = shorthand;
    }

    public Set<Lehrer> getLehrer() {
        return lehrer;
    }

    public void setLehrer(Set<Lehrer> lehrerSet) {
        this.lehrer = lehrerSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fach fach1 = (Fach) o;
        return Objects.equals(id, fach1.id) &&
                Objects.equals(fach, fach1.fach) &&
                Objects.equals(lehrer, fach1.lehrer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fach, lehrer);
    }

    @Override
    public String toString() {
        return fach;
    }
}
