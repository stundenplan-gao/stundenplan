package org.stundenplan_gao.jpa.database;

import org.stundenplan_gao.rest.server.PasswordHash;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "schueler")
public class Schueler {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stufenId")
    private Stufe stufe;

    private String benutzername;

    @Column(name = "passwort")
    private String passwortHash;

    private String salt;
    private String vorname;
    private String nachname;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "schueler_kurs",
            joinColumns = @JoinColumn(name = "schuelerId"),
            inverseJoinColumns = @JoinColumn(name = "kursId")
    )
    private Set<Kurs> kurse;

    public Schueler() {
    }

    public Schueler(Stufe stufe, String benutzername, String passwortHash, String vorname, String nachname) {
        this.stufe = stufe;
        this.benutzername = benutzername;
        this.passwortHash = passwortHash;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Schueler(NeuerNutzer nutzer, Stufe empty) {
        this.vorname = nutzer.getVorname();
        this.nachname = nutzer.getNachname();
        this.benutzername = nutzer.getBenutzername();
        this.salt = PasswordHash.generateSalt();
        this.passwortHash = PasswordHash.computeHash(nutzer.getPasswort(), salt);
        this.stufe = empty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Stufe getStufe() {
        return stufe;
    }

    public void setStufe(Stufe stufe) {
        this.stufe = stufe;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String username) {
        this.benutzername = username;
    }

    public String getPasswortHash() {
        return passwortHash;
    }

    public void setPasswortHash(String password) {
        this.passwortHash = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String firstname) {
        this.vorname = firstname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String lastname) {
        this.nachname = lastname;
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
        Schueler schueler = (Schueler) o;
        return Objects.equals(id, schueler.id) &&
                Objects.equals(stufe, schueler.stufe) &&
                Objects.equals(benutzername, schueler.benutzername) &&
                Objects.equals(passwortHash, schueler.passwortHash) &&
                Objects.equals(vorname, schueler.vorname) &&
                Objects.equals(nachname, schueler.nachname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stufe, benutzername, passwortHash, vorname, nachname);
    }

    public String toFullString() {
        String header = vorname + " " + nachname + ", " + stufe.getStufe() + " (" + benutzername + "," + passwortHash + ")\n";
        for (Kurs kurs : kurse) {
            header += "----------------------\n" + kurs + "\n";
        }
        return header;
    }

    @Override
    public String toString() {
        return vorname + " " + nachname + ", " + stufe.getStufe();
    }
}
