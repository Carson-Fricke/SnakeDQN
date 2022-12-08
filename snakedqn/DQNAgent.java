package snakedqn;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;

import game.Board;
import game.Direction;
import game.Snake;
import game.Tile;
import janet.*;
import snakedqn.ReplayMemory.Transition;

public class DQNAgent implements game.Operator, Serializable {

    private Snake snake;
	private Board board;
    
    public static Network agentNetwork;
    private int camWidth;
    private int camHeight;

    private static boolean train = true;
    private static Random r = new Random(10);
    private static ArrayList<DQNAgent> agentList = new ArrayList<DQNAgent>();
    private static ReplayMemory memory = new ReplayMemory();

    private static int trainingStepsDone = 0;
    private static double epsilonStart = 0.95;
    private static double epsilonFinish = 0.00;
    private static double epsilonDecay = 500;

    private static int batchSize;
    // VERY IMPORTANT - don't set too high, this is the lookahead rate
    private static double gamma = 0.2;

    public boolean print = false;

    private double[] state;
    private int action;
    private double[] nextState;
    private double reward;
    private boolean wasAlive = true;
    private double lastLength = 1000;

    public void saveAgent(String loadPath) {
		try {
			FileOutputStream f = new FileOutputStream(loadPath + ".network");
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(this);
			o.close();
			f.close();
		} catch (Exception e) {	
		}
		
	}

	public static DQNAgent loadAgent(String loadPath) {
		DQNAgent out = null;
		try {
			FileInputStream f = new FileInputStream(loadPath + ".network");
			ObjectInputStream o = new ObjectInputStream(f);
			out = (DQNAgent) o.readObject();
			o.close();
			f.close();
		} catch (Exception e) {
		}

		return out;
	}


    // for all intents and purposes, camX and camY ought to be odd numbers so the snakes head in in the middle
    public DQNAgent(int camX, int camY, int numHiddenLayers, int layerWidth, double eta, int batchSize_) {
        this.camWidth = camX;
        this.camHeight = camY;

        batchSize = batchSize_;
        
        Layer[] layers = new Layer[numHiddenLayers+2];
        layers[0] = new Dense(camX * camY + 5, 0, new RMSprop(eta, batchSize));
        
        int lastLayerWidth = camX * camY + 4;
        
        for (int i = 1; i <= numHiddenLayers; i++) {
            layers[i] = new Dense(layerWidth, lastLayerWidth, new RMSprop(eta, batchSize));
            lastLayerWidth = layerWidth;
        }
        layers[numHiddenLayers + 1] = new Output(4, lastLayerWidth, new RMSprop(eta, batchSize));

        agentNetwork = new Network(layers);

        agentList.add(this);
    } 

    public static void addTransition() {
        for (DQNAgent i : agentList) {
            if (i.wasAlive) {
                memory.add(i.state, i.action, i.nextState, i.reward);
            }
            i.wasAlive = i.getSnake().isAlive();
            
        }

        if (train) {

            if (memory.getLength() > batchSize) {
                trainingStep();
                System.out.println(trainingStepsDone);
            }

        }
        
    }

    public DQNAgent(Network agent, int camX, int camY) {
        agentNetwork = agent;
        agentList.add(this);

        this.camWidth = camX;
        this.camHeight = camY;
    
    }

    public DQNAgent(String loadPath, int camX, int camY) {
        agentNetwork = Network.loadNetwork(loadPath);
        agentList.add(this);
        
        this.camWidth = camX;
        this.camHeight = camY;
    }

