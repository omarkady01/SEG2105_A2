package edu.seg2105.edu.server.backend;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.ChatIF;
import edu.seg2105.client.ui.ClientConsole;

import java.io.*;
import java.util.Scanner;
import ocsf.server.*;


public class ServerConsole implements ChatIF { 
	
	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	
	Scanner fromConsole;
	
	public ServerConsole (int port) {
		
		server = new EchoServer(port);
		
		try {
			server.listen();
			
		}
		catch (IOException e) {
			
			System.out.println("Error: Can't Listen for Clients"
	                + " Terminating Server.");
	      System.exit(1);
			
		}
	      
	    fromConsole = new Scanner(System.in); 
	    
	  }
	
	@Override
	public void display(String message) {
		
	    System.out.println("> " + message);
	    
	  }
	
	 public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        
	        if (message.startsWith("#")) {
	        	
	        	handleCommand(message);
	        	
	        } else {
	        	
	        	
	        	server.handleMessageFromServer(message);
	        	
	        }
	        
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	 
	 private void handleCommand(String command) {
		 
		 if (command.equals("#quit")) {
			 
			 try {
				server.close();
				
			 } catch (IOException e) {
				
				System.out.println("Error Processing Quit Request, Please try Again");
			 }
			 System.exit(0);
			 
		 } 
		 
		 else if (command.equals("#stop")) {
			 
			 server.stopListening();
			 
			 
		 }
		 
		 else if (command.equals("#close")) {
			 
			 try {
				server.close();
				
			 } catch (IOException e) {
				 
				 System.out.println("Error Processing Close Request, Please try Again");
			 }
			 
		 }
		 
		 else if (command.startsWith("#setport")) {
			 
			  if (!server.isListening()) {
				  
				  command = command.substring(command.indexOf("<")+1);
				  command = command.substring(0,command.indexOf(">"));
				  server.setPort(Integer.parseInt(command));
				  
			  } else {
				  
				  System.out.println("ERROR: Cannot Process Request Until Server Closes");
				  
			  }
			 
		 }
		 
		 else if (command.equals("#start")) {
			 
			 if (!server.isListening()) {
				 
				 try {
					server.listen();
					
				 } catch (IOException e) {
					 
					 System.out.println("Error Processing Start Request, Please try Again");
					
				 }
			 } else {
				 
				 System.out.println("Server Already Listening..");
			 }
			 
			 
		 }
		 
		 else if (command.equals("#getport")) {
			 
			 System.out.println(Integer.toString(server.getPort()));
			 
		 }
		 
	 }
	 
	  public static void main(String[] args) 
	  {
	    int port = 0;

	    try
	    {
	      port = Integer.parseInt(args[1]);
	      
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      port = DEFAULT_PORT;
	    }
	    catch(NumberFormatException num) {
	    	port = DEFAULT_PORT;
	    }
	    ServerConsole server = new ServerConsole(port);
	    server.accept(); 
	  }

}
