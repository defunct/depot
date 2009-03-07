package com.goodworkalan.depot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ResponseBuilder
{
    private String tag;
    
    private String name;
    
    private final Set<String> attributes;
    
    private final List<String> messages;
    
    private boolean attributed;
    
    public ResponseBuilder()
    {
        tag = "*";
        attributes = new TreeSet<String>();
        messages = new ArrayList<String>();
    }
    
    public void setTag(String tag)
    {
        this.tag = tag;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void addAttribute(String attribute)
    {
        attributes.add(attribute);
    }
    
    public void setAttributed(boolean attributed)
    {
        this.attributed = attributed;
    }
    
    public void addMessage(String message)
    {
        messages.add(message);
    }
    
    public void addQuotedMessage(String message)
    {
        addMessage('"' + message.replace("\\", "\\\\").replace("\"", "\\\"") + '"');
    }
    
    @Override
    public String toString()
    {
        StringBuilder newString = new StringBuilder();
        newString.append(tag).append(" ").append(name).append(" ");
        if (attributed)
        {
            String separator = "";
            newString.append("(");
            for (String attribute : attributes)
            {
                newString.append(separator).append("\\").append(attribute);
                separator = " ";
            }
            newString.append(") ");
        }
        String separator = "";
        for (String message : messages)
        {
            newString.append(separator).append(message);
            separator = " ";
        }
        newString.append("\r\n");
        return newString.toString();
    }
}
