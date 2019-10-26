package com.hk.sso.client.service;

import com.hk.sso.common.service.SsoRemoteService;
import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.enums.LoginStatus;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/26 16:19
 */
public class HttpSsoRemoteService implements SsoRemoteService {
    /**
     * http认证方式时远端地址
     */
    public String ssoServiceUrl = "http://sso.hk.com";

    @Override
    public LoginStatus checkFromRemote(AuthTicket authTicket, boolean validateSign) {
        String sign = signForRemoteSsoServer(authTicket);
        return LoginStatus.SUCCESS;
    }
}
