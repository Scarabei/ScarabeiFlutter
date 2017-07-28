package io.flutter.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.view.FlutterView;

public class FlutterActivity extends Activity implements FlutterView.Provider, PluginRegistry, FlutterActivityDelegate.ViewFactory {
   private final FlutterActivityDelegate delegate = new FlutterActivityDelegate(this, this);
   private final FlutterActivityEvents eventDelegate;
   private final FlutterView.Provider viewProvider;
   private final PluginRegistry pluginRegistry;

   public FlutterActivity() {
      this.eventDelegate = this.delegate;
      this.viewProvider = this.delegate;
      this.pluginRegistry = this.delegate;
   }

   public FlutterView getFlutterView() {
      return this.viewProvider.getFlutterView();
   }

   public FlutterView createFlutterView(Context context) {
      return null;
   }

   public final boolean hasPlugin(String key) {
      return this.pluginRegistry.hasPlugin(key);
   }

   public final Object valuePublishedByPlugin(String pluginKey) {
      return this.pluginRegistry.valuePublishedByPlugin(pluginKey);
   }

   public final PluginRegistry.Registrar registrarFor(String pluginKey) {
      return this.pluginRegistry.registrarFor(pluginKey);
   }

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.eventDelegate.onCreate(savedInstanceState);
   }

   protected void onDestroy() {
      this.eventDelegate.onDestroy();
      super.onDestroy();
   }

   public void onBackPressed() {
      if(!this.eventDelegate.onBackPressed()) {
         super.onBackPressed();
      }

   }

   protected void onPause() {
      super.onPause();
      this.eventDelegate.onPause();
   }

   protected void onPostResume() {
      super.onPostResume();
      this.eventDelegate.onPostResume();
   }

   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      this.eventDelegate.onRequestPermissionResult(requestCode, permissions, grantResults);
   }

   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if(!this.eventDelegate.onActivityResult(requestCode, resultCode, data)) {
         super.onActivityResult(requestCode, resultCode, data);
      }

   }

   protected void onNewIntent(Intent intent) {
      this.eventDelegate.onNewIntent(intent);
   }

   public void onUserLeaveHint() {
      this.eventDelegate.onUserLeaveHint();
   }

   public void onTrimMemory(int level) {
      this.eventDelegate.onTrimMemory(level);
   }

   public void onLowMemory() {
      this.eventDelegate.onLowMemory();
   }

   public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      this.eventDelegate.onConfigurationChanged(newConfig);
   }
}
