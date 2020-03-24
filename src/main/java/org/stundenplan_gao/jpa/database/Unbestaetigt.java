package org.stundenplan_gao.jpa.database;

import org.stundenplan_gao.rest.server.PasswordHash;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "unbestaetigter_nutzer")
public class Unbestaetigt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String vorname;
    private String nachname;
    private String benutzername;

    @Column(name = "passwort")
    private String passwortHash;

    private String salt;
    private String bestaetigungs_schluessel;

    public Unbestaetigt() {
    }

    public Unbestaetigt(String vorname, String nachname, String benutzername, String passwortHash, String salt, String bestaetigungs_schluessel) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.benutzername = benutzername;
        this.passwortHash = passwortHash;
        this.salt = salt;
        this.bestaetigungs_schluessel = bestaetigungs_schluessel;
    }

    public Unbestaetigt(NeuerNutzer nutzer) {
        this.vorname = nutzer.getVorname();
        this.nachname = nutzer.getNachname();
        this.benutzername = nutzer.getBenutzername();
        this.salt = PasswordHash.generateSalt();
        this.passwortHash = PasswordHash.computeHash(nutzer.getPasswort(), salt);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getPasswortHash() {
        return passwortHash;
    }

    public void setPasswortHash(String passwortHash) {
        this.passwortHash = passwortHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getBestaetigungs_schluessel() {
        return bestaetigungs_schluessel;
    }

    public void setBestaetigungs_schluessel(String bestaetigungs_schluessel) {
        this.bestaetigungs_schluessel = bestaetigungs_schluessel;
    }

    @Override
    public String toString() {
        return benutzername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unbestaetigt that = (Unbestaetigt) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(vorname, that.vorname) &&
                Objects.equals(nachname, that.nachname) &&
                Objects.equals(benutzername, that.benutzername) &&
                Objects.equals(passwortHash, that.passwortHash) &&
                Objects.equals(bestaetigungs_schluessel, that.bestaetigungs_schluessel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vorname, nachname, benutzername, passwortHash, bestaetigungs_schluessel);
    }
}
