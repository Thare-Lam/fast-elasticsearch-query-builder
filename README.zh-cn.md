# fast-es-query-builder

迅速构建**ElasticSearch**查询语句，***甚至可以不写一行实现代码。***

*其它语言: [English](README.md), [简体中文](README.zh-cn.md)*

## 简介

只需以下几步即可快速集成:

1. 编写搜索条件DTO **MySearchCirteria**（在字段上添加相应@注解）

   ```java
   public class MySearchCirteria {
       @Must
       @Match
       private String name;
   }
   ```

2. 编写query builder类 **MyQueryBuilder** 继承 **BaseQueryBuilder**, 并指定 **SearchCriteria** 为范型类

   ```java
   public class MyQueryBuilder extends BaseQueryBuilder<MySearchCriteria> {
   }
   ```

3. 调用 **MyQueryBuilder#build** 构造query

   ```java
   public class TestQueryBuilder {
       public static void main(String[] args) {
           MyQueryBuilder myQueryBuilder = new MyQueryBuilder();
   		MySearchCriteria mySearchCriteria = new MySearchCriteria();
   		mySearchCriteria.setName("jack");
   		System.out.println(myQueryBuilder.build(mySearchCriteria));
       }
   }
   ```

   结果

   ```json
   {
     "from": 0,
     "size": 20,
     "query": {
       "bool": {
         "must": [
           {
             "match": {
               "name2": {
                 "query": "jack",
                 "operator": "OR",
                 "prefix_length": 0,
                 "max_expansions": 50,
                 "fuzzy_transpositions": true,
                 "lenient": false,
                 "zero_terms_query": "NONE",
                 "auto_generate_synonyms_phrase_query": true,
                 "boost": 1
               }
             }
           }
         ],
         "adjust_pure_negative": true,
         "boost": 1
       }
     }
   }
   ```

## 用法

***fase-es-query-builder*** 提供两种类型的注解来构造query: **func** 和 **query**

### func

功能性注解，用于设置查询的from/size/_sources等属性

### query

查询类注解，用于构造query子句