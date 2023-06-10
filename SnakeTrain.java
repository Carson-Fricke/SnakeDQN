import game.*;
import snakedqn.*;
import janet.*;


class SnakeTrain {

    public static void main(String[] args) {
		
        Setting s = new Setting(1000,100000);

        //DQNAgent agent = new DQNAgent(15, 15, 4, 120, 0.001, 3000);
        Network ln = DQNAgent.loadNetwork("SnakeAgentSave5.network");
        DQNAgent agent = new DQNAgent(ln, 15, 15);
        DQNAgent.train = true;
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
                    DQNAgent.saveNetwork("SnakeAgentSave.network");
                } else if (games % 2000 == 1) {
                    agent.print = false;
                    app.getBoard().setUps(1000000);
                }
                app.reset();

            }

        }

    }

}

