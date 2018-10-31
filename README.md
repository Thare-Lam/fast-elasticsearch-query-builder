# fast-es-query-builder
A fast way to build **ElasticSearch** query string, ***even without writing implement code.***

*Read this in other languages: [English](README.md), [简体中文](README.zh-cn.md)*

## Brief

You can integrate it in your project with these steps:

1. Complete the search criteria DTO **MySearchCirteria**（***add correct @annotation on the field***）

   ```java
   public class MySearchCirteria {
       @Must
       @Match
       private String name;
   }
   ```

2. Complete the query builder class **MyQueryBuilder** extending the  **BaseQueryBuilder**, with specifing **MySearchCirteria**  as the generics class

   ```java
   public class MyQueryBuilder extends BaseQueryBuilder<MySearchCriteria> {
   }
   ```

3. Invoke **MyQueryBuilder#build** to build the query string

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

   result

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


