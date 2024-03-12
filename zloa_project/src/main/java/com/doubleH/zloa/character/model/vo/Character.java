package com.doubleH.zloa.character.model.vo;

// VO, DTO
// lombok 적용시키자.
public class Character {
	
	private String data;
	private String data1;
	
	public Character(String data, String data1) {
		super();
		this.data = data;
		this.data1 = data1;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}
	
	

	
}
