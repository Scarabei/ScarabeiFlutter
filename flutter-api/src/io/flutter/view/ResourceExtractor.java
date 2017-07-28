package io.flutter.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import io.flutter.util.PathUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

class ResourceExtractor {
   private static final String TAG = "ResourceExtractor";
   private static final String TIMESTAMP_PREFIX = "res_timestamp-";
   private final Context mContext;
   private final HashSet mResources;
   private ResourceExtractor.ExtractTask mExtractTask;

   ResourceExtractor(Context context) {
      this.mContext = context;
      this.mResources = new HashSet();
   }

   ResourceExtractor addResource(String resource) {
      this.mResources.add(resource);
      return this;
   }

   ResourceExtractor addResources(Collection resources) {
      this.mResources.addAll(resources);
      return this;
   }

   ResourceExtractor start() {
      assert this.mExtractTask == null;

      this.mExtractTask = new ResourceExtractor.ExtractTask();
      this.mExtractTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
      return this;
   }

   void waitForCompletion() {
      assert this.mExtractTask != null;

      try {
         this.mExtractTask.get();
      } catch (CancellationException var2) {
         this.deleteFiles();
      } catch (ExecutionException var3) {
         this.deleteFiles();
      } catch (InterruptedException var4) {
         this.deleteFiles();
      }

   }

   private String[] getExistingTimestamps(File dataDir) {
      return dataDir.list(new FilenameFilter() {
         public boolean accept(File dir, String name) {
            return name.startsWith("res_timestamp-");
         }
      });
   }

   private void deleteFiles() {
      File dataDir = new File(PathUtils.getDataDirectory(this.mContext));
      Iterator var2 = this.mResources.iterator();

      while(var2.hasNext()) {
         String resource = (String)var2.next();
         File file = new File(dataDir, resource);
         if(file.exists()) {
            file.delete();
         }
      }

      String[] var6 = this.getExistingTimestamps(dataDir);
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String timestamp = var6[var8];
         (new File(dataDir, timestamp)).delete();
      }

   }

   private class ExtractTask extends AsyncTask {
      private static final int BUFFER_SIZE = 16384;

      private void extractResources() {
         File dataDir = new File(PathUtils.getDataDirectory(ResourceExtractor.this.mContext));
         String timestamp = this.checkTimestamp(dataDir);
         if(timestamp != null) {
            ResourceExtractor.this.deleteFiles();
         }

         AssetManager manager = ResourceExtractor.this.mContext.getResources().getAssets();

         try {
            byte[] buffer = null;
            String[] assets = manager.list("");
            String[] var6 = assets;
            int var7 = assets.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String asset = var6[var8];
               if(ResourceExtractor.this.mResources.contains(asset)) {
                  File output = new File(dataDir, asset);
                  if(!output.exists()) {
                     InputStream is = null;
                     FileOutputStream os = null;

                     try {
                        is = manager.open(asset);
                        os = new FileOutputStream(output);
                        if(buffer == null) {
                           buffer = new byte[16384];
                        }

                        boolean var13 = false;

                        int count;
                        while((count = is.read(buffer, 0, 16384)) != -1) {
                           os.write(buffer, 0, count);
                        }

                        os.flush();
                     } finally {
                        try {
                           if(is != null) {
                              is.close();
                           }
                        } finally {
                           if(os != null) {
                              os.close();
                           }

                        }

                     }
                  }
               }
            }
         } catch (IOException var35) {
            Log.w("ResourceExtractor", "Exception unpacking resources: " + var35.getMessage());
            ResourceExtractor.this.deleteFiles();
            return;
         }

         if(timestamp != null) {
            try {
               (new File(dataDir, timestamp)).createNewFile();
            } catch (IOException var32) {
               Log.w("ResourceExtractor", "Failed to write resource timestamp");
            }
         }

      }

      private String checkTimestamp(File dataDir) {
         PackageManager packageManager = ResourceExtractor.this.mContext.getPackageManager();
         PackageInfo packageInfo = null;

         try {
            packageInfo = packageManager.getPackageInfo(ResourceExtractor.this.mContext.getPackageName(), 0);
         } catch (NameNotFoundException var6) {
            return "res_timestamp-";
         }

         if(packageInfo == null) {
            return "res_timestamp-";
         } else {
            String expectedTimestamp = "res_timestamp-" + packageInfo.versionCode + "-" + packageInfo.lastUpdateTime;
            String[] existingTimestamps = ResourceExtractor.this.getExistingTimestamps(dataDir);
            return existingTimestamps.length == 1 && expectedTimestamp.equals(existingTimestamps[0])?null:expectedTimestamp;
         }
      }

      protected Void doInBackground(Void... unused) {
         this.extractResources();
         return null;
      }
   }
}
