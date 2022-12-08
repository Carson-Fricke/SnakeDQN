package janet;

import java.io.Serializable;

abstract class Optimizer implements Serializable {

	public Layer l;

	abstract double[][] optimizeWeights(double[][] grads);
	abstract double[] optimizeBiases(double[] grads);
	
	
}
