
abstract class Optimizer {

	abstract float[][] optimizeWeights(float[][] grads);
	abstract float[] optimizeBiases(float[] grads);
	
	
}
