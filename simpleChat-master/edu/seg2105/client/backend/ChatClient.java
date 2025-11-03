// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;


import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	
    if (message.startsWith("#")) {
    	
    	handleCommand(message);
    	
    }
    
    else {
    	
    	sendToServer(message);
    	
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String command) {
	  
	  if (command.equals("#quit")) {
		  
		  quit();
		  
	  }
	  
	  else if (command.equals("#logoff")) {
		  
		  connectionClosed();
		  
	  }
	  
	  else if (command.startsWith("#sethost")) {
		  
		  if (!isConnected()) {
			  
			  command = command.substring(command.indexOf("<")+1);
			  command = command.substring(0,command.indexOf(">"));
			  setHost(command);
			  
		  } else {
			  
			  clientUI.display("ERROR: Client Must Be Logged Off to Proccess Request");
			  
		  }
		  
	  }
	  
	  else if (command.startsWith("#setport")) {
		  
		  if (!isConnected()) {
			  
			  command = command.substring(command.indexOf("<")+1);
			  command = command.substring(0,command.indexOf(">"));
			  setPort(Integer.parseInt(command));
			  
		  } else {
			  
			  clientUI.display("ERROR: Client Must Be Logged Off to Proccess Request");
			  
		  }
		  
	  }
	  
	  else if (command.equals("#login")) {
		  
		  if (!isConnected()) {
			  
			  try {
				  
				openConnection();
				
			  } catch (IOException e) {
				
				  System.out.println("Error Logging in, Please try Again");
				  
			  }
		  } else {
			  
			  clientUI.display("ERROR: Client Must Be Logged Off to Proccess Request");
		  }
		  
	  }
	  
	  else if (command.equals("#gethost")) {
		  
		  clientUI.display(getHost());
		  
	  }
	  
	  else if (command.equals("#getport")) {
		  
		  clientUI.display(Integer.toString(getPort()));
		  
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  @Override
  protected void connectionException(Exception exception) {
	  
	  clientUI.display("Server has Shut Down, Quitting...");
	  System.exit(0);
  }
  
  @Override
  protected void connectionClosed() {
	  clientUI.display("Connection is Closed");
  }
}
//End of ChatClient class
