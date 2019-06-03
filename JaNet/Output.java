import java.util.Random;

public class Output extends Layer{
	
	private float[] activation(float[] x, boolean deriv) {
		
		float[] out = new float[x.length];
		
		x = Util.clip(x.clone(), -15f, 15f);
		
		if (deriv) {
			out = Util.mul(activation(x, false), Util.sub(1, activation(x, false)));

		} else {
			for (int i = 0; i < x.length; i++) {
				out[i] = (float) (1 / (1 + Math.exp(-x[i])));
			}
		}
		return out;
	}
	
	public Output(int size, int inSize, Optimizer optimizer) {
		
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
	
	public float[] errorFunc(float[] y, float[] a) {
		return Util.add(Util.mul(y, Util.log(a)), Util.mul(Util.sub(1, y), Util.log(Util.sub(1, a))));
	}
	
	@Override
	public float[] forward(float[] x) {
		
		this.sums = Util.add(Util.dot(x, this.weights), this.biases);
		this.activations = activation(this.sums, false);
		return this.activations;
	
	}

	@Override
	public float[] backward(float[] x, float[] next) {
		
		float[] error = Util.mul(Util.mul(Util.sub(x, this.activations), this.activation(this.sums, true)), errorFunc(x, this.activations));
		System.out.println(this.activations[0] + " " + x[0] + " " + error[0] + " " + this.activation(this.sums, true)[0]);
		System.out.println();
		float[] out = Util.dot(error, Util.transpose(this.weights));
		this.weights = Util.sub(this.weights, this.optimizer.optimizeWeights(Util.outer(error, next)));
		float[] temp = this.optimizer.optimizeBiases(error);
		this.biases = Util.sub(this.biases, this.optimizer.optimizeBiases(temp));
		return out;
	}

}
