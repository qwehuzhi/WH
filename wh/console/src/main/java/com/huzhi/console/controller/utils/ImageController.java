package com.huzhi.console.controller.utils;

import com.huzhi.module.response.Response;
import com.huzhi.module.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
@RestController
public class ImageController {
    private static final String fileSavePath="C:/Users/Administrator/IdeaProjects/work/WH/wh/module/src/main/java/com/huzhi/module/images";
    @RequestMapping("/image")
    public Response image(@RequestParam(value = "picture") MultipartFile picture) {
        if (picture.isEmpty()) {
            return new Response(2006);
        }
        //本地上传
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM/dd/");
        String directory = format.format(new Date());
        File dir = new File(fileSavePath + directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = Objects.requireNonNull(picture.getOriginalFilename()).substring(0, picture.getOriginalFilename().lastIndexOf("."));
        String fileSuffix = picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf("."));
        fileName = UUID.randomUUID().toString().replaceAll("-", "") + fileName;
        File newFile = new File(fileSavePath + directory  + fileName+fileSuffix);
        try{
            FileOutputStream picOutPut = new FileOutputStream(newFile.toString());
            picOutPut.write(picture.getBytes());
            picOutPut.close();
        } catch (Exception e) {
            return new Response(2006);
        }
        return new Response(1001, newFile.toString());
    }
}
