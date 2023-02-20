package com.huzhi.module.utils;


import com.aliyun.oss.*;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OSSUtil {
    private static final String domainName="https://huzhi-image-path.oss-cn-hangzhou.aliyuncs.com/";
        public static String addimage(MultipartFile file, String imageName) throws Exception {
            // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
            String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
            // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
            String accessKeyId = new String(Base64.getDecoder().decode("TFRBSTV0OWpoUzlnYWNXZ1Btb2llaHJz".getBytes(StandardCharsets.UTF_8)));
            String accessKeySecret = new String(Base64.getDecoder().decode("N29mMW1Td0VsVXNvQnFkamVid2R2S2pPSkF0QWND".getBytes(StandardCharsets.UTF_8)));
            // 填写Bucket名称，例如examplebucket。
            String bucketName = "huzhi-image-path";
            // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
            //String objectName = "exampledir/exampleobject.txt";
            String filePath="image/yymm/dd/";

            // 创建OSSClient实例。
            OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);

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
            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message:" + ce.getMessage());
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
            return domainName+imageName;
        }
    }

