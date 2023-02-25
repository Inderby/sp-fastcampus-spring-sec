package com.sp.fc.web.config;

import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class NameCheck {
    public boolean check(String name){
        return name.equals("inderby");
    }
}
