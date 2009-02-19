package com.goodworkalan.depot;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;


public class DepotExceptionTest
{
    @Test
    public void constructor()
    {
        DepotException e = new DepotException(101);
        assertEquals(e.getCode(), 101);
    }
}
