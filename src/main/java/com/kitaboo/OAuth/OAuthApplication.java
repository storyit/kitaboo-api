package com.kitaboo.OAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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

		if (System.getenv("ACTION").equals("list_books")) {
			String getServiceUrl = "https://cloud.kitaboo.com/DistributionServices/ext/api/ListBooks";
			String response = callGetService(getServiceUrl, System.getenv("CONSUMER_KEY"), System.getenv("SECRET_KEY"));
			System.out.println(response);
		} else if (System.getenv("ACTION").equals("register_user")) {
			String postServiceUrl = "https://cloud.kitaboo.com/DistributionServices/ext/api/registerUser";
			String body = System.getenv("DATA");
			String response = callPostService(postServiceUrl, System.getenv("CONSUMER_KEY"),
					System.getenv("SECRET_KEY"), body);
			System.out.println(response);
		} else if (System.getenv("ACTION").equals("purchase")) {
			String postServiceUrl = "https://cloud.kitaboo.com/DistributionServices/ext/api/order";
			String body = System.getenv("DATA");
			String response = callPostService(postServiceUrl, System.getenv("CONSUMER_KEY"),
					System.getenv("SECRET_KEY"), body);
			System.out.println(response);
		} else if (System.getenv("ACTION").equals("launch_book")) {
			String postServiceUrl = "https://cloud.kitaboo.com/DistributionServices/ext/api/LaunchBook?userID=testuser76122&bookID=14104841";
			String body = null;
			String response = callPostService(postServiceUrl, System.getenv("CONSUMER_KEY"),
					System.getenv("SECRET_KEY"), body);
			System.out.println(response);
		}
	}

	public static String callGetService(String serviceEndPoint, String consumerKey, String secretKey) {
		String response = new String();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(serviceEndPoint);
			urlConnection = (HttpURLConnection) url.openConnection();
			// Create an HttpURLConnection and add some headers
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestMethod("GET");
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

	public static String callPostService(String serviceEndPoint, String consumerKey, String secretKey, String payLoad) {
		String response = new String();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(serviceEndPoint);
			urlConnection = (HttpURLConnection) url.openConnection();
			// Create an HttpURLConnection and add some headers
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
			urlConnection.setRequestMethod("POST");
			urlConnection.setReadTimeout(5 * 60 * 1000);
			urlConnection.setDoOutput(true);

			// Sign the request
			OAuthConsumer dealabsConsumer = new DefaultOAuthConsumer(consumerKey, secretKey);
			HttpParameters doubleEncodedParams = new HttpParameters();
			doubleEncodedParams.put("realm", "");
			dealabsConsumer.setAdditionalParameters(doubleEncodedParams);
			dealabsConsumer.sign(urlConnection);

			if (payLoad != null) {
				OutputStreamWriter outputStreamWriter = null;
				try {
					outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
					outputStreamWriter.write(payLoad);
				} finally {
					if (outputStreamWriter != null) {
						outputStreamWriter.close();
					}
				}
			}

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
