import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        char color;
        int optCol = randomMove();
        int colNum = myGame.getColumnCount();
        int[] winIndex = new int[colNum];
        char[][] board = myGame.getBoardMatrix();
        
        if (iAmRed)
        {
            color = 'R';
        }
        else
        {
            color = 'Y';
        }
        
        for (int i = 2; i<myGame.getColumnCount();i++)
        {
           winIndex[i] = checkLeft(i, color, board)*checkLeft(i, color, board) + checkRight(i, color, board)*checkRight(i, color, board) + checkUpLeft(i, color, board)*checkUpLeft(i, color, board)
           + checkUpRight(i, color, board)*checkUpRight(i, color, board) + checkBottom(i, color, board)*checkBottom(i, color, board)  
           + checkBottomLeft(i, color, board)*checkBottomLeft(i, color, board) + checkBottomRight(i, color, board)*checkBottomRight(i, color, board);
        }
        
       
        // find optimal column
        if (iCanWin() != -1)
        {
            optCol = iCanWin();
        } 
        else if (theyCanWin() != -1)
        {
            optCol = theyCanWin();
        } 
        else
        {
            for (int i = 0; i < myGame.getColumnCount(); i++)
            {
                if (winIndex[optCol] < winIndex[i])
                {
                    optCol = i;
                }
            }
        }
        
        //optCol = this.randomMove(); 
        
        this.moveOnColumn(optCol);
        

    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Check the left few slots and return how many adjacent slots are in same color
     * 
     * @return adjacent slots in certain color, which are to the left
     */
    public int checkLeft(int i, char color, char[][] board)
    {
        int numInSameColor = 0;
        int confine = 3;
        int lowestSpace = getLowestEmptyIndex(myGame.getColumn(i));
        
        if (i == 0 || myGame.getColumn(i).getIsFull())
        {
            return 0;
        }
        else if (i < 3)
        {
            confine = i;
        }
         
        for (int step = 1; step<=confine; step++)
        {
            if (board[lowestSpace][i-step] == color)
            {
                numInSameColor++;
            } 
            else
            {
                step = 4;
            }
        }

        return numInSameColor;
    }

    /**
     * Check the right few slots and return how many adjacent slots are in same color
     * 
     * @return adjacent slots in certain color, which are to the right
     */
    public int checkRight(int i, char color, char[][] board)
    {
        int numInSameColor = 0;
        int confine = 3;
        int lowestSpace = getLowestEmptyIndex(myGame.getColumn(i));
        
        if (i == myGame.getColumnCount()-1 || myGame.getColumn(i).getIsFull())
        {
            return 0;
        }
        else if (i > myGame.getColumnCount()-4)
        {
            confine = myGame.getColumnCount()-1-i;
        }
         
        for (int step = 1; step<=confine; step++)
        {
            if (board[lowestSpace][i+step] == color)
            {
                numInSameColor++;
            } 
            else
            {
                step = 4;
            }
        }

        return numInSameColor;
    }    

    /**
     * Check the bottom few slots and return how many adjacent slots are in same color
     * 
     * @return adjacent slots in certain color, which are to the bottom
     */
    public int checkBottom(int i, char color, char[][] board)
    {
        int lowestSpace = getLowestEmptyIndex(myGame.getColumn(i));
        int numInSameColor = 0;
        int confine = 3;
        
        if (lowestSpace == myGame.getRowCount()-1 || myGame.getColumn(i).getIsFull())
        {
            return 0;
        }
        else if (lowestSpace > myGame.getRowCount()-4)
        {
            confine = myGame.getRowCount()-1-lowestSpace;
        }
         
        for (int step = 1; step<=confine; step++)
        {
            if (board[lowestSpace+step][i] == color)
            {
                numInSameColor++;
            } 
            else
            {
                step = 4;
            }
        }

        return numInSameColor;
    }
    
    /**
     * Check the bottom-left few slots and return how many adjacent slots are in same color
     * 
     * @return adjacent slots in certain color, which are to the bottom-left
     */
    public int checkBottomLeft(int i, char color, char[][] board)
    {
        int lowestSpace = getLowestEmptyIndex(myGame.getColumn(i));
        int numInSameColor = 0;
        int confine = 3;
        
        if (i == 0 || lowestSpace == myGame.getRowCount()-1 || myGame.getColumn(i).getIsFull())
        {
            return 0;
        }
        else if (i < 3 || lowestSpace > myGame.getRowCount()-4)
        {
            if (i < myGame.getRowCount()-1-lowestSpace)
            {
                confine = i;
            } 
            else
            {
                confine = myGame.getRowCount()-1-lowestSpace;
            }
        }
         
        for (int step = 1; step<=confine; step++)
        {
            if (board[lowestSpace+step][i-step] == color)
            {
                numInSameColor++;
            } 
            else
            {
                step = 4;
            }
        }

        return numInSameColor;
    }
    
    /**
     * Check the bottom-right few slots and return how many adjacent slots are in same color
     * 
     * @return adjacent slots in certain color, which are to the bottom-right
     */
    public int checkBottomRight(int i, char color, char[][] board)
    {
        int lowestSpace = getLowestEmptyIndex(myGame.getColumn(i));
        int numInSameColor = 0;
        int confine = 3;
        
        if (i == myGame.getColumnCount()-1 || lowestSpace == myGame.getRowCount()-1 || myGame.getColumn(i).getIsFull())
        {
            return 0;
        }
        else if (i > myGame.getColumnCount()-4 || lowestSpace > myGame.getRowCount()-4)
        {
            if (myGame.getColumnCount()-1-i < myGame.getRowCount()-1-lowestSpace)
            {
                confine = myGame.getColumnCount()-1-i;
            } 
            else
            {
                confine = myGame.getRowCount()-1-lowestSpace;
            }
        }
         
        for (int step = 1; step<=confine; step++)
        {
            if (board[lowestSpace+step][i+step] == color)
            {
                numInSameColor++;
            } 
            else
            {
                step = 4;
            }
        }

        return numInSameColor;
    }
    
    /**
     * Check the up-left few slots and return how many adjacent slots are in same color
     * 
     * @return adjacent slots in certain color, which are to the up-left
     */
    public int checkUpLeft(int i, char color, char[][] board)
    {
        int lowestSpace = getLowestEmptyIndex(myGame.getColumn(i));
        int numInSameColor = 0;
        int confine = 3;
        
        if (i == 0 || lowestSpace == 0 || myGame.getColumn(i).getIsFull())
        {
            return 0;
        }
        else if (i < 3 || lowestSpace < 3)
        {
            if (i < lowestSpace)
            {
                confine = i;
            } 
            else
            {
                confine = lowestSpace;
            }
        }
         
        for (int step = 1; step<=confine; step++)
        {
            if (board[lowestSpace-step][i-step] == color)
            {
                numInSameColor++;
            } 
            else
            {
                step = 4;
            }
        }

        return numInSameColor;
    }
    
    /**
     * Check the up-right few slots and return how many adjacent slots are in same color
     * 
     * @return adjacent slots in certain color, which are to the up-right
     */
    public int checkUpRight(int i, char color, char[][] board)
    {
        int lowestSpace = getLowestEmptyIndex(myGame.getColumn(i));
        int numInSameColor = 0;
        int confine = 3;
        
        if (i == myGame.getColumnCount()-1 || lowestSpace ==0 || myGame.getColumn(i).getIsFull())
        {
            return 0;
        }
        else if (i > myGame.getColumnCount()-4 || lowestSpace < 3)
        {
            if (myGame.getColumnCount()-1-i < lowestSpace)
            {
                confine = myGame.getColumnCount()-1-i;
            } 
            else
            {
                confine = lowestSpace;
            }
        }
         
        for (int step = 1; step<=confine; step++)
        {
            if (board[lowestSpace-step][i+step] == color)
            {
                numInSameColor++;
            } 
            else
            {
                step = 4;
            }
        }

        return numInSameColor;
    }
    
    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin()
    {
        char targetColor;
        char[][] board = myGame.getBoardMatrix();
        
        if (iAmRed)
        {
            targetColor = 'R';
        }
        else
        {
            targetColor = 'Y';
        }
    
        for (int i = 0; i< myGame.getColumnCount(); i++)
        {
            if (checkLeft(i, targetColor, board)==3 || checkRight(i, targetColor, board)==3 || (checkLeft(i, targetColor, board)==2 && checkRight(i, targetColor, board)>=1)
            || (checkRight(i, targetColor, board)==2 && checkLeft(i, targetColor, board)>=1))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin() 
    {
        char targetColor;
        char[][] board = myGame.getBoardMatrix();
        
        if (iAmRed)
        {
            targetColor = 'Y';
        }
        else
        {
            targetColor = 'R';
        }
        
        for (int i = 0; i< myGame.getColumnCount(); i++)
        {
            if (checkLeft(i, targetColor, board)==3 || checkRight(i, targetColor, board)==3 || (checkLeft(i, targetColor, board)==2 && checkRight(i, targetColor, board)>=1)
            || (checkRight(i, targetColor, board)==2 && checkLeft(i, targetColor, board)>=1) || checkBottom(i, targetColor, board)==3 || checkUpLeft(i, targetColor, board)==3 
            || checkUpRight(i, targetColor, board)==3 || checkBottomLeft(i, targetColor, board)==3 || checkBottomRight(i, targetColor, board)==3 
            || (checkUpLeft(i, targetColor, board)==2 && checkBottomRight(i, targetColor, board)>=1) || (checkUpLeft(i, targetColor, board)>=1 && checkBottomRight(i, targetColor, board)==2)
            || (checkBottomLeft(i, targetColor, board)==2 && checkUpRight(i, targetColor, board)>=1) || (checkBottomLeft(i, targetColor, board)>=1 && checkUpRight(i, targetColor, board)==2))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }
}
