package com.tamilshout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.tamilshout.R;
import com.tamilshout.utils.TouchEffect;

public class BaseClass extends Activity implements OnClickListener {
	public static final TouchEffect TOUCH = new TouchEffect();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
	}
	
	
	public void setHeader(String txt){
		((TextView)findViewById(R.id.txtTitle)).setText(txt+"");
	}

	public void setRightButton(){
		findViewById(R.id.btnTopRight).setVisibility(View.VISIBLE);
		setTouchNClick(R.id.btnTopRight);
	}
	public void setLeftButton(int id){
		Button button=(Button) findViewById(R.id.btnTopLeft);
		button.setVisibility(View.VISIBLE);
		button.setBackgroundResource(id);
		
		
		setTouchNClick(R.id.btnTopLeft);
	}
	
	public void setLeft(){
//		setTouchNClick(R.id.btnLeft);
	}
	
	public View setTouchNClick(int id) {

		View v = findViewById(id);
		v.setOnClickListener(this);
		v.setOnTouchListener(TOUCH);
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		/*switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;

		default:
			break;
		}*/
		
	}
	
	public void setViewEnable(int id, boolean flag) {
		View v = findViewById(id);
		v.setEnabled(flag);
	}

	public void setViewVisibility(int id, int flag) {
		View v = findViewById(id);
		v.setVisibility(flag);
	}

	public void setTextViewText(int id, String text) {
		((TextView) findViewById(id)).setText(text);
	}

	public void setEditText(int id, String text) {
		((EditText) findViewById(id)).setText(text);
	}

	public String getEditTextText(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	public String getTextViewText(int id) {
		return ((TextView) findViewById(id)).getText().toString();
	}

	public String getButtonText(int id) {
		return ((Button) findViewById(id)).getText().toString();
	}

	public void setButtonText(int id, String text) {
		((Button) findViewById(id)).setText(text);
	}

	public void replaceButtoImageWith(int replaceId, int drawable) {
		((Button) findViewById(replaceId)).setBackgroundResource(drawable);
	}

	public void setButtonSelected(int id, boolean flag) {
		((Button) findViewById(id)).setSelected(flag);
	}

	public boolean isButtonSelected(int id) {
		return ((Button) findViewById(id)).isSelected();
	}
}
