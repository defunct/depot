package com.goodworkalan.depot;

import java.nio.charset.Charset;

import com.goodworkalan.manifold.Sender;


public abstract class Command {
    public abstract Interpreter execute(String code, String parameters, DepotSession session, Response response);
    
    public void send(Sender sender, String response) {
        sender.send(Charset.forName("UTF-8").encode(response));
    }

    public String no(String code, String message) {
        return code + " NO " + message + "\r\n";
    }

    public String bad(String code, String message) {
        return code + " BAD " + message + "\r\n";
    }
}
