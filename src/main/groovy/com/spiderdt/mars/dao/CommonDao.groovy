package com.spiderdt.mars.dao

import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository


@Repository
interface CommonDao {

    ArrayList<Object> tableInfo(@Param("xschema") schema, @Param("xtable") table)

    ArrayList<Object> colInfo(@Param("xschema") schema, @Param("xtable") table)

    Object lastDate(@Param("xtable") table)

    Object firstDate(@Param("xtable") table)

    Object predictDateRange(@Param("data_source") schema, @Param("job_id") job_id)

    Object displayColName(@Param("col_name") col_name, @Param("tab") tab)

    Object getDatesource(@Param("user_name") user_name)
}
