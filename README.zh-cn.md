# fast-elasticsearch-query-builder

迅速构建**ElasticSearch**查询 dsl，***甚至可以不写一行实现代码。***

*其它语言: [English](README.md), [简体中文](README.zh-cn.md)*

## 简介

只需以下几步即可快速集成:

1. 编写搜索条件 DTO **MySearchCirteria**（***在字段上添加相应@注解***）

   ```java
   public class MySearchCirteria {
       @Must
       @Match
       private String name;
   }
   ```

2. 编写 query builder 类 **MyQueryBuilder** 继承 **BaseQueryBuilder**, 并指定 **MySearchCriteria** 为泛型类

   ```java
   public class MyQueryBuilder extends BaseQueryBuilder<MySearchCriteria> {
   }
   ```

3. 调用 **MyQueryBuilder#build** 构造 query

   ```java
   public class TestQueryBuilder {
       public static void main(String[] args) {
           // 实例化 MyQueryBuilder
           MyQueryBuilder myQueryBuilder = new MyQueryBuilder();
           // 实例化 MySearchCriteria
   		MySearchCriteria mySearchCriteria = new MySearchCriteria();
   		mySearchCriteria.setName("jack");
           // 调用 build 方法
           String dsl = myQueryBuilder.build(mySearchCriteria);
           // enjoy it!
   		System.out.println(dsl);
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
               "name": {
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

***fase-elasticsearch-query-builder*** *基于 `org.elasticsearch-elasticsearch-6.3.0` 来生成dsl，故兼容性与 `org.elasticsearch-elasticsearch-6.3.0` 一致。*

## 说明

### 注解说明

***fast-elasticsearch-query-builder*** 提供两种类型的注解来构造 query: **功能性注解**和**查询类注解**

#### 功能性注解

用于设置查询属性

| 注解              | 字段类型            | 功能                                            | 参数                                                         |
| ----------------- | ------------------- | ----------------------------------------------- | ------------------------------------------------------------ |
| @Higilighters     | Collection\<String> | 设置 ***highlight***                            | **type**: 高亮类型，默认为"fvh"                              |
| @PageNo           | Integer             | 设置  ***from***                                | -                                                            |
| @PageSize         | Integer             | 设置 **size**                                   | -                                                            |
| @Sort             | List\<Sortable>     | 设置 ***sort***，需自行实现 ***Sortable*** 接口 | -                                                            |
| @Source           | Collection\<String> | 设置 ***_source.includes***                     | -                                                            |
| @TermsAggregation | Integer             | 设置 ***terms aggregation***                    | **name**: 聚合名称<br />**field**: 聚合字段<br />**maxSize**: 聚合最大结果集<br />**order**: 聚合结果排序方式<br />**executionHint**: 聚合机制 |

> 保证 from + size <= max_result_window

#### 查询类注解

用于构造query子句，又分为**搜索上下文注解**和**搜索类型注解**，两者**必须一起使用**。

##### 搜索上下文注解

决定**搜索类型注解**如何影响搜索结果（过滤还是影响评分）

| 注解     | 功能                                                         |
| -------- | ------------------------------------------------------------ |
| @Must    | 设置 ***must query***                                        |
| @MustNot | 设置 ***must_not query***                                    |
| @Should  | 设置 ***should query***                                      |
| @Filter  | 设置 ***filter query***                                      |
| *@Or*    | 将转换成 ***filter query*** 中的 **should** 子句，同 **sql** 中的 **or** |
| *@OrNot* | 将转换成 ***filter query*** 中的 **should.mustNot** 子句，同 **sql** 中的 **or not** |

> @Or 和 @OrNot 不是标准的 ES 搜索上下文类型

##### 搜索类型注解

决定匹配行为

| 注解      | 字段类型      | 功能                      | 参数                                                         |
| --------- | ------------- | ------------------------- | ------------------------------------------------------------ |
| @Match    | String        | 设置 ***match query***    | **operator**: 控制 boolean 子句关系（or / and）              |
| @Term     | -             | 设置 ***term query***     | -                                                            |
| @Terms    | Collection<?> | 设置 ***terms query***    | -                                                            |
| @Range    | Number        | 设置 ***range query***    | **type**: 边界类型（from / to）<br />**includedBoundary**: 是否包含边界 |
| @Exists   | Boolean       | 设置 ***exists query***   | -                                                            |
| @Wildcard | String        | 设置 ***wildcard query*** | -                                                            |

> 以上注解均包含 **fieldName** 参数，表示构造 query 时的索引字段名，默认取注解所在字段的名称

### 方法说明

AbstractQueryBuilder#build 方法流程如下

![AbstractQueryBuilder#build flow chart](https://raw.githubusercontent.com/Thare-Lam/fast-elasticsearch-query-builder/master/query-builder-flow-chart.jpg)

你可以在抽象方法中自定义 query。

例如，自定义 **filter query**

```java
@Override
protected void customFilterQueries(List<QueryBuilder> filterQueries, 
                                   StuSearchCriteria stuSearchCriteria) {
    // 在这里自定义不通过注解方式生成的 query
    Optional.ofNullable(stuSearchCriteria.getCustomField())
        .ifPresent(t -> filterQueries.add(termQuery("customFilterField", t)));
}
```

**更详细的用法可以参考`test/`目录下的样例。**
