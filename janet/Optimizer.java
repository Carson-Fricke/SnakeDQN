package janet;

import java.io.Serializable;

abstract class Optimizer implements Serializable {

	public Layer l;

	protected int batch;

	abstract double[][] optimizeWeights(double[][] grads);
	abstract double[] optimizeBiases(double[] grads);
	
	
	public void updateBatchSize(int newBatch) {
		this.batch = newBatch;
	}
	
}
