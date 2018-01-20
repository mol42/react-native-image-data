
package com.mol42.imagedata;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNReactNativeImageDataModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNReactNativeImageDataModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNReactNativeImageData";
  }

  @ReactMethod
  public void getPixels(String filePath, final Promise promise) {
    try {
      WritableNativeMap result = new WritableNativeMap();
      WritableNativeArray pixels = new WritableNativeArray();

      Bitmap bitmap = BitmapFactory.decodeFile(filePath);
      if (bitmap == null) {
        promise.reject("Failed to decode. Path is incorrect or image is corrupted");
        return;
      }

      int width = bitmap.getWidth();
      int height = bitmap.getHeight();

      boolean hasAlpha = bitmap.hasAlpha();

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          int color = bitmap.getPixel(x, y);
          String hex = Integer.toHexString(color);
          pixels.pushString(hex);
        }
      }

      result.putInt("width", width);
      result.putInt("height", height);
      result.putBoolean("hasAlpha", hasAlpha);
      result.putArray("pixels", pixels);

      promise.resolve(result);

    } catch (Exception e) {
      promise.reject(e);
    }

  }  
}