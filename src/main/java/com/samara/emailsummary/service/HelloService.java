package com.samara.emailsummary.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String obterMensagem() {
        return "Email Summary funcionando!";
    }
}