package com.huzhi.module.module.car.service;

import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.entity.CarTag;
import com.huzhi.module.module.car.entity.CarTagRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * 专门处理car，tag，relation表之间联动行为
 */
@Service
public class BaseCarAndTagService {
    private final CarService carService;
    private final CarTagService tagService;
    private final CarTagRelationService relationService;
    @Autowired
    public BaseCarAndTagService(CarService carService,
                                CarTagService tagService,
                                CarTagRelationService relationService){
        this.carService=carService;
        this.tagService = tagService;
        this.relationService = relationService;
    }
    @Transactional(rollbackFor = Exception.class)
    public int CarAndRelationDeleted(BigInteger carId){
        relationService.deleteByCarId(carId);
        return carService.delete(carId);
    }
    @Transactional(rollbackFor = Exception.class)
    public int DeleteTagAndRelation(BigInteger tagId){
        relationService.DeleteByTagId(tagId);
        return tagService.delete(tagId);
    }

    /**
     * carList查询
     * @param numberPlate
     * @param enterpriseName
     * @param tag
     * @param page
     * @param pageSize
     * @return
     */
    public List<Car> baseGetCarList(String numberPlate, String enterpriseName,String tag, int page, int pageSize){
        //如果搜索的标签在数据库有效,获取relation表中对应的车辆id列表
        StringBuilder idRange=new StringBuilder();
        if(tag == null || tag.equals("")){
            idRange=null;
        }else {
            CarTag carTag=tagService.extractByTag(tag);
            if(carTag.getIsDeleted()==0){
                List<CarTagRelation> relations = relationService.getByCarIdOrTagIdList(null, carTag.getId());
                for (CarTagRelation relation:relations) {
                    if (relation.getIsDeleted()==0){
                        idRange.append(relation.getCarId());
                        idRange.append(',');
                    }
                }
                idRange.deleteCharAt(idRange.length()-1);
            }
        }
        return carService.getCarList(numberPlate,enterpriseName,idRange==null?null:idRange.toString(),page,pageSize);
    }

    /**
     *  事务新增车辆以及对应的标签
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BigInteger baseEditCar(BigInteger id, String numberPlate, String model, Integer businessStatus, Integer examineStatus,
                                  String licenseFrontPic, String licenseBackPic, String transportPic, String trailerPic,
                                  String transport, String trailer, Integer isDeleted, BigInteger enterpriseId,List<String> tags){
        BigInteger carId=carService.editCar(id,numberPlate,model,businessStatus,examineStatus,
                                            licenseFrontPic,licenseBackPic,transportPic,trailerPic,
                                            transport,trailer,isDeleted,enterpriseId);
        if(tags != null){
            for (String tag:tags) {
                BigInteger tagId = tagService.insert(tag);
                relationService.insert(carId,tagId);
            }
        }
        return carId;
    }
}
