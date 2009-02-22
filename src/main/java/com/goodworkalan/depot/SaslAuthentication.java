package com.goodworkalan.depot;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

public class SaslAuthentication extends LineInterpreter
{
    private final String code;
    
    private final String auth;

    private final SaslServer saslServer;
    
    public SaslAuthentication(String code, String auth, SaslServer saslServer)
    {
        this.code = code;
        this.auth = auth;
        this.saslServer = saslServer;
    }
    
    @Override
    public void execute(DepotSession session, Response response) throws Bad
    {
        // Decode the Base64 data.
        byte[] decoded = Base64.decode(getLine());

        try
        {
            byte[] challenge = saslServer.evaluateResponse(decoded);
            if (saslServer.isComplete())
            {
                session.setAuthorizationId(saslServer.getAuthorizationID());
                String qop = (String) saslServer.getNegotiatedProperty(Sasl.QOP);
                boolean hasSecurityLayer = (qop != null &&  (qop.equals("auth-int") || qop.equals("auth-conf")));
                if (hasSecurityLayer)
                {
                    session.setDataWrapper(new SaslWrapper(saslServer));
                }
                else
                {
                    saslServer.dispose();
                }
                response.ok(code, auth + " authentication successful");
                setNextInterpreter(new CommandInterpreter());
            }
            else
            {
                response.send(Base64.encodeBytes(challenge, Base64.DONT_BREAK_LINES));
            }
        }
        catch (SaslException e)
        {
            try
            {
                saslServer.dispose();
            }
            catch (SaslException ignore)
            {
            }
            response.no(code, "Invalid credentials");
            setNextInterpreter(new CommandInterpreter());
        }
    }
}
