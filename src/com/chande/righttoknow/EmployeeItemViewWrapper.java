package com.chande.righttoknow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EmployeeItemViewWrapper {

	View 		base;
	TextView 	employeeNameTextView = null;
	TextView 	employeeTitleTextView = null;
	TextView 	employeeSalaryTextView = null;
	
	public EmployeeItemViewWrapper( View base )
	{
		this.base = base;
	}
	
	public TextView getEmployeeNameTextView() {
		if(employeeNameTextView == null)
			employeeNameTextView = (TextView) base.findViewById(R.id.employeeNameTextView);
		return employeeNameTextView;
	}
	
	public TextView getEmployeeTitleTextView() {
		if(employeeTitleTextView == null)
			employeeTitleTextView = (TextView) base.findViewById(R.id.employeeTitleTextView);
		return employeeTitleTextView;
	}
	
	public TextView getEmployeeSalaryTextView() {
		if(employeeSalaryTextView == null)
			employeeSalaryTextView = (TextView) base.findViewById(R.id.employeeSalaryTextView);
		return employeeSalaryTextView;
	}

}
