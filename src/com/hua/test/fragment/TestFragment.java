package com.hua.test.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hua.test.activity.R;
import com.hua.test.utils.LogUtils2;

public class TestFragment extends BaseFragment{

	private ProgressBar progressBar;
	private Button testButton;
	private TextView testTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils2.i("***onCreate***");
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils2.i("***onCreateView***");
		final View view = inflater.inflate(R.layout.test_fragment, null);
		String con = "Testing...";
		if(savedInstanceState != null){
			Bundle mBundle = savedInstanceState;
			 con = mBundle.getString("textContent");
		}

		
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		testButton = (Button) view.findViewById(R.id.test_button);
		testButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "testButton", 300).show();
			}
		});
		testTextView = (TextView) view.findViewById(R.id.testTextView);
		testTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "textView", 300).show();
			}
		});
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				progressBar.setVisibility(View.GONE);
//				Toast.makeText(getActivity(), "123", 3000).show();
//				view.setFocusable(true);
				view.setClickable(true);
			}
		}, 10000);
		
//		view.setFocusable(false);
//		showProgressDialog();
//		view.setClickable(true);
		progressBar.setFocusable(true);
		progressBar.requestFocusFromTouch();
		progressBar.requestFocus();
		progressBar.setClickable(true);
//		progressBar.
//		view.
//		progressBar.re
		LogUtils2.e("--"+progressBar.isFocusable());
//		progressBar.setVisibility(View.GONE);
		return view;
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils2.i("***onViewCreated***");
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils2.i("***onActivityCreated***");
	}
	

	@Override
	public void onStart() {
		super.onStart();
		LogUtils2.i("***onStart***");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LogUtils2.i("***onResume***");
	}
	
}
