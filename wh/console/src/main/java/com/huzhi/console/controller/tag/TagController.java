package com.huzhi.console.controller.tag;

import com.huzhi.module.response.Response;
import com.huzhi.module.module.car.service.BaseCarAndTagService;
import com.huzhi.module.module.car.service.CarTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
public class TagController {

    private final CarTagService carTagService;
    private final BaseCarAndTagService baseService;
    @Autowired
    public TagController(CarTagService carTagService,
                         BaseCarAndTagService baseService){
        this.carTagService=carTagService;
        this.baseService=baseService;
    }

    /**
     * 标签新增
     * @param tag
     * @return
     */
    @RequestMapping("/tag/insert")
    public Response tagInsert(@RequestParam(value = "tag")String tag){
        if(tag!=null){tag=tag.trim();}
        BigInteger id=carTagService.insert(tag);
        return new Response<>(1001,id);
    }

    /**
     * 标签删除
     * @param tag
     * @return
     */
    @RequestMapping("/tag/delete")
    public Response tagDelete(@RequestParam(value = "tag")BigInteger tag){
        int number=baseService.DeleteTagAndRelation(tag);
        return number==1? new Response<>(1001): new Response<>(1003);
    }

}
