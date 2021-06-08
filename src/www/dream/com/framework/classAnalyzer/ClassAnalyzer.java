package www.dream.com.framework.classAnalyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 어떤 클래스가 주어지면 한계통으로 쭉 올라가서 계속 검사하는것
 * 
 * @author kosmo_10
 *
 */
public class ClassAnalyzer {
   /**
    * 주어진 클래스에서 정의되어 있는 모든 속성과 함수를 상속 구조까지 분석하여 모아서 반환한다.
    * 
    * @param targetClass
    * @return
    */
   public static List<AccessibleObject> findAllFeature(Class targetClass) {
      // Annotation이 달려있는 변수나 메소드를 찾아서 합친것을 반환해줄 리스트
      List<AccessibleObject> ret = new ArrayList<>();
      // 탐색할 클래스, 찾을 annotation, 결과를 담을 list
      findAllFeatureByRecursion(targetClass, ret);
      return ret;
   }

   /**
    * 주어진 클래스에서 정의되어 있는 모든 속성과 함수에 지정된 annotation을 붙혀놓은 것들을 상속 구조까지 분석하여 모아서 반환한다
    * 
    * @param targetClass
    * @param targetAnno
    * @return
    */
   public static List<AccessibleObject> findFeatureByAnnotation(Class targetClass, Class targetAnno) {
      return findAllFeature(targetClass).stream().filter(ao -> ao.getAnnotation(targetAnno) != null)
            .collect(Collectors.toList());
   }

   /**
    * 주어진 클래스에서 정의되어 있는 모든 속성과 함수를 상속 구조까지 분석하여 모아서 반환한다.
    * 
    * @param targetClass
    * @param ret
    */
   private static void findAllFeatureByRecursion(Class targetClass, List<AccessibleObject> ret) {
      if (targetClass == null) {
         return;
      }
      try {
         // 내가 찾고싶은 클래스의 필드들을 가져옴
         Field[] fields = targetClass.getDeclaredFields();
         // 찾은 field에 대하여 반복문을 돈다
         for (Field field : fields) {
            ret.add(field);
         }

         Method[] methods = targetClass.getDeclaredMethods();
         for (Method method : methods) {
            ret.add(method);
         }
      } catch (Exception e) {
      }

      /** 만약 내 위에 상위클래스가 정의되어 있다면 */
      findAllFeatureByRecursion(targetClass.getSuperclass(), ret);
   }
}