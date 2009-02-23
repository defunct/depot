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
        try
        {
            if (auth.equals("PLAIN"))
            {
                PlainAuthentication plainAuthentication = new PlainAuthentication(code);
                response.sendLine("+ ");
                return plainAuthentication;
            }
            SaslServer saslServer = Sasl.createSaslServer(auth, "imap", "localhost", null, new SaslCallbackHandler(session.getAuthenticator()));
            if (saslServer != null)
            {
                response.send(Charset.forName("UTF-8").encode("+ "));
                response.sendLine(Base64.encodeBytes(saslServer.evaluateResponse(new byte[0]), Base64.DONT_BREAK_LINES));
                return new SaslAuthentication(code, auth, saslServer);
            }
            response.bad(code, "Unable to authenticate with " + auth);
            return session.newCommandInterpreter();
        }
        catch (SaslException e)
        {
            response.bad(code, "Unable to authenticate with " + auth);
            return session.newCommandInterpreter();
        }
    }
}
