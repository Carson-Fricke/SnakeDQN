SnakeDQN
-------------

Snake Deep Q Network (DQN) is an artificial intelligence agent capable of playing the classic game of Snake using reinforcement learning techniques, all built from scratch in Java. The project involves training a neural network to approximate the Q-values, which represent the expected rewards of taking specific actions as a function of state. The DQN agent learns to navigate the game environment, avoiding obstacles and collecting food, by interacting with the game and updating its Q-values through a combination of exploration and exploitation over many iterations.
## Getting Started

Simply download the code and run the demo to see a pretrained model in action.

To train a new model

## Implementation
The neural network is built on top naive implementations of a linear algebra operations found in Util.java. It only supports the ReLU activation function out of the box. The output layer has a linear output, IE no activation function because this allows for more flexibility with the reward function described later. 

As for gradient descent optimization, this uses standard minibatch gradient descent. RMSprop performed poorly for small gradients since rewards can be sparse in this environment, and ADAM was susecpitable to beta decay over large training times, which are neccessary in sparse environments like snake.

The DQN agent "sees" the board as a 2D array (flattened to one dimention) rotated to match the agent's direction. This allows the agent to be direction agnostic, and focus on learning how best to play the game by adding symmetry, rather than determining rules based on direction.

The DQN is rewarded whenever it eats a food, and is punished whenever it dies. It recieves a smaller reward when moving towards the food, and a small punishment when moving away from it. 

