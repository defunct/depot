package com.goodworkalan.depot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class List extends Command
{
    @Override
    public Interpreter execute(String code, String parameters, DepotSession session, Response response)
    {
        String string = "\"(?:[^\"\\\\]|\\\\\\\\|\\\\\")*\"";
        String atomString = "[^(){ %*\\\\\"\\]]+";
        Pattern pattern = Pattern.compile("(" + atomString + "|" + string + ")\\s+(" + atomString + "|" + string + ")");
        Matcher matcher = pattern.matcher(parameters.trim());
        if (matcher.matches())
        {
            String reference = unquote(matcher.group(1));
            String mailbox = unquote(matcher.group(2));
            if (reference.equals("") && mailbox.equals(""))
            {
                ResponseBuilder newResponse = new ResponseBuilder();
                newResponse.setName("LIST");
                newResponse.addAttribute("Noselect");
                newResponse.addQuotedMessage("/");
                newResponse.addQuotedMessage("");
                response.send(newResponse.toString());

                newResponse = new ResponseBuilder();
                newResponse.setTag(code);
                newResponse.setName("LIST");
                newResponse.addMessage("Completed");
                response.send(newResponse.toString());
            }
        }
        return session.newCommandInterpreter();
    }
    
    private String unquote(String quoted)
    {
        if (quoted.charAt(0) == '"')
        {
            return quoted.substring(1, quoted.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return quoted;
    }
}
