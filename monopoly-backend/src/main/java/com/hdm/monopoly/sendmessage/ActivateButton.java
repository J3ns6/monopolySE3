package com.hdm.monopoly.sendmessage;

import com.hdm.monopoly.gameplay.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivateButton {
    private static final Logger log = LogManager.getLogger(ActivateButton.class);

    private final SendMessage sendMessage;
    private final String[] sessionIds;
    private final Game game;

    @Autowired
    public ActivateButton(SendMessage sendMessage, String[] sessionIds, Game game) {
        this.sendMessage = sendMessage;
        this.sessionIds = sessionIds;
        this.game = game;
        log.info("New Object 'ActivateButton' created");
    }

    public void diceNumber() {
        sendMessage.sendToPlayer(sessionIds[game.getCurrentPlayerIndex()], "/client/toggleDiceNumberBtn", "false" );
        log.info("Dicenumber button is now activ");
    }

    public void buyEstate() {
        sendMessage.sendToPlayer(sessionIds[game.getCurrentPlayerIndex()], "/client/toggleBuyEstateBtn", "false" );
        log.info("Buy estate button is now activ");
    }

    public void nextPlayer() {
        sendMessage.sendToPlayer(sessionIds[game.getCurrentPlayerIndex()], "/client/toggleNextPlayerBtn", "false" );
        log.info("Next player button is now activ");
    }
}
