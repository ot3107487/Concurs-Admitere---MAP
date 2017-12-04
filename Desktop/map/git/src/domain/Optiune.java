package domain;

import javafx.util.Pair;
import repository.HasId;

public class Optiune implements Cloneable, HasId<Pair<Long,Long>> {
    private Pair<Long,Long> id;
    private Candidat candidat;
    private Sectie sectie;
    private int priority;

    public Optiune(Candidat candidat,Sectie sectie) {
        this.candidat = candidat;
        this.sectie = sectie;
        this.priority = candidat.enrollCandidat();
        this.id=new Pair<Long,Long>(candidat.getId(),sectie.getId());
        this.sectie.applyFor();
    }

    //overriding clone() from cloneable
    public Object clone() throws CloneNotSupportedException {
        return (Optiune) super.clone();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public Pair<Long,Long> getId() {
        return this.id;

    }

    @Override
    public void setId(Pair<Long,Long> id) {
        this.id=id;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(domain.Candidat candidat) {
        this.candidat = candidat;
    }

    public Sectie getSectie() {
        return sectie;
    }

    public void setSectie(domain.Sectie sectie) {
        this.sectie = sectie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Optiune optiune = (Optiune) o;

        return id.equals(optiune.id);
    }

    @Override
    public String toString() {
        return candidat.getNume()+" - "
                + sectie.getNume()+"("+Integer.toString(priority)+")";
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}