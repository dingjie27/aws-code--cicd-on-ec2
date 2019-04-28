package com.aws.demo4cicf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Value("${server.port}")
    String port;

    @GetMapping("/")
    public String getIndex() {
        System.out.println("server is up and the port is "+ port);
        return "hello,this is a response from my web service!";
    }
}

