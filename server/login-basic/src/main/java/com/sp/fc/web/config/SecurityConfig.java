package com.sp.fc.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) //controller에서 지정한 role대로 권한을 제한한다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomAuthDetails customAuthDetails;

    public SecurityConfig(CustomAuthDetails customAuthDetails) {
        this.customAuthDetails = customAuthDetails;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()//테스트에 한정해서 사용가능한 legacy
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                ).withUser(
                        User.withDefaultPasswordEncoder()
                                .username("admin")
                                .password("2222")
                                .roles("ADMIN")
                );


    }
    @Bean
    RoleHierarchy roleHierarchy(){ //권한에 대한 계층을 정해주는 함수
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests(request->{
                    request

                            .antMatchers("/").permitAll()
                            .anyRequest().authenticated()
                            ;
                })
                .formLogin(
                        login->login.loginPage("/login")
                                .permitAll() //이 설정을 하지 않으면 무한루프에 빠질 수 있다.
                                .defaultSuccessUrl("/", false)//로그인에 성공하면 이동하는 페이지 설정
                                .failureUrl("/login-error")
                                .authenticationDetailsSource(customAuthDetails)

                ) //usernamepasswordFilter를 적용
                .logout(logout->logout.logoutSuccessUrl("/"))
                .exceptionHandling(exception->exception.accessDeniedPage("/access-denied"))
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring() //web resource에 대해서는 security가 적용되지 않도록 설정해줘야한다.
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }
}
