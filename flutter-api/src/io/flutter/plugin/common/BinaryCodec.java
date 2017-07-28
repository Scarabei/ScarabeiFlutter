package io.flutter.plugin.common;

import java.nio.ByteBuffer;

public final class BinaryCodec implements MessageCodec {
   public static final BinaryCodec INSTANCE = new BinaryCodec();

   public ByteBuffer encodeMessage(ByteBuffer message) {
      return message;
   }

   public ByteBuffer decodeMessage(ByteBuffer message) {
      return message;
   }
}
