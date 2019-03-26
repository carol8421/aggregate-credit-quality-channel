package com.aggregate.framework.open.service.impl;

import com.aggregate.framework.gzt.bean.vo.UpstreamVO;
import com.aggregate.framework.open.entity.mysql.Upstream;
import com.aggregate.framework.open.mapper.mysql.UpstreamMapper;
import com.aggregate.framework.open.service.UpstreamService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UpstreamServiceImpl extends ServiceImpl<UpstreamMapper, Upstream> implements UpstreamService {


	@Autowired
    UpstreamMapper upstreamMapper;

    @Override
    public UpstreamVO selectByClientId(String clientId) {
        Upstream upstream =new Upstream();
        upstream.setClientId(clientId);
        upstream = upstreamMapper.selectOne(upstream);
        UpstreamVO upstreamVO= new UpstreamVO();

        BeanUtils.copyProperties(upstream,upstreamVO);
        return upstreamVO;
    }
}
