import game.*;
import snakedqn.*;
import janet.*;


class SnakeDemo {

    public static final String loadPath = "SnakeAgentSave.network";

    public static void main(String[] args) {

        Setting s = new Setting(100, 6);
        Network agentNet = DQNAgent.loadNetwork(loadPath);
        DQNAgent agent = new DQNAgent(agentNet, 17, 17);
        DQNAgent.train = false;
        s.setWidth(25);
        s.setHeight(25);
        s.addOperator(agent);
        

        Application app = new Application(s);
    
        app.getBoard().setUps(6);

        while (true) {
            if (app.allDead() && !app.getBoard().isRunning()) {
                app.getBoard().setUps(6);

                agent.print = true;
                
                app.reset();
                
            }

        }

    }

}

