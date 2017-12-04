package repository;

import validators.Validator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryRepository<Id, T extends Cloneable&HasId<Id>> implements Repository<Id, T> {
    //List implementation
    private List<T> list;
    private Validator<T> validator;

    public InMemoryRepository(Validator<T> validator) {
        this.list = new ArrayList<T>();
        this.validator = validator;
    }

    /**
     * Saves an elem into list. If elem already exists , throws exception
     * @param elem - the element that has to be saved
     */
    public void save(T elem) throws Exception {
        if (list.contains(elem))
            throw new Exception("Element existent");
        if (validator.isValid(elem))
            list.add(elem);
    }
    public void put(T elem) throws Exception{
        if(validator.isValid(elem)){
            list.set(list.indexOf(elem),elem);
        }
        else throw new Exception("Element inexistent");
    }

    /**
     * Function that deletes an elem from list
     *
     * @param elem - the element that has to be deleted
     * @return true if the specified element was found into the list
     * @throws Exception if the element is null
     */
    public boolean delete(T elem) throws Exception {
        return list.remove(elem);
    }

    /**
     * Deletes an element on a given position
     * @param index - position in list
     * @return deleted element
     * @throws Exception if index is invalid
     */
    public Optional<T> deleteAt(int index){
        return Optional.ofNullable(list.remove(index));
    }

    /**
     * Retrieves an elem which id's matching id
     * @param id - id to match
     * @return elemenet with id id
     */
    @Override
    public T findById(Id id) throws Exception {
        for(T elem : list)
            if(elem.getId().equals(id))
                return elem;
        throw new Exception("Element not found");
    }

    /**
     * Get an element from repository at specified index
     *
     * @param index
     * @return element found at position index
    */
    public Optional<T> getOne(int index){
        return Optional.ofNullable(list.get(index));
    }

    /**
     * Verify if an element exists in list
     *
     * @param elem - the verified elemt
     * @return true - if elem is in list
     * false - otherwise
     */
    public boolean contains(T elem) {
        return list.contains(elem);
    }

    /**
     * Retrieve all elements from list
     *
     * @return Iterable - an iterable with all copied elements from list
     */
    public Iterable<T> getAll() throws Exception{
        List<T> readList = new ArrayList<T>();
        Method clone = Object.class.getDeclaredMethod("clone");
        clone.setAccessible(true);

        for (T elem : list)
            readList.add((T) clone.invoke(elem));
        return readList;
    }

    /**
     * See the number of elements in list
     *
     * @return repository's size as int
     */
    public int size() {
        return list.size();
    }

}
