package janet;
public class Util {
	
	public static double[][] transpose(double[][] x) {
		double[][] out = new double[x[0].length][x.length];
		
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				out[j][i] = x[i][j];
			}
		}
		
		return out;
	}
	
	public static double[] dot(double[] x, double[][] y) {
		
		double[] out = new double[y.length];
		double sum = 0;
		for (int i = 0; i < y.length; i++) {
			sum = 0;
			for (int j = 0; j < y[0].length; j++) {
				sum += x[j] * y[i][j];
			}
			out[i] = sum;
		}
		
		return out;
	}

	public static double[][] outer(double[] x, double[] y) {
		double[][] out = new double[x.length][y.length];
		
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < y.length; j++) {
				out[i][j] = x[i] * y[j];
			}
		}
		
		return out;
	}

	public static double[] log(double[] x) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = (double) Math.log(x[i]);
		}
		return out;
	}
	
	public static double[] sqrt(double[] x) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = (double) Math.sqrt(x[i]);
		}
		return out;
	}
	
	public static double[][] sqrt(double[][] x) {
		double[][] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = sqrt(x[i]);
		}
		return out;
	}

	public static double[] clip(double[] x, double min, double max) {
		double[] out = new double[x.length];
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
	
	public static double[] add(double[] x, double[] y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] += y[i];
		}
		return out;
	}
	
	public static double[] sub(double[] x, double[] y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] -= y[i];
		}
		return out;
	}
	
	public static double[] mul(double[] x, double[] y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] *= y[i];
		}
		return out;
	}
	
	public static double[] div(double[] x, double[] y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] = y[i];
		}
		return out;
	}
	
	public static double[] add(double[] x, double y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] += y;
		}
		return out;
	}
	
	public static double[] sub(double[] x, double y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] -= y;
		}
		return out;
	}
	
	public static double[] mul(double[] x, double y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] *= y;
		}
		return out;
	}
	
	public static double[] div(double[] x, double y) {
		double[] out = x.clone();
		for (int i = 0; i < x.length; i++) {
			out[i] /= y;
		}
		return out;
	}
	
	public static double[] sub(double x, double[] y) {
		double[] out = y.clone();
		for (int i = 0; i < y.length; i++) {
			out[i] = x - y[i];
		}
		return out;
	}
	
	public static double[] div(double x, double[] y) {
		double[] out = y.clone();
		for (int i = 0; i < y.length; i++) {
			out[i] = x / y[i];
		}
		return out;
	}

	public static double[][] add(double[][] x, double y) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = add(x[i], y);
		}
		return out;
	}
	
	public static double[][] sub(double[][] x, double y) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = sub(x[i], y);
		}
		return out;
	}
	
	public static double[][] mul(double[][] x, double y) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = mul(x[i], y);
		}
		return out;
	}
	
	public static double[][] div(double[][] x, double y) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = div(x[i], y);
		}
		return out;
	}
	
	public static double[][] add(double[][] x, double y[][]) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = add(x[i], y[i]);
		}
		return out;
	}
	
	public static double[][] sub(double[][] x, double y[][]) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = sub(x[i], y[i]);
		}
		return out;
	}
	
	public static double[][] mul(double[][] x, double y[][]) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = mul(x[i], y[i]);
		}
		return out;
	}
	
	public static double[][] div(double[][] x, double y[][]) {
		double[][] out = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			out[i] = div(x[i], y[i]);
		}
		return out;
	}

}
