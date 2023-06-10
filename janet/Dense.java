package janet;
import java.util.Random;

public class Dense extends Layer {
	
	private double[] activation(double[] x, boolean deriv) {
		
		double[] out = new double[x.length];
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
		
		this.sums = new double[size];
		this.activations = new double[size];
		
		this.weights = new double[size][inSize];
		this.biases = new double[size];
		
		for(int i = 0; i < this.weights.length; i++) {
			this.biases[i] = r.nextGaussian() / inSize;
			for(int j = 0; j < this.weights[0].length; j++) {
				this.weights[i][j] = r.nextGaussian() * Math.sqrt(2.0 / (size + inSize));
				
			}
		}
		
		this.optimizer = optimizer;
		this.optimizer.l = this;
	}
	
	public double[] forward(double[] lastLayerActivations) {
		
		this.sums = Util.add(Util.dot(lastLayerActivations, this.weights), this.biases);
		this.activations = activation(this.sums, false);
		return this.activations;
		
	}
	
	public double[] backward(double[] dlda, double[] lastLayerActivations) {
		
		double[] dlds = Util.mul(dlda, activation(this.sums, true));
		double[] dldlla = Util.dot(dlds, Util.transpose(this.weights));
		
		this.weights = Util.sub(this.weights, this.optimizer.optimizeWeights(Util.outer(dlds, lastLayerActivations)));

		this.biases = Util.sub(this.biases, this.optimizer.optimizeBiases(dlds));
		
		return dldlla;
	}
	
}
