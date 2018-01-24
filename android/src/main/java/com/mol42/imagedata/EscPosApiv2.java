package com.mol42.imagedata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.BitSet;

public class EscPosApiv2 {

    public EscPosApiv2() {
    }

  /**
   * Send image to the printer to be printed.
   * 
   * @param pixels 2D Array of RGB colors (Row major order)
   */
  public String printImage(int[][] pixels) {
    StringBuffer buf = new StringBuffer();
    // printPort.writeBytes(SET_LINE_SPACE_24);
    buf.append("SET_LINE_SPACE_24");
    System.out.println("pixels.length : " + pixels.length);
    for (int y = 0; y < pixels.length; y += 24) {
      //printPort.writeBytes(SELECT_BIT_IMAGE_MODE);// bit mode
      buf.append("SELECT_BIT_IMAGE_MODE");
      
      byte[] bytes = new byte[]{(byte) (0x00ff & pixels[y].length), (byte) ((0xff00 & pixels[y].length) >> 8)};

      for (int i=0; i < bytes.length; i++) {
          buf.append(Integer.toHexString(bytes[i]));
      }
      // printPort.writeBytes(new byte[]{(byte) (0x00ff & pixels[y].length), (byte) ((0xff00 & pixels[y].length) >> 8)});// width, low & high
      // buf.append(Integer.toHe)
      for (int x = 0; x < pixels[y].length; x++) {
        // For each vertical line/slice must collect 3 bytes (24 bytes)
        //printPort.writeBytes(collectSlice(y, x, pixels));
        byte[] collectedBytes = collectSlice(y, x, pixels);
        for (int j = 0; j < collectedBytes.length; j++) {
            buf.append(Integer.toHexString(collectedBytes[j]));
        }
      }
      buf.append("PrinterCommands.FEED_LINE");
      System.out.println("PrinterCommands.FEED_LINE");
      //printPort.writeBytes(PrinterCommands.FEED_LINE);
    }
    // printPort.writeBytes(SET_LINE_SPACE_30);
    buf.append("SET_LINE_SPACE_30");
    return buf.toString();
  }

  /**
   * Gets the pixels stored in an image. 
   * TODO very slow, could be improved (use different class, cache result, etc.)
   * @param image image to get pixels from.
   * @return 2D array of pixels of the image (RGB, row major order)
   */
  public int[][] getPixelsSlow(Bitmap image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[][] result = new int[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        // result[row][col] = image.getRGB(col, row);
        result[row][col] = image.getPixel(col, row);
        if (row % 10 == 0) {
            System.out.println("result[row][col] : " + result[row][col]);
        }
      }
    }

    return result;
  }

  /**
   * Defines if a color should be printed (burned).
   * @param color RGB color.
   * @return true if should be printed/burned (black), false otherwise (white).
   */
  public boolean shouldPrintColor(int color) {
    final int threshold = 127;
    int a, r, g, b, luminance;
    a = (color >> 24) & 0xff;
    if (a != 0xff) { // ignore pixels with alpha channel
      return false;
    }
    r = (color >> 16) & 0xff;
    g = (color >> 8) & 0xff;
    b = color & 0xff;

    luminance = (int) (0.299 * r + 0.587 * g + 0.114 * b);

    return luminance < threshold;
  }

  /**
   * Collect a slice of 3 bytes with 24 dots for image printing.
   * @param y row position of the pixel.
   * @param x column position of the pixel.
   * @param img 2D array of pixels of the image (RGB, row major order).
   * @return 3 byte array with 24 dots (field set).
   */
  public byte[] collectSlice(int y, int x, int[][] img) {
    byte[] slices = new byte[]{0, 0, 0};
    for (int yy = y, i = 0; yy < y + 24 && i < 3; yy += 8, i++) {// va a hacer 3 ciclos
      byte slice = 0;
      for (int b = 0; b < 8; b++) {
        int yyy = yy + b;
        if (yyy >= img.length) {
          continue;
        }
        int col = img[yyy][x];
        boolean v = shouldPrintColor(col);
        slice |= (byte) ((v ? 1 : 0) << (7 - b));
      }
      slices[i] = slice;
    }

    return slices;
  }
}