package me.lefted.lunacyforge.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ContentHandlerFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class FritzBoxReconnect {

    public static void reconnectApache(HttpClient httpclient) throws IOException {
	String xmldata = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
	    + "<s:Envelope s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<s:Body>"
	    + "<u:ForceTermination xmlns:u=\"urn:schemas-upnp-org:service:WANIPConnection:1\" />" + "</s:Body>" + "</s:Envelope>";

	// HttpPost post = new HttpPost("/upnp/control/WANIPConn1 HTTP/1.1");
	// HttpPost post = new HttpPost("http://fritz.box:49000/upnp/control/WANIPConn1");
	HttpPost post = new HttpPost("http://fritz.box:49000/igdupnp/control/WANIPConn1");

	post.addHeader("Host", "fritz.box:49000");
	post.addHeader("SOAPACTION", "urn:schemas-upnp-org:service:WANIPConnection:1#ForceTermination");
	post.addHeader("Content-Type", "text/xml; charset=utf-8");
	post.setProtocolVersion(HttpVersion.HTTP_1_1);

	post.setEntity(new StringEntity(xmldata));

	// execute request
	HttpResponse response = httpclient.execute(post);

	// Response
	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	String line;
	while ((line = rd.readLine()) != null) {
	    System.out.println(line);
	}
	// verify the valid error code first
	int statusCode = response.getStatusLine().getStatusCode();
	if (statusCode != 200) {
	    throw new RuntimeException("Failed with HTTP error code : " + statusCode);
	}
    }

}
