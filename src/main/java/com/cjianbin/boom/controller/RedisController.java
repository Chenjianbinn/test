package com.cjianbin.boom.controller;

import com.cjianbin.boom.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author cjianbin
 * Company: 南威软件股份有限公司
 * Createtime : 2021/7/23 0023 下午 2:41
 * Description :
 */
@RestController
public class RedisController {

    public static final Logger log = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisUtils redisUtils;
    @RequestMapping(value = "/hello/{id}")
    public String hello(@PathVariable(value = "id") String id){
        //查询缓存中是否存在
        boolean hasKey = redisUtils.exists(id);
        String str = "";
        if(hasKey){
            //获取缓存
            Object object =  redisUtils.get(id);
            log.info("从缓存获取的数据"+ object);
            str = object.toString();
        }else{
            //从数据库中获取信息
            log.info("插入值：hello,pig！");
            String s = "hello,pig!";
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id,s,10L,TimeUnit.MINUTES);
            log.info("数据插入缓存" + str);
        }
        return str;
    }
}
