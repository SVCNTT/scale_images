package com.scale.images.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scale.images.form.LinkLoadImagesScaleForm;
import com.scale.images.service.ImageScaleService;
import com.scale.images.util.ImageUtil;
import com.scale.images.validate.LinkValidate;

@Controller
public class BaseController{
	
	@Autowired
	private LinkValidate linkValidate;
	
	@Autowired
	private ImageScaleService imageScaleService;
	
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public String helloPages(HttpServletRequest request){
		return "hello";
	}
	
	@RequestMapping(value = "/static/{folder}/{year}/{month}/{day}/{name}.{type}",method = RequestMethod.GET)
	public void getImage(
			Model model,
			@PathVariable("folder") String folder,
			@PathVariable("year") String year,
			@PathVariable("month") String month,
			@PathVariable("day") String day,
			@PathVariable("name") String name,
			@PathVariable("type") String type,
			LinkLoadImagesScaleForm form,
			BindingResult result,
			HttpServletResponse response) {
		try {
			form = new LinkLoadImagesScaleForm(folder,Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day),name,type);
		} catch (Exception e) {
			System.out.println("Not Found");
		}
		linkValidate.validate(form, result);
		if (result.hasErrors()) {
			System.out.println("Not Found");
		} else {
			int res = imageScaleService.checkLinkExist(form);
			if (res == 1) {
				try {
					File f = new File(ImageUtil.IMAGE_PATH + "/" + folder + "/" + year + "/" + month + "/" + day + "/" + name + "." + type);
					BufferedImage bi = ImageIO.read(f);
					OutputStream out = response.getOutputStream();
					ImageIO.write(bi, type, out);
					out.close();
				} catch (IOException e) {
					System.out.println("Not Found");
				}
			} else {
				System.out.println("Not Found");
			}
		}
	}
	
}
