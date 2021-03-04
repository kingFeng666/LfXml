package com.lf.goods.controller;

import com.lf.goods.service.MemberService;

/**
 * @author 青牛
 * @date 2021/2/24 10:40
 */

public class MemberController {
    private MemberService memberService01;
    private String vipNumber;

    public void add() {
        memberService01.add();
    }
}
