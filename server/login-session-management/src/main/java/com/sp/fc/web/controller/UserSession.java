package com.sp.fc.web.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.session.SessionInformation;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSession {
    private String username;
    private List<SessionInfo> sessions;

    public int getCount(){
        return sessions.size();
    }
}
