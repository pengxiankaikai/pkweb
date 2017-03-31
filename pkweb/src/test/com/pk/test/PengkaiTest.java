package com.pk.test;

import com.pk.entity.PengKai;
import com.pk.service.PengkaiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by dev on 2016/7/4.
 */
public class PengkaiTest{

    private PengkaiService pengkaiService;
    /**
      * 这个before方法在所有的测试方法之前执行，并且只执行一次
      * 所有做Junit单元测试时一些初始化工作可以在这个方法里面进行
      * 比如在before方法里面初始化ApplicationContext和userService
      */
    @Before
     public void before(){
             //使用"spring.xml"和"spring-mybatis.xml"这两个配置文件创建Spring上下文
             ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"spring.xml","spring-mybatis.xml"});
             //从Spring容器中根据bean的id取出我们要使用的userService对象
            pengkaiService = ac.getBean(PengkaiService.class);
         }

    @Test
    public void test001(){
        PengKai pengKai = new PengKai();
        pengKai.setName("pengkaikai");
        pengKai.setCreate_date(new Date());
//        pengkaiService.insert(pengKai);
        PengKai pk = pengkaiService.get(1);
    }
}
