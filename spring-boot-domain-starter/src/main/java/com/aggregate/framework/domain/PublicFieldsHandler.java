package com.aggregate.framework.domain;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;


public class PublicFieldsHandler extends MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		long time = System.currentTimeMillis();
		setFieldValByName("createTime", time, metaObject);
		setFieldValByName("updateTime", time, metaObject);
		setFieldValByName("delete_flag", 0, metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);
	}
}
