package com.winter;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MainMapper {

    int insertBatchTestData1(@Param("d") String d, @Param("tableName") String tableName);
}
