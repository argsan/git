package com.solace.rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class RESTPublishSample {
	 public static void main(String[] args) throws ClientProtocolException, IOException {
		  HttpClient client = new DefaultHttpClient();
		  HttpPost post = new HttpPost("http://192.168.56.102:2000/QUEUE/solace_requests");
		  StringEntity input = new StringEntity("product");
		  post.setEntity(input);
		  post.addHeader("Client Username", "solace_user");
		  post.addHeader("Client Password", "");
		  HttpResponse response = client.execute(post);
		  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
		   System.out.println(line);
		  }
		 }
}
