package com.huzhi.module.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FileUtil {
    public static int[] getImageAr(MultipartFile picture){
        int[] wxh = new int[2];
        int h=0;
        int w=0;
        String imageName=picture.getOriginalFilename();
        if(imageName == null){
            return null;
        }
        String[] imageStr=imageName.split("_");
        if (imageStr.length >=2){
            String[] imageStrEnd = imageStr[imageStr.length -1].split("\\.");
            String[] imageParam = imageStrEnd[0].split("x");
            if(imageParam.length == 2 && BaseUtil.isNumeric(imageParam[0]) && BaseUtil.isNumeric(imageParam[1])){
                wxh[0] = new Integer(imageParam[0]);
                wxh[1] = new Integer(imageParam[1]);
                return wxh;
            }
        }
        try{
            BufferedImage bufferedImage = ImageIO.read(picture.getInputStream());
            h = bufferedImage.getHeight();
            w = bufferedImage.getWidth();
            wxh[0]=w;
            wxh[1]=h;
        }catch (IOException e){
            wxh[0]=0;
            wxh[1]=0;
        }
        return wxh;
    }


}
