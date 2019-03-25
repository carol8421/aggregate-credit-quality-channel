package com.aggregate.framework.gzt.mapper;

import com.aggregate.framework.gzt.entity.Upstream;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface UpstreamMapper extends BaseMapper<Upstream> {
}
