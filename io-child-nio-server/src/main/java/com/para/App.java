package com.para;

import com.para.nioserver.NioServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new Thread(new NioServer()).start();
    }
}
