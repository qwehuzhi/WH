package com.huzhi.module.module.car.service;

import com.huzhi.module.module.car.entity.CarTag;
import com.huzhi.module.module.car.mapper.CarTagMapper;
import com.huzhi.module.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author huzhi
 * @since 2023-02-16
 */
@Service
public class CarTagService {
    private final CarTagMapper mapper;
    @Autowired
    public CarTagService(CarTagMapper mapper){
        this.mapper=mapper;
    }

    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public CarTag getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public CarTag extractById(BigInteger id){
        return mapper.extractById(id);
    }
    /**
     * 插入信息
     * 如果数据库里是否已经有这个标签会进行不同的操作
     * @return
     */
    public BigInteger insert(String tag){
        CarTag carTag= extractByTag(tag);
        BigInteger id = null;
        if (carTag.getIsDeleted() != null){
            if(carTag.getIsDeleted()==1){
                update(carTag.getId(),tag,0);
                id=carTag.getId();
            }
            if (carTag.getIsDeleted()==0){
                id=carTag.getId();
            }
        }else {
            CarTag insertCarTag=new CarTag();
            insertCarTag.setTag(tag);
            insertCarTag.setCreateTime(TimeUtil.getNowTime());
            id= BigInteger.valueOf(mapper.insert(insertCarTag));
        }
        return id;
    }
    /**
     * 修改信息
     * @return
     */
    public BigInteger update(BigInteger id,String tag,Integer isDeleted){
        CarTag updateCarTag=new CarTag();
        updateCarTag.setId(id);
        updateCarTag.setTag(tag);
        updateCarTag.setIsDeleted(isDeleted);
        mapper.update(updateCarTag);
        return updateCarTag.getId();
    }
    /**
     * 需要带着relation表一起删
     * 删除信息（逻辑删除）
     * @return
     */
    public int delete(BigInteger id){
        return mapper.delete(id,TimeUtil.getNowTime());
    }
    /**
     * 根据标签查询
     */
    public CarTag extractByTag(String tag){
        if(tag==null || tag.equals("")){
            throw new RuntimeException("tag is null");
        }
        return mapper.getByTag(tag);
    }
}
