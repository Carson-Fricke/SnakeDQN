import java.util.Arrays;

public class Network {
	
	public Layer[] layers;
	
	public Network(Layer[] layers) {
		this.layers = layers;
	}
	
	public float[] forward(float[] x) {
		
		float[] carry = x.clone();
		this.layers[0].activations = x;
		
		for (int i = 1; i < this.layers.length; i++) {
			carry = this.layers[i].forward(carry).clone();
		}
		
		return carry;
	}
	
	public float[] backward(float[] x) {
		
		float[] carry = x.clone();
		
		for (int i = this.layers.length - 1; i >= 1; i--) {
			carry = this.layers[i].backward(carry, this.layers[i - 1].activations).clone();
		}
		
		return carry;
	}
	
}
