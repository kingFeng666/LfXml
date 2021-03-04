package com.lf.goods.service;

import com.lf.goods.dao.GoodsDao;
import com.lf.goods.dao.MemberDao;

/**
 * @author 青牛
 * @date 2021/2/24 10:41
 */

public class GoodsService {
    private GoodsDao goodsDao;
    private MemberDao memberDao;

    public void remove() {
        goodsDao.delete();
    }
}
