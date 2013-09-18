package edu.uco.kjames21.p02;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComputeActivity extends Activity {
	public static final String NUMBER_ONE = "edu.uco.kjames21.p02.NumberOne";
	public static final String NUMBER_TWO = "edu.uco.kjames21.p02.NumberTwo";
	public static final String NUMBER_RESULT = "edu.uco.kjames21.p02.NumberResult";
	public static final int ADD_ACTIVITY_RESULT = 1;
	public static final int MULTIPLY_ACTIVITY_RESULT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compute);
		
		Button btnAdd = (Button)this.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(ComputeActivity.this.validateNumberFields()){
					EditText txtNumberOne = (EditText)ComputeActivity.this.findViewById(R.id.txtNumberOne);
					EditText txtNumberTwo = (EditText)ComputeActivity.this.findViewById(R.id.txtNumberTwo);
					Intent addIntent = new Intent(ComputeActivity.this, AddActivity.class);
					addIntent.putExtra(ComputeActivity.NUMBER_ONE, Integer.parseInt(txtNumberOne.getText().toString()));
					addIntent.putExtra(ComputeActivity.NUMBER_TWO, Integer.parseInt(txtNumberTwo.getText().toString()));
					
					startActivityForResult(addIntent, ComputeActivity.ADD_ACTIVITY_RESULT);
				}	
			}
		});
		
		Button btnMultiply = (Button)this.findViewById(R.id.btnMultiply);
		btnMultiply.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(ComputeActivity.this.validateNumberFields()){
					EditText txtNumberOne = (EditText)ComputeActivity.this.findViewById(R.id.txtNumberOne);
					EditText txtNumberTwo = (EditText)ComputeActivity.this.findViewById(R.id.txtNumberTwo);
					Intent addIntent = new Intent(ComputeActivity.this, MultiplyActivity.class);
					addIntent.putExtra(ComputeActivity.NUMBER_ONE, Integer.parseInt(txtNumberOne.getText().toString()));
					addIntent.putExtra(ComputeActivity.NUMBER_TWO, Integer.parseInt(txtNumberTwo.getText().toString()));
					
					startActivityForResult(addIntent, ComputeActivity.MULTIPLY_ACTIVITY_RESULT);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compute, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		EditText txtResult = (EditText)ComputeActivity.this.findViewById(R.id.txtResult);
		if(resultCode == Activity.RESULT_OK){
			Integer result = data.getIntExtra(ComputeActivity.NUMBER_RESULT, 0);
			txtResult.setText(result.toString());
		}
		else{
			txtResult.setText(R.string.result_pending);
		}
	}

	public boolean validateNumberFields(){
		TextView txtErrorLabel = (TextView)ComputeActivity.this.findViewById(R.id.txtErrorLabel);
		EditText txtNumberOne = (EditText)ComputeActivity.this.findViewById(R.id.txtNumberOne);
		EditText txtNumberTwo = (EditText)ComputeActivity.this.findViewById(R.id.txtNumberTwo);
	
		String numberOneString = txtNumberOne.getText().toString();
		if(numberOneString == null || numberOneString == ""){
			txtErrorLabel.setText("Please provide number one.");
			txtErrorLabel.setVisibility(View.VISIBLE);
			return false;
		}
		else{
			try{
				Integer.parseInt(numberOneString);
			}
			catch(NumberFormatException e){
				txtErrorLabel.setText("Invalid input for number one.");
				txtErrorLabel.setVisibility(View.VISIBLE);
				return false;
			}
		}
		
		String numberTwoString = txtNumberTwo.getText().toString();
		if(numberTwoString == null || numberTwoString == ""){
			txtErrorLabel.setText("Please provide number two.");
			txtErrorLabel.setVisibility(View.VISIBLE);
			return false;
		}
		else{
			try{
				Integer.parseInt(numberTwoString);
			}
			catch(NumberFormatException e){
				txtErrorLabel.setText("Invalid input for number two.");
				txtErrorLabel.setVisibility(View.VISIBLE);
				return false;
			}
		}
		
		txtErrorLabel.setText("");
		txtErrorLabel.setVisibility(View.GONE);
		return true;
	}

}
