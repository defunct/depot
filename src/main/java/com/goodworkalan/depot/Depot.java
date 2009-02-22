package com.goodworkalan.depot;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.goodworkalan.manifold.Manifold;

public class Depot
{
    public static void main(String[] args) throws IOException
    {
        new Manifold(new DepotSessionFactory(), Executors.newCachedThreadPool()).bind();
    }
}
