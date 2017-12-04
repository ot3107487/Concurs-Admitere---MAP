package service;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import domain.Sectie;
import repository.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SectieService extends Service<Long, Sectie> {

    public SectieService(Repository<Long, Sectie> repository) {
        super(repository);
    }

    /**
     * Filters by a given language of teaching and sorts them by number of attendances
     * @param language -language to filter
     * @return - filtered and ordered list
     * @throws Exception if sth wrong happens
     */
    public List<Sectie> filterByLanguage(String language) throws Exception {
        Predicate<Sectie> filter = sectie -> sectie.getNume().toUpperCase().endsWith(language.toUpperCase());
        Comparator<Sectie> comparator = (s1, s2) -> Long.compare(s1.getAplicanti(), s2.getAplicanti());
        List<Sectie> list = getAll();
        return filterAndSorter(list, filter, comparator);
    }
    public List<Sectie> filterByNume(String nume) throws Exception{
        Predicate<Sectie> filter=sectie -> sectie.getNume().toUpperCase().contains(nume.toUpperCase());
        Comparator<Sectie> comparator=(s1,s2)->s1.getNume().compareTo(s2.getNume());
        List<Sectie> list=getAll();
        return filterAndSorter(list,filter,comparator);
    }

    /**
     * Filters by a given numarLocuri and sorts them by numarLocuri
     * @param nrLocuri - number to comapare
     * @return filtered and sorted list
     * @throws Exception if sth wrong happens
     */
    public List<Sectie> filterByNumarLocuri(int nrLocuri) throws Exception {
        Predicate<Sectie> filter = sectie -> sectie.getNumarLocuri() >= nrLocuri;
        Comparator<Sectie> comparator = (s1, s2) -> Integer.compare(s1.getNumarLocuri(), s2.getNumarLocuri());
        List<Sectie> list = getAll();
        return filterAndSorter(list, filter, comparator);
    }

    /**
     * Filters by number of attendances snd sorts them alphabetically
     * @param aplicanti - number of attendances to compare with
     * @return filtered and sorted list of sectii
     * @throws Exception if sth wrong happens
     */
    public List<Sectie> filterByAplicanti(int aplicanti) throws Exception {
        Predicate<Sectie> filter = sectie -> sectie.getAplicanti() >=aplicanti;
        Comparator<Sectie> comparator = (s1, s2) -> s1.getNume().compareTo(s2.getNume());
        List<Sectie> list = getAll();
        return filterAndSorter(list, filter, comparator);
    }

}
