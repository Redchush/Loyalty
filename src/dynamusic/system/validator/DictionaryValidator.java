package dynamusic.system.validator;


import java.util.Dictionary;

public interface DictionaryValidator extends Validator<Dictionary, Void>{

    ValidationResult validate(Dictionary values);
}
