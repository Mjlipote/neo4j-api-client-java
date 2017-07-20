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
package com.github.mjli.neo4j.api.client;

import static com.google.common.base.Charsets.ISO_8859_1;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;

public final class Neo4jClientBuilder
    implements Comparable<Neo4jClientBuilder> {

  private final ObjectMapper mapper;
  private final URI uri;
  private final String authHeader;
  private final CloseableHttpClient httpclient;
  private final CloseableHttpResponse closeableHttpResponse;
  private final String json;

  /**
   * 
   * Neo4jClientBuilder
   * 
   * @author Ming-Jheng Li
   * 
   */
  public static class Builder {

    private final ObjectMapper mapper;
    private final URI uri;
    private final String authHeader;
    private final CloseableHttpClient httpclient;
    private CloseableHttpResponse closeableHttpResponse;
    private String json;

    public Builder(URI uri, String id, String password)
        throws NoSuchAlgorithmException, KeyStoreException,
        KeyManagementException {
      this.uri = uri;
      String auth = id + ":" + password;
      byte[] encodedAuth = encodeBase64(auth.getBytes(ISO_8859_1));
      this.authHeader = "Basic " + new String(encodedAuth);
      this.mapper = new ObjectMapper();
      SSLContextBuilder builder = new SSLContextBuilder();
      builder.loadTrustMaterial(new TrustSelfSignedStrategy());
      SSLConnectionSocketFactory sslsf =
          new SSLConnectionSocketFactory(builder.build());
      httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    /**
     * Cypher
     * 
     * @param cypher
     * @param params
     * @return
     * @throws UnsupportedCharsetException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Builder cypher(String cypher, Object params)
        throws UnsupportedCharsetException, ClientProtocolException,
        IOException {
      HttpPost httpPost = new HttpPost(uri + "/db/data/cypher");
      httpPost.addHeader("Content-type", "application/json");
      httpPost.addHeader("Authorization", authHeader);
      httpPost.setEntity(new StringEntity(
          mapper.writeValueAsString(new Neo4jObject(cypher, params)), "UTF-8"));
      closeableHttpResponse = httpclient.execute(httpPost);
      return this;
    }

    public Builder cypher(String cypher) throws UnsupportedCharsetException,
        ClientProtocolException, IOException {
      return cypher(cypher, null);
    }

    /**
     * Creates an Neo4jClientBuilder.
     * 
     * @return an Neo4jClientBuilder
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    public Neo4jClientBuilder execute()
        throws UnsupportedOperationException, IOException {
      json = IOUtils.toString(closeableHttpResponse.getEntity().getContent());
      return new Neo4jClientBuilder(this);
    }

  }

  private Neo4jClientBuilder(Builder builder) {
    this.mapper = builder.mapper;
    this.uri = builder.uri;
    this.authHeader = builder.authHeader;
    this.httpclient = builder.httpclient;
    this.closeableHttpResponse = builder.closeableHttpResponse;
    this.json = builder.json;
  }

  public Integer getStatusCode() {
    return closeableHttpResponse.getStatusLine().getStatusCode();
  }

  public String getNodeLabel() {
    Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);
    return flattenJson.get("data[0][0].metadata.labels[0]").toString();
  }

  public List<String> getNodeLabels() {
    List<String> labels = newArrayList();
    Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);
    int i = 0;
    if (flattenJson.get("data[0][0].metadata.labels[" + i + "]")
        .toString() != null) {
      labels.add(
          flattenJson.get("data[0][0].metadata.labels[" + i + "]").toString());
    }
    return labels;
  }

  public String getNodePropertiesValue(String proprties) {
    Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);
    return flattenJson.get("data[0][0].data." + proprties).toString();
  }

  public String getNodeId() {
    Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);
    return flattenJson.get("data[0][0].metadata.id").toString();
  }

  /**
   * @return the json
   */
  public String getJson() {
    return json;
  }

  /**
   * @return the mapper
   */
  public ObjectMapper getMapper() {
    return mapper;
  }

  /**
   * @return the uri
   */
  public URI getUri() {
    return uri;
  }

  /**
   * @return the authHeader
   */
  public String getAuthHeader() {
    return authHeader;
  }

  /**
   * @return the httpclient
   */
  public CloseableHttpClient getHttpclient() {
    return httpclient;
  }

  /**
   * @return the closeableHttpResponse
   */
  public CloseableHttpResponse getCloseableHttpResponse() {
    return closeableHttpResponse;
  }

  @Override
  public String toString() {
    return "Neo4jClientBuilder ["
        + (mapper != null ? "mapper=" + mapper + ", " : "")
        + (uri != null ? "uri=" + uri + ", " : "")
        + (authHeader != null ? "authHeader=" + authHeader + ", " : "")
        + (httpclient != null ? "httpclient=" + httpclient + ", " : "")
        + (closeableHttpResponse != null
            ? "closeableHttpResponse=" + closeableHttpResponse + ", " : "")
        + (json != null ? "json=" + json : "") + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime * result + ((authHeader == null) ? 0 : authHeader.hashCode());
    result = prime * result + ((closeableHttpResponse == null) ? 0
        : closeableHttpResponse.hashCode());
    result =
        prime * result + ((httpclient == null) ? 0 : httpclient.hashCode());
    result = prime * result + ((json == null) ? 0 : json.hashCode());
    result = prime * result + ((mapper == null) ? 0 : mapper.hashCode());
    result = prime * result + ((uri == null) ? 0 : uri.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Neo4jClientBuilder other = (Neo4jClientBuilder) obj;
    if (authHeader == null) {
      if (other.authHeader != null) return false;
    } else if (!authHeader.equals(other.authHeader)) return false;
    if (closeableHttpResponse == null) {
      if (other.closeableHttpResponse != null) return false;
    } else if (!closeableHttpResponse.equals(other.closeableHttpResponse))
      return false;
    if (httpclient == null) {
      if (other.httpclient != null) return false;
    } else if (!httpclient.equals(other.httpclient)) return false;
    if (json == null) {
      if (other.json != null) return false;
    } else if (!json.equals(other.json)) return false;
    if (mapper == null) {
      if (other.mapper != null) return false;
    } else if (!mapper.equals(other.mapper)) return false;
    if (uri == null) {
      if (other.uri != null) return false;
    } else if (!uri.equals(other.uri)) return false;
    return true;
  }

  public int compareTo(Neo4jClientBuilder o) {
    return 0;
  }

}
