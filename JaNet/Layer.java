package janet;

import java.io.Serializable;

public abstract class Layer  implements Serializable {	
	
	public double[][] weights;
	public double[] biases;
	
	public double[] sums;
	public double[] activations;
	
	public Optimizer optimizer;
	
	abstract double[] forward(double[] lastLayerActivations);
	abstract double[] backward(double[] dlda, double[] lastLayerActivations);
	
//	abstract double[] activation(double[] x, boolean deriv);
	
	
	
}
