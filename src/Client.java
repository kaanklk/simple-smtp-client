import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Client {
    private Socket s;
    private int serverPort; //configuration works on port 25(SMTP) with Postfix
    private String serverIpAddress;

    BufferedReader in;
    PrintWriter out;

    Scanner inputs = new Scanner(System.in);
    Scanner commands = new Scanner(System.in);

    public Client() {
        clientStart();
    }

    private void clientStart(){

        selectServerIpAddress(inputs);
        selectPort(inputs);
        printServerDetails(serverIpAddress,serverPort);

        try{
            s = new Socket(serverIpAddress,serverPort); //creating a connection
            System.out.println("\nConnected!");
            clientService(commands); //running commands to send e-mail
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

    public void clientService(Scanner inputs){
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(),true); //flush to clean buffer after inputs.

            //sending mail
            sendMail(in,out,inputs);

            in.close();
            out.close();

            System.out.println("\nYour e-mail successfully sent.");
        }
        catch (Exception e){
            System.out.println("Error"+e);
        }
    }

    private void sendMail(BufferedReader in,PrintWriter out, Scanner inputs) throws IOException {
        String var;

        //SMTP protocol commands to send e-mail

        System.out.print("MAIL FROM: ");
        var = inputs.nextLine();
        System.out.println(in.readLine());
        out.println("MAIL FROM: "+var);
        System.out.println(in.readLine());

        System.out.print("RCPT TO: ");
        var = inputs.nextLine();
        out.println("RCPT TO: "+var);
        System.out.println(in.readLine());

        out.println("DATA");
        System.out.println(in.readLine());

        System.out.print("DATA: ");
        var = inputs.nextLine();
        out.println(var);
        out.println(".");

        System.out.print("SUBJECT: ");
        var = inputs.nextLine();

        out.println("SUBJECT: "+var);
        System.out.println(in.readLine());
        out.println("quit");

    }

    private void selectServerIpAddress(Scanner inputs) {
        System.out.print("IP Address(can be localhost): ");
        this.serverIpAddress = inputs.nextLine();
    }

    private void selectPort(Scanner inputs){
        System.out.print("Server PORT(25,465,587): ");
        this.serverPort = inputs.nextInt();
    }

    private void printServerDetails(String serverIpAddress,int serverPort){
        System.out.println("\nServer IP   : "+serverIpAddress);
        System.out.println("Server PORT : "+serverPort);
    }
}
