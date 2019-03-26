package com.aggregate.framework.open.service;

import com.aggregate.framework.gzt.bean.vo.UpstreamVO;

public interface UpstreamService {

    /**
     * 根据client_id的查询
     * @param clientId
     * @return
     */
    UpstreamVO selectByClientId(String clientId);


}
