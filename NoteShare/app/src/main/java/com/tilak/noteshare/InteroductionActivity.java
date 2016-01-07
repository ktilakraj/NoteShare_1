package com.tilak.noteshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

public class InteroductionActivity extends Activity {
    /** Called when the activity is first created. */
	public int finish=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interoduction_activity);
        
        final ViewFlipper page = (ViewFlipper)findViewById(R.id.flipper);
        final Button btnNext = (Button)findViewById(R.id.next);
        Button btnPrevious = (Button)findViewById(R.id.previous);
        
        final Animation animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
        final Animation animFlipOutForeward = AnimationUtils.loadAnimation(this, R.anim.flipout);
        final Animation animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
        final Animation animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);
        
        btnNext.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				if (finish<4)
				{
					page.setInAnimation(animFlipInForeward);
					page.setOutAnimation(animFlipOutForeward);
					page.showNext();
					if (finish==3)
					{
						btnNext.setText("FINISH");
					}	
					System.out.println("Nxt");
				}
				else
				{
					System.out.println("finish");
					Intent i = new Intent(InteroductionActivity.this, LoginActivity.class);
	                startActivity(i);
	                // close this activity
	                finish();
	                
	              //--SAVE Data
	                SharedPreferences preferences = InteroductionActivity.this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);  
	                SharedPreferences.Editor editor = preferences.edit();
	                editor.putBoolean("FINISHED", true);
	                //editor.putBoolean("FINISHED", false);
	                editor.commit();

				}
				finish++;
				
			}});
        
        btnPrevious.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				page.setInAnimation(animFlipInBackward);
				page.setOutAnimation(animFlipOutBackward);
				page.showPrevious();
				
				
			}});
    }
}