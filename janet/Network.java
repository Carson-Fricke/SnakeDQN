package janet;

import java.io.*;

public class Network implements Serializable {
	
	public Layer[] layers;
	
	public void saveNetwork(String loadPath) {
		try {
			FileOutputStream f = new FileOutputStream(loadPath + ".network");
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(this);
			o.close();
			f.close();
		} catch (Exception e) {	
		}
		
	}

	public static Network loadNetwork(String loadPath) {
		Network out = null;
		try {
			FileInputStream f = new FileInputStream(loadPath + ".network");
			ObjectInputStream o = new ObjectInputStream(f);
			out = (Network) o.readObject();
			o.close();
			f.close();
		} catch (Exception e) {
		}

		return out;
	}


	public Network(Layer[] layers) {
		this.layers = layers;
	}
	
	public double[] forward(double[] x) {
		
		double[] carry = x.clone();
		this.layers[0].activations = x;
		
		for (int i = 1; i < this.layers.length; i++) {
			carry = this.layers[i].forward(carry).clone();
		}
		
		return carry;
	}
	
	public double[] backward(double[] desiredValue) {
		
		double[] carry = desiredValue.clone();
		
		for (int i = this.layers.length - 1; i >= 1; i--) {
			carry = this.layers[i].backward(carry, this.layers[i - 1].activations).clone();
		}
		
		return carry;
	}
	
}
