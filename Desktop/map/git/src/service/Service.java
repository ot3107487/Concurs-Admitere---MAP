package service;

import repository.Repository;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observable;
import utils.Observer;

import javax.xml.stream.StreamFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Service<Id, T> implements Observable<T> {
    protected Repository<Id, T> repository;
    ArrayList<Observer<T>> observers = new ArrayList<>();

    public Service(Repository<Id, T> repository) {
        this.repository = repository;
    }

    /**
     * Saves an element in repository
     *
     * @param elem - element to be saved
     * @throws Exception if the element is not valid
     */
    public void save(T elem) throws Exception {
        repository.save(elem);
        ListEvent<T> ev = createEvent(ListEventType.ADD, elem, getAll());
        notifyObservers(ev);
    }

    /**
     * Deletes an element from repository
     *
     * @param elem element to be deleted
     * @return true if the element was successfully deleted
     * false - otherwise
     * @throws Exception if elem is null
     */
    public boolean delete(T elem) throws Exception {
        if (repository.delete(elem)) {
            ListEvent<T> ev = createEvent(ListEventType.REMOVE, elem, getAll());
            notifyObservers(ev);
            return true;
        }
        return false;

    }

    public Optional<T> deleteAt(int index) {
        return repository.deleteAt(index);
    }

    /**
     * Get an element from repository
     *
     * @param index - position of element in repository
     * @return - retrieved element
     * @throws Exception if index is invalid
     */
    public Optional<T> getOne(int index) {
        return repository.getOne(index);
    }

    public T findById(Id id) throws Exception {
        return repository.findById(id);
    }

    /**
     * Verify if repository contains an element
     *
     * @param elem - element to verif
     * @return true - if repository contains elem
     * false - otherwise
     */
    public boolean contains(T elem) {
        return repository.contains(elem);
    }

    /**
     * Retrievs all elements from repository
     *
     * @return an an iterable with all copied elements from repository
     */
    public List<T> getAll() throws Exception {
        return (List<T>) repository.getAll();
    }

    public void put(T elem) throws Exception {
        repository.put(elem);
        ListEvent<T> ev = createEvent(ListEventType.UPDATE, elem, getAll());
        notifyObservers(ev);
    }

    /**
     * See the number of elements in list
     *
     * @return repository's size as int
     */

    public int size() {
        return repository.size();
    }

    /**
     * Filter and sort a list by given filter and sorter
     *
     * @param list       - querried list
     * @param predicate  - filter condition
     * @param comparator - sorting condition
     * @return filtered and sorted list
     */
    public List<T> filterAndSorter(List<T> list, Predicate<T> predicate, Comparator<T> comparator) {
        List<T> filteredList = list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
        filteredList.sort(comparator);
        return (List<T>) filteredList;
    }

    @Override
    public void addObserver(Observer<T> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<T> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<T> event) {
        observers.forEach(x -> x.notifyEvent(event));
    }

    private ListEvent<T> createEvent(ListEventType type, final T elem, final Iterable<T> l) {
        return new ListEvent<T>(type) {
            @Override
            public Iterable<T> getList() {
                return l;
            }

            @Override
            public T getElement() {
                return elem;
            }
        };
    }
}