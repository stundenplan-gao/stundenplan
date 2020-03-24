package org.stundenplan_gao.jpa.database;

import java.util.Objects;

public class NeuerNutzer {

    private String vorname;
    private String nachname;
    private String benutzername;
    private String passwort;

    public NeuerNutzer() {
    }

    public NeuerNutzer(String vorname, String nachname, String benutzername, String passwort) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.benutzername = benutzername;
        this.passwort = passwort;
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

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeuerNutzer that = (NeuerNutzer) o;
        return Objects.equals(vorname, that.vorname) &&
                Objects.equals(nachname, that.nachname) &&
                Objects.equals(benutzername, that.benutzername) &&
                Objects.equals(passwort, that.passwort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vorname, nachname, benutzername, passwort);
    }

    @Override
    public String toString() {
        return benutzername + " : " + vorname + " " + nachname;
    }
}
