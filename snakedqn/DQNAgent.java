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
    private int camWidth;
    private int camHeight;
    
    public static Network agentNetwork;
    public static Network previousNetwork;

    public static boolean train = true;
    private static Random r = new Random(15);
    private static ArrayList<DQNAgent> agentList = new ArrayList<DQNAgent>();
    private static ReplayMemory memory = new ReplayMemory();

    private static int trainingStepsDone = 0;
    private static double epsilonStart = 0.5;
    private static double epsilonFinish = 0.05;
    private static double epsilonDecay = 30;
    private static int batchSize = 10000;

    private static int sampleSize = 10000;
    // keep low
    private static int networkUpdateDelay = 5;

    // VERY IMPORTANT - don't set too high, this is the lookahead rate
    private static double gamma = 0.3;

    public boolean print = false;

    private double[] state;
    private int action;
    private double[] nextState;
    private double reward;
    private boolean wasAlive = true;
    private boolean ateFood = false;

    public static void saveNetwork(String savePath) {
        try {
			FileOutputStream f = new FileOutputStream(savePath);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(agentNetwork);
			o.close();
			f.close();
		} catch (Exception e) {	
		}
    }

    public static Network loadNetwork(String loadPath) {
        Network out = null;
		try {
			FileInputStream f = new FileInputStream(loadPath);
			ObjectInputStream o = new ObjectInputStream(f);
			out = (Network) o.readObject();
			o.close();
			f.close();
		} catch (Exception e) {
		}

		return out;
    }

    public void saveAgent(String savePath) {
		try {
			FileOutputStream f = new FileOutputStream(savePath);
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
			FileInputStream f = new FileInputStream(loadPath);
			ObjectInputStream o = new ObjectInputStream(f);
			out = (DQNAgent) o.readObject();
			o.close();
			f.close();
		} catch (Exception e) {
            System.out.println(e);
		}

		return out;
	}


    // for all intents and purposes, camX and camY ought to be odd numbers so the snakes head in in the middle
    public DQNAgent(int camX, int camY, int numHiddenLayers, int layerWidth, double eta) {
        this.camWidth = camX;
        this.camHeight = camY;

        
        Layer[] layers = new Layer[numHiddenLayers+2];
        layers[0] = new Dense(camX * camY + 5, 0, new SGD(eta, batchSize));
        
        int lastLayerWidth = camX * camY + 5;
        
        for (int i = 1; i <= numHiddenLayers; i++) {
            layers[i] = new Dense(layerWidth, lastLayerWidth, new SGD(eta, batchSize));
            lastLayerWidth = layerWidth;
        }
        layers[numHiddenLayers + 1] = new Output(3, lastLayerWidth, new SGD(eta, batchSize));

        agentNetwork = new Network(layers);

        agentList.add(this);
    } 

    public DQNAgent(Network agent, int camX, int camY) {
        agentNetwork = agent;
        agentList.add(this);

        agentNetwork.setBatchSize(batchSize);

        this.camWidth = camX;
        this.camHeight = camY;
    }

    public DQNAgent(String loadPath, int camX, int camY) {
        agentNetwork = Network.loadNetwork(loadPath);
        agentList.add(this);
        
        agentNetwork.setBatchSize(batchSize);

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

    // in perfect working order atm
    public double[] readBoard() {

        // snake head width and height
        int sx = this.snake.getX();
        int sy = this.snake.getY();

        // board width and height
        int bw = this.board.width();
        int bh = this.board.height();

        // true x and y, adjusted for snake head position
        int tx = 0;
        int ty = 0;
        
        Tile tile;
        
        // here, -1 will represent a wall or snake body, 0 will represent empty space, and 1 will represent food.
        // this will keep the input small, and ought to have the nice inverse behavior of "Snake goes away from walls and towards food"
        double[] output = new double[this.camWidth * this.camHeight + 5];
        
        output[output.length - 1] = sx / bw;
        output[output.length - 2] = sy / bh;
        output[output.length - 3] = this.snake.getFood().getX() / bw;
        output[output.length - 4] = this.snake.getFood().getY() / bh;
        output[output.length - 5] = this.snake.isAlive() ? 1.0 : -1.0;
        
        Direction d = this.snake.getDirection();

        for (int y = 0; y < this.camHeight; y++) {
            for (int x = 0; x < this.camWidth; x++) {

                if (d == Direction.UP) {
                    tx = x + sx - camWidth/2;
                    ty = y + sy - camHeight/2;
                }
                else if (d == Direction.DOWN) {
                    tx = -x + sx + camWidth/2;
                    ty = -y + sy + camHeight/2;
                }
                else if (d == Direction.LEFT) {
                    tx = y + sx - camWidth/2;
                    ty = -x + sy + camHeight/2;
                }
                else if (d == Direction.RIGHT) {
                    tx = -y + sx + camWidth/2;
                    ty = x + sy - camHeight/2;
                }
                
                
                if (tx == -1 || ty == -1 || tx == bw || ty == bh) {
                    output[x + this.camWidth * y] = -1;
                }
                else if (tx < -1 || ty < -1 || tx > bw || ty > bh) 
                {
                    output[x + this.camWidth * y] = -1;
                } else {
                    tile = this.board.get(tx, ty);
                    if (tile == null) {
                        output[x + this.camWidth * y] = 0;
                    } 
                    else if (tile instanceof Snake.Segment) {
                        output[x + this.camWidth * y] = -1;
                    }
                    else if (tile instanceof game.Food) {
                        output[x + this.camWidth * y] = 5;
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

    public static void addTransition() {
        for (DQNAgent i : agentList) {
            if (i.wasAlive) {
                memory.add(i.state, i.action, i.nextState, i.reward);
            }
            i.wasAlive = i.getSnake().isAlive();
        }

        if (train) {

            if (memory.getLength() > sampleSize) {
                trainingStep();
                System.out.println("Training step: " + trainingStepsDone);
            }

        }
        
    }


    private void setReward() {
        if (!this.snake.isAlive()) {
            this.reward = -30.0;
        } 
        else if (this.ateFood) {
            this.reward = 15.0;
        }
        else {

            double xd = (this.snake.getFood().getX() - this.snake.getX());
            double yd = (this.snake.getFood().getY() - this.snake.getY());
            Direction d = this.snake.getDirection();
            switch (d) 
            {
                case UP:
                    if (yd < 0) {
                        this.reward = 0.9;
                        return;
                    }
                case DOWN:
                    if (yd > 0) {
                        this.reward = 0.9;
                        return;
                    }
                case LEFT:
                    if (xd < 0) {
                        this.reward = 0.9;
                        return;
                    }
                case RIGHT:
                    if (xd > 0) {
                        this.reward = 0.9;
                        return;
                    }
                this.reward = -1.1;
            }

        }      
    }

    @Override
    public Direction getMove() {
        this.ateFood = false;
        double epsilonThreshold = epsilonFinish + (epsilonStart - epsilonFinish) * Math.exp(-trainingStepsDone/epsilonDecay);
        double d = r.nextDouble();

        this.state = readBoard();

        if (train && d < epsilonThreshold) {
            
            this.action = r.nextInt(3);
            switch (this.action) {
                case 0: 
                    return this.snake.getDirection().getLeft();
                case 1:
                    return this.snake.getDirection();
                case 2:
                    return this.snake.getDirection().getRight();
            }
        }

        double[] decision = agentNetwork.forward(this.state);

        if (this.print) {
            System.out.println(decision[0] + " " + decision[1] + " " + decision[2]);
            System.out.println(this.snake.getDirection());
            String bs = "";
            for (int y = 0; y < this.camHeight; y++) {
                for (int x = 0; x < this.camWidth; x++) {
                    bs += (this.state[x + this.camWidth * y]) >= 0 ? (" " + this.state[x + this.camWidth * y]) : (this.state[x + this.camWidth * y]);
                    bs += " ";
                }
                bs += "\n";
            }
            System.out.println(bs);
        }
        
        this.action = 0;

        for (int i = 0; i < decision.length; i++) {
            this.action = decision[i] > decision[this.action] ? i : this.action;
        }
        

        switch (this.action) {
            case 0: 
                return this.snake.getDirection().getLeft();
            case 1:
                return this.snake.getDirection();
            case 2:
                return this.snake.getDirection().getRight();
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
        
        if (trainingStepsDone % networkUpdateDelay == 0) {
            saveNetwork("previousNetwork");
            previousNetwork = loadNetwork("previousNetwork");
        }

        trainingStepsDone++;

        Transition[] trainingSample = memory.sample(sampleSize);


        for (Transition t : trainingSample) {
            
            double[] q;
            if (t.nextState[t.state.length - 5] == 1) {

                double[] qPrime = previousNetwork.forward(t.nextState);
                double maxQPrime = qPrime[0];
            
                for (double i : qPrime) {
                    maxQPrime = (i > maxQPrime) ? i : maxQPrime;
                }

                q = agentNetwork.forward(t.state);
                q[t.action] = t.reward + maxQPrime * gamma;
                agentNetwork.backward(q);
            } else {
                q = agentNetwork.forward(t.state);
                q[t.action] = t.reward;
                agentNetwork.backward(q);
            }
            
        }

        return 0.0;
    }

    @Override
    public void onFood() 
    {
        this.ateFood = true;
    }

}
