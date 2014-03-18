package com.chande.righttoknow;

public class Employee {

	String name = "";
	String payString = "";
	String salarySource = "";
	String title = "";
	int payInteger;
	Boolean salaried = false;

	public Employee(String name, String salarySource, String title, String payString, int payInteger, Boolean salaried) {
		this.name = name;
		this.salarySource = salarySource;
		this.title = title;
		this.payString = payString;
		this.payInteger = payInteger;
		this.salaried = salaried;
	}
	
	public int getPayInteger() {
		return payInteger;
	}

	public void setPayInteger(int payInteger) {
		this.payInteger = payInteger;
	}

	public Boolean getSalaried() {
		return salaried;
	}

	public void setSalaried(Boolean salaried) {
		this.salaried = salaried;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayString() {
		return payString;
	}

	public void setPayString(String payString) {
		this.payString = payString;
	}

	public String getSalarySource() {
		return salarySource;
	}

	public void setSalarySource(String salarySource) {
		this.salarySource = salarySource;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}