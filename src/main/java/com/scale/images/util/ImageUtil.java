package com.scale.images.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import org.springframework.web.multipart.MultipartFile;

import com.scale.images.model.CopperDataModel;

public class ImageUtil {
	
	final public static String IMAGE_PATH = "C:/xampp/htdocs/s3-beauty/static";
	
	final static int HOUR_TO_MINUTE = 60;
	final static int DAY_TO_HOUR = 24;
	final static int MONTH_TO_DAY = 30;
	final static int YEAR_TO_MONTH = 12;
	
	private static int time[] = {HOUR_TO_MINUTE,DAY_TO_HOUR,MONTH_TO_DAY,YEAR_TO_MONTH};
	
	public static String DistanceBetweentTwoTime(Date timeStart,Date timeEnd){
		String result = "";
		long between = (timeEnd.getTime() - timeStart.getTime())/60000;//Time to minute
		if (between < 0){
			between = 0 - between;
		}
		int count = 1; //Biến đếm để biết khoảng cách tính bằng giờ ngày hay tháng?
		long balances = 0; // Số dư của mỗi lần tính
		for(Integer i : time){
			if(between < i){ //Nếu khoảng cách đó nhỏ hơn đơn vị cần xét thì thoát khỏi vòng lặp
				break;
			} else {
				balances = between%i;
				between = between/i;
				if(balances >= (i/2)) {
					between++;
				}
				count++;
			}
		}
		switch (count) {
		case 1:
			result = String.valueOf(between) + " phút";
			break;
		case 2:
			result = String.valueOf(between) + " giờ";
			break;
		case 3:
			result = String.valueOf(between) + " ngày";
			break;
		case 4:
			result = String.valueOf(between) + " tháng";
			break;
		case 5:
			result = String.valueOf(between) + " năm";
			break;
		}
		return result;
	}
	
	public static String getDayCurrent() {
		String day = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		day = "/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/"; 
		return day;
	}
	
	public static String uploadImage(MultipartFile file,CopperDataModel imageData, String filesDirectory,String oldImage){
    	String type = file.getOriginalFilename().split("\\.")[1];
    	String fileName = "";
		try {
	    	File imageFile = multipartToFile(file);
			BufferedImage originalImgage = ImageIO.read(imageFile);
			fileName = uploadImage(originalImgage, imageData, filesDirectory, type, oldImage);
		} catch (IOException e) {
			fileName = "";
		}
    	return fileName;
    }
	
	public static String saveImageFromUrl(String url,String filesDirectory,String oldImage){
    	String type = "jpg";
    	String fileName = "";
		try {
			URL imageUrl = new URL(url);
			BufferedImage originalImgage = ImageIO.read(imageUrl);
			String name = UUID.randomUUID().toString();
			// get year,month,day current format is '/Year/Month/Day/' 
			String current_day = getDayCurrent();
			String directory = filesDirectory + "/original" + current_day;
			if(type.equalsIgnoreCase("jpeg")) {
				type = "jpg";
			}
	    	fileName = name + "."+ type;
			//Upload original Image
			uploadFile(originalImgage,directory ,fileName,type,oldImage);
	    	return current_day + fileName;
		} catch (IOException e) {
			fileName = "";
		}
    	return fileName;
    }
	
	public static void uploadFile(BufferedImage  file, String filesDirectory,String fileName,String type,String oldImage){
    	try {
			// Creating the directory to store file
			File dir = new File(filesDirectory);
			if (!dir.exists())
				dir.mkdirs();
			if(type.equalsIgnoreCase("jpeg")) {
				type = "jpg";
			}
			// Create the file on server
			File serverFile = new File(dir.getAbsolutePath()
					+ File.separator + fileName);
			 ImageIO.write(file, type, serverFile);
			if(oldImage != null) {
				deleteImage(oldImage);
			}
		} catch (IOException  e) {
			e.printStackTrace();
		}
    }
	
