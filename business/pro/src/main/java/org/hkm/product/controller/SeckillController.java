package org.hkm.product.controller;


import org.hkm.product.service.impl.AbstractSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private AbstractSeckillService seckillService;

    static int x = 4;

    @PostMapping("/stock/reduce")
    public String reduceStock(){
//        int productId = x++%3;
        int productId = 1;
        int count = 1;

        boolean ret = this.seckillService.reduceStock(productId, count);
        return ret?"success":"failure";
    }

    @GetMapping("/reload")
    public String reloadStock(){
        this.seckillService.reloadStock();
        return "success";
    }

    @GetMapping("/scantask/start")
    public String startScanTask(){
        this.seckillService.startStockProcess();
        return "success";
    }


    @GetMapping("/scantask/stop")
    public String stopScanTask(){
        this.seckillService.stopStockProcess();
        return "success";
    }


}
