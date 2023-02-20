<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

    <#--<#if enableCache>-->
    <#--    <!-- 开启二级缓存 &ndash;&gt;-->
    <#--    <cache type="${cacheClassName}"/>-->

    <#--</#if>-->
    <#if baseResultMap>
        <!-- 通用查询映射结果 -->
        <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
            <#list table.fields as field>
                <#if field.keyFlag><#--生成主键排在第一位-->
                    <id column="${field.name}" property="${field.propertyName}" />
                </#if>
            </#list>
            <#list table.commonFields as field><#--生成公共字段 -->
                <result column="${field.name}" property="${field.propertyName}" />
            </#list>
            <#list table.fields as field>
                <#if !field.keyFlag><#--生成普通字段 -->
                    <result column="${field.name}" property="${field.propertyName}" />
                </#if>
            </#list>
        </resultMap>

    </#if>
    <#if baseColumnList>
        <!-- 通用查询结果列 -->
        <sql id="Base_Column_List">
            <#list table.commonFields as field>
                ${field.name},
            </#list>
            ${table.fieldNames}
        </sql>
    </#if>

    <!--insert插入数据-->
    <#list table.fields as field>
        <#if field.keyFlag>
    <!-- 新增表${table.name}信息 -->
    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO ${table.name} (
        <#list table.commonFields as field><#--生成公共字段-->
        <if test="${table.name}.${field.propertyName}!=null and ${table.name}.${field.propertyName}!=''">${field.name},</if>
        </#list>
        <#list table.fields as field>
        <#if !field.keyFlag><#--生成普通字段 -->
        <if test="${table.name}.${field.propertyName}!=null and ${table.name}.${field.propertyName}!=''">${field.name},</if>
        </#if>
        </#list>
        ) VALUES (
        <#list table.commonFields as field><#--生成公共字段-->
        <if test="${table.name}.${field.propertyName}!=null and ${table.name}.${field.propertyName}!=''">${r"#{"}${table.name}.${field.propertyName}${r"}"},</if>
        </#list>
        <#list table.fields as field>
        <#if !field.keyFlag><#--生成普通字段 -->
        <if test="${table.name}.${field.propertyName}!=null and ${table.name}.${field.propertyName}!=''">${r"#{"}${table.name}.${field.propertyName}${r"}"},</if>
        </#if>
        </#list>
        )
    </insert>
        </#if>
    </#list>
    <!--update修改数据-->
    <#list table.fields as field>
    <#if field.keyFlag>
    <!-- 根据主键${field.propertyName}更新表${table.name}信息 -->
    <update id="update">
         UPDATE ${table.name}
         set ${field.name}=${r"#{"}${table.name}.${field.propertyName}${r"}"}
         <#list table.commonFields as field><#--生成公共字段-->
         <if test="${table.name}.${field.propertyName}!=null and ${table.name}.${field.propertyName}!=''">${field.name} = ${r"#{"}${table.name}.${field.propertyName}${r"}"},</if>
         </#list>
         <#list table.fields as field>
         <#if !field.keyFlag><#--生成普通字段 -->
         <if test="${table.name}.${field.propertyName}!=null and ${table.name}.${field.propertyName}!=''">${field.name} = ${r"#{"}${table.name}.${field.propertyName}${r"}"},</if>
         </#if>
         </#list>
         WHERE ${field.name}=${r"#{"}${table.name}.${field.propertyName}${r"}"}
    </update>
    </#if>
    </#list>
</mapper>