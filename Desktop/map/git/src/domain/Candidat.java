package domain;

import repository.HasId;

public class Candidat implements Cloneable, HasId<Long> {
    private int sectiiAplicate=0;
    private long id;
    private String nume;
    private String telefon;
    private String mail;
    private String sex;

    public Candidat(long id, String nume, String telefon, String mail, String sex) {
        this.id = id;
        this.nume = nume;
        this.telefon = telefon;
        this.mail = mail;
        this.sex = sex;
    }

    public Integer getSectiiAplicate() {
        return sectiiAplicate;
    }

    //overriding clone() from cloneable
    public Object clone() throws CloneNotSupportedException {
        return (Candidat) super.clone();
    }
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

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int enrollCandidat(){
        return ++this.sectiiAplicate;
    }
    public void outrollSectii(){
        this.sectiiAplicate--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candidat candidat = (Candidat) o;

        return id == candidat.id;
    }

    @Override
    public int hashCode() {
        int result = nume.hashCode();
        result = 31 * result + telefon.hashCode();
        result = 31 * result + mail.hashCode();
        result = 31 * result + sex.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Long.toString(id)+";"+nume+";"+telefon+";"+mail+";"+sex;
    }
}