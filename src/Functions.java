/*
Functions - everything our virtual world is doing right now - is this a good design?
 */

final class Functions {
   public static int clamp(int value, int low, int high) {
      return Math.min(high, Math.max(value, low));
   }
}



