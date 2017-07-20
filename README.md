neo4j-api-client-java
=============
Neo4j API Client for Java.

##Quick Start

####Neo4jClientBuilder

```java
Neo4jClientBuilder n4jcb =
        new Neo4jClientBuilder.Builder(new URI("https://localhost:7474"), "neo4j",
            "password").cypher("CREATE (p:Person {name:\"Alice\"}) RETURN p")
                .execute();

    System.out.println(n4jcb.getStatusCode());
    System.out.println(n4jcb.getJson());
```

Prints Results
```json
200
{
  "columns" : [ "p" ],
  "data" : [ [ {
    "metadata" : {
      "id" : 5,
      "labels" : [ "Person" ]
    },
    "data" : {
      "name" : "Alice"
    },
    "paged_traverse" : "http://localhost:7474/db/data/node/5/paged/traverse/{returnType}{?pageSize,leaseTime}",
    "outgoing_relationships" : "http://localhost:7474/db/data/node/5/relationships/out",
    "outgoing_typed_relationships" : "http://localhost:7474/db/data/node/5/relationships/out/{-list|&|types}",
    "create_relationship" : "http://localhost:7474/db/data/node/5/relationships",
    "labels" : "http://localhost:7474/db/data/node/5/labels",
    "traverse" : "http://localhost:7474/db/data/node/5/traverse/{returnType}",
    "extensions" : { },
    "all_relationships" : "http://localhost:7474/db/data/node/5/relationships/all",
    "all_typed_relationships" : "http://localhost:7474/db/data/node/5/relationships/all/{-list|&|types}",
    "property" : "http://localhost:7474/db/data/node/5/properties/{key}",
    "self" : "http://localhost:7474/db/data/node/5",
    "incoming_relationships" : "http://localhost:7474/db/data/node/5/relationships/in",
    "properties" : "http://localhost:7474/db/data/node/5/properties",
    "incoming_typed_relationships" : "http://localhost:7474/db/data/node/5/relationships/in/{-list|&|types}"
  } ] ]
}
```