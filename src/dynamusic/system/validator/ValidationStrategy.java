package dynamusic.system.validator;


import java.util.Map;

public interface ValidationStrategy {

    <T, D> D validateWithLastResult(Iterable<Validator<? super T, D>> validators, T value, ValidatorCallback<D>
            failCallback);

    <T,D> D validateWithLastResult(Validator<? super T, D> validator, T value,
                                   ValidatorCallback<D> callback);

    <T, D> boolean executeValidation(Iterable<Validator<? super T, D>> validators,
                                     T value,
                                     ValidatorCallback<D> failCallback);

    <T, D> boolean executeValidation(Validator<? super T, D>[] validators,
                              T value,
                              ValidatorCallback<D> failCallback);

    <T, D> boolean executeValidation(Map<Validator<? super T, D>, ValidatorCallback<D>>
                                             validatorCallbackMap, T
                                             value);

    <T, D> boolean executeValidation(Validator<? super T, D> validator, T value, ValidatorCallback<D> failCallback);

}
