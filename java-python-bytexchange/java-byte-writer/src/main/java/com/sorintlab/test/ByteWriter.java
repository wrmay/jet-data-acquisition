package com.sorintlab.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteWriter {

    static final String FILENAME = "bytes.data";

    public static void main(String []args){
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for( short i=0; i < 10; ++i){
            buffer.putShort(i);
        }

        try {
            FileOutputStream outf = new FileOutputStream(FILENAME);
            outf.write(buffer.array());
            outf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("20 bytes representing 10 shorts written to " + FILENAME);
    }
}
