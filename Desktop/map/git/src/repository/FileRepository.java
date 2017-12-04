package repository;

import validators.Validator;

import java.util.Optional;

public  abstract class FileRepository<Id, T extends Cloneable&HasId<Id>> extends InMemoryRepository<Id, T> {
    private String fileName;

    public FileRepository(Validator<T> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * read entities from specified file
     */

    public abstract void readFromFile();

    /**
     * write entitites to specified file . Rewrites all
     */
    protected abstract void writeToFile();

    @Override
    public void save(T elem) throws Exception {
        super.save(elem);
        writeToFile();
    }

    @Override
    public boolean delete(T elem) throws Exception {
        boolean response = super.delete(elem);
        writeToFile();
        return response;
    }

    @Override
    public Optional<T> deleteAt(int index){
        Optional<T> deletedElement=super.deleteAt(index);
        writeToFile();
        return deletedElement;
    }

    @Override
    public void put(T elem) throws Exception{
        super.put(elem);
        writeToFile();
    }
}
