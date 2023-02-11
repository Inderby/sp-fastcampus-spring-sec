package com.sp.fc.user.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_user_authority")
@IdClass(SpAuthority.class)
public class SpAuthority implements GrantedAuthority {
    @Id
    @Column(name = "user_id")
    private Long UserId;
    @Id
    private String authority;
    @Override
    public String getAuthority() {
        return null;
    }
}
