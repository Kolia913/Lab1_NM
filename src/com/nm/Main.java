package com.nm;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter left border: ");
		double leftBorder = scanner.nextDouble();
		System.out.println("Enter right border: ");
		double rightBorder = scanner.nextDouble();
		System.out.println("Enter accuracy: ");
		double epsilon = scanner.nextDouble();

		DichotomyMethod dm = DichotomyMethod.createDichotomyMethod(leftBorder, rightBorder, epsilon);
		if(dm != null){
			System.out.println("Result for dichotomy method: " + dm.solve());
			System.out.println("Iterations: " + dm.getIterations());
		}

		System.out.println("----------------------------------------");

		ChordMethod chm = ChordMethod.createChordMethod(leftBorder, rightBorder, epsilon);

		if(chm != null) {
			System.out.println("Result chord method: " + chm.solve());
			System.out.println("Iterations: " + chm.getIterations());
		}
		scanner.close();
	}
}

class DichotomyMethod {
	
	private double leftBorder;
	private double rightBorder;
	private final double epsilon;

	private int resSign;
	private int leftBSign;
	private int rightBSign;

	private int iterations;

	public static DichotomyMethod createDichotomyMethod(double leftBorder, double rightBorder, double epsilon) {
		if(Validations.validateInput(leftBorder, rightBorder, epsilon))
		{
			return new DichotomyMethod(leftBorder, rightBorder, epsilon);
		} else return null;
	}

	private DichotomyMethod(double leftBorder, double rightBorder, double epsilon) {
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
		this.epsilon = epsilon;
		this.iterations = 0;
	}

	public double solve() {
		double x = (this.leftBorder + this.rightBorder) / 2;
		while(isBorderDiffHigherThenEps())
		{
			this.getAndCompareSigns(x);
			x = (this.leftBorder + this.rightBorder) / 2;
			this.iterations++;
		}
		return x;
	}

	private boolean isBorderDiffHigherThenEps() {
		return Math.abs((this.leftBorder - this.rightBorder)) > epsilon;
	}

	private void getAndCompareSigns(double x)
	{
		this.getSigns(x);
		this.compareSignsAndChangeBorders(x);
	}

	private void compareSignsAndChangeBorders(double x) {
		if(this.leftBSign == this.resSign)
		{
			this.leftBorder = x;
		} else if(this.rightBSign == this.resSign) {
			this.rightBorder = x;
		}
	}

	private void getSigns(double x) {
		this.resSign = (int)Math.signum(this.getResultOfEquation(x));
		this.leftBSign = (int)Math.signum(this.getResultOfEquation(this.leftBorder));
		this.rightBSign = (int)Math.signum(this.getResultOfEquation(this.rightBorder));
	}

	private double getResultOfEquation(double x) 
	{
		return (Math.pow(x, 3.0)-(3*(Math.pow(x, 2.0)))+6*x+3);
	}

	public int getIterations()
	{
		return this.iterations;
	}

}


class ChordMethod {

	private final double leftBorder;
	private final double rightBorder;

	private int iterations;

	private final double epsilon;

	public static ChordMethod createChordMethod(double leftBorder, double rightBorder, double epsilon) {
		if(Validations.validateInput(leftBorder, rightBorder, epsilon)) {
			return new ChordMethod(leftBorder, rightBorder, epsilon);
		} else return null;
	}

	private ChordMethod(double leftBorder, double rightBorder, double epsilon)
	{
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
		this.epsilon = epsilon;
		this.iterations = 0;
	}

	public double solve() {
		if(this.isStaticBorder(this.leftBorder)) {
			return this.iterateRightBorder();
		}
		else if (this.isStaticBorder(this.rightBorder)) {
			return this.iterateLeftBorder();
		} else {
			System.out.println("Chord method is unavailable");
			return 0;
		}
	}

	private boolean isStaticBorder(double border)
	{
		return this.getResultOfSecondDerivative(border)*this.getResultOfSecondDerivative(border) > 0;
	}

	private double getResultOfSecondDerivative(double x)
	{
		return 6*x-6;
	}

	private double iterateRightBorder() {
		double x = this.getNextArgForRightBorder(this.rightBorder);
		double prev = this.rightBorder;
		while(Math.abs(x-prev) > epsilon) {
			prev = x;
			x = this.getNextArgForRightBorder(x);
			this.iterations++;
		}
		return x;
	}

	private double getNextArgForRightBorder(double x) {
		return x - (this.getResultOfEquation(x)*(x-this.leftBorder))/
				(this.getResultOfEquation(x)-this.getResultOfEquation(this.leftBorder));
	}

	private double iterateLeftBorder() {
		double x = this.getNextArgForLeftBorder(this.leftBorder);
		double prev = this.rightBorder;
		while(Math.abs(x-prev) > epsilon) {
			prev = x;
			x = this.getNextArgForLeftBorder(x);
			this.iterations++;
		}
		return x;
	}

	private double getNextArgForLeftBorder(double x) {
		return x - (this.getResultOfEquation(x)*(this.leftBorder-x))/
				(this.getResultOfEquation(this.rightBorder)-this.getResultOfEquation(x));
	}
	private double getResultOfEquation(double x)
	{
		return (Math.pow(x, 3.0)-(3*(Math.pow(x, 2.0)))+6*x+3);
	}

	public int getIterations()
	{
		return this.iterations;
	}

}

class Validations {
	static boolean validateInput(double leftBorder, double rightBorder, double epsilon)	{
		if((leftBorder*rightBorder < 0) && (leftBorder < rightBorder)){
			System.out.println("Invalid borders!");
			return false;
		} else if(epsilon <= 0)
		{
			System.out.println("Invalid accuracy!");
			return false;
		} else {
			return true;
		}
	}
}
