package com.ep.bean;

import com.opencsv.bean.CsvBindByName;

public class EventBean {
	
	@CsvBindByName(column = "Name")
	private String name;
	
	@CsvBindByName(column = "Age")
	private String age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "EventBean [name=" + name + ", age=" + age + "]";
	}

}
