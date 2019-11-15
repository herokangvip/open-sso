package com.hk.sso.common.service;


/**
 * @author heroking
 * @version 1.0
 * @date 2019/11/7 14:54
 */
public interface SsoLimitService {
    boolean isLimited(String pin);
}
