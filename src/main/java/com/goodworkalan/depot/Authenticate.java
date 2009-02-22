package com.goodworkalan.depot;

import java.nio.charset.Charset;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

public class Authenticate extends Command
{
    @Override
    public Interpreter execute(String code, String parameters, DepotSession session, Response response)
    {
        String auth = parameters;
        String contents = "";
        try
        {
            if (parameters.indexOf(" ") != -1)
            {
                auth = parameters.substring(0, parameters.indexOf(" "));
                contents = parameters.substring(parameters.indexOf(" ")).trim();
            }
            if (auth.equals("PLAIN"))
            {
                PlainAuthentication plainAuthentication = new PlainAuthentication(code, contents);
                response.send(plainAuthentication.begin());
                return plainAuthentication;
            }
            SaslServer saslServer = Sasl.createSaslServer(auth, "imap", "localhost", null, new SaslCallbackHandler(session.getAuthenticator()));
            response.send(Charset.forName("UTF-8").encode("+ "));
            response.sendLine(Base64.encodeBytes(saslServer.evaluateResponse(new byte[0]), Base64.DONT_BREAK_LINES));
            return new SaslAuthentication(code, auth, saslServer);
        }
        catch (SaslException e)
        {
            response.bad(code, "Unable to authenticate with " + auth);
            return new CommandInterpreter();
        }
    }
}
