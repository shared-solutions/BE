package friend.spring.validation.validator;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.validation.annotation.TitleTextLimit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class TitleTextLimitValidator implements ConstraintValidator<TitleTextLimit, String> {

    @Override
    public void initialize(TitleTextLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = value.length() >= 5 && value.length() < 30;
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.TITLE_TEXT_LIMIT.getMessage()).addConstraintViolation();
        }

        return isValid;
    }
}
