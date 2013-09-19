package edu.uco.kjames21.p04;

import java.io.Serializable;

public interface IOperator extends Serializable {
	public String getName();
	public void setName(String name);
	public int getNumberOne();
	public void setNumberOne(int numberOne);
	public int getNumberTwo();
	public void setNumberTwo(int numberTwo);
	public boolean hasResult();
	public int getResult();
	public void performOperation();
}
