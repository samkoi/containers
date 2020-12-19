package de.uniba.dsg.jaxrs;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.Scanner;

import javax.ws.rs.core.UriBuilder;


import de.uniba.dsg.jaxrs.API.BeverageApi;
import de.uniba.dsg.jaxrs.API.DbHandlerApi;
import de.uniba.dsg.jaxrs.API.ManagementApi;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class JaxRsServer {
    private static Properties properties = Configuration.loadProperties();

    public static void main(String[] args) throws IOException {
        String serverUri = properties.getProperty("serverUri");
        String service = properties.getProperty("service");
        URI baseUri = UriBuilder.fromUri(serverUri).build();
        ResourceConfig config;
        config = choseApi(service);
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
        System.out.println("Server ready to serve your JAX-RS requests...");
        System.out.println("Enter exit key to exit...");
        Boolean bool = true;
        while(bool) {
        	Scanner sc = new Scanner(System.in);
        	if(sc.hasNext()) {
        		if (sc.next().contains("exit")) {
            		bool = false;
            	}
        	}
        }
        System.out.println("Stopping server");
        server.stop(1);
    }


	private static ResourceConfig choseApi(String service) {
		ResourceConfig config;
		switch(service) {
        case "beverage":
        	System.out.println("BeverageApiStarted");
        	config = ResourceConfig.forApplicationClass(BeverageApi.class);
        	break;
        case "management":
        	System.out.println("ManagementApiStarted");
        	config = ResourceConfig.forApplicationClass(ManagementApi.class);
        	break;
        case "dbhandler":
        	System.out.println("DbHandlerApiStarted");
        	config = ResourceConfig.forApplicationClass(DbHandlerApi.class);
        	break;
        default:
        	config = ResourceConfig.forApplicationClass(BeverageApi.class);
        }
		return config;
	}
}

