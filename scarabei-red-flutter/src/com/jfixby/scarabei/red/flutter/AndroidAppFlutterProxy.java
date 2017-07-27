
package com.jfixby.scarabei.red.flutter;

import com.jfixby.scarabei.api.log.L;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BasicMessageChannel.Reply;
import io.flutter.plugin.common.StringCodec;
import io.flutter.view.FlutterView;

public class AndroidAppFlutterProxy {

	private static final String CHANNEL_NAME = "BasicMessageChannel.AndroidAppFlutterProxy";

	public AndroidAppFlutterProxy () {

	}

	private void processMessage (final String channelName, final String message, final Reply<String> messageResponse) {
		L.d(channelName, message);
		L.d("   replyTo", messageResponse);

	}

	AndroidAppFlutterProxy deploy (final FlutterActivity activity) {
		final FlutterView FLUTTER_VIEW = activity.getFlutterView();
		final String channelName = CHANNEL_NAME;
		new BasicMessageChannel<>(FLUTTER_VIEW, channelName, StringCodec.INSTANCE)
			.setMessageHandler(new BasicMessageChannel.MessageHandler<String>() {
				@Override
				public void onMessage (final String message, final BasicMessageChannel.Reply<String> messageResponse) {
					// messageResponseCamera = messageResponse;
					// openCamera();
					AndroidAppFlutterProxy.this.processMessage(channelName, message, messageResponse);
				}
			});
		return this;
	}

}
