package com.chande.righttoknow;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	
	//menu stuff
    private String[] mUniversities;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Boolean introScreen = true;
    
    //search stuff
	private EditText mSearchEditText;
	private View mActivityRootView;
	private Button mClearButton;
	private ProgressDialog mProgressDialog;
	private ArrayList<Employee> mEmployeeList = new ArrayList<Employee>();
	private ArrayList<Employee> mEmployeeListOneTerm = new ArrayList<Employee>();
	private ArrayList<Employee> mEmployeeListMultipleTerms = new ArrayList<Employee>();
	private EmployeeItemAdapter	mEmployeeItemAdapter;
	private ListView mEmployeeListView;
	private Context mContext;
	private HashMap<String, Employee> mHashMap = new HashMap<String, Employee>();
	
	private int mFAMUData = R.raw.famudata;
	private int mFAUData = R.raw.faudata;
	private int mFGCUData = R.raw.fgcudata;
	private int mFIUData = R.raw.fiudata;
	private int mFSUData = R.raw.fsudata;
	private int mNCFData = R.raw.ncfdata;
	private int mUCFData = R.raw.ucfdata;
	private int mUFData = R.raw.ufdata;
	private int mUNFData = R.raw.unfdata;
	private int mUSFData = R.raw.usfdata;
	private int mUWFData = R.raw.uwfdata;
	private int mCurrentData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        setupMenu();
    }
    
    public void setupMenu() {
        mUniversities = getResources().getStringArray(R.array.university_names);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mUniversities));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	if (introScreen){
          setContentView(R.layout.activity_main);
          setupMenu();
          mSearchEditText = (EditText) findViewById(R.id.searchEditText);
      	  mSearchEditText.setText("");
          mActivityRootView = findViewById(R.id.imageView2);
          mClearButton = (Button) findViewById(R.id.clearButton);
          
          mSearchEditText.setSelected(false);

          mActivityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
              Rect r = new Rect();
              //r will be populated with the coordinates of your view that area still visible.
              mActivityRootView.getWindowVisibleDisplayFrame(r);
              int heightDiff = mActivityRootView.getRootView().getHeight() - (r.bottom - r.top);
              if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
              	mSearchEditText.setBackgroundResource(R.drawable.search_focused);
              }
              else{
              	mSearchEditText.setBackgroundResource(R.drawable.search_not_focused);
              }
           }
          }); 
          
  		mSearchEditText.addTextChangedListener(new TextWatcher() {
  			
  			@Override
  			public void onTextChanged(CharSequence s, int start, int before, int count) {
  				// TODO Auto-generated method stub			
  			}
  			
  			@Override
  			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  				// TODO Auto-generated method stub
  				
  			}

  			@Override
  			public void afterTextChanged(Editable s) {
  				String searchText = mSearchEditText.getText().toString().trim();
  				Log.e("SIZE", Integer.toString(mEmployeeList.size()));
  				String[] searchitems = searchText.split(" ");
  				if (searchitems.length > 1) {
  					mEmployeeListMultipleTerms.addAll(mEmployeeList);
  					mEmployeeList.clear();
  					for(int i = 0; i < mEmployeeListMultipleTerms.size(); i++) {
  						Employee employee = mEmployeeListMultipleTerms.get(i);
  						String fullName = employee.getName();
  						String title = employee.getTitle();
  							if(fullName.toLowerCase().contains(searchitems[searchitems.length - 1].toLowerCase()) ||
  									title.toLowerCase().contains(searchitems[searchitems.length - 1].toLowerCase())) {
  										mEmployeeList.add(employee);
  							}
  						mEmployeeItemAdapter.notifyDataSetChanged();
  					}
  					mEmployeeListMultipleTerms.clear();
  				}
  				else if(searchText.length() == 0) {
  					mEmployeeList.clear();
  					mEmployeeList.addAll(mEmployeeListOneTerm);
  				} else {
  					mEmployeeList.clear();
  					for(int i = 0; i < mEmployeeListOneTerm.size(); i++) {
  						Employee employee = mEmployeeListOneTerm.get(i);
  						String fullName = employee.getName();
  						String title = employee.getTitle();
  							if(fullName.toLowerCase().contains(searchText.toLowerCase()) ||
  									title.toLowerCase().contains(searchText.toLowerCase())) {
  										mEmployeeList.add(employee);
  							}
  						mEmployeeItemAdapter.notifyDataSetChanged();
  					}
  				}
  				mEmployeeItemAdapter.notifyDataSetChanged();
  			}
  		});
          introScreen = false;
    	}
    	
    	if (!mEmployeeList.isEmpty()) {
        	mEmployeeList.clear();
    	}
    	if (!mEmployeeListOneTerm.isEmpty()) {
        	mEmployeeListOneTerm.clear();
    	}
    	if (!mEmployeeListMultipleTerms.isEmpty()) {
        	mEmployeeListMultipleTerms.clear();
    	}
    	mHashMap = new HashMap<String, Employee>();
    	
    	if (mUniversities[position].equals("FSU")) {
    		mCurrentData = mFSUData;
    	}
    	else if (mUniversities[position].equals("UF")) {
    		mCurrentData = mUFData;
    	}
    	else if (mUniversities[position].equals("UCF")) {
    		mCurrentData = mUCFData;
    	}
    	else if (mUniversities[position].equals("FAMU")) {
    		mCurrentData = mFAMUData;
    	}
    	else if (mUniversities[position].equals("FAU")) {
    		mCurrentData = mFAUData;
    	}
    	else if (mUniversities[position].equals("FGCU")) {
    		mCurrentData = mFGCUData;
    	}
    	else if (mUniversities[position].equals("FIU")) {
    		mCurrentData = mFIUData;
    	}
    	else if (mUniversities[position].equals("NCF")) {
    		mCurrentData = mNCFData;
    	}
    	else if (mUniversities[position].equals("UNF")) {
    		mCurrentData = mUNFData;
    	}
    	else if (mUniversities[position].equals("USF")) {
    		mCurrentData = mUSFData;
    	}
    	else if (mUniversities[position].equals("UWF")) {
    		mCurrentData = mUWFData;
    	}

        mContext = this;

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.rel);
        rl.setBackgroundColor(Color.BLACK);

		mClearButton.setOnClickListener(clearButton);
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		
		new getEmployeesList().execute();

    	Log.e("NAME", mUniversities[position]);

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mUniversities[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    private class getEmployeesList extends
	AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {		
				InputStream fstream = getResources().openRawResource(mCurrentData);
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                //Read File Line By Line
                while ((strLine = br.readLine()) != null){
                    if (strLine.length() > 0){
                        String[] line = strLine.split(",");
                        String lastName = line[0].toLowerCase();
                        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
                        String firstName = line[1].toLowerCase();
                        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
                        String middleInitial = line[2];
                        String fullName = "";
                        if (!middleInitial.equals(".")) {
                        	fullName = firstName + " " + middleInitial + ". " + lastName;
                        }
                        else {
                        	fullName = firstName + " " + lastName;
                        }
                        Boolean salaried = false;
                        if (line[3].equals("Salaried")) {
                        	salaried = true;
                        }
                        //Log.e("NAME", fullName);
                        String salarySource = line[4];
                        String title = formatTitle(line[5]);
                        String payString = line[6];
                        int payInteger = Integer.parseInt(payString);
                        
                        //hashmap is for counting salary of employees who have multiple sources of income
                        if (!mHashMap.containsKey(fullName)){
                        	mHashMap.put(fullName, new Employee(fullName, salarySource, title, payString, payInteger, salaried));
                        }
                        else {
                        	Employee currentEmp = mHashMap.get(fullName);
                        	int updatedPay = payInteger + currentEmp.getPayInteger();
                        	mHashMap.remove(fullName);
                        	mHashMap.put(fullName, new Employee(fullName, salarySource, title, payString, updatedPay, salaried));
                        }

                    }
                }
                
                //populate lists from hashmap
                for (String name : mHashMap.keySet()){
                	Employee theEmployee = mHashMap.get(name);
                	theEmployee.setPayString(formatSalaried(String.valueOf(theEmployee.getPayInteger()), theEmployee.getSalaried()));
                    mEmployeeList.add(theEmployee);
                    mEmployeeListOneTerm.add(theEmployee);	
                }
                Collections.sort(mEmployeeList, new EmployeeComparator());
                Collections.sort(mEmployeeListOneTerm, new EmployeeComparator());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
    	
		@Override
		protected void onPreExecute() {
			mProgressDialog.setTitle("Right to Know");
			mProgressDialog
					.setMessage("Loading Employee Information");
			mProgressDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				mProgressDialog.dismiss();
		        mEmployeeListView = (ListView) findViewById(R.id.employeeListView);
		        
		        mEmployeeItemAdapter = new EmployeeItemAdapter(mContext,
						R.layout.employee_list_item,
						mEmployeeList);
				mEmployeeListView.setAdapter(mEmployeeItemAdapter);
				mSearchEditText.setText("");
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    public String formatSalary(String payAmount){
    	String formattedAmount = "";
    	
    	if (payAmount.length() == 3){
    		formattedAmount = payAmount;
    	}
    	else if (payAmount.length() == 4){
    		formattedAmount = payAmount.charAt(0) + "," + payAmount.substring(1, 4);
    	}
    	else if (payAmount.length() == 5){
    		formattedAmount = payAmount.substring(0, 2) + "," + payAmount.substring(2, 5);
    	}
    	else if (payAmount.length() == 6){
    		formattedAmount = payAmount.substring(0, 3) + "," + payAmount.substring(3, 6);
    	}
    	
    	return formattedAmount;
    }
    
    public String formatTitle(String title) {
    	String formattedTitle = "";
    	StringBuilder builder = new StringBuilder();
   	
    	String[] titleArray = title.split(" ");
    	
    	for (String word : titleArray) {
    		if (!word.equals("IT") && !word.equals("II") && !word.equals("III") && !word.equals("IV")){
    			word = word.toLowerCase();
    			builder.append(word.substring(0, 1).toUpperCase() + word.substring(1) + " ");
    		}
    		else {
    			builder.append(word + " ");
    		}
    	}
    	formattedTitle = builder.toString();
    	
    	return formattedTitle;
    }
    
    public void printEmployeeList(ArrayList<Employee> employeeList) {
		for (Employee e : employeeList) {
			Log.e("NAME", e.getName());
			Log.e("TITLE", e.getTitle());
			Log.e("SALARYSOURCE", e.getSalarySource());
			Log.e("SALARY", e.getPayString());
		}
    }
    
    public String formatSalaried(String payType, Boolean salaried){
    	String termOrYear = "";
    	
        if (salaried){
        	termOrYear = "$" + formatSalary(payType) + "/year";
        }
        else {
        	termOrYear = "$" + formatSalary(payType) + "/term";
        }
    	
    	return termOrYear;
    }

    View.OnClickListener clearButton = new View.OnClickListener() {
        public void onClick(View v) {
          mSearchEditText.setText("");
        }
      };
      
      public ArrayList<Employee> employeeSearch(ArrayList<Employee> empList, String[] searchText){
    	ArrayList<Employee> outerIterationList = new ArrayList<Employee>();
    	outerIterationList.addAll(mEmployeeListOneTerm);
    	ArrayList<Employee> innerIterationList = new ArrayList<Employee>();
    	
    	for (String s :searchText) {  
    		innerIterationList.clear();
			for(int i = 0; i < outerIterationList.size(); i++) {
				Employee employee = outerIterationList.get(i);
				Log.e("NAME", employee.getName());
				String fullName = employee.getName();
				String title = employee.getTitle();
				if(fullName.toLowerCase().contains(s.toLowerCase()) ||
						title.toLowerCase().contains(s.toLowerCase())) {
							innerIterationList.add(employee);
							for (Employee e : innerIterationList){
								Log.e("NAME", e.getName());
							}
				}
    		outerIterationList = new ArrayList<Employee>();
    		outerIterationList.addAll(innerIterationList);
			}	
        }
		return innerIterationList;
      }
}
