package com.sp.fc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Order(1)//filter의 적용 순서를 정해주는 어노테이션
@EnableWebSecurity(debug = true) //request가 올때마다 어떤 filter chain을 타고 있는지 보여주는 설정
@EnableGlobalMethodSecurity(prePostEnabled = true)//지금부터 prepost로 권한 체크를 하겠다는 의미
public class SecurityConfig extends WebSecurityConfigurerAdapter {//어떤 필터를 사용하여 체인을 구성할지 설정하는 클래스

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //user을 추가하는 메서드 //provider을 추가하게 되면 더이상 application.yml에 있는 유저 정보는 사용하지 않다는다.
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password(passwordEncoder().encode("2222")) //사용자의 password를 인코딩하지 않으면 오류가 생긴다.
                        .roles("USER")
                ).withUser(User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("3333"))
                        .roles("ADMIN"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //기본 설정이 모든 request는 인증(authorize)받으라고 설정되어있다.
        http.authorizeRequests((requests) ->
                requests.antMatchers("/").permitAll() //홈페이지의 경우에는 모두에게 허용하라는 설정. antmathcer을 이용하여 request를 처리할 필터를 구분해줄 수 있음
                        .anyRequest().authenticated());
        http.formLogin();
        http.httpBasic();

        http
                .headers().disable() //각종 필터들일 키고 끄고 할 수 있다.
                .csrf().disable()
                .formLogin(login->
                        login.defaultSuccessUrl("/", false))//로그인을 성공 했을 때 로그인 후 보여지는 화면을 설정, alwaysUse
                //를 false로 처리해야 사용자가 편리해진다.
                .logout().disable()
                .requestCache().disable()
                ;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
