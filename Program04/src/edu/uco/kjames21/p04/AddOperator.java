package edu.uco.kjames21.p04;

public class AddOperator extends Operator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddOperator(int numberOne, int numberTwo){
		super.setNumberOne(numberOne);
		super.setNumberTwo(numberTwo);
		super.setName("Add");
	}
	
	@Override
	protected int generateOperationResult() {
		return super.getNumberOne() + super.getNumberTwo();
	}

}
