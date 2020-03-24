package org.jgrosshardt.jpa.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "stufe")
public class Stufe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String stufe;

    @OneToMany(mappedBy = "stufe", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Schueler> schueler;

    public Stufe() {
    }

    public Stufe(String stufe) {
        this.stufe = stufe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStufe() {
        return stufe;
    }

    public void setStufe(String stufe) {
        this.stufe = stufe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stufe stufe1 = (Stufe) o;
        return Objects.equals(id, stufe1.id) &&
                Objects.equals(stufe, stufe1.stufe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stufe);
    }

    @Override
    public String toString() {
        return stufe;
    }
}
