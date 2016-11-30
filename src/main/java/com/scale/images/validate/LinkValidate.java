package com.scale.images.validate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.scale.images.form.LinkLoadImagesScaleForm;
	
@Component
public class LinkValidate implements Validator{
	
	final static Integer DEFAULT_YEAR = 2016;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return LinkLoadImagesScaleForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		LinkLoadImagesScaleForm form = (LinkLoadImagesScaleForm) obj;
		if (form.getFolder().isEmpty()) {
			errors.rejectValue("folder","error.folder","Link không hợp lệ");
		} else {
			if (!form.getFolder().equalsIgnoreCase("original")) {
				String[] folder = form.getFolder().split("_");
				if(folder.length != 3) {
					errors.rejectValue("folder","error.folder","Link không hợp lệ");
				} else {
					int width = 0;
					int height = 0;
					try {
						width = Integer.parseInt(folder[1]);
						height = Integer.parseInt(folder[2]);
					} catch (Exception e) {
					}
					if (width < 10 || height < 10) {
						errors.rejectValue("folder","error.folder","Link không hợp lệ");
					}
				}
			}
		}
		if (form.getYear() < DEFAULT_YEAR) {
			errors.rejectValue("year","error.year","Link không hợp lệ");
		}
		if (form.getMonth() < 1 || form.getMonth() > 12) {
			errors.rejectValue("month","error.month","Link không hợp lệ");
		}
		if (form.getDay() < 1 || form.getDay() > 31) {
			errors.rejectValue("day","error.day","Link không hợp lệ");
		}
		if (form.getName().isEmpty()) {
			errors.rejectValue("name","error.name","Link không hợp lệ");
		} 
		if (form.getType().isEmpty()){
			errors.rejectValue("name","error.name","Link không hợp lệ");
		} else {
			if (   form.getType().equalsIgnoreCase("png") 
				|| form.getType().equalsIgnoreCase("jpg") 
				|| form.getType().equalsIgnoreCase("jpeg")
				|| form.getType().equalsIgnoreCase("gif")) {
			} else {
				errors.rejectValue("name","error.name","Link không hợp lệ");
			}
		}
	}

}
