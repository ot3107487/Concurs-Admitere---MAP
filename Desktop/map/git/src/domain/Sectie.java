package domain;

import repository.HasId;

public class Sectie implements Cloneable, HasId<Long> {
    private long aplicanti=0;
    private long id;
    private String nume;
    private int numarLocuri;

    public Sectie(long id, String nume, int numarLocuri) {
        this.id = id;
        this.nume = nume;
        this.numarLocuri = numarLocuri;
    }

    //overriding clone() from cloneable
    public Object clone() throws CloneNotSupportedException {
        return (Sectie) super.clone();
    }

    public long applyFor(){
        return ++aplicanti;
    }
    public void unapply(){aplicanti--;}
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(int numarLocuri) {
        this.numarLocuri = numarLocuri;
    }

    public long getAplicanti() {
        return aplicanti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sectie sectie = (Sectie) o;

        return id == sectie.id;
    }

    @Override
    public String toString() {
        return
                id +
                " " + nume +
                " " + numarLocuri;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
