package edu.uco.kjames21.p04;

public abstract class Operator implements IOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int numberOne;
	private int numberTwo;
	private boolean hasResult = false;
	private int result;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberOne() {
		return numberOne;
	}
	public void setNumberOne(int numberOne) {
		this.numberOne = numberOne;
		this.hasResult = false;
	}
	public int getNumberTwo() {
		return numberTwo;
	}
	public void setNumberTwo(int numberTwo) {
		this.numberTwo = numberTwo;
		this.hasResult = false;
	}
	
	public boolean hasResult(){
		return this.hasResult;
	}
	
	public int getResult(){
		return this.result;
	}
	
	protected abstract int generateOperationResult();
	public void performOperation(){
		this.result = this.generateOperationResult();
		this.hasResult = true;
	}
}
