package com.goodworkalan.depot;

public class Bad extends Exception
{
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a bad exception with the given message.
     * 
     * @param message
     *            The message.
     */
    public Bad(String code, String message)
    {
        super(code + " BAD " + message + "\r\n");
    }
}
