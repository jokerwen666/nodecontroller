package com.hust.nodecontroller.fnlencrypt.hashutils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


public interface HashUtils {
    public String toHashString(String data);
}
