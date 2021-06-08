package www.dream.com.framework.langPosAnalyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import www.dream.com.framework.classAnalyzer.ClassAnalyzer;

/**
 * 품사 분석기가 정의한 Annotation을 달아 놓은 객체의 속성에 들어있는 정보를 Komoran을 활용하여 품사분석하고 해쉬태그로 활용할
 * 만한 단어들이 몇번 사용되었는지 까지를 ComparablePair의 List로 반환할 것임. 품사 : 2
 */
public class PosAnalyzer {
	private static Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

	public static Map<String, Integer> getHashTags(Object obj) {
		// 출력에 나올 Map을 담는 배열
		Map<String, Integer> ret = new HashMap<>();
		// annotation이 달려있는 것들을 검사해서 분석기 의 역할
		// obj = 넘겨준 post , 결과를 담을 ret
		getHashTags(obj, ret);
		return ret;
	}

	// 넘겨받은 obj = 넘겨준 post , 결과를 담을 ret을 가지고 있음
	private static void getHashTags(Object obj, Map<String, Integer> map) {
		if (obj == null) {
			return;
		} else if (obj instanceof String) {
			analyzeHashTag((String) obj, map);// 값을확보했을때
		} else if (obj instanceof Iterable) {
			((Iterable) obj).forEach(ele -> getHashTags(ele, map));
		} else if (obj instanceof Map) {
			((Map) obj).entrySet().forEach(ele -> getHashTags(ele, map));
		} else {
			// 내가 찾고 싶은 @HashTag 달려있는것들을 모두 찾은 집합 //넘겨받은 post의 클래스, HashTarget
			List<AccessibleObject> listFeature = ClassAnalyzer.findFeatureByAnnotation(obj.getClass(),
					HashTarget.class);

			// @HashTag 이 달려 있는 field와 method의 집합인 listFeature반복문을 돈다
			for (AccessibleObject ao : listFeature) {
				// 내부의 원소가 Field형이라면
				if (ao instanceof Field) {
					Field field = (Field) (ao);
					try {
						// setAccessible()은 필드나 메서드의 접근제어 지시자(private)에 의한 제어를 변경한다.
						field.setAccessible(true); // 값을꺼냄
						// 필드에 있는 값이 ??
						Object fieldValue = field.get(obj);
						getHashTags(fieldValue, map);
					} catch (IllegalArgumentException | IllegalAccessException e) {
					}

				} else if (ao instanceof Method) {
					Method method = (Method) (ao);
					try {
						Object returnValue = method.invoke(obj, null); // 값을꺼냄
						getHashTags(returnValue, map);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 코모란을 실행을 시키는 함수. 받은 문자열에 대해서 Token으로 나누고 나눈 Token들에 대해서 TargetPos 에 존재하는 속성인
	 * NNG, NNP, SL, SH인지 확인한다 확인을 하고 쌍방으로 존재하는 토큰에 대해서 ret에 담는데 containsKey를 이용해서
	 * ret에 담을때 이미 존재하는 key에 대해서는 value의 값을 1 증가시킨다.
	 * 
	 * @param analysisTargetString
	 * @param ret
	 */
	private static void analyzeHashTag(String analysisTargetString, Map<String, Integer> ret) {
		if (analysisTargetString == null) {
			return;
		}
		KomoranResult analyzeResultList = komoran.analyze(analysisTargetString);
		List<Token> tokenList = analyzeResultList.getTokenList();
		for (Token token : tokenList) {
			TargetPos pos = null; // 품사
			try {
				pos = TargetPos.valueOf(token.getPos()); //valueOf string 타입으로 바꿔주는 역할.
			} catch (Exception e) {
			}
			if (pos != null) {
				String hashTag = token.getMorph();   //getMorph 통해 단어를 자른다.
				// 맵에서 인자로 보낸 키가 있으면 true 없으면 false를 반환한다.
				if (ret.containsKey(hashTag)) {
					// ret에 만약에 기존 품사가 있으면 +1 시키기
					ret.put(hashTag, (ret.get(hashTag) + 1));
				} else {
					// 그렇지않고 처음 들어가는 품사이면 value에 1넣기.
					ret.put(hashTag, 1);
				}
			}
		}
	}

}