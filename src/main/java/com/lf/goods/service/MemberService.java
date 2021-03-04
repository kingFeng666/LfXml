package com.lf.goods.service;

import com.lf.goods.dao.MemberDao;

/**
 * @author 青牛
 * @date 2021/2/24 10:40
 */

public class MemberService {
    private MemberDao memberDao;

    public void add() {
        memberDao.insert();

    }
}
