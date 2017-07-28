package io.flutter.plugin.common;

import android.util.Log;
import java.nio.ByteBuffer;

public final class BasicMessageChannel {
   private static final String TAG = "BasicMessageChannel#";
   private final BinaryMessenger messenger;
   private final String name;
   private final MessageCodec codec;

   public BasicMessageChannel(BinaryMessenger messenger, String name, MessageCodec codec) {
      assert messenger != null;

      assert name != null;

      assert codec != null;

      this.messenger = messenger;
      this.name = name;
      this.codec = codec;
   }

   public void send(Object message) {
      this.send(message, (BasicMessageChannel.Reply)null);
   }

   public void send(Object message, BasicMessageChannel.Reply callback) {
      this.messenger.send(this.name, this.codec.encodeMessage(message), callback == null?null:new BasicMessageChannel.IncomingReplyHandler(callback));
   }

   public void setMessageHandler(BasicMessageChannel.MessageHandler handler) {
      this.messenger.setMessageHandler(this.name, handler == null?null:new BasicMessageChannel.IncomingMessageHandler(handler));
   }

   private final class IncomingMessageHandler implements BinaryMessenger.BinaryMessageHandler {
      private final BasicMessageChannel.MessageHandler handler;

      private IncomingMessageHandler(BasicMessageChannel.MessageHandler handler) {
         this.handler = handler;
      }

      public void onMessage(ByteBuffer message, final BinaryMessenger.BinaryReply callback) {
         try {
            this.handler.onMessage(BasicMessageChannel.this.codec.decodeMessage(message), new BasicMessageChannel.Reply() {
               public void reply(Object reply) {
                  callback.reply(BasicMessageChannel.this.codec.encodeMessage(reply));
               }
            });
         } catch (RuntimeException var4) {
            Log.e("BasicMessageChannel#" + BasicMessageChannel.this.name, "Failed to handle message", var4);
            callback.reply((ByteBuffer)null);
         }

      }

      // $FF: synthetic method
      IncomingMessageHandler(BasicMessageChannel.MessageHandler x1, Object x2) {
         this(x1);
      }
   }

   private final class IncomingReplyHandler implements BinaryMessenger.BinaryReply {
      private final BasicMessageChannel.Reply callback;

      private IncomingReplyHandler(BasicMessageChannel.Reply callback) {
         this.callback = callback;
      }

      public void reply(ByteBuffer reply) {
         try {
            this.callback.reply(BasicMessageChannel.this.codec.decodeMessage(reply));
         } catch (RuntimeException var3) {
            Log.e("BasicMessageChannel#" + BasicMessageChannel.this.name, "Failed to handle message reply", var3);
         }

      }

      // $FF: synthetic method
      IncomingReplyHandler(BasicMessageChannel.Reply x1, Object x2) {
         this(x1);
      }
   }

   public interface Reply {
      void reply(Object var1);
   }

   public interface MessageHandler {
      void onMessage(Object var1, BasicMessageChannel.Reply var2);
   }
}
