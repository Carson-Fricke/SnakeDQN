import game.*;
import snakedqn.*;
import janet.*;


class SnakeTrain {

    public static final boolean newModel = false;
    public static final String savePath = "SnakeAgentSave.network";
    public static final int gamesPerSave = 500;
    public static final boolean demonstrate = false;



    public static void main(String[] args) {
		
        Setting s = new Setting(1000,100000);
        
        DQNAgent agent;
        if (newModel) {
            agent = new DQNAgent(17, 17, 5, 120, 0.001);
        } else {
            Network ln = DQNAgent.loadNetwork("SnakeAgentSave.network");
            agent = new DQNAgent(ln, 17, 17);
        }
        
        
        DQNAgent.train = true;
        s.setWidth(25);
        s.setHeight(25);
        s.addOperator(agent);
    
        Application app = new Application(s);
    
        int games = 2;

        while (true) {
            if (app.allDead() && !app.getBoard().isRunning()) {
                
                games++;

                if (games % gamesPerSave == 0) {
                    if (demonstrate) {
                        agent.print = true;
                        app.getBoard().setUps(6);
                    }
                    DQNAgent.saveNetwork(savePath);
                    System.out.println("Agent Saved");
                } else if (games % gamesPerSave == 1) {
                    agent.print = false;
                    app.getBoard().setUps(1000000);
                }
                app.reset();

            }

        }

    }

}

