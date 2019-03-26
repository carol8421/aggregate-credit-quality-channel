package com.aggregate.framework.open.mapper.mysql;

import com.aggregate.framework.open.entity.mysql.Upstream;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface UpstreamMapper extends BaseMapper<Upstream> {
}
