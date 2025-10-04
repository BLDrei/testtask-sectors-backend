package com.bldrei.sectors;

public class StringHelper {
  private StringHelper() {}

  public static String repeat(String string, int times) {
    if (times <= 0) {
      return "";
    }
    return string + repeat(string, times - 1);
  }
}
