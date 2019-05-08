package com.para;

import com.para.ioclient.BioClient;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println("BIO Client 客户端启动......");
        for(int i = 0; i < 10; i++) {
            BioClient.clientReq(i);
        }
    }
}
