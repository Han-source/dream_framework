package www.dream.com.framework.printer;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import www.dream.com.framework.classAnalyzer.ClassAnalyzer;
import www.dream.com.framework.util.ComparablePair;
import www.dream.com.framework.util.Penta;
import www.dream.com.framework.util.StringUtil;

public class TablePrinter {
	/**
	 * 받은 객체의 속성 및 메소드에 추가된 @PrintTarget 애노태이션을 기반으로
	 * <table>
	 * <tr>
	 * 한줄을 만들어 준다.
	 * 
	 * @param obj
	 * @return
	 */
	
	//tr을 출력할 함수
	public static String printTableRow(Object obj, String tagClass) {
		// 받은 객체에서 @PrintTarget 이 달린 Feature의 값을 읽고 이를 순서에 따라 저장한다<순서, featureName, Type, ,값>
		TreeSet<Penta<Integer, String, Class, Object, Object>> ordered = new TreeSet<>();
		collectValues(obj, PrintTarget.class, ordered);

		StringBuilder sb = new StringBuilder();
		for (Penta<Integer, String, Class, Object,  Object> penta : ordered) {
			PrintTarget printTarget = (PrintTarget) penta.getD();
			if(printTarget.withAnchor()) {
				List<AccessibleObject> listAO = ClassAnalyzer.findFeatureByAnnotation(obj.getClass(), AnchorTarget.class);
				try {
					Field field = ((Field) listAO.get(0));
					field.setAccessible(true); // 값을꺼냄
					Object id = field.get(obj);
					AnchorTarget anchorAnno= field.getAnnotation(AnchorTarget.class);
					
					sb.append("<td><a class='" + tagClass +"' href=" + id + ">" + penta.getE() + "</a></td>");
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
				
			}else if(!StringUtil.hasInfo(printTarget.pattern())) {
				sb.append("<td>" + penta.getE() + "</td>");
				
			} else {
				SimpleDateFormat fmt = new SimpleDateFormat(printTarget.pattern());
				sb.append("<td>" + fmt.format((Date) penta.getE()) + "</td>");
				
				
			}	
		}
		return sb.toString();
	}

	private static void collectValues(Object obj, Class<PrintTarget> anno,
			TreeSet<Penta<Integer, String, Class, Object ,Object>> ordered) {
		// 속성과 필드의 모든것
		List<AccessibleObject> listAO = ClassAnalyzer.findAllFeature(obj.getClass());
		// annotation이 달려있는집합
		List<AccessibleObject> listAnnotatedAO = listAO.stream().filter(ao -> ao.getAnnotation(anno) != null)
				.collect(Collectors.toList());
		// 모든것 - annotation있는것 = annotation 없는 구문
		listAO.removeAll(listAnnotatedAO);
		// annoatation이 있는 애들 처리영역
		for (AccessibleObject ao : listAnnotatedAO) {
			PrintTarget printTarget = ao.getAnnotation(anno);
			// 순서꺼내기. PrintTarget Annotation이 추가되어 있는 속성 또는 메소드라면
			int order = printTarget.order();
			// featureName, Type꺼내기, Value꺼내기
			String featureName = null;
			Class type = null;
			Object value = null;
			if (ao instanceof Field) {
				Field field = (Field) (ao);
				// private int readCnt의 값 가져오기
				featureName = field.getName();
				type = field.getType();
				try {
					field.setAccessible(true); // 값을꺼냄
					value = field.get(obj);
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
			} else if (ao instanceof Method) {
				Method method = (Method) (ao);
				featureName = method.getName();
				type = method.getReturnType();
				try {
					value = method.invoke(obj, new Object[] {}); // 값을꺼냄
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			}
			ordered.add(new Penta<>(order, featureName, type, printTarget, value));

		}

		// annoatation이 없는 애들 처리영역
		for (AccessibleObject ao : listAO) {
			if (ao instanceof Field) {
				Field field = (Field) (ao);
				Class type = field.getType();
				ClassPrintTarget classPrintTarget = (ClassPrintTarget) type.getAnnotation(ClassPrintTarget.class);
				if (classPrintTarget != null) {
					try {
						field.setAccessible(true); // 값을꺼냄
						Object value = field.get(obj);
						collectValues(value, PrintTarget.class, ordered);
					} catch (IllegalArgumentException | IllegalAccessException e) {
					}
				}
			} else if (ao instanceof Method) {
				Method method = (Method) (ao);
				Class type = method.getReturnType();
				ClassPrintTarget classPrintTarget = (ClassPrintTarget) type.getAnnotation(ClassPrintTarget.class);
				if (classPrintTarget != null) {
					try {
						Object value = method.invoke(obj, new Object[] {}); // 값을꺼냄
						collectValues(value, PrintTarget.class, ordered);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					}
				}

			}

		}
	}

	// class를 넘겨받아 헤더에서만 쓰는anno 정의
	public static String printHeader(Class cls) {
		// TableHeader가 추가된 모든 AccessibleObject를 모음
		// Integer : 순서, String : 내용
		// TreeSet : 순서에 따라 정렬을 해야하므로 TreeSet 사용
		TreeSet<ComparablePair<Integer, String>> ordered = new TreeSet<>();

		// @HeaderTarget 을 모아주는 함수
		collectHeaders(cls, PrintTarget.class, ordered);

		// 문자열 출력
		// 탐색할 클래스, 찾을 annotation, 결과를 담을 list
		StringBuilder sb = new StringBuilder();
		for (ComparablePair<Integer, String> cp : ordered) {
			sb.append("<th>" + cp.getSecond() + "</th>");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param cls     : annotation을 찾을 클래스
	 * @param anno    : 찾을 annotation
	 * @param ordered : annotation을 담을 TreeSet
	 */
	private static void collectHeaders(Class cls, Class anno, TreeSet<ComparablePair<Integer, String>> ordered) {
		// 클래스 위에 @HeaderTarget이 없다면
		if (cls.getAnnotation(ClassPrintTarget.class) == null) {
			return;
		}
		// cls에 정의된것들을 listFeature에 담는다
		List<AccessibleObject> listFeature = ClassAnalyzer.findAllFeature(cls);
		for (AccessibleObject ao : listFeature) {
			PrintTarget ht = (PrintTarget) ao.getAnnotation(anno);
			// ht에 값이 존재 한다면
			if (ht != null) {
				// order TreeSert에 @HeaderTarget에 있는 순서와 내용을 담아준다
				ordered.add(new ComparablePair<>(ht.order(), ht.caption()));
			} else {
				// 값이 존재하지 않는다면 다시 함수를 호출시켜 함수를 끝냄
				if (ao instanceof Field) {
					collectHeaders(((Field) ao).getType(), anno, ordered);
				} else if (ao instanceof Method) {
					collectHeaders(((Method) ao).getReturnType(), anno, ordered);
				}
			}
		}
	}
}