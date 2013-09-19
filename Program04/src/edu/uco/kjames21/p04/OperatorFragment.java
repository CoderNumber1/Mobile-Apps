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

public class OperatorFragment extends Fragment {
	private IOperator operator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.operator = (IOperator)getArguments().getSerializable(OperatorActivity.OPERATOR);
	}

	public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstance){
		View result = inflator.inflate(R.layout.fragment_operator, parent, false);
		
		getActivity().setResult(Activity.RESULT_CANCELED);
		Button btnCompute = (Button)result.findViewById(R.id.btnCompute);
		btnCompute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OperatorFragment.this.operator.performOperation();
				Intent resultData = new Intent();
				resultData.putExtra(OperatorActivity.NUMBER_RESULT, OperatorFragment.this.operator.getResult());
				getActivity().setResult(Activity.RESULT_OK, resultData);
				
				EditText resultText = (EditText)getActivity().findViewById(R.id.txtResult);
				resultText.setText(Integer.toString(OperatorFragment.this.operator.getResult()));
			}
		});
		btnCompute.setText(this.operator.getName());
		
		EditText txtNumberOne = (EditText)result.findViewById(R.id.txtNumberOne);
		txtNumberOne.setText(Integer.toString(this.operator.getNumberOne()));
		EditText txtNumberTwo = (EditText)result.findViewById(R.id.txtNumberTwo);
		txtNumberTwo.setText(Integer.toString(this.operator.getNumberTwo()));
		
		return result;
	}
	
}
