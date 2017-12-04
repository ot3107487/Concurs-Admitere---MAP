package service;

import domain.Candidat;
import repository.Repository;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observable;
import utils.Observer;

import java.util.*;
import java.util.function.Predicate;

public class CandidatService extends Service<Long, Candidat>{
    public CandidatService(Repository<Long, Candidat> repository) {
        super(repository);
    }


    public List<Candidat> filterByMail(String mail) throws Exception {
        Predicate<Candidat> filter = candidat -> candidat.getMail().toUpperCase().contains(mail.toUpperCase());
        Comparator<Candidat> comparator = (c1, c2) -> c1.getNume().compareTo(c2.getNume());
        List<Candidat> list = getAll();
        return filterAndSorter(list, filter, comparator);
    }

    public List<Candidat> filterByNume(String nume) throws Exception{
        Predicate<Candidat> filter=candidat -> candidat.getNume().toUpperCase().contains(nume.toUpperCase());
        Comparator<Candidat> comparator=(c1,c2)->c1.getNume().compareTo(c2.getNume());
        List<Candidat> list=getAll();
        return filterAndSorter(list,filter,comparator);
    }

    /**
     * Filters candidats by sex and sorts them alphabetically
     *
     * @param sex - candidat's sex
     * @return filtered and sorted list
     * @throws Exception if somethin went wrong
     */
    public List<Candidat> filterBySex(String sex) throws Exception {
        Predicate<Candidat> filter = candidat -> candidat.getSex().toUpperCase().contains(sex.toUpperCase());
        Comparator<Candidat> comparator = (c1, c2) -> c1.getNume().compareTo(c2.getNume());
        List<Candidat> list = getAll();
        return filterAndSorter(list, filter, comparator);
    }

    /**
     * Filters ...whatever
     *
     * @return
     * @throws Exception
     */
    public List<Candidat> filterByPhone(String phone) throws Exception {
        Predicate<Candidat> filter = candidat -> candidat.getTelefon().contains(phone);
        Comparator<Candidat> comparator = (c1, c2) -> c1.getId().compareTo(c2.getId());
        List<Candidat> list = getAll();
        return filterAndSorter(list, filter, comparator);
    }

    public List<Candidat> filterByAll(String key) throws Exception{
        List<Candidat> byNume=filterByNume(key);
        List<Candidat> bySex=filterBySex(key);
        List<Candidat> byPhone=filterByPhone(key);
        List<Candidat> byMail=filterByMail(key);
        Set<Candidat> set=new HashSet<>();
        List<Candidat> result=new ArrayList<>();
        set.addAll(byNume);
        set.addAll(byPhone);
        set.addAll(bySex);
        set.addAll(byMail);
        result.addAll(set);
        return result;
    }
}
