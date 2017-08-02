package dynamusic.system.validator.impl;


import atg.nucleus.logging.ApplicationLoggingImpl;
import dynamusic.system.validator.ValidationStrategy;
import dynamusic.system.validator.Validator;
import dynamusic.system.validator.ValidatorCallback;

import java.util.Arrays;

public abstract class AbstractStrategy extends ApplicationLoggingImpl implements ValidationStrategy {

    public<T, D> boolean executeValidation(Validator<? super T, D>[] validators, T value, ValidatorCallback<D>
            callback) {
        return executeValidation(Arrays.asList(validators), value, callback);
    }
}
