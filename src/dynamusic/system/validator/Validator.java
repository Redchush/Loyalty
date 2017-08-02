package dynamusic.system.validator;


public interface Validator<T, D> {

    ValidationResult<D> validate(T value);

}
