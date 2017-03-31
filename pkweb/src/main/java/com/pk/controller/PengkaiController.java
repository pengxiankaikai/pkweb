package com.pk.controller;

import com.pk.common.QrcodeUtil;
import com.pk.entity.PengKai;
import com.pk.service.PengkaiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by pengkai
 * @date 2016/7/4.
 */
@Controller
@RequestMapping("/test")
public class PengkaiController {

    @Resource
    private PengkaiService pengkaiService;

    @RequestMapping("/index")
    public String index(Model model){
        PengKai pengkai = new PengKai();
        pengkai.setName("pengkaikai");
        pengkai.setCreate_date(new Date());
        PengKai pengKai = pengkaiService.get(1);
        pengkaiService.insert(pengkai);
        model.addAttribute("pengkai", pengkai);
        return "index";
    }

    @RequestMapping("/img")
    public void img(HttpServletRequest request, HttpServletResponse response){
        QrcodeUtil.getComposeImg("http://www.baidu.com", "png", 80, 80, response, null);
    }
}
