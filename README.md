# fast-es-query-builder
迅速构建es query，甚至可以不写一行实现代码。你只需

1. 编写搜索条件DTO（在字段上添加相应@注解）

   ```java
   public class SearchCirteria {
       @Must
       @Match
       private String name;
   }
   ```

2. 继承BaseQueryBuilder

   ```java
   public class QueryBuilder extends BaseQueryBuilder<SearchCriteria> {
   }
   ```

3. build

   ```java
   QueryBuilder queryBuilder = new QueryBuilder();
   SearchCriteria searchCriteria = new SearchCriteria();
   searchCriteria.setName("jack");
   String queryString = queryBuilder.build(searchCriteria);
   ```

   queryString: 

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
