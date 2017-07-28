package io.flutter.plugin.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class StringCodec implements MessageCodec {
   private static final Charset UTF8 = Charset.forName("UTF8");
   public static final StringCodec INSTANCE = new StringCodec();

   public ByteBuffer encodeMessage(String message) {
      if(message == null) {
         return null;
      } else {
         byte[] bytes = message.getBytes(UTF8);
         ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
         buffer.put(bytes);
         return buffer;
      }
   }

   public String decodeMessage(ByteBuffer message) {
      if(message == null) {
         return null;
      } else {
         int length = message.remaining();
         byte[] bytes;
         int offset;
         if(message.hasArray()) {
            bytes = message.array();
            offset = message.arrayOffset();
         } else {
            bytes = new byte[length];
            message.get(bytes);
            offset = 0;
         }

         return new String(bytes, offset, length, UTF8);
      }
   }
}
