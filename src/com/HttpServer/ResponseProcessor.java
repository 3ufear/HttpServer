/**
 * Created by phil on 13-Dec-14.
 */
package com.HttpServer;


import java.io.*;

public class ResponseProcessor {
    HeaderProcessor Header;
    public int error_code;
    String DocumentRoot;
    public static String response = "HTTP/1.1 404 Not Found\r\n"+
                                       "Server: MyServer\r\n"+
                                        "Content-Type: text/html\r\n"+
                                         "Connection: close\r\n\r\n"+
                                         "<html><head></head><body><h1>404 NOT FOUND</h1></body></html>\n";
    public static String response_200;
    public static String response_500 = "HTTP/1.1 500 Forbidden\r\n"+
            "Server: MyServer\r\n"+
            "Content-Type: text/html\r\n"+
            "Connection: close\r\n\r\n"+
            "<html><head></head><body><h1>500 Forbidden</h1></body></html>\n";

    ResponseProcessor(HeaderProcessor header, String root) {
        this.Header = header;
        this.DocumentRoot = root;
    }


    public byte[] MakeResponse() {
        byte[] file_output = null;
        boolean flag_file_is_found = false;
        error_code = 404;
        if (Header.Type.equals("GET")) {
            try {
                if (Header.File_root.contains("../")) {
                    error_code = 500;
                } else {
                    File file = new File(DocumentRoot + Header.File_root);
                    if (file.exists()) {
                        error_code = 200;
                        flag_file_is_found = true;
                        file_output = new byte[(int) file.length()];
                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        bis.read(file_output, 0, file_output.length);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.err.print("File " + DocumentRoot + Header.File_root + "not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
                if (flag_file_is_found) {
                    String content_type = Get_content_type(Header.File_root);
                    response_200 = new String( "HTTP/1.1 200 OK\r\n" +
                            "Server: MyServer\r\n" +
                            "Content-Type: " + content_type + "\r\n" +
                            "Content-Length: " + file_output.length + "\r\n" +
                            "Connection: close\r\n\r\n");
                    return file_output;
                } else {
                    return null;
                }
        }

    return null;
    }

    private String Get_content_type(String file_root) {
        String[] pointers = file_root.split("\\.");
        String type = pointers[pointers.length-1];

        if (type.equals("jpg")) {
            return "image/jpeg";
        } else if(type.equals("jpeg")) {
            return "image/jpeg";
        } else if(type.equals("gif")) {
            return "image/gif";
        } else if(type.equals("png")) {
            return "image/png";
        } else if(type.equals("swf")) {
            return "application/x-shockwave-flash";
        } else if(type.equals("js")) {
            return "application/javascript";
        } else if(type.equals("css")) {
            return "text/css";
        } else if(type.equals("html")) {
            return "text/html";
        } else if(type.equals("htm")) {
            return "text/html";
        } else if (type.equals("txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";
        }
    }

}
