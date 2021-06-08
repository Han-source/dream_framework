package www.dream.com.framework.test;

import java.util.ArrayList;
import java.util.List;

import www.dream.com.framework.langPosAnalyzer.HashTarget;


public class Party {
	private String name;
	//이런것까지 정보 찾아서 프레임워크 만들기는 각자의노력 generic
	@HashTarget
	private List<ContactPoint> listContactPoint = new ArrayList<>();
	
	@HashTarget
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addCP(ContactPoint cp) {
		listContactPoint.add(cp);
	}
}
