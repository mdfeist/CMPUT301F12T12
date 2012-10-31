package ca.ualberta.cs.completemytask;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingView {
	private Activity activity;
	
	private int viewId;
	private String message;
	
	private int LOAD_VIEW_STUB = 1;
	private int LOAD_VIEW = 2;
	
	public LoadingView(Activity activity, int viewId) {
		this.activity = activity;
		this.viewId = viewId;
		
		initView();
	}
	
	public LoadingView(Activity activity, 
			int viewId, String message) {
		this.activity = activity;
		this.viewId = viewId;
		this.message = message;
		
		initView();
	}
	
	private void initView() {
		ViewStub loadingView = new ViewStub(this.activity);
		loadingView.setLayoutResource(R.layout.progress_overlay);
		loadingView.setId(LOAD_VIEW_STUB);
		loadingView.setInflatedId(LOAD_VIEW);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		
        loadingView.setLayoutParams(lp);
        
		ViewGroup viewGroup = (ViewGroup) this.activity.findViewById(this.viewId);
		viewGroup.addView(loadingView);
	}
	
	/**
     * Hides and displays the loading screen when the
     * application is loading.
     * @param whether the screen should be displayed
     */
	public void showLoadView(boolean show) {
		if (show) {
    		enableView(false);
			ViewStub loadViewStub = (ViewStub) this.activity.findViewById(LOAD_VIEW_STUB);
			
			if (loadViewStub != null) {
				loadViewStub.inflate();
				
				TextView loadingMessage = (TextView) this.activity.findViewById(R.id.loadingMessage);
				loadingMessage.setText(this.message);
			} else {
				View loadView = (View) this.activity.findViewById(LOAD_VIEW);
	        	loadView.setVisibility(View.VISIBLE);
			}
    	} else {
            enableView(true);
            
        	View loadView = (View) this.activity.findViewById(LOAD_VIEW);
        	loadView.setVisibility(View.INVISIBLE);
    	}
	}
	
	/**
     * Disables the main view so buttons can't be
     * clicked.
     * @param enable or disable
     */
    private void enableView(boolean enable) {
    	ViewGroup layout = (ViewGroup) this.activity.findViewById(viewId);
        enableView(enable, layout);
    }
    
    /**
     * A helper class of enableView(boolean) to find all
     * the views contained in ViewGroups.
     * @param enable or disable
     * @param A ViewGroup
     */
    private void enableView(boolean enable, ViewGroup viewGroup) {
    	for (int i = 0; i < viewGroup.getChildCount(); i++) {
    		View child = viewGroup.getChildAt(i);
    		
            if (ViewGroup.class.isAssignableFrom(child.getClass())) {
            	enableView(enable, (ViewGroup)child);
            }
            
            child.setEnabled(enable);
        }
    }
}
