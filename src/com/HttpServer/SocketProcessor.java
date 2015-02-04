/**
 * Created by phil on 13-Dec-14.
 */
package com.HttpServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

class SocketProcessor implements Runnable {

    private Socket sock;
    private InputStream instream;
    private OutputStream outstream;
    private String root;

    SocketProcessor(Socket s, String root) throws Throwable {
        this.root = root;
        this.sock = s;
        this.instream = s.getInputStream();
        this.outstream = s.getOutputStream();
    }

    public void run() {
        try {
            String[] Headers = readInputHeaders();
            HeaderProcessor Head = new HeaderProcessor(Headers);
            Head.ParseHeader();
            ResponseProcessor Response = new ResponseProcessor(Head, root);
            String response = null;
            byte[] file_output = Response.MakeResponse();
            if (Response.error_code == 404) {
                response = ResponseProcessor.response;
            } else if (Response.error_code == 200) {
                response = ResponseProcessor.response_200;
            } else if (Response.error_code == 500) {
                response = ResponseProcessor.response_500;
            }
            writeResponse(response, file_output);
        } catch (Throwable t) {
            System.err.println("Error" + t.toString());
        } finally {
            try {
                sock.close();
            } catch (Throwable t) {
                System.err.println("Error" + t.toString());
            }
        }
        System.out.println("Client processing finished");
    }

    private void writeResponse(String s, byte [] file_output) throws Throwable {

        outstream.write(s.getBytes());
        if (file_output != null) {
            outstream.write(file_output,0,file_output.length);
       }
        outstream.flush();
    }

    private String[] readInputHeaders() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(instream));
        String[] buf = new String[10];
        int i = 0;
        while(true) {
            String s = br.readLine();
            if(s == null || s.trim().length() == 0) {
                return buf;
            }
            buf[i] = s;
            i++;

        }
    }
}