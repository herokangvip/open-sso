package com.hk.sso.server.service;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.enums.LoginStatus;
import com.hk.sso.common.service.SsoRemoteService;
import com.hk.sso.common.utils.CookieSignUtils;
import com.hk.sso.common.utils.TicketUtils;
import com.hk.sso.server.utils.TicketRedisUtils;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/26 16:20
 */
public class RpcSsoRemoteService implements SsoRemoteService {

    /**
     * cookie签名加密key
     */
    public String cookieSignKey = "8f8c36c8071fc4d15e49888902f348c8";


    @Override
    public int checkFromRemote(AuthTicket authTicket, boolean validateSign) {
        if (validateSign) {
            //校验cookie签名
            boolean cookieSignResult = CookieSignUtils.validateSign(authTicket, cookieSignKey);
            if (!cookieSignResult) {
                return LoginStatus.SIGN_ERROR.code;
            }
        }
        //校验ticket是否合法(是否存在登录态，登录态是否过期，是否修改密码等)
        AuthTicket ticket = TicketRedisUtils.getTicket(authTicket.getPin());
        //没有登录态
        if (ticket == null) {
            return LoginStatus.LOGOUT.code;
        }
        //密码修改
        Integer versionParam = authTicket.getVersion();
        Integer version = ticket.getVersion();
        if (!versionParam.equals(version)) {
            return LoginStatus.PASSWORD_CHANGED.code;
        }
        //登陆过期
        long time = System.currentTimeMillis();
        long expire = ticket.getExpire();
        if (expire < time) {
            return LoginStatus.EXPIRED.code;
        }
        return LoginStatus.SUCCESS.code;
    }
}
