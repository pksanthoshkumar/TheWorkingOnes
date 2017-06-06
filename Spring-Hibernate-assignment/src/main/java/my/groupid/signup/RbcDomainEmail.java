package my.groupid.signup;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.logging.Logger;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.stereotype.Component;

@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = my.groupid.signup.RbcDomainEmailValidator.class)
@Documented
public @interface RbcDomainEmail {
	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

@Component
class RbcDomainEmailValidator implements ConstraintValidator<my.groupid.signup.RbcDomainEmail, String> {

	@Override
	public void initialize(RbcDomainEmail constraintAnnotation) {}
	
	static Logger log = Logger.getLogger(RbcDomainEmailValidator.class.getName());

	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		log.info("Checking if this is a valid email for " + value);
		return value.endsWith("@rbc.com");
	}
}
