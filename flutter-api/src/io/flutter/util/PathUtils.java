package io.flutter.util;

import android.content.Context;

public final class PathUtils {
   public static String getDataDirectory(Context applicationContext) {
      return applicationContext.getDir("flutter", 0).getPath();
   }

   public static String getCacheDirectory(Context applicationContext) {
      return applicationContext.getCacheDir().getPath();
   }
}
