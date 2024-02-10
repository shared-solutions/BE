package friend.spring.validation.validator;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.validation.annotation.ContentTextLimit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ContentTextLimitValidator implements ConstraintValidator<ContentTextLimit,String> {
    @Override
    public void initialize(ContentTextLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid= value.length() >= 5 && value.length() < 1000;
        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.CONTENT_TEXT_LIMIT.getMessage()).addConstraintViolation();
        }

        return isValid;
    }

}
