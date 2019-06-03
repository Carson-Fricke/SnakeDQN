import java.util.Arrays;
import java.util.Random;

public class Dense extends Layer {
	
	private float[] activation(float[] x, boolean deriv) {
		
		float[] out = new float[x.length];
		if (deriv) {
			for (int i = 0; i < x.length; i++) {
				if (x[i] >= 0) {
					out[i] = 1;
				} else {
					out[i] = 0;
				}
			}
		} else {
			for (int i = 0; i < x.length; i++) {
				if (x[i] >= 0) {
					out[i] = x[i];
				} else {
					out[i] = 0;
				}
			}
		}
		return out;
	}
	
	public Dense(int size, int inSize, Optimizer optimizer) {
		
		Random r = new Random();
		
		this.sums = new float[size];
		this.activations = new float[size];
		
		this.weights = new float[size][inSize];
		this.biases = new float[size];
		
		for(int i = 0; i < this.weights.length; i++) {
			this.biases[i] = (float) (r.nextGaussian());
			for(int j = 0; j < this.weights[0].length; j++) {
				this.weights[i][j] = (float) (r.nextGaussian());
				
			}
		}
		
		this.optimizer = optimizer;
	}
	
	public float[] forward(float[] x) {
		
		this.sums = Util.add(Util.dot(x, this.weights), this.biases);
		this.activations = activation(this.sums, false);
		return this.activations;
		
	}
	
	public float[] backward(float[] x, float[] next) {
		
		float[] error = Util.mul(x, activation(this.sums, true));
		float[] out = Util.dot(error, Util.transpose(this.weights));
		this.weights = Util.sub(this.weights, this.optimizer.optimizeWeights(Util.outer(error, next)));
		this.biases = Util.sub(this.biases, this.optimizer.optimizeBiases(error));
		
		return out;
	}
	
}
