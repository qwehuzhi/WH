package com.huzhi.console.controller.utils;

import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import com.huzhi.module.utils.FileUtil;
import com.huzhi.module.utils.OSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Slf4j
@RestController
public class OssController {
    private static final String urlHead="https://huzhi-image-path.oss-cn-hangzhou.aliyuncs.com/";
    @RequestMapping("/image/oss")
    public Response image(@RequestParam(value = "picture") MultipartFile picture) {
        //获取图片宽高
        int[] wh = FileUtil.getImageAr(picture);
        String ar;
        if(BaseUtil.isEmpty(wh)){
            ar="0x0";
        }else {
            String w = String.valueOf(wh[0]);
            String h = String.valueOf(wh[1]);
            ar = w + "x" + h;
        }
        //重命名
        String newName = UUID.randomUUID().toString().replaceAll("-", "") + "_" + ar + ".png";
        //重新指定上传路径
        SimpleDateFormat formatYM = new SimpleDateFormat("yyyy-MM/");
        SimpleDateFormat formatDD = new SimpleDateFormat("dd/");
        String directoryYM = formatYM.format(new Date());
        String directoryDD = formatDD.format(new Date());
        String filePath="image/"+directoryYM+directoryDD;
        //上传到oos
        try {
            OSSUtil.upload(picture.getInputStream(), filePath+newName);
            return new Response(1001, urlHead+filePath+newName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Response(2006);
        }
    }
}
