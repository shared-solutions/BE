package friend.spring.validation.validator;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.validation.annotation.DeadlineLimit;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DeadlineLimitValidator implements ConstraintValidator<DeadlineLimit, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime deadline, ConstraintValidatorContext context) {
        if (deadline == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        long minutesUntilDeadline = ChronoUnit.MINUTES.between(now, deadline);
        long daysUntilDeadline = ChronoUnit.DAYS.between(now, deadline);

        boolean isValid = minutesUntilDeadline >= 1 && daysUntilDeadline <= 30;
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.DEADLINE_LIMIT.getMessage()).addConstraintViolation();
        }
        return isValid;
    }
}
