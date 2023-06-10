package janet;
import java.util.Random;

public class Output extends Layer{
	
	private double[] activation(double[] x, boolean deriv) {
				
		// Sigmoid output
		/*x = Util.clip(x.clone(), -15f, 15f);
		
		if (deriv) {
			out = Util.mul(activation(x, false), Util.sub(1, activation(x, false)));

		} else {
			for (int i = 0; i < x.length; i++) {
				out[i] = (double) (1 / (1 + Math.exp(-x[i])));
			}
		}*/

		if (deriv) {
			double[] out = new double[x.length];
			for (int i = 0; i < x.length; i++) {
				out[i] = 1.0;
			}
			return out;
		} 
		else {
			return x;
		}
	}
	
	public Output(int size, int inSize, Optimizer optimizer) {
		
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
	
	public double[] lossFunction(double[] expectedValue, double[] actualValue, boolean deriv) {
		// Binary Cross entropy Loss
		// Util.add(Util.mul(expectedValue, Util.log(actualValue)), Util.mul(Util.sub(1, expectedValue), Util.log(Util.sub(1, actualValue))));
		
		// Huber Loss
		/*
		double[] output = new double[expectedValue.length];
		double[] delta = Util.sub(actualValue, expectedValue);
		if (deriv) {

			for (int i = 0; i < output.length; i++) {
				
				if (delta[i] > 1) {
					output[i] = 2.0;
				} 
				else if (delta[i] < -1) {

					output[i] = -2.0;
				} 
				else {
					output[i] = 2 * delta[i];
				}

			}

		}
		else {

			for (int i = 0; i < output.length; i++) {
				if (delta[i] < 1) {
					output[i] =  delta[i] * delta[i]; 
				}
				else {
					output[i] = Math.abs(delta[i]);
				}
			}
		}
		
		return output; */

		// MSE loss
		double[] delta = Util.sub(actualValue, expectedValue);

		if (deriv) {
			return delta;
		} else {
			return Util.div(Util.mul(delta, delta), 2);
		}
	}
	
	@Override
	public double[] forward(double[] lastLayerActivations) {
		this.sums = Util.add(Util.dot(lastLayerActivations, this.weights), this.biases);
		this.activations = activation(this.sums, false);
		return this.activations;
	}

	@Override
	public double[] backward(double[] desiredValue, double[] lastLayerActivations) {
		

		// d loss / d Baises
		// d loss / d weights
		// w = w - dl/dw
		// b = b - dl/db

		// d Loss / d activations = Loss'(activations)
		double[] dlda = lossFunction(desiredValue, this.activations, true);
		// d Loss / d sums = d Loss / d activations  *  d activations / d sums
		double[] dlds = Util.mul(dlda, activation(this.sums, true));

		// this was the dL/dS value in the past, but I don't know how I came to this conclusion
		//double[] error = Util.mul(Util.mul(Util.sub(desiredValue, this.activations), this.activation(this.sums, true)), errorFunc(desiredValue, this.activations));
		
		//System.out.println(this.activations[0] + " " + desiredValue[0] + " " + dlds[0] + " " + this.activation(this.sums, true)[0]);
		//System.out.println();

		// d Loss / d lastLayerActivtions
		double[] dldlla = Util.dot(dlds, Util.transpose(this.weights));

		// Util.outer(dlds, lastLayerActivations) = d Loss / d weights
		this.weights = Util.sub(this.weights, this.optimizer.optimizeWeights(Util.outer(dlds, lastLayerActivations)));

		// d Loss / d biases = d Loss / d sums  *  d sums / d biases, ds/db = 1
		this.biases = Util.sub(this.biases, this.optimizer.optimizeBiases(dlds));
		return dldlla;
	}

}
