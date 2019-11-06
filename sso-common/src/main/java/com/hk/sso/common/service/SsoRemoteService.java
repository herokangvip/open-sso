package com.hk.sso.common.service;

import com.hk.sso.common.domain.AuthTicket;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/26 16:18
 */
public interface SsoRemoteService {

    int checkFromRemote(AuthTicket authTicket, boolean validateSign);
}
