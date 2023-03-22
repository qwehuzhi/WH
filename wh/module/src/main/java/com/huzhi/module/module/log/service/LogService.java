package com.huzhi.module.module.log.service;

import com.huzhi.module.module.log.entity.Log;
import com.huzhi.module.module.log.mapper.LogMapper;
import com.huzhi.module.utils.BaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author huzhi
 * @since 2023-03-09
 */
@Service
public class LogService{
    private final LogMapper mapper;
    @Autowired
    public LogService(LogMapper mapper){
        this.mapper=mapper;
    }
    /**
     * 根据id查询（未删除）数据
     * @return
     */
    public Log getById(BigInteger id){
        return mapper.getById(id);
    }
    /**
     * 根据id取出(删除未删除)数据
     * @return
     */
    public Log extractById(BigInteger id){
        return mapper.extractById(id);
    }

    /**
     * 插入信息
     * @return
     */
    public Integer insert(BigInteger userId,Integer type,Integer operateTime,String operate,String extra){
        Log entity=new Log();
        entity.setUserId(userId);
        entity.setType(type);
        entity.setOperateTime(operateTime);
        entity.setOperate(operate);
        entity.setExtra(extra);
        entity.setIsDeleted(0);
        entity.setCreateTime(BaseUtil.currentSeconds());
        return mapper.insert(entity);
    }
    /**
     * 修改信息
     * @return
     */
    /**
    public BigInteger update(BigInteger id,BigInteger userId,Integer type,Integer operateTime,String operate,
                             String extra,Integer isDeleted){
        Log entity=target(id,userId,type,operateTime,operate,extra,isDeleted);
        mapper.update(entity);
        return entity.getId();
    }
    */
    /**
     *
     * 删除信息（逻辑删除）
     * @return
     */
    public int delete(BigInteger id){
        return mapper.delete(id,BaseUtil.currentSeconds());
    }

    /**
     * 查询：列表显示所有信息并分页
     */
    public List<Log> getList(BigInteger userId, Integer type,String operate, Integer page, Integer pageSize){
        return mapper.getList(userId,type,operate,(page-1)*pageSize,pageSize);
    }
    /**
     * 查询：信息总数附带模糊查询
     */
    public Integer getListTotal(BigInteger userId, Integer type,String operate){
        return mapper.getListTotal(userId,type,operate);
    }
}
