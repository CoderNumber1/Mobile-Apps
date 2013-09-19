package edu.uco.kjames21.p04;

public class MultiplyOperator extends Operator {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MultiplyOperator(int numberOne, int numberTwo){
		super.setNumberOne(numberOne);
		super.setNumberTwo(numberTwo);
		super.setName("Multiply");
	}

	@Override
	protected int generateOperationResult() {
		return super.getNumberOne() * super.getNumberTwo();
	}

}
