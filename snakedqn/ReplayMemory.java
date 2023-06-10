package snakedqn;
import java.util.LinkedList;
import java.util.Collections;


public class ReplayMemory {
    
    public class Transition {

        public double[] state;
        public int action;

        public double[] nextState;
        public double reward;

        public Transition(double[] state, int action, double[] nextState, double reward) {
            this.state = state;
            this.action = action;
            this.nextState = nextState;
            this.reward = reward; 
        }

        public Transition() {}

    }

    public ReplayMemory() {
        this.memory = new LinkedList<Transition>();
    }

    public LinkedList<Transition> memory;

    public void add(double[] state, int action, double[] nextState, double reward) {
        memory.push(new Transition(state, action, nextState, reward));
    }
    
    public Transition[] sample(int batchSize) {

        Transition[] output = new Transition[batchSize];

        Collections.shuffle(memory);

        for (int i = 0; i < batchSize; i++) {
            output[i] = memory.remove(0);
        }

        return output;
    }

    

    public int getLength() {
        return this.memory.size();
    }

}
