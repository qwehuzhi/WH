package com.huzhi.module.utils;


import com.aliyun.oss.*;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class OSSUtil {
    private static final String domainName="https://huzhi-image-path.oss-cn-hangzhou.aliyuncs.com/";
        public static String[] addimage(MultipartFile file, String imageName) throws Exception {
            String[] response=new String[2];
            // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
            String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
            // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
            String accessKeyId = "STS.NTt5Y35uBHCKijeYyAudXY183";
            String accessKeySecret ="BUPbUDPKL3xGUpAHDMqjMUbX281ji1oVL8X6aYJ9AuXs";
            String securityToken = " CAISmQJ1q6Ft5B2yfSjIr5fBfuOH2Kpj/4Gga0zUvXkUeet0tvST0Tz2IH1KdHFuCO4Wsvw+lWlQ6/gYlqJIRoReREvCUcZr8sy6PfUK7NKT1fau5Jko1be+ewHKeRiZsebWZ+LmNpy/Ht6md1HDkAJq3LL+bk/Mdle5MJqP+/kFC9MMRVuAcCZhDtVbLRcY/K18D3bKMuu3ORPHm3fZCFES2jBxkmRi86+ysIX+nBPVlw/90fRH5dazcJi5fclteZZ6Wti4mfR/aqqGkgwoskITq/kr1/IZoWuY74rEUmM8uUvWb7DmlfR0NxJ8a6QAHKpJkePxj/UQuJaIytqnk0gcY7EJC3WOHdj9npHedvmgM9EibqZE4rMPC1ZzXBqAAYS/Xq5FlN3gf2XWX6btFzNz1NBj3vv6Vl4TXglGP8nZI2PKjxdxdsv66Iynq18qL2A5T/jXMXCjCLQzcY3X5S3HaPOGvv7D6LP5KjORyIyJVYH2NCgHYa2RlDZktmVfvQ1G3GaxbTks+c15OJc5rdLnGx7cpok3gr0QBqamb442";
            // 填写Bucket名称，例如examplebucket。
            String bucketName = "huzhi-image-path";
            // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
            //String objectName = "exampledir/exampleobject.txt";
            SimpleDateFormat formatYM = new SimpleDateFormat("yyyy-MM/");
            SimpleDateFormat formatDD = new SimpleDateFormat("dd/");
            String directoryYM = formatYM.format(new Date());
            String directoryDD = formatDD.format(new Date());
            String filePath="image/"+directoryYM+directoryDD;

            // 创建OSSClient实例。
            OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret,securityToken);

            try {
                // 创建PutObjectRequest对象。
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath+imageName, file.getInputStream());

                // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
                // ObjectMetadata metadata = new ObjectMetadata();
                // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
                // metadata.setObjectAcl(CannedAccessControlList.Private);
                // putObjectRequest.setMetadata(metadata);

                // 设置该属性可以返回response。如果不设置，则返回的response为空。
                putObjectRequest.setProcess("true");
                // 上传字符串。
                PutObjectResult result = ossClient.putObject(putObjectRequest);
                // 如果上传成功，则返回200。
                System.out.println(result.getResponse().getStatusCode());
                response[0]= String.valueOf(result.getResponse().getStatusCode());
                response[1]=domainName+imageName;
                return response;
            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
                response[0]="404";
                response[1]=oe.getErrorMessage();
                return response;
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message:" + ce.getMessage());
                response[0]="404";
                response[1]=ce.getMessage();
                return response;
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
    }

