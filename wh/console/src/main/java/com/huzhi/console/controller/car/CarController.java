package com.huzhi.console.controller.car;

import com.huzhi.console.domain.car.*;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.service.BaseCarAndTagService;
import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.enterprise.entity.Enterprise;
import com.huzhi.module.module.enterprise.service.EnterpriseService;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.FileUtil;
import com.huzhi.module.utils.ImageUtil;
import com.huzhi.module.utils.OSSUtil;
import com.huzhi.module.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CarController {
    @Value("${file.save.path}")
    private String fileSavePath;

    private final CarService carService;
    private final EnterpriseService enterpriseService;
    private final BaseCarAndTagService baseCarAndTagService;
    @Autowired
    public CarController(CarService carService,
                         EnterpriseService enterpriseService,
                         BaseCarAndTagService baseCarAndTagService){
        this.carService=carService;
        this.enterpriseService=enterpriseService;
        this.baseCarAndTagService=baseCarAndTagService;
    }

    /**
     *后台新增车辆信息
     *测试语句：<a href="http://localhost:8081/car/add?numberPlate=浙b7 &model=A&transport=123&trailer=gua&licenseFrontPic=tu&licenseBackPic=tu&transportPic=tu&trailerPic=tu&businessStatus=1">...</a>
     */
    @RequestMapping("/car/add")
    public Response carAdd(@RequestParam(value = "numberPlate") String numberPlate,               //车牌
                         @RequestParam(value = "model") String model,                           //车型????有问题，这个应该是下拉框
                         @RequestParam(value = "transport") String transport,                   //运输证号
                         @RequestParam(value = "trailer",required = false) String trailer,      //挂车牌号（可以不传）
                         @RequestParam(value = "licenseFrontPic") String licenseFrontPic,       //行驶证正面
                         @RequestParam(value = "licenseBackPic") String licenseBackPic,         //行驶证反面
                         @RequestParam(value = "transportPic") String transportPic,             //运输证照片
                         @RequestParam(value = "trailerPic",required = false) String trailerPic,//挂车行驶证照片（可以不传）
                         @RequestParam(value = "businessStatus") Integer businessStatus,        //业务状态
                         @RequestParam(value = "enterpriseId") BigInteger enterpriseId,         //公司id
                         @RequestParam(value = "tagList",required = false)List<String> tags     //标签
    ){
        String message;
        numberPlate=numberPlate.trim();
        model=model.trim();
        transport=transport.trim();
        trailer=trailer==null ?null:trailer.trim();
        try{
            BigInteger add=baseCarAndTagService.baseEditCar(null,numberPlate,model,businessStatus,null,licenseFrontPic,
                    licenseBackPic,transportPic,trailerPic,transport,trailer,0,enterpriseId,tags);

            message="insert success id:"+add.toString();
            return new Response<>(1001,message);
        }catch (RuntimeException e){
            message=e.getMessage();
            e.printStackTrace();
            return new Response<>(1003);
        }

    }
    /**
     * 后台修改车辆信息
     * 测试语句：
     * <a href="http://localhost:8081/car/update?id=7&numberPlate=ce&model=&transport=123&trailer=gua&licenseFrontPic=tu&licenseBackPic=tu&transportPic=tu&trailerPic=tu&businessStatus=1&examineStatus=0">...</a>
     */
    @RequestMapping("/car/update")
    public Response carUpdate(@RequestParam(value = "id") BigInteger id,
                            @RequestParam(value = "examineStatus") Integer examineStatus,       //审核状态
                            @RequestParam(value = "numberPlate") String numberPlate,
                            @RequestParam(value = "model") String model,
                            @RequestParam(value = "transport") String transport,
                            @RequestParam(value = "trailer",required = false) String trailer,
                            @RequestParam(value = "licenseFrontPic") String licenseFrontPic,
                            @RequestParam(value = "licenseBackPic") String licenseBackPic,
                            @RequestParam(value = "transportPic") String transportPic,
                            @RequestParam(value = "trailerPic",required = false) String trailerPic,
                            @RequestParam(value = "businessStatus") Integer businessStatus,
                            @RequestParam(value = "enterpriseId") BigInteger enterpriseId,
                            @RequestParam(value = "isDeleted" )Integer isDeleted,
                            @RequestParam(value = "tagList",required = false)List<String> tags     //标签
    ){
        String message;
        numberPlate=numberPlate.trim();
        model=model.trim();
        transport=transport.trim();
        trailer=trailer==null ?null:trailer.trim();
        try{
            BigInteger update= baseCarAndTagService.baseEditCar(id,numberPlate,model,businessStatus,examineStatus,licenseFrontPic,
                    licenseBackPic,transportPic,trailerPic,transport,trailer,isDeleted,enterpriseId,tags);
            message="update success id:"+update.toString();
            return new Response<>(1001);
        }catch (RuntimeException e){
            message=e.getMessage();
            e.printStackTrace();
            return new Response<>(1003);
        }
    }
    /**
     * 后台删除车辆信息
     * 测试语句：<a href="http://localhost:8081/car/delete?id=8">...</a>
     */
    @RequestMapping("/car/delete")
    public Response carDelete(@RequestParam(value = "id") BigInteger id){
        int rem=baseCarAndTagService.CarAndRelationDeleted(id);
        return rem==1? new Response<>(1001): new Response<>(1003);
    }
    /**
     * 后台车辆列表
     *测试语句：<a href="http://localhost:8081/car/list?page=1&numberPlate=nu">...</a>
     */
    @RequestMapping("/car/list")
    public Response carList(@RequestParam(value = "page")int page,
                          @RequestParam(value = "numberPlate",required = false)String numberPlate,
                          @RequestParam(value = "enterpriseName",required = false) String enterpriseName,
                          @RequestParam(value = "tag",required = false) String tag
    ){
        int pageSize=5;
        tag=tag==null?null:tag.trim();
        numberPlate=numberPlate==null?null:numberPlate.trim();
        enterpriseName=enterpriseName==null?null:enterpriseName.trim();
        List<Car> cars=baseCarAndTagService.baseGetCarList(numberPlate,enterpriseName,tag,page,pageSize);
        //将carList的id重组成新的list
        List<BigInteger> idList=cars.stream().map(Car::getEnterpriseId).collect(Collectors.toList());
        List<Enterprise> enterpriseList=enterpriseService.getByIdList(idList);
        //获取id和名称的键值对，方便返回名称
        Map<BigInteger,String> enterpriseMap=enterpriseList.stream().collect(Collectors.toMap(Enterprise::getId,Enterprise::getName));
        List<BigInteger> matchedId=enterpriseList.stream().map(Enterprise::getId).collect(Collectors.toList());
        List<CarListBaseVO> list=new ArrayList<>();
        for (Car e: cars) {
            if(matchedId.contains(e.getEnterpriseId())){
                CarListBaseVO entity=new CarListBaseVO();
                entity.setId(e.getId());
                entity.setExamineStatus(e.getExamineStatus());
                entity.setEnterpriseName(enterpriseMap.get(e.getEnterpriseId()));
                entity.setBusinessStatus(e.getBusinessStatus());
                entity.setNumberPlate(e.getNumberPlate());
                entity.setIsDeleted(e.getIsDeleted()==1?"已删除":"未删除");
                list.add(entity);
            }
        }
        CarListVO carListVO=new CarListVO();
        carListVO.setList(list);
        Integer carsTotal=carService.getCarsTotalForApp(numberPlate,enterpriseName);
        carListVO.setTotal(carsTotal);
        carListVO.setPageSize(pageSize);
        return new Response<>(1001,carListVO);
    }
    /**
     *后天车辆信息详情
     *测试语句：<a href="http://localhost:8081/car/info?carId=2">...</a>
     */
    @RequestMapping("/car/info")
    public Response carInfo(@RequestParam(value = "id") BigInteger id){
        Car car=carService.extractById(id);
        Integer[] licenseFrontWh= ImageUtil.getImageWidthAndHeight(car.getLicenseFrontPic());
        Integer[] licenseBackWh=ImageUtil.getImageWidthAndHeight(car.getLicenseBackPic());
        Integer[] transportWh=ImageUtil.getImageWidthAndHeight(car.getTransportPic());
        Integer[] trailerWh=ImageUtil.getImageWidthAndHeight(car.getTrailerPic());
        CarInfoVO infoVO=new CarInfoVO();
        infoVO.setId(car.getId());
        infoVO.setNumberPlate(car.getNumberPlate());
        infoVO.setModel(car.getModel());
        infoVO.setTransport(car.getTransport());
        infoVO.setTrailer(car.getTrailer());
        infoVO.setBusinessStatus(car.getBusinessStatus());
        infoVO.setExamineStatus(car.getExamineStatus());
        infoVO.setLicenseFrontPic(new imageVO().setScr(car.getLicenseFrontPic()).setAr(licenseFrontWh[0].doubleValue()/licenseFrontWh[1].doubleValue()));
        infoVO.setLicenseBackPic(new imageVO().setScr(car.getLicenseBackPic()).setAr(licenseBackWh[0].doubleValue()/licenseBackWh[1].doubleValue()));
        infoVO.setTransportPic(new imageVO().setScr(car.getTransportPic()).setAr(transportWh[0].doubleValue()/transportWh[1].doubleValue()));
        infoVO.setTrailerPic(new imageVO().setScr(car.getTrailerPic()).setAr(trailerWh[0].doubleValue()/trailerWh[1].doubleValue()));
        infoVO.setEnterpriseName(enterpriseService.getById(car.getEnterpriseId()).getName());
        infoVO.setCreateTime(TimeUtil.timeConvert(car.getCreateTime()));
        infoVO.setIsDeleted(car.getIsDeleted()==1?"已删除":"未删除");
        return new Response<>(1001,infoVO);
    }
    /**
     * 图片上传接口
     */
    @RequestMapping("/image")
    public Response image(@RequestParam(value = "picture") MultipartFile picture){
        if (picture.isEmpty()){
            return new Response<>(1004);
        }
        //本地上传
//        //目录
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/");
//        String directory = format.format(new Date());
//        File dir = new File(fileSavePath + directory);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        // 文件名
//        //String fileName= picture.getOriginalFilename().substring(0,picture.getOriginalFilename().lastIndexOf("."));这里获取的图片名字被认为可能是空的
//        String fileName= Objects.requireNonNull(picture.getOriginalFilename()).substring(0,picture.getOriginalFilename().lastIndexOf("."));
//        String fileSuffix = picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf("."));
//        fileName = UUID.randomUUID().toString().replaceAll("-", "")+fileName+fileSuffix;
//        //断言：会扔出来一个异常,因为我加了一个uuid，就不存在为空的情况，就没有这个断言
//        //assert fileSuffix != null;
//        // 后缀名
//        //String suffixName = fileSuffix.substring(fileSuffix.lastIndexOf("."));
//        File newFile = new File(fileSavePath + directory + directory.substring(0,directory.length()-1)+ fileName);
//        try(FileOutputStream picOutPut = new FileOutputStream(newFile.toString())){
//            picOutPut.write(picture.getBytes());
//        }catch (Exception e){
//            return new Response<>(1004);
//        }
//        return new Response<>(1001,newFile.toString());

        String imageName=picture.getOriginalFilename();
        //获取图片宽高
        int[] wh=FileUtil.getImageAr(picture);
        Integer w=wh[0];
        Integer h=wh[1];
        String ar=w.toString()+"x"+h.toString();
        //重命名
        String newName=UUID.randomUUID().toString().replaceAll("-", "")+"_"+ar+".png";
        //上传到oos
        try {
            String imageUrl = OSSUtil.addimage(picture, newName);
            imageVO imageVo=new imageVO();
            imageVo.setScr(imageUrl);
            imageVo.setAr(w.doubleValue()/h.doubleValue());
            imageBaseVO baseVO=new imageBaseVO();
            baseVO.setImage(imageVo);
            return new Response<>(1001,baseVO);
        }catch (Exception e){
            return new Response<>(1004,e.getMessage());
        }
    }
}