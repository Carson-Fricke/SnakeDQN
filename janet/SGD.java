package janet;


public class SGD extends Optimizer {
	
	// here be problems
	

	private double eta;
	private int parsedW;
	private int parsedB;
	private double[][] wGrads;
	private double[][] wUpdates;
	private double[][] wZeros;
	private double[] bGrads;
	private double[] bUpdates;
	private double[] bZeros;
	
	public SGD(double eta, int batchSize) {
		this.eta = eta;
		this.batch = batchSize;
		this.parsedW = -1;
		this.parsedB = -1;
		this.wGrads = new double[0][0];
		this.wUpdates = new double[0][0];
		this.bGrads = new double[0];
		this.bUpdates = new double[0];
		
		this.wZeros= new double[0][0];
		this.bZeros = new double[0];
	}

	// problems here
	@Override
	double[][] optimizeWeights(double[][] grads) {
		if (this.parsedW == -1) {
			this.wGrads = new double[grads.length][grads[0].length];
			this.wUpdates = new double[grads.length][grads[0].length];
			this.wZeros = new double[grads.length][grads[0].length];
			this.parsedW = 0;
			for (int i = 0; i < grads.length; i++) {
				for (int j = 0; j < grads[0].length; j++) {
					this.wGrads[i][j] = 0.0;
					this.wUpdates[i][j] = 1.0;
					this.wZeros[i][j] = 0.0;
				}
			}

		}
		
		this.parsedW++;
		this.wGrads = Util.add(this.wGrads, grads);
		
		if (this.batch == this.parsedW) {
			//this.wUpdates = Util.add(Util.mul(this.wUpdates, 0.9), Util.mul(Util.mul(this.wGrads, this.wGrads), 0.1));

			//double[][] out = Util.div(Util.mul(Util.div(this.wGrads, Util.sqrt(Util.add(this.wUpdates, 0.1))), this.eta), this.batch);
			double[][] out = Util.div(Util.mul(this.wGrads, this.eta), this.batch);
			
			for (int i = 0; i < this.wGrads.length; i++) {
				for (int j = 0; j < this.wGrads[i].length; j++) {
					this.wGrads[i][j] = 0;
				}
			}

			this.parsedW = 0;
			return out;
		} else {
			return this.wZeros;
		}
		
	}

	// problems here
	@Override
	double[] optimizeBiases(double[] grads) {
		
		if (this.parsedB == -1) {
			this.bGrads = new double[grads.length];
			this.bUpdates = new double[grads.length];
			this.bZeros = new double[grads.length];
			this.parsedB = 0;
			for (int i = 0; i < grads.length; i++) {
				this.bGrads[i] = 0.0;
				this.bUpdates[i] = 1.0;
				this.bZeros[i] = 0.0;
				
			}
		}
		
		
		this.parsedB++;
		this.bGrads = Util.add(this.bGrads, grads);
		
		if (this.batch == this.parsedB) {
			
			//this.bUpdates = Util.add(Util.mul(this.bUpdates, 0.9), Util.mul(Util.mul(this.bGrads, this.bGrads), 0.1));
			//double[] out = Util.div(Util.mul(Util.div(this.bGrads, Util.sqrt(Util.add(this.bUpdates, 0.000001))), this.eta), this.batch);
			//System.out.println("bu: " + this.bUpdates[0] + " bg*bg: " + Math.sqrt(this.bGrads[0] * this.bGrads[0] + 0.001));
			double[] out = Util.div(Util.mul(this.bGrads, this.eta), this.batch);

			for (int i = 0; i < this.bGrads.length; i++) {
				this.bGrads[i] = 0;
			}
			
			this.parsedB = 0;
			
			return out;
		} else {
			
			return this.bZeros;
		}
	}

}
