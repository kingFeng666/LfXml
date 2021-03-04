package com.lf.goods.controller;

import com.lf.goods.service.GoodsService;

/**
 * @author 青牛
 * @date 2021/2/24 10:41
 */

public class GoodsController {

    private GoodsService goodsService;

    public void remove() {
            goodsService.remove();
    }

}
