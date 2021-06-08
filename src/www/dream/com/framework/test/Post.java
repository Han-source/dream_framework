package www.dream.com.framework.test;

import www.dream.com.framework.langPosAnalyzer.HashTarget;

public class Post extends Reply{
	private String title;

	@HashTarget
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	} 
}
