package dynamusic.system.validator;


import java.util.Map;

public interface MapValidator extends Validator<Map, Void> {

    ValidationResult<Void> validate(Map values);
}
