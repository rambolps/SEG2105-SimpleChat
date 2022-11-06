import java.io.*;
import java.util.Scanner;
import common.*;

public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
    try 
    {
      server= new EchoServer(port);
      
      
    } 
    catch(Exception e) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating Server.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        if (Character.toString(message.charAt(0)).equals("#")) {
            ServerCommands(message);
        } else {
            server.sendToAllClients("SERVER MSG>".concat(message));
            System.out.println("SERVER MSG>".concat(message));
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  private void ServerCommands(String message){
    switch (message.substring(1)) {
        case "quit":
          System.out.println("Closing Application...");
            try {
                server.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            };
          break;
        case "stop":
          server.stopListening();
          break;
        case "close":
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
          break;
        case "setport":
          if (!server.isListening()) {
            server.setPort(Integer.parseInt(message.substring(9)));
            System.out.println("Port Set To: " + message.substring(9));
          } else {
            System.out.println("Error: Server Is Currently Listening");
          }
          break;
        case "start":
          if (!server.isListening()) {
            try {
                server.listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
          } else {
            System.out.println("Error: Server Is Currently Listening");
          }
          break;
        case "getport":
          System.out.println("Port Is Set To: "+Integer.toString(server.getPort()));
          break;
        default:
          System.out.println("Sorry Command: " + message.substring(1) + " Does Not Exist!");
          break;
      }
  }
  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    
  }


  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of ConsoleChat class
