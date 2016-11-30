package com.scale.images.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.scale.images.form.LinkLoadImagesScaleForm;
import com.scale.images.util.ImageUtil;

@Service
public class ImageScaleService {

	final int FILE_NOT_FOUND = 0;
	final int FILE_EXIST = 1;
	
	public int checkLinkExist(LinkLoadImagesScaleForm form) {
		
		String from = ImageUtil.IMAGE_PATH + 
				"/original" + 
				"/" + form.getYear() +
				"/" + form.getMonth() +
				"/" + form.getDay() ;
		String to = ImageUtil.IMAGE_PATH + 
				"/" + form.getFolder() + 
				"/" + form.getYear() +
				"/" + form.getMonth() +
				"/" + form.getDay();
		
		File file = new File(to +
				"/" + form.getName()+ "." + form.getType());
		if (file.exists()) {
			return FILE_EXIST;
		} else {
			file = new File(from +
					"/" + form.getName() + "." + form.getType());
			if (file.exists()) {
				String[] folder = form.getFolder().split("_");
				int width = 0;
				int height = 0;
				try {
					width = Integer.parseInt(folder[1]);
					height = Integer.parseInt(folder[2]);
				} catch (Exception e) {
					return FILE_NOT_FOUND;
				}
				if(ImageUtil.moveAndScaleImage(form.getName(),form.getType(),from,to,width,height)) {
					return FILE_EXIST;
				} else {
					return FILE_NOT_FOUND;
				}
			} else {
				return FILE_NOT_FOUND;
			}
		}
	}
	
}
