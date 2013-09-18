package edu.uco.kjames21.p02;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends Activity {
	
	private Integer numberOne;
	private Integer numberTwo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		numberOne = getIntent().getIntExtra(ComputeActivity.NUMBER_ONE, 0);
		numberTwo = getIntent().getIntExtra(ComputeActivity.NUMBER_TWO, 0);
		
		this.setResult(Activity.RESULT_CANCELED);
		Button btnCompute = (Button)this.findViewById(R.id.btnComputeAdd);
		btnCompute.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Integer computation = numberOne + numberTwo;
				Intent resultData = new Intent();
				resultData.putExtra(ComputeActivity.NUMBER_RESULT, computation);
				AddActivity.this.setResult(Activity.RESULT_OK, resultData);
				
				EditText resultText = (EditText)AddActivity.this.findViewById(R.id.txtResult);
				resultText.setText(computation.toString());
			}
		});
		
		EditText txtNumberOne = (EditText)AddActivity.this.findViewById(R.id.txtNumberOne);
		txtNumberOne.setText(numberOne.toString());
		EditText txtNumberTwo = (EditText)AddActivity.this.findViewById(R.id.txtNumberTwo);
		txtNumberTwo.setText(numberTwo.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

}
