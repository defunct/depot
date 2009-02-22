package com.goodworkalan.depot;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;

public class SaslCallbackHandler implements CallbackHandler
{
    private final Authenticator authenticator;

    private String name;
    
    public SaslCallbackHandler(Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }

    public void handle(Callback[] callbacks)
    throws IOException, UnsupportedCallbackException
    {
        for (int i = 0; i < callbacks.length; i++)
        {
            if (callbacks[i] instanceof NameCallback)
            {
                NameCallback nameCallback = (NameCallback) callbacks[i];
                name = nameCallback.getDefaultName();
            }
            else if (callbacks[i] instanceof PasswordCallback)
            {
                PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
                passwordCallback.setPassword(authenticator.passwordOf(name).toCharArray());
            }
            else if (callbacks[i] instanceof AuthorizeCallback)
            {
                AuthorizeCallback authorizeCallback = (AuthorizeCallback) callbacks[i];
                authorizeCallback.setAuthorized(authorizeCallback.getAuthenticationID().equals(authorizeCallback.getAuthorizationID()));
            }
        }
    }
}

