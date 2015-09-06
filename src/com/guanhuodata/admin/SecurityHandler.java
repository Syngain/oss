package com.guanhuodata.admin;

import java.lang.reflect.Method;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import com.guanhuodata.framework.core.CoreConstants;

public class SecurityHandler {
    
    public void check(JoinPoint param) throws PermissionControlException, Exception {
        LoginUser loginUser = null;
        Method targetMethod = null;
        String action = "";
        Object obj = param.getTarget();
        String methodName = param.getSignature().getName();
        Method[] methods = obj.getClass().getMethods();
        for(Method m : methods){
            if(m.getName().equals(methodName)){
                targetMethod = m;
                break;
            }
        }

        if (targetMethod != null) {
            if (targetMethod.isAnnotationPresent(PermissionControl.class)) {
                PermissionControl pc = targetMethod.getAnnotation(PermissionControl.class);
                action = pc.value();
            }
        }
        if (action != null && !action.equals("")) {
            Object[] args = param.getArgs();
            for (Object arg : args) {
                if (arg instanceof LoginUser) {
                    loginUser = (LoginUser) arg;
                    break;
                }
            }
            if (loginUser != null) {
                if (loginUser.getSysUser() != null && loginUser.getSysUser().getId() != null) {
                    if (loginUser.getSysUser().getId().equals(CoreConstants.ADMIN_ID)) {
                        return;
                    }
                }
                List<String> actions = loginUser.getActions();
                for (String aid : actions) {
                    if (aid.equals(action)) {
                        return;
                    }
                }
                throw new PermissionControlException("error.access.denied");

            } else {
                throw new PermissionControlException("error.access.denied");
            }
        }
    }
}
