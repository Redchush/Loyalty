package dynamusic.system.validator.impl;


import dynamusic.system.validator.ValidationResult;
import dynamusic.system.validator.Validator;
import dynamusic.system.validator.ValidatorCallback;

import java.util.Map;

public class ValidateWhileOneFailStrategy extends AbstractStrategy {


    public <T, D> D validateWithLastResult(Iterable<Validator<? super T, D>> validators, T value, ValidatorCallback<D>
            failCallback){
        if (validators != null )
        {
            for (Validator<? super T, D> validator : validators)
            {
                D result = validateWithLastResult(validator, value, failCallback);
                if (result != null)
                {
                    return result;
                }
            }
        } else {
            logError("Validation can't be executed: no validators set!");
        }
        return null;
    }


    public<T,D> D validateWithLastResult(Validator<? super T, D> validator, T value,
                                         ValidatorCallback<D> callback) {
        if (validator != null)
        {
            ValidationResult<D> validationResult = validator.validate(value);
            if (validationResult.getStatus() != ValidationResult.STATUS_OK)
            {
                for (String msg: validationResult.getErrorMsg())
                {
                    if (callback != null)
                    {
                        callback.executeCallback(msg, validationResult.getResult());
                    }
                }
                return validationResult.getResult();
            }
        }
        return null;
    }

    public<T,D> boolean executeValidation(Iterable<Validator<? super T, D>> validators, T value, ValidatorCallback<D>
            failCallback) {
        if (validators != null )
        {
            for (Validator<? super T, D> validator : validators)
            {
                if (!executeValidation(validator, value, failCallback))
                {
                    return false;
                }
            }
        } else {
            logError("Validation can't be executed: no validators set!");
        }
        return true;
    }

    public <T, D> boolean executeValidation(Map<Validator<? super T, D>, ValidatorCallback<D>>
                                                    validatorCallbackMap, T value){
        if (validatorCallbackMap != null )
        {
            for(Map.Entry<Validator<? super T, D>, ValidatorCallback<D>> entry : validatorCallbackMap.entrySet())
            {
                if (!executeValidation(entry.getKey(), value, entry.getValue()))
                {
                    return false;
                }
            }
        } else {
            logError("Validation can't be executed: no validators set!");
        }
        return true;
    }


    public<T,D> boolean executeValidation(Validator<? super T, D> validator, T value,
                                          ValidatorCallback<D> callback) {
        if (validator != null)
        {
            ValidationResult<D> validationResult = validator.validate(value);
            if (validationResult.getStatus() != ValidationResult.STATUS_OK)
            {
                for (String msg: validationResult.getErrorMsg())
                {
                    if (callback != null)
                    {
                        callback.executeCallback(msg, validationResult.getResult());
                    }
                }
                return false;
            }
        }
        return true;
    }


}
