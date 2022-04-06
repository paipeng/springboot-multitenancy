package com.paipeng.saas.util;

import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilTest {

    @org.junit.jupiter.api.Test
    void generateJWTToken() {
        String token = CommonUtil.generateJWTToken("5161127a80ff47a1855176c345a1de39833b486ea3dd40629081ab0370a1632c87496492fb634c60a458182c69a7f0d0", "paipeng");
        System.out.println("token: " + token);
    }
}