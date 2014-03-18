package com.chande.righttoknow;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class EmployeeItemAdapter extends ArrayAdapter<Employee> {

	private int resource;
	private int numberOfImageDownloading = 0;
	public EmployeeItemAdapter(
							Context context, 
							int resource, 
							List<Employee> employeeList
							
						  ) 
	{
		super(context, resource, employeeList );
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View categoryItemView = convertView;
		EmployeeItemViewWrapper employeeItemViewWrapper = null;
		Employee employee = getItem(position);

		if ( categoryItemView  == null) {
			//productItemView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			
			categoryItemView = vi.inflate( resource, null );
			
			//initiate the viewWrapper for this view
			employeeItemViewWrapper = new EmployeeItemViewWrapper( categoryItemView );
			
			categoryItemView.setTag( employeeItemViewWrapper );
		} 
		else 
		{
			
			employeeItemViewWrapper = ( EmployeeItemViewWrapper )categoryItemView.getTag();
		}
		
		employeeItemViewWrapper.getEmployeeNameTextView().setText(employee.getName());
		employeeItemViewWrapper.getEmployeeTitleTextView().setText(employee.getTitle());
		employeeItemViewWrapper.getEmployeeSalaryTextView().setText(employee.getPayString());
		
		return categoryItemView;
	}	
}
