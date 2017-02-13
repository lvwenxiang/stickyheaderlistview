package com.example.bean;

public class DateState {
	private String title;
	private String content;
	private String state;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public DateState(String content, String state) {
		super();
		this.content = content;
		this.state = state;
	}

	@Override
	public String toString() {
		return "DateState [title=" + title + ", content=" + content
				+ ", state=" + state + "]";
	}

}
