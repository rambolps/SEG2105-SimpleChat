// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * The interface type variable. It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String host, int port, ChatIF clientUI)
      throws IOException {
    super(host, port); // Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());
  }

  private void ClientCommands(String message) {
    switch (message.substring(1)) {
      case "quit":
        System.out.println("Closing Application...");
        quit();
        break;
      case "logoff":
        try {
          closeConnection();
        } catch (IOException e) {
        }
        break;
      case "sethost":
        if (!this.isConnected()) {
          this.setHost(message.substring(9));
          System.out.println("Host Set To: " + message.substring(9));
        } else {
          System.out.println("Error: Client Is Currently Logged In");
        }
        break;
      case "setport":
        if (!this.isConnected()) {
          this.setPort(Integer.parseInt(message.substring(9)));
          System.out.println("Port Set To: " + message.substring(9));
        } else {
          System.out.println("Error: Client Is Currently Logged In");
        }
        break;
      case "login":
        if (!this.isConnected()) {
          try {
            this.openConnection();
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          System.out.println("Error: Client Is Currently Logged In");
        }
        break;
      case "gethost":
        System.out.println("Host Is Set To: "+getHost());
        break;
      case "getport":
        System.out.println("Port Is Set To: "+Integer.toString(getPort()));
        break;
      default:
        System.out.println("Sorry Command: " + message.substring(1) + " Does Not Exist!");
        break;
    }
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message) {

    if (Character.toString(message.charAt(0)).equals("#")) {
      ClientCommands(message);
    } else {
      try {
        sendToServer(message);
      } catch (IOException e) {
        clientUI.display("Could not send message to server.  Terminating client.");
        quit();
      }
    }

  }

  @Override
  protected void connectionClosed() {
    System.out.println("Server Has Shut Down!");
    System.out.println("Shutting Off Client...");
    quit();

  }

  @Override
  protected void connectionException(Exception exception) {
    System.out.println("The Server Is Displaying Errors!");
    System.out.println("Shutting Off Client...");
    quit();
  }

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }
}
// End of ChatClient class
