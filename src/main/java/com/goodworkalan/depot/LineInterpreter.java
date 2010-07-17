package com.goodworkalan.depot;

import java.nio.ByteBuffer;

public abstract class LineInterpreter extends Interpreter {
    private boolean carriageReturn;

    private final StringBuilder newString = new StringBuilder();

    private String line;

    @Override
    public boolean read(ByteBuffer data) throws Bad {
        while (data.remaining() != 0) {
            char ch = (char) data.get();
            if (ch == '\r') {
                carriageReturn = true;
            } else if (ch == '\n') {
                if (!carriageReturn) {
                    throw new Bad("*", "Malformed line");
                }
                line = newString.toString();
                return true;
            } else {
                newString.append(ch);
            }
        }
        return false;
    }

    protected String getLine() {
        return line;
    }
}
