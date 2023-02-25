package com.sp.fc.web.config;

import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

@Getter
@Setter
public class CustomMethodSecurityExpressRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    MethodInvocation invocation;
    public CustomMethodSecurityExpressRoot(Authentication authentication) {
        super(authentication);
    }

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressRoot(Authentication authentication, MethodInvocation invocation) {
        super(authentication);
        this.invocation = invocation;
    }

    public boolean isStudent(){
        return getAuthentication().getAuthorities().stream()
                .filter(a->a.getAuthority().equals("ROLE_STUDENT"))
                .findAny().isPresent();
    }
    @Override
    public Object getThis() {
        return this;
    }
}
