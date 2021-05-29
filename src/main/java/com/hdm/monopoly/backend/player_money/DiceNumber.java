package com.hdm.monopoly.backend.player_money;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdm.monopoly.Game;
import com.hdm.monopoly.backend.di.GameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@Component("diceNumber")
public class DiceNumber {

    private final Player[] players;
    private final Game game;

    @Autowired
    public DiceNumber(Player[] players, Game game) {
        this.players = players;
        this.game = game;
    }


    /*
    get message, if player clicked on button
    and deactivate the button.
     */
    @MessageMapping("/diceNumberBtnClicked")
    @SendToUser("/client/toggleDiceNumberBtn")
    public String addPlayer() throws JsonProcessingException {
        int diceNumber = diceRandomNumber();    //maybe to display the result of the dice
        game.movePlayer(game.getCurrentPlayer(), diceNumber);

        game.endOfTurn();//maybe not the best moment to change the current player

        return new ObjectMapper().writeValueAsString(true);
    }

    public int diceRandomNumber() {
        return (int)(Math.random()*5)+1;
    }
}
