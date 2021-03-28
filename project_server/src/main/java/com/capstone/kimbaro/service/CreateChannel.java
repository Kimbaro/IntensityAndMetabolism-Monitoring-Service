package com.capstone.kimbaro.service;

import org.springframework.stereotype.Service;

@Service
public class CreateChannel {
    public static int channel() {
        int hash = "".hashCode();
        return hash;
    }
}
