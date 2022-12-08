import java.util.Random;

import game.*;
import snakedqn.*;
import janet.*;



class SnakeApp {


    public static double f(double x) {
        return 1;
    }

    public static void main(String[] args) {
		
        /*Random r = new Random();

        double[][] x = new double[100][1];

        double[][] y = new double[100][1];

        for (int i = 0; i < 100; i++) {
            x[i][0] = r.nextGaussian();
            y[i][0] = f(x[i][0]);
        }

        Layer[] l = {
            new Dense(1, 0, new RMSprop(0.1, 1)),
            new Dense(10, 1, new RMSprop(0.1, 1)),
            new Output(1, 10, new RMSprop(0.1, 1))
        };

        Network n = new Network(l);

        for (int i = 0; i < x.length; i++) {
            System.out.println(n.forward(x[i])[0]);
            n.backward(y[i]);
        }

        for (int i = 0; i < x.length; i++) {
            System.out.println(n.forward(x[i])[0]);
            n.backward(y[i]);
        }*/


        Setting s = new Setting();

        DQNAgent agent = new DQNAgent(21, 21, 5, 25, 0.003, 5000);

        s.setWidth(25);
        s.setHeight(25);
        s.addOperator(agent);
    
        Application app = new Application(s);
    
        int games = 2;


        while (true) {
            if (app.allDead() && !app.getBoard().isRunning()) {
                
                games++;

                if (games % 2000 == 0) {
                    agent.print = true;
                    app.getBoard().setUps(6);
                    agent.saveAgent("SnakeAgent.snake");
                } else if (games % 2000 == 1) {
                    agent.print = false;
                    app.getBoard().setUps(100000);
                }
                app.reset();


            }

        }

    }

}

