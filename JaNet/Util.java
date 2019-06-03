
public class Util {
	
	public static float[][] transpose(float[][] x) {
		float[][] out = new float[x[0].length][x.length];
		
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				out[j][i] = x[i][j];
			}
		}
		
		return out;
	}
	
	public static float[] dot(float[] x, float[][] y) {
		
		float[] out = new float[y.length];
		float sum = 0;
		for (int i = 0; i < y.length; i++) {
			sum = 0;
			for (int j = 0; j < y[0].length; j++) {
				sum += x[j] * y[i][j];
			}
			out[i] = sum;
		}
		
		return out;
	}

	public static float[][] outer(float[] x, float[] y) {
		float[][] out = new float[x.length][y.length];
		
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < y.length; j++) {
				out[i][j] = x[i] * y[j];
			}
		}
		
		return out;
	}

	public static float[] log(float[] x) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = (float) Math.log(x[i]);
		}
		return out;
	}
	
	public static float[] sqrt(float[] x) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = (float) Math.sqrt(x[i]);
		}
		return out;
	}
	
	public static float[][] sqrt(float[][] x) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = sqrt(x[i]);
		}
		return out;
	}

	public static float[] clip(float[] x, float min, float max) {
		float[] out = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			if (x[i] > max) {
				out[i] = max;
			} else if (x[i] < min) {
				out[i] = min;
			} else {
				out[i] = x[i];
			}
		}
		return out;
	}
	
	public static float[] add(float[] x, float[] y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] += y[i];
		}
		return out;
	}
	
	public static float[] sub(float[] x, float[] y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] -= y[i];
		}
		return out;
	}
	
	public static float[] mul(float[] x, float[] y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] *= y[i];
		}
		return out;
	}
	
	public static float[] div(float[] x, float[] y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = y[i];
		}
		return out;
	}
	
	public static float[] add(float[] x, float y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] += y;
		}
		return out;
	}
	
	public static float[] sub(float[] x, float y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] -= y;
		}
		return out;
	}
	
	public static float[] mul(float[] x, float y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] *= y;
		}
		return out;
	}
	
	public static float[] div(float[] x, float y) {
		float[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] /= y;
		}
		return out;
	}
	
	public static float[] sub(float x, float[] y) {
		float[] out = y.clone();
		for (int i = 0; i < y.length; i++) {
			out[i] = x - y[i];
		}
		return out;
	}
	
	public static float[] div(float x, float[] y) {
		float[] out = y.clone();
		for (int i = 0; i < y.length; i++) {
			out[i] = x / y[i];
		}
		return out;
	}

	public static float[][] add(float[][] x, float y) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = add(out[i], y);
		}
		return out;
	}
	
	public static float[][] sub(float[][] x, float y) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = sub(out[i], y);
		}
		return out;
	}
	
	public static float[][] mul(float[][] x, float y) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = mul(out[i], y);
		}
		return out;
	}
	
	public static float[][] div(float[][] x, float y) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = div(out[i], y);
		}
		return out;
	}
	
	public static float[][] add(float[][] x, float y[][]) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = add(out[i], y[i]);
		}
		return out;
	}
	
	public static float[][] sub(float[][] x, float y[][]) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = sub(out[i], y[i]);
		}
		return out;
	}
	
	public static float[][] mul(float[][] x, float y[][]) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = mul(out[i], y[i]);
		}
		return out;
	}
	
	public static float[][] div(float[][] x, float y[][]) {
		float[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = div(out[i], y[i]);
		}
		return out;
	}

}
