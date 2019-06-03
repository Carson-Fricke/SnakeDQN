
public class RMSprop extends Optimizer{
	
	private float eta;
	private int batch;
	private int parsedW;
	private int parsedB;
	private float[][] wGrads;
	private float[][] wUpdates;
	private float[] bGrads;
	private float[] bUpdates;
	
	
	public RMSprop(float eta, int batch) {
		this.eta = eta;
		this.batch = batch;
		this.parsedW = -1;
		this.parsedB = -1;
		this.wGrads = new float[0][0];
		this.wUpdates = new float[0][0];
		this.bGrads = new float[0];
		this.bUpdates = new float[0];
	}
	
	@Override
	float[][] optimizeWeights(float[][] grads) {
		if (parsedW == -1) {
			this.wGrads = new float[grads.length][grads[0].length];
			this.wUpdates = new float[grads.length][grads[0].length];
		}
		
		this.parsedW++;
		this.wGrads = Util.add(this.wGrads, grads);
		
		if (this.batch == this.parsedW) {
			this.wUpdates = Util.add(Util.mul(this.wUpdates, 0.9f), Util.mul(Util.mul(grads, grads), 0.1f));

			float[][] out = Util.mul(Util.div(this.wGrads, Util.sqrt(Util.add(this.wUpdates, 0.000001f))), this.eta);
			
			this.wGrads = new float[grads.length][grads[0].length];
			this.parsedW = 0;
			return out;
		} else {
			return new float[grads.length][grads[0].length];
		}
		
	}

	@Override
	float[] optimizeBiases(float[] grads) {
		if (parsedB == -1) {
			this.bGrads = new float[grads.length];
			this.bUpdates = new float[grads.length];
		}
		
		
		this.parsedB++;
		this.bGrads = Util.add(this.bGrads, grads);
		
		if (this.batch == this.parsedB) {
			this.bUpdates = Util.add(Util.mul(this.bUpdates, 0.9f), Util.mul(Util.mul(grads, grads), 0.1f));
			
			float[] out = Util.mul(Util.div(this.bGrads, Util.sqrt(Util.add(this.bUpdates, 0.000001f))), this.eta);
			
			this.bGrads = new float[grads.length];
			this.parsedB = 0;
//			System.out.println(out[0]);
			
			return out;
		} else {
			
			return new float[grads.length];
		}
	}

}
