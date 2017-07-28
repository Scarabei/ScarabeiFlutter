
package com.jfixby.scarabei.red.flutter;

import com.jfixby.scarabei.api.flutter.plugins.FlutterPlugin;
import com.jfixby.scarabei.api.flutter.plugins.FlutterPluginSpecs;
import com.jfixby.scarabei.api.flutter.plugins.FlutterPluginsComponent;
import com.jfixby.scarabei.api.log.L;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class RedFlutterPlugins implements FlutterPluginsComponent {

	private final FlutterActivity activity;

	public RedFlutterPlugins (final FlutterActivity activity) {
		this.activity = activity;

	}

	void registerPlugin (final MethodChannel.MethodCallHandler plugin, final String channelName) {
		final PluginRegistry registry = this.activity;
		final String className = plugin.getClass().getCanonicalName();
		final Registrar registrar = registry.registrarFor(className);
		final MethodChannel channel = new MethodChannel(registrar.messenger(), channelName);
		channel.setMethodCallHandler(plugin);
		L.d("FlutterPlugin registed[" + channelName + "]", plugin);
	}

	@Override
	public FlutterPluginSpecs newPluginSpecs () {
		return new FlutterPluginSpecs();
	}

	@Override
	public FlutterPlugin registerPlugin (final FlutterPluginSpecs specs) {
		return new RedFlutterPlugin(this, specs);
	}

}
