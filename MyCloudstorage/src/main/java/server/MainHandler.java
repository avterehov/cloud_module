package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainHandler  extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = ((ByteBuf)msg);
        try {if (msg == null) {return;}



            int sizeMsg =    buf.readInt();
            byte[] dataMsg = new byte [sizeMsg];
            buf.readBytes(dataMsg);
            String command = new String(dataMsg);
            if (command.startsWith("Upload")){
                        String filename = command.split(" ")[1];
                        File file = new File ("server" + File.separator + filename);
                        FileOutputStream fos = new FileOutputStream(file
                        );
                        long size = buf.readLong();

                        for (int i = 0; i < (size + 255) / 256; i++) {
                            byte[] buffer = new byte[256];
                            buf.readBytes(buffer);
                            fos.write(buffer);

                        }
                        fos.close();
                    }
            byte[] buffer = new byte[256];
        } finally {
        }ReferenceCountUtil.release(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}