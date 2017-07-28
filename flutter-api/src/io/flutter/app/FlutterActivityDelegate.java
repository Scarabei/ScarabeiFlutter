package io.flutter.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.util.Preconditions;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class FlutterActivityDelegate implements FlutterActivityEvents, FlutterView.Provider, PluginRegistry {
   private final Activity activity;
   private final FlutterActivityDelegate.ViewFactory viewFactory;
   private final Map pluginMap = new LinkedHashMap(0);
   private final List requestPermissionResultListeners = new ArrayList(0);
   private final List activityResultListeners = new ArrayList(0);
   private final List newIntentListeners = new ArrayList(0);
   private final List userLeaveHintListeners = new ArrayList(0);
   private FlutterView flutterView;

   public FlutterActivityDelegate(Activity activity, FlutterActivityDelegate.ViewFactory viewFactory) {
      this.activity = (Activity)Preconditions.checkNotNull(activity);
      this.viewFactory = (FlutterActivityDelegate.ViewFactory)Preconditions.checkNotNull(viewFactory);
   }

   public FlutterView getFlutterView() {
      return this.flutterView;
   }

   public boolean hasPlugin(String key) {
      return this.pluginMap.containsKey(key);
   }

   public Object valuePublishedByPlugin(String pluginKey) {
      return this.pluginMap.get(pluginKey);
   }

   public PluginRegistry.Registrar registrarFor(String pluginKey) {
      if(this.pluginMap.containsKey(pluginKey)) {
         throw new IllegalStateException("Plugin key " + pluginKey + " is already in use");
      } else {
         this.pluginMap.put(pluginKey, (Object)null);
         return new FlutterActivityDelegate.FlutterRegistrar(pluginKey);
      }
   }

   public boolean onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
      Iterator var4 = this.requestPermissionResultListeners.iterator();

      PluginRegistry.RequestPermissionResultListener listener;
      do {
         if(!var4.hasNext()) {
            return false;
         }

         listener = (PluginRegistry.RequestPermissionResultListener)var4.next();
      } while(!listener.onRequestPermissionResult(requestCode, permissions, grantResults));

      return true;
   }

   public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
      Iterator var4 = this.activityResultListeners.iterator();

      PluginRegistry.ActivityResultListener listener;
      do {
         if(!var4.hasNext()) {
            return false;
         }

         listener = (PluginRegistry.ActivityResultListener)var4.next();
      } while(!listener.onActivityResult(requestCode, resultCode, data));

      return true;
   }

   public void onCreate(Bundle savedInstanceState) {
      if(VERSION.SDK_INT >= 21) {
         Window window = this.activity.getWindow();
         window.addFlags(Integer.MIN_VALUE);
         window.setStatusBarColor(1073741824);
         window.getDecorView().setSystemUiVisibility(1280);
      }

      String[] args = getArgsFromIntent(this.activity.getIntent());
      FlutterMain.ensureInitializationComplete(this.activity.getApplicationContext(), args);
      this.flutterView = this.viewFactory.createFlutterView(this.activity);
      if(this.flutterView == null) {
         this.flutterView = new FlutterView(this.activity);
         this.flutterView.setLayoutParams(new LayoutParams(-1, -1));
         this.activity.setContentView(this.flutterView);
      }

      if(!this.loadIntent(this.activity.getIntent())) {
         String appBundlePath = FlutterMain.findAppBundlePath(this.activity.getApplicationContext());
         if(appBundlePath != null) {
            this.flutterView.runFromBundle(appBundlePath, (String)null);
         }

      }
   }

   public void onNewIntent(Intent intent) {
      if(!this.isDebuggable() || !this.loadIntent(intent)) {
         Iterator var2 = this.newIntentListeners.iterator();

         while(var2.hasNext()) {
            PluginRegistry.NewIntentListener listener = (PluginRegistry.NewIntentListener)var2.next();
            if(listener.onNewIntent(intent)) {
               return;
            }
         }
      }

   }

   private boolean isDebuggable() {
      return (this.activity.getApplicationInfo().flags & 2) != 0;
   }

   public void onPause() {
      if(this.flutterView != null) {
         this.flutterView.onPause();
      }

   }

   public void onResume() {
   }

   public void onPostResume() {
      if(this.flutterView != null) {
         this.flutterView.onPostResume();
      }

   }

   public void onDestroy() {
      if(this.flutterView != null) {
         this.flutterView.destroy();
      }

   }

   public boolean onBackPressed() {
      if(this.flutterView != null) {
         this.flutterView.popRoute();
         return true;
      } else {
         return false;
      }
   }

   public void onUserLeaveHint() {
   }

   public void onTrimMemory(int level) {
      if(level == 10) {
         this.flutterView.onMemoryPressure();
      }

   }

   public void onLowMemory() {
      this.flutterView.onMemoryPressure();
   }

   public void onConfigurationChanged(Configuration newConfig) {
   }

   private static String[] getArgsFromIntent(Intent intent) {
      ArrayList args = new ArrayList();
      if(intent.getBooleanExtra("trace-startup", false)) {
         args.add("--trace-startup");
      }

      if(intent.getBooleanExtra("start-paused", false)) {
         args.add("--start-paused");
      }

      if(intent.getBooleanExtra("use-test-fonts", false)) {
         args.add("--use-test-fonts");
      }

      if(intent.getBooleanExtra("enable-dart-profiling", false)) {
         args.add("--enable-dart-profiling");
      }

      if(intent.getBooleanExtra("enable-software-rendering", false)) {
         args.add("--enable-software-rendering");
      }

      if(!args.isEmpty()) {
         String[] argsArray = new String[args.size()];
         return (String[])args.toArray(argsArray);
      } else {
         return null;
      }
   }

   private boolean loadIntent(Intent intent) {
      String action = intent.getAction();
      if("android.intent.action.RUN".equals(action)) {
         String route = intent.getStringExtra("route");
         String appBundlePath = intent.getDataString();
         if(appBundlePath == null) {
            appBundlePath = FlutterMain.findAppBundlePath(this.activity.getApplicationContext());
         }

         if(route != null) {
            this.flutterView.setInitialRoute(route);
         }

         this.flutterView.runFromBundle(appBundlePath, intent.getStringExtra("snapshot"));
         return true;
      } else {
         return false;
      }
   }

   private class FlutterRegistrar implements PluginRegistry.Registrar {
      private final String pluginKey;

      FlutterRegistrar(String pluginKey) {
         this.pluginKey = pluginKey;
      }

      public Activity activity() {
         return FlutterActivityDelegate.this.activity;
      }

      public BinaryMessenger messenger() {
         return FlutterActivityDelegate.this.flutterView;
      }

      public FlutterView view() {
         return FlutterActivityDelegate.this.flutterView;
      }

      public PluginRegistry.Registrar publish(Object value) {
         FlutterActivityDelegate.this.pluginMap.put(this.pluginKey, value);
         return this;
      }

      public PluginRegistry.Registrar addRequestPermissionResultListener(PluginRegistry.RequestPermissionResultListener listener) {
         FlutterActivityDelegate.this.requestPermissionResultListeners.add(listener);
         return this;
      }

      public PluginRegistry.Registrar addActivityResultListener(PluginRegistry.ActivityResultListener listener) {
         FlutterActivityDelegate.this.activityResultListeners.add(listener);
         return this;
      }

      public PluginRegistry.Registrar addNewIntentListener(PluginRegistry.NewIntentListener listener) {
         FlutterActivityDelegate.this.newIntentListeners.add(listener);
         return this;
      }

      public PluginRegistry.Registrar addUserLeaveHintListener(PluginRegistry.UserLeaveHintListener listener) {
         FlutterActivityDelegate.this.userLeaveHintListeners.add(listener);
         return this;
      }
   }

   public interface ViewFactory {
      FlutterView createFlutterView(Context var1);
   }
}
