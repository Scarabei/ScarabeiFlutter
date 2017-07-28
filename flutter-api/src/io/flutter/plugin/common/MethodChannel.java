package io.flutter.plugin.common;

import android.util.Log;
import java.nio.ByteBuffer;

public final class MethodChannel {
   private static final String TAG = "MethodChannel#";
   private final BinaryMessenger messenger;
   private final String name;
   private final MethodCodec codec;

   public MethodChannel(BinaryMessenger messenger, String name) {
      this(messenger, name, StandardMethodCodec.INSTANCE);
   }

   public MethodChannel(BinaryMessenger messenger, String name, MethodCodec codec) {
      assert messenger != null;

      assert name != null;

      assert codec != null;

      this.messenger = messenger;
      this.name = name;
      this.codec = codec;
   }

   public void invokeMethod(String method, Object arguments) {
      this.invokeMethod(method, arguments, (MethodChannel.Result)null);
   }

   public void invokeMethod(String method, Object arguments, MethodChannel.Result callback) {
      this.messenger.send(this.name, this.codec.encodeMethodCall(new MethodCall(method, arguments)), callback == null?null:new MethodChannel.IncomingResultHandler(callback));
   }

   public void setMethodCallHandler(MethodChannel.MethodCallHandler handler) {
      this.messenger.setMessageHandler(this.name, handler == null?null:new MethodChannel.IncomingMethodCallHandler(handler));
   }

   private final class IncomingMethodCallHandler implements BinaryMessenger.BinaryMessageHandler {
      private final MethodChannel.MethodCallHandler handler;

      IncomingMethodCallHandler(MethodChannel.MethodCallHandler handler) {
         this.handler = handler;
      }

      public void onMessage(ByteBuffer message, final BinaryMessenger.BinaryReply reply) {
         MethodCall call = MethodChannel.this.codec.decodeMethodCall(message);

         try {
            this.handler.onMethodCall(call, new MethodChannel.Result() {
               public void success(Object result) {
                  reply.reply(MethodChannel.this.codec.encodeSuccessEnvelope(result));
               }

               public void error(String errorCode, String errorMessage, Object errorDetails) {
                  reply.reply(MethodChannel.this.codec.encodeErrorEnvelope(errorCode, errorMessage, errorDetails));
               }

               public void notImplemented() {
                  reply.reply((ByteBuffer)null);
               }
            });
         } catch (RuntimeException var5) {
            Log.e("MethodChannel#" + MethodChannel.this.name, "Failed to handle method call", var5);
            reply.reply(MethodChannel.this.codec.encodeErrorEnvelope("error", var5.getMessage(), (Object)null));
         }

      }
   }

   private final class IncomingResultHandler implements BinaryMessenger.BinaryReply {
      private final MethodChannel.Result callback;

      IncomingResultHandler(MethodChannel.Result callback) {
         this.callback = callback;
      }

      public void reply(ByteBuffer reply) {
         try {
            if(reply == null) {
               this.callback.notImplemented();
            } else {
               try {
                  this.callback.success(MethodChannel.this.codec.decodeEnvelope(reply));
               } catch (FlutterException var3) {
                  this.callback.error(var3.code, var3.getMessage(), var3.details);
               }
            }
         } catch (RuntimeException var4) {
            Log.e("MethodChannel#" + MethodChannel.this.name, "Failed to handle method call result", var4);
         }

      }
   }

   public interface Result {
      void success(Object var1);

      void error(String var1, String var2, Object var3);

      void notImplemented();
   }

   public interface MethodCallHandler {
      void onMethodCall(MethodCall var1, MethodChannel.Result var2);
   }
}
