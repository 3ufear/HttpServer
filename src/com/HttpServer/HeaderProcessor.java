/**
 * Created by phil on 13-Dec-14.
 */
package com.HttpServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeaderProcessor {
    String[] buf = new String[10];
    String Host;
    String Connection;
    String Type;
    String File_root;
    public static final Pattern p_req = Pattern.compile("\\S*: .*");
    public static final Pattern p_req_type = Pattern.compile("\\S+ \\S+ \\S+");
    HeaderProcessor(String[] buffer) {
        this.buf = buffer;
    }

    public void ParseHeader() {
        int i =0;
        while(buf[i] != null) {
            Matcher m = p_req.matcher(buf[i]);
            if (m.matches()) {
                String[] parse_str = buf[i].split(":");
                if (parse_str[0].equals("Host")) {
                    Host = parse_str[0];
                } else if (parse_str[0].equals("Connection")) {
                    Connection = parse_str[1];
                }
            } else {
                Matcher m_type = p_req_type.matcher(buf[i]);
                if (m_type.matches()) {
                    String[] parse_str = buf[i].split(" ");
                    Type = parse_str[0];
                    File_root = parse_str[1];
                }
            }
            i++;
        }
    }

}