    @Override
    public Snake getSnake() {
        return snake;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    public double[] readBoard() {

        // snake head width and height
        int sx = this.snake.getX();
        int sy = this.snake.getY();

        // board width and height
        int bw = this.board.width();
        int bh = this.board.height();

        // true x and y, adjusted for snake head position
        int tx;
        int ty;
        
        Tile tile;
        
        // here, -1 will represent a wall or snake body, 0 will represent empty space, and 1 will represent food.
        // this will keep the input small, and ought to have the nice inverse behavior of "Snake goes away from walls and towards food"
        double[] output = new double[this.camWidth * this.camHeight + 5];
        
        output[output.length - 1] = sx / bw;
        output[output.length - 2] = sy / bh;
        output[output.length - 3] = this.snake.getFood().getX() / bw;
        output[output.length - 4] = this.snake.getFood().getY() / bh;
        output[output.length - 5] = this.snake.isAlive() ? 1.0 : -1.0;

        for (int x = 0; x < this.camWidth; x++) {
            for (int y = 0; y < this.camHeight; y++) {
                tx = x + sx - camWidth/2;
                ty = y + sy - camHeight/2;
                
                if (tx == -1 || ty == -1 || tx == bw || ty == bh) {
                    output[x * this.camHeight + y] = -1;
                }
                else if (tx < -1 || ty < -1 || tx > bw || ty > bh) 
                {
                    output[x * this.camHeight + y] = -1;
                } 
                else {
                    tile = this.board.get(tx, ty);
                    if (tile == null) {
                        output[x * this.camHeight + y] = 0;
                    } 
                    else if (tile instanceof Snake.Segment) {
                        output[x * this.camHeight + y] = -1;
                    }
                    else if (tile instanceof game.Food) {
                        output[x * this.camHeight + y] = 1;
                    }
                }


            }
        }

        return output;
    }

    public static void populateNextState() {
        DQNAgent temp;
        for (int i = 0; i < agentList.size(); i++) {
            temp = agentList.get(i);
            temp.nextState = temp.readBoard();
        }
    }

    public static void populateReward() {
        for (int i = 0; i < agentList.size(); i++) {
            agentList.get(i).setReward();
        }
    }

    private void setReward() {
        if (!this.snake.isAlive()) {
            this.reward = -20.0;
        } 
        else if ((double)this.snake.getLength() > this.lastLength) {
            this.reward = 5.0;
        }
        else {

            double xd = (this.snake.getX() - this.snake.getFood().getX());
            double yd = (this.snake.getY() - this.snake.getFood().getY());

            this.reward = 5.0 / (yd * yd + xd * xd) ;
        }

        this.lastLength = this.snake.getLength();        
    }

    @Override
    public Direction getMove() {
        
        double epsilonThreshold = epsilonFinish + (epsilonStart - epsilonFinish) * Math.exp(-trainingStepsDone/epsilonDecay);
        double d = r.nextDouble();

        this.state = readBoard();

        if (d < epsilonThreshold) {
            
            this.action = r.nextInt(4);
            switch (this.action) {
                case 0: 
                    return Direction.UP;
                case 1:
                    return Direction.RIGHT;
                case 2:
                    return Direction.DOWN;
                case 3:
                    return Direction.LEFT;
            }
        }


        

        double[] decision = agentNetwork.forward(this.state);

        

        if (this.print) {
            System.out.println(decision[0] + " " + decision[1] + " " + decision[2] + " " + decision[3]);
            //Output o = ((Output)agentNetwork.layers[agentNetwork.layers.length - 1]);
            //System.out.println(o.biases[0] + " " + o.biases[1] + " " + o.biases[2] + " " + o.biases[3] + "\n");
        }
        
        this.action = 0;

        for (int i = 0; i < decision.length; i++) {
            this.action = decision[i] > decision[this.action] ? i : this.action;
        }
        

        switch (this.action) {
            case 0: 
                return Direction.UP;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.DOWN;
            case 3:
                return Direction.LEFT;
            default:
                return Direction.UP;
        }
    }

    @Override
    public void linkSnake(Snake s) {
        this.snake = s;
    }

    @Override
    public void linkBoard(Board b) {
        this.board = b;        
    }
    
    public static double trainingStep() {

        trainingStepsDone++;

        Transition[] trainingSample = memory.sample(batchSize);


        for (Transition t : trainingSample) {
            
            

            double[] qPrime = agentNetwork.forward(t.nextState);
            double maxQPrime = qPrime[0];

            for (double i : qPrime) {
                maxQPrime = i > maxQPrime ? i : maxQPrime;
            }


            double[] q;
            if (t.nextState[t.state.length - 5] == 1) {
                q = agentNetwork.forward(t.state);
                q[t.action] = t.reward + maxQPrime * gamma;
            } else {

                agentNetwork.backward(new double[] {t.reward, t.reward, t.reward, t.reward});
                q = agentNetwork.forward(t.state);
                q[t.action] = t.reward;
            }
            
            
            agentNetwork.backward(q);
            
        }

        return 0.0;

    }

}
