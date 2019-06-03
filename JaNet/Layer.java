abstract class Layer {	
	
	protected float[][] weights;
	protected float[] biases;
	
	protected float[] sums;
	protected float[] activations;
	
	protected Optimizer optimizer;
	
	abstract float[] forward(float[] x);
	abstract float[] backward(float[] x, float[] next);
	
//	abstract float[] activation(float[] x, boolean deriv);
	
	
	
}
