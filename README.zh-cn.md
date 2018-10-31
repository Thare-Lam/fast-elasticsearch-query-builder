# fast-es-query-builder

迅速构建**ElasticSearch**查询语句，***甚至可以不写一行实现代码。***

*其它语言: [English](README.md), [简体中文](README.zh-cn.md)*

## 简介

只需以下几步即可快速集成:

1. 编写搜索条件DTO **MySearchCirteria**（***在字段上添加相应@注解***）

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

功能性注解，用于设置查询属性

| 注解              | 字段类型            | 功能                                  | 参数                                                         |
| ----------------- | ------------------- | ------------------------------------- | ------------------------------------------------------------ |
| @Higilighters     | Collection\<String> | 设置高亮                              | **type**: 高亮类型，默认为"fvh"                              |
| @PageNo           | Integer             | 设置  ***from***                      | -                                                            |
| @PageSize         | Integer             | 设置 **size**                         | -                                                            |
| @Sort             | List\<Sortable>     | 设置 ，需自行实现 ***Sortable*** 接口 | -                                                            |
| @Source           | Collection\<String> | 设置 ***_source.includes***           | -                                                            |
| @TermsAggregation | Integer             | 设置 ***Terms Aggregation***          | **name**: 聚合名称<br />**field**: 聚合字段<br />**maxSize**: 聚合最大结果集<br />**order**: 聚合结果排序方式<br />**executionHint**: 聚合机制 |

> 注：***@PageNo*** 和 ***@PageSize*** 最终将转换成 ***from*** 和 ***size***，且保证 `from + size <= max_result_window`

### query

查询类注解，用于构造query子句