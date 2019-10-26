package com.hk.sso.client.service;

import com.hk.sso.common.service.SsoRemoteService;
import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.enums.LoginStatus;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/26 16:20
 */
public class RpcSsoRemoteService implements SsoRemoteService {


    @Override
    public LoginStatus checkFromRemote(AuthTicket authTicket, boolean validateSign) {
        String sign = signForRemoteSsoServer(authTicket);
        return LoginStatus.SUCCESS;
    }
}
