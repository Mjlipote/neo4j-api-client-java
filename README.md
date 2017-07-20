##Quick Start

####Neo4jClientBuilder
```java
Neo4jClientBuilder n4jcb =
        new Neo4jClientBuilder.Builder(new URI(SERVER_ROOT_URI), USER_NAME,
            PASSWORD).cypher("CREATE (p:Person {name:\"Alice\"}) RETURN p")
                .execute();

    System.out.println(n4jcb.getStatusCode());
    System.out.println(n4jcb.getJson());
```
