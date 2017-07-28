package io.flutter.plugin.common;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {
   public static Object wrap(Object o) {
      if(o == null) {
         return JSONObject.NULL;
      } else if(!(o instanceof JSONArray) && !(o instanceof JSONObject)) {
         if(o.equals(JSONObject.NULL)) {
            return o;
         } else {
            try {
               Iterator var2;
               JSONArray result;
               if(o instanceof Collection) {
                  result = new JSONArray();
                  var2 = ((Collection)o).iterator();

                  while(var2.hasNext()) {
                     Object e = var2.next();
                     result.put(wrap(e));
                  }

                  return result;
               }

               if(o.getClass().isArray()) {
                  result = new JSONArray();
                  int length = Array.getLength(o);

                  for(int i = 0; i < length; ++i) {
                     result.put(wrap(Array.get(o, i)));
                  }

                  return result;
               }

               if(o instanceof Map) {
                  JSONObject result = new JSONObject();
                  var2 = ((Map)o).entrySet().iterator();

                  while(var2.hasNext()) {
                     Entry entry = (Entry)var2.next();
                     result.put((String)entry.getKey(), wrap(entry.getValue()));
                  }

                  return result;
               }

               if(o instanceof Boolean || o instanceof Byte || o instanceof Character || o instanceof Double || o instanceof Float || o instanceof Integer || o instanceof Long || o instanceof Short || o instanceof String) {
                  return o;
               }

               if(o.getClass().getPackage().getName().startsWith("java.")) {
                  return o.toString();
               }
            } catch (Exception var4) {
               ;
            }

            return null;
         }
      } else {
         return o;
      }
   }
}
