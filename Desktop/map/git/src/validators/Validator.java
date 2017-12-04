package validators;

public interface Validator<T> {
    boolean isValid(T entity) throws Exception;
}
