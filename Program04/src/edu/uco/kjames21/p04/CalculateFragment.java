package edu.uco.kjames21.p04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculateFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstance){
		View result = inflator.inflate(R.layout.fragment_calculate, parent, false);
		
		Button btnAdd = (Button)result.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(CalculateFragment.this.validateNumberFields()){
					EditText txtNumberOne = (EditText)CalculateFragment.this.getView().findViewById(R.id.txtNumberOne);
					EditText txtNumberTwo = (EditText)CalculateFragment.this.getView().findViewById(R.id.txtNumberTwo);
					Intent addIntent = new Intent(getActivity(), OperatorActivity.class);
					addIntent.putExtra(OperatorActivity.NUMBER_ONE, Integer.parseInt(txtNumberOne.getText().toString()));
					addIntent.putExtra(OperatorActivity.NUMBER_TWO, Integer.parseInt(txtNumberTwo.getText().toString()));
					addIntent.putExtra(OperatorActivity.OPERATOR_MODE, OperatorActivity.ADD_OPERATOR);
					
					startActivityForResult(addIntent, OperatorActivity.ADD_ACTIVITY_RESULT);
				}	
			}
		});
		
		Button btnMultiply = (Button)result.findViewById(R.id.btnMultiply);
		btnMultiply.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(CalculateFragment.this.validateNumberFields()){
					EditText txtNumberOne = (EditText)getActivity().findViewById(R.id.txtNumberOne);
					EditText txtNumberTwo = (EditText)getActivity().findViewById(R.id.txtNumberTwo);
					Intent addIntent = new Intent(getActivity(), OperatorActivity.class);
					addIntent.putExtra(OperatorActivity.NUMBER_ONE, Integer.parseInt(txtNumberOne.getText().toString()));
					addIntent.putExtra(OperatorActivity.NUMBER_TWO, Integer.parseInt(txtNumberTwo.getText().toString()));
					addIntent.putExtra(OperatorActivity.OPERATOR_MODE, OperatorActivity.MULTIPLY_OPERATOR);
					
					startActivityForResult(addIntent, OperatorActivity.MULTIPLY_ACTIVITY_RESULT);
				}
			}
		});
		
		return result;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		EditText txtResult = (EditText)getActivity().findViewById(R.id.txtResult);
		if(resultCode == Activity.RESULT_OK){
			Integer result = data.getIntExtra(OperatorActivity.NUMBER_RESULT, 0);
			txtResult.setText(result.toString());
		}
		else{
			txtResult.setText(R.string.result_pending);
		}
	}

	public boolean validateNumberFields(){
		TextView txtErrorLabel = (TextView)getActivity().findViewById(R.id.txtErrorLabel);
		EditText txtNumberOne = (EditText)getActivity().findViewById(R.id.txtNumberOne);
		EditText txtNumberTwo = (EditText)getActivity().findViewById(R.id.txtNumberTwo);
	
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