	public static String uploadImage(BufferedImage  file,CopperDataModel imageData, String filesDirectory,String type,String oldImage){			
		String name = UUID.randomUUID().toString();
		// get year,month,day current format is '/Year/Month/Day/' 
		String current_day = getDayCurrent();
		String directory_backup = filesDirectory + "/backup" + current_day;
		String directory_crop = filesDirectory + "/original" + current_day;
		if(type.equalsIgnoreCase("jpeg")) {
			type = "jpg";
		}
    	String fileName = name + "."+ type;
    	
    	int width = file.getWidth();
		int height = file.getHeight();
		if(width > height){
			width = height;
		}
		//Crop image with type square 
		BufferedImage subImage;
		if(imageData == null) {
			subImage = file.getSubimage(0, 0,width, width);
		} else {
			subImage = file.getSubimage((int)imageData.x, (int)imageData.y,(int)imageData.width, (int)imageData.height);
		}
		//Upload original Image
		uploadFile(file,directory_backup ,fileName,type,oldImage);
		//Upload crop Image
		uploadFile(subImage,directory_crop,fileName,type,oldImage);
    	return current_day + fileName;
    }
	
	public static void resizeAndSave(BufferedImage imageInput,
            String fileName,String filesDirectory,String type,int scaledWidth, int scaledHeight)
            throws IOException {
		File dir = new File(filesDirectory);
		if (!dir.exists())
			dir.mkdirs();
		if(type.equalsIgnoreCase("jpeg")) {
			type = "jpg";
		}
		// Create the file on server
		File serverFile = new File(dir.getAbsolutePath()
				+ File.separator + fileName + "." + type);
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, imageInput.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(imageInput.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH), 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        // writes to output file
        ImageIO.write(outputImage, type, serverFile);
    }
	
	public static boolean moveAndScaleImage(String fileName,String type,String fromFolder,
			String toFolder,Integer scaledWidth, Integer scaledHeight) {
		try {	
			File file = new File(fromFolder + File.separator + fileName + "." + type);
			Path path = file.toPath();
			byte[] data = Files.readAllBytes(path);
			BufferedImage image = getBufferedImageFromByte(data);
			if (image == null) {
				return false; 
			} else {
				resizeAndSave(image, fileName, toFolder, type, scaledWidth, scaledHeight);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public static BufferedImage getBufferedImageFromByte(byte[] bytes) {
		BufferedImage image_file = null;
		 ImageInputStream iis;
		 try {
				InputStream is = new ByteArrayInputStream(bytes);
	            image_file = ImageIO.read(is);
	            is.close();
			} catch (IOException e) {
				return null;
			}
		return image_file;
	}
    
    public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
            File convFile = new File( multipart.getOriginalFilename());
            multipart.transferTo(convFile);
            return convFile;
    }
    public static void deleteImage(String oldImage) {
    	File file = new File(IMAGE_PATH);
    	String[] names = file.list();
    	for(String name : names)
    	{
    		String path = IMAGE_PATH + File.separator  + name;
    	    if (new File(path).isDirectory()){
    	    	File oldFile = new File(path + "/" + oldImage);
	        	if(oldFile != null) {
	        		oldFile.delete();
	        	}
    	    }
    	}
    }
    
    public static String uploadImageNotCrop(BufferedImage  file, String filesDirectory,String type,String oldImage){			
		String name = UUID.randomUUID().toString();
		if(type.equalsIgnoreCase("jpeg")) {
			type = "jpg";
		}
		String originalFileName =  name + "_original."+ type;
    	

		String oldOriginalImageName = "";
		if(oldImage != null){
			if(!oldImage.isEmpty()){
				String[] arrayText = oldImage.split("\\.");
				oldOriginalImageName = arrayText[0].toString() + "_original." + arrayText[1].toString(); 
			}
		}
		//Upload original Image
		uploadFile(file,filesDirectory ,originalFileName,type,oldOriginalImageName);
    	return originalFileName;
    }
	public static String uploadImage(MultipartFile file, String filesDirectory){
    	String type = file.getOriginalFilename().split("\\.")[1];
    	String fileName = "";
		try {
	    	File imageFile = multipartToFile(file);
			BufferedImage buffImage = ImageIO.read(imageFile);
			
			String name = UUID.randomUUID().toString();
			if(type.equalsIgnoreCase("jpeg")) {
				type = "jpg";
			}
			fileName = name + "."+ type;
			uploadFile(buffImage, filesDirectory, fileName, type , null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fileName = "";
		}
    	return fileName;
    }
}
