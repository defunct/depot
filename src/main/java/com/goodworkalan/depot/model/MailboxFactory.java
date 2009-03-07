package com.goodworkalan.depot.model;

public class MailboxFactory
{
    public Mailbox newMailbox(String user)
    {
        return new Mailbox();
    }
}
