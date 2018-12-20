package com.sec.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sec.entity.User;
import com.sec.service.UserService;

@Component
public class UserValidator implements Validator {
	
	private UserService userService;
	
	@Autowired
	public UserValidator(UserService userService) {
		this.userService = userService;
	}
	

	@Override
	public boolean supports(Class<?> clazz) {
//		return User.class.equals(clazz);
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
		if (user.getEmail() != "" && (user.getEmail().length() < 5 || !(user.getEmail().contains("@")) || !(user.getEmail().contains(".")))) {
			errors.rejectValue("email", "Size.user.email");
		}
		if (userService.findByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.user.email");
        }
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (!user.getConfirmPassword().equals(user.getPassword())) {
            errors.rejectValue("confirmPassword", "Diff.user.confirmPassword");
        }
		
	}

}
