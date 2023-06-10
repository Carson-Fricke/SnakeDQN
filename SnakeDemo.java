import game.*;
import snakedqn.*;
import janet.*;


class SnakeDemo {


    public static void main(String[] args) {

        Setting s = new Setting(100, 6);
        Network agentNet = DQNAgent.loadNetwork("SnakeAgentSave5.network");
        DQNAgent agent = new DQNAgent(agentNet, 15, 15);
        //DQNAgent agent = new DQNAgent(15, 15, 4, 120, 0.001, 3000);
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

