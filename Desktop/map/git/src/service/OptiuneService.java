package service;

import domain.Candidat;
import domain.Optiune;
import domain.Sectie;
import javafx.util.Pair;
import repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class OptiuneService extends Service<Pair<Long, Long>, Optiune> {
    private Repository<Long, Sectie> sectieRepository;
    private Repository<Long, Candidat> candidatRepository;

    public OptiuneService(Repository<Pair<Long, Long>, Optiune> repository,
                          Repository<Long, Sectie> sectieRepository,
                          Repository<Long, Candidat> candidatRepository) {
        super(repository);
        this.sectieRepository = sectieRepository;
        this.candidatRepository = candidatRepository;
    }

    public Candidat getCandidatById(long idCandidat) throws Exception {
        return candidatRepository.findById(idCandidat);
    }

    public Sectie getSectieById(long idSectie) throws Exception {
        return sectieRepository.findById(idSectie);
    }

    @Override
    public boolean delete(Optiune optiune) throws Exception {
        if (super.delete(optiune)) {
            optiune.getCandidat().outrollSectii();
            optiune.getSectie().unapply();
            return true;
        }
        return false;
    }

    /**
     * Method keep the repository of Option up-to-date. Verifies all Optiuni and if a Candidat or a Sectie doesn't
        exists anymore , it deletes the Option
     */
    public void maintenance() {
        try {
            List<Optiune> list = (List<Optiune>) super.getAll();
            for (Optiune optiune : list) {

                Sectie sectie = optiune.getSectie();
                Candidat candidat = optiune.getCandidat();
                if (sectieRepository.contains(sectie) && candidatRepository.contains(candidat)) {
                    Sectie realSectie = sectieRepository.findById(sectie.getId());
                    Candidat realCandidat = candidatRepository.findById(candidat.getId());
                    if (!(sectie.hashCode() == realSectie.hashCode()))
                        optiune.setSectie(realSectie);
                    if (!(candidat.hashCode() == realCandidat.hashCode()))
                        optiune.setCandidat(realCandidat);
                    put(optiune);
                } else
                    delete(optiune);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Filters all candidats by a given sectie and orders them alphabetically
     * @param idSectie - sectie's id to compare
     * @return - filtered and sorted list of candidat
     * @throws Exception if sth wrong happens
     */
    public List<Candidat> filterCandidatBySectie(long idSectie) throws Exception{
        Predicate<Optiune> filter=optiune -> optiune.getSectie().getId().equals(idSectie);
        Comparator<Optiune> comparator=(o1,o2)->o1.getCandidat().getNume().compareTo(o2.getCandidat().getNume());
        List<Optiune> list=getAll();
        List<Candidat> result=new ArrayList<>();
        filterAndSorter(list,filter,comparator).forEach(optiune -> result.add(optiune.getCandidat()));
        return result;
    }
    /**
     * Filters all sections by a given candidat and orders them by priority in candidat;s options list
     * @param idCandidat - candidat's id to compare
     * @return - filtered and sorted list of sections
     * @throws Exception if sth wrong happens
     */
    public List<Sectie> filterSectiiByCandidat(long idCandidat) throws Exception{
        Predicate<Optiune> filter=optiune -> optiune.getCandidat().getId().equals(idCandidat);
        Comparator<Optiune> comparator=(o1,o2)->Long.compare(o1.getPriority(),o2.getPriority());
        List<Optiune> list=getAll();
        List<Sectie> result=new ArrayList<>();
        filterAndSorter(list,filter,comparator).forEach(optiune -> result.add(optiune.getSectie()));
        return result;

    }
    /**
     * Filters all sections by a given number of enrolled candidats and orders them by popularity
     * @return - filtered and sorted list of sections
     * @throws Exception if sth wrong happens
     */
    public List<Optiune> getFirstPriority() throws Exception{
        Predicate<Optiune> filter=optiune -> optiune.getPriority()==1;
        Comparator<Optiune> comparator=(o1,o2)->Long.compare(o1.getSectie().getAplicanti(),o2.getSectie().getAplicanti());
        List<Optiune> list=getAll();
        return filterAndSorter(list,filter,comparator);
    }

    public Repository<Long, Sectie> getSectieRepository() {
        return sectieRepository;
    }

    public Repository<Long, Candidat> getCandidatRepository() {
        return candidatRepository;
    }
}
