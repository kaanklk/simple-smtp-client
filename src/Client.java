import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Client {
    private Socket s;
    private int serverPort; //my configuration works on port 25(SMTP)
    private String serverIpAddress;

    BufferedReader in; //reads everything in a file to a variable
    PrintWriter out;

    String inData;
    String outData;

    //constructor for client
    public Client() {
        clientStart();
    }

    private void clientStart(){
        serverIpAddress = "127.0.0.1"; //localhost
        serverPort = 25; //25 for SMTP
        System.out.println("Server IP   : "+serverIpAddress);
        System.out.println("Server PORT : "+serverPort);
        try{
            s = new Socket(serverIpAddress,serverPort); //creating a connection point(socket)
            System.out.println("\nConnected!");
            Scanner inputs = new Scanner(System.in);
            clientService(inputs);
        }
        catch (Exception e){
            System.out.println("Error : "+e);
        }
        finally {
            try{
                s.close(); //closing the connection
            }
            catch (Exception e){
                System.out.println("Error"+e);
            }
        }
    }

    //sending commands through telnet
    public void clientService(Scanner inputs){
        try {
            String var;

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(),true); //autoflush to clean buffer after inputs.

            //sending mail
            sendMail(in,out,inputs);


            in.close();
            out.close();

            System.out.println("Your e-mail successfully sent.");
        }
        catch (Exception e){
            System.out.println("Error"+e);
        }
    }

    private void sendMail(BufferedReader in,PrintWriter out, Scanner inputs) throws IOException {
        String var;

        //MAIL FROM: Implementation for SMTP protocol
        System.out.println("MAIL FROM: ");
        var = inputs.nextLine();
        System.out.println(in.readLine());
        out.println("MAIL FROM: "+var);
        System.out.println(in.readLine());

        System.out.println("RCPT TO: ");
        var = inputs.nextLine();
        out.println("RCPT TO: "+var);
        System.out.println(in.readLine());

        out.println("data");
        System.out.println(in.readLine());

        System.out.println("Your message: ");
        var = inputs.nextLine();
        out.println(var);
        out.println(".");

        System.out.println("SUBJECT: ");
        var = inputs.nextLine();

        out.println("SUBJECT: "+var);
        System.out.println(in.readLine());
        out.println("quit");
    }
}
