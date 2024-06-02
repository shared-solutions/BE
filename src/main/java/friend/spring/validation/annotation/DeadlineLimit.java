package friend.spring.validation.annotation;

import friend.spring.validation.validator.DeadlineLimitValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeadlineLimitValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeadlineLimit {
    String message() default "Deadline must be between 1 minute and 30 days from now.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
