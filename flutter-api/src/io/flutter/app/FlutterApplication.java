package io.flutter.app;

import android.app.Application;
import io.flutter.view.FlutterMain;

public class FlutterApplication extends Application {
   public void onCreate() {
      super.onCreate();
      FlutterMain.startInitialization(this);
   }
}
