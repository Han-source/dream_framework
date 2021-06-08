package www.dream.com.framework.util;

public class StringUtil {

   public static int[] convertCommaSepString2IntArr(String ids) {
      String[] splited = ids.split(",");
      int[] ret = new int[splited.length];
      for (int i = 0; i < splited.length; i++) {
         try {
            ret[i] = Integer.parseInt(splited[i]);
         } catch (Exception e) {
         }
      }
      return ret;
   }
   
   public static boolean hasInfo(String str) {
		//trim : 문자열 공백제거
		return str != null && !str.trim().isEmpty();
		
	}
}