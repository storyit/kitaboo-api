package com.kitaboo.OAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.http.HttpParameters;

@SpringBootApplication
public class OAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuthApplication.class, args);
		String getServiceUrl = "https://cloud.kitaboo.com/DistributionServices/ext/api/ListBooks";

		if (System.getenv("ACTION") == "list_books") {
			String methodGet = "GET";
			String response = callGetService(getServiceUrl, System.getenv("CONSUMER_KEY"), System.getenv("SECRET_KEY"), methodGet);
			System.out.println(response);
		}
	}

	public static String callGetService(String serviceEndPoint, String consumerKey, String secretKey, String method) {
		String response = new String();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(serviceEndPoint);
			urlConnection = (HttpURLConnection) url.openConnection();
			// Create an HttpURLConnection and add some headers
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestMethod(method);
			urlConnection.setReadTimeout(5 * 60 * 1000);
			urlConnection.setDoOutput(true);

			// Sign the request
			OAuthConsumer dealabsConsumer = new DefaultOAuthConsumer(consumerKey, secretKey);
			HttpParameters doubleEncodedParams = new HttpParameters();
			doubleEncodedParams.put("realm", "");
			dealabsConsumer.setAdditionalParameters(doubleEncodedParams);
			dealabsConsumer.sign(urlConnection);

			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			response = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		} finally {
			if (null != urlConnection)
				urlConnection.disconnect();
		}
		return response;
	}

	public static String callPostService(String serviceEndPoint, String consumerKey, String secretKey, String payLoad,
			String method, String contentType) {
		String response = new String();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(serviceEndPoint);
			urlConnection = (HttpURLConnection) url.openConnection();
			// Create an HttpURLConnection and add some headers
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestMethod(method);
			urlConnection.setReadTimeout(5 * 60 * 1000);
			urlConnection.setDoOutput(true);

			// Sign the request
			OAuthConsumer dealabsConsumer = new DefaultOAuthConsumer(consumerKey, secretKey);
			HttpParameters doubleEncodedParams = new HttpParameters();
			doubleEncodedParams.put("realm", "");
			dealabsConsumer.setAdditionalParameters(doubleEncodedParams);
			dealabsConsumer.sign(urlConnection);

			// Send the request and read the output
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			response = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		} finally {
			if (null != urlConnection)
				urlConnection.disconnect();
		}
		return response;
	}

}
