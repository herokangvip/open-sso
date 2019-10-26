package com.hk.sso.common.service;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.enums.LoginStatus;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/26 16:18
 */
public interface SsoRemoteService {

    /**
     * 是否启用远程校验服务签名验证
     */
    boolean ssoSignValidate = true;

    /**
     * sso认证中心数字签名的加密key，由sso认证中心服务端颁发
     */
    String ssoServerEncryptKey = "5JY9D5G8RET54HSD";

    /**
     * 生成调用远程服务器验证的签名
     *
     * @param authTicket 用户ticket
     * @return 数字签名
     */
    default String signForRemoteSsoServer(AuthTicket authTicket) {
        return null;
    }

    LoginStatus checkFromRemote(AuthTicket authTicket, boolean validateSign);
}
