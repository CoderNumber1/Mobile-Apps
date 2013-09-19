package edu.uco.kjames21.p04;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

public class OperatorActivity extends FragmentActivity {
	public static final String NUMBER_ONE = "edu.uco.kjames21.p04.NumberOne";
	public static final String NUMBER_TWO = "edu.uco.kjames21.p04.NumberTwo";
	public static final String NUMBER_RESULT = "edu.uco.kjames21.p04.NumberResult";
	public static final int ADD_ACTIVITY_RESULT = 1;
	public static final int MULTIPLY_ACTIVITY_RESULT = 2;
	public static final String OPERATOR_MODE = "edu.uco.kjames21.p04.OperatorMode";
	public static final String OPERATOR = "edu.uco.kjames21.p04.Operator";
	public static final int ADD_OPERATOR = 1;
	public static final int MULTIPLY_OPERATOR = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operator);
		
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null){
			fragment = new OperatorFragment();
			Bundle args = new Bundle();
			int operatorMode = getIntent().getIntExtra(OPERATOR_MODE, ADD_OPERATOR);
			int numberOne = getIntent().getIntExtra(NUMBER_ONE, 0);
			int numberTwo = getIntent().getIntExtra(NUMBER_TWO, 0);
			if(operatorMode == ADD_OPERATOR){
				args.putSerializable(OPERATOR, new AddOperator(numberOne, numberTwo));
			}
			else if(operatorMode == MULTIPLY_OPERATOR){
				args.putSerializable(OPERATOR, new MultiplyOperator(numberOne, numberTwo));
			}
			fragment.setArguments(args);
			
			manager.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.operator, menu);
		return true;
	}

}
