package com.goodworkalan.depot;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PlainAuthentication extends LineInterpreter {
    private final String code;

    public PlainAuthentication(String code) {
        this.code = code;
    }
    
    /**
     * Reads the Base64 encoded actor/authorizor/password and stores it for
     * comparison during execute.
     *
     * @param response A response to send to the client.
     */
    @Override
    public void execute(DepotSession session, Response response) {
        // Decode the Base64 data.
        byte[] decoded = Base64.decode(getLine());

        // Convert the raw byte buffer to characters using UTF-8.
        Charset cs = Charset.forName("UTF-8");
        CharBuffer chars = cs.decode(ByteBuffer.wrap(decoded));
        
        // Read the zero value sepratated string parameters.
        List<String> credentials = new ArrayList<String>();
        StringBuilder newString = new StringBuilder();
        while (chars.remaining() != 0) {
            char ch = chars.get();
            if (ch == '\0') {
                credentials.add(newString.toString());
                newString.setLength(0);
            } else {
                newString.append(ch);
            }
        }
        credentials.add(newString.toString());
        if (credentials.get(1).equals("alan") && credentials.get(2).equals("password")) {
            session.setAuthorizationId(credentials.get(0));
            response.ok(code, "PLAIN  authentication successful");
        } else {
            response.no(code, "Invalid credentials");
        }
        setNextInterpreter(session.newCommandInterpreter());
    }
}
