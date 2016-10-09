/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

/**
 *
 * @author Chris Li
 * CS 3913 Java Homework 4
 * Server code
 */

import java.net.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class ChatServer {
    static ArrayList<ClientConnect> clientSockets;  //List of all the people that connected to this chat server
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            ServerSocket ss = new ServerSocket(5190);
            System.out.println("Server started");
            clientSockets = new ArrayList();
            while(true){        //Handle accepting a new connection
                Socket s = ss.accept();
                Scanner sin = new Scanner(s.getInputStream());
                String name = sin.nextLine();
                //System.out.println(s.getInetAddress() + " " + name);
                //Alert all users that a person has connected to the chat.
                for(ClientConnect c: clientSockets){
                    c.getPS().println(name + " connected.");
                }
                //Create new ClientConnect object that holds the input/output streams of the connection as well as their name
                ClientConnect cs = new ClientConnect(sin, new PrintStream(s.getOutputStream(),true), name);
                cs.start();
                clientSockets.add(cs);

            }
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Caught Exception");
        }
    }


    static class ClientConnect extends Thread{
        String name;
        PrintStream out;
        Scanner in;
        ClientConnect(Scanner sin, PrintStream p, String n){
            in = sin; out = p; name = n;
        }

        public void run(){
            try{
                String sin = in.nextLine();
                while(true){
                   sin = in.nextLine();
                   //System.out.println(sin);
                   if(sin.equals("abcdefghijklmnopqrstuvwxyz1234567890")){
                        for(ClientConnect c: clientSockets)
                            c.out.println(name + " disconnected.");
                        clientSockets.remove(this);
                        this.interrupt();
                    }
                   else{
                       for(ClientConnect c: clientSockets)
                            c.out.println(name + ": " + sin);
                   }
                }
            }
            catch(Exception e){}
        }

        public String getUserName(){
            return name;
        }

        public PrintStream getPS(){
            return out;
        }

    }
}

