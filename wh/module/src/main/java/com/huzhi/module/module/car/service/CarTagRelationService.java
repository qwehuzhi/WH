package com.huzhi.module.module.car.service;

import com.huzhi.module.module.car.entity.CarTagRelation;
import com.huzhi.module.module.car.mapper.CarTagRelationMapper;
import com.huzhi.module.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 车辆标签关系表 服务实现类
 * </p>
 *
 * @author huzhi
 * @since 2023-02-16
 */
@Service
public class CarTagRelationService{
    private final CarTagRelationMapper mapper;
    @Autowired
    public CarTagRelationService(CarTagRelationMapper mapper){
        this.mapper=mapper;
    }

    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public CarTagRelation getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public CarTagRelation extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入信息,只针对一条数据的操作
     * @return
     */
    public BigInteger insert(BigInteger carId,BigInteger tagId){
        CarTagRelation carTagRelation= getByCarIdAndTagId(carId,tagId);
        BigInteger id = null;
        if(carTagRelation == null){
            CarTagRelation insetCarTagRelation=new CarTagRelation();
            insetCarTagRelation.setCarId(carId);
            insetCarTagRelation.setTagId(tagId);
            insetCarTagRelation.setCreateTime(TimeUtil.getNowTime());
            id= BigInteger.valueOf(mapper.insert(insetCarTagRelation));
        }else {
            if(carTagRelation.getIsDeleted()==1){
                id=update(carTagRelation.getId(),carId,tagId,0);
            }
            if(carTagRelation.getIsDeleted()==0){
                id=carTagRelation.getId();
            }
        }
        return id;
    }
    /**
     * 修改信息
     * @return
     */
    public BigInteger update(BigInteger id,BigInteger carId,BigInteger tagId,Integer isDeleted){
        CarTagRelation updateCarTagRelation=new CarTagRelation();
        updateCarTagRelation.setId(id);
        updateCarTagRelation.setCarId(carId);
        updateCarTagRelation.setTagId(tagId);
        updateCarTagRelation.setIsDeleted(isDeleted);
        mapper.update(updateCarTagRelation);
        return updateCarTagRelation.getId();
    }
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    public int delete(BigInteger id){
        return mapper.delete(id,TimeUtil.getNowTime());
    }
    /**
     * 根据carId删除
     */
    public int deleteByCarId(BigInteger carId){
        if (carId==null){
            throw new RuntimeException("carId is null");
        }
        return mapper.deleteByCarId(carId,TimeUtil.getNowTime());
    }
    /**
     * 根据tagId删除
     */
    public int DeleteByTagId(BigInteger tagId){
        if (tagId==null){
            throw new RuntimeException("tagId is null");
        }
        return mapper.deleteByTagId(tagId,TimeUtil.getNowTime());
    }
    /**
     * 根据carId或tagId取出多条数据
     */
    public List<CarTagRelation> getByCarIdOrTagIdList(BigInteger carId, BigInteger tagId){
        return mapper.getByCarIdOrTagIdList(carId,tagId);
    }
    /**
     * 根据carId和tagId取出单条数据
     */
    public CarTagRelation getByCarIdAndTagId(BigInteger carId, BigInteger tagId){
        if (carId==null){
            throw new RuntimeException("carId is null");
        }
        if (tagId==null){
            throw new RuntimeException("tagId is null");
        }
        return mapper.getByCarIdAndTagId(carId,tagId);
    }
}
