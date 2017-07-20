/*
*
* Copyright 2017 Ming-Jheng Li
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*
*/
package com.github.mjli.neo4j.api.client.examples;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;

import com.github.mjli.neo4j.api.client.Neo4jClientBuilder;

public class Neo4jClientBuilderExamples {

  private static final String SERVER_ROOT_URI = "http://localhost:7474";
  private static final String USER_NAME = "username";
  private static final String PASSWORD = "password";

  /**
   * @param args
   * @throws URISyntaxException
   * @throws IOException
   * @throws KeyStoreException
   * @throws NoSuchAlgorithmException
   * @throws ClientProtocolException
   * @throws UnsupportedOperationException
   * @throws KeyManagementException
   * @throws UnsupportedCharsetException
   */
  public static void main(String[] args) throws UnsupportedCharsetException,
      KeyManagementException, UnsupportedOperationException,
      ClientProtocolException, NoSuchAlgorithmException, KeyStoreException,
      IOException, URISyntaxException {

    Neo4jClientBuilder n4jcb =
        new Neo4jClientBuilder.Builder(new URI(SERVER_ROOT_URI), USER_NAME,
            PASSWORD).cypher("CREATE (p:Person {name:\"Alice\"}) RETURN p")
                .execute();

    System.out.println(n4jcb.getStatusCode());
    System.out.println(n4jcb.getJson());
  }

}
