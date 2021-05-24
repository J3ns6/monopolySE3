package com.hdm.monopoly.backend.player_money;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@Component("createPlayer")
public class CreatePlayers {
    private int playerNumber;
    private final Colours colours = new Colours();
    private Boolean isPartyFull = false;
    private final String[] SESSIONIDS = new String[4];
    private SimpMessagingTemplate messagingTemplate;
    private final Player[] players;
    private final SendMessage sendMessage;

    @Autowired
    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    public CreatePlayers(Player[] players, SendMessage sendMessage) {
        this.players = players;
        this.sendMessage = sendMessage;
    }

    /*
    gets the entered player name.
    Creates the player and sends the array of all players to the client.
    */
    @MessageMapping("/playerName")
    @SendTo("/client/playerList")
    public String addPlayer(Player message, @Header("simpSessionId") String sessionId)
            throws JsonProcessingException {

        if (playerNumber < 4) {

            SESSIONIDS[playerNumber] = (sessionId);

            players[playerNumber] = new Player(
                    playerNumber,
                    message.getName(),
                    colours.getColours(playerNumber)
            );

            playerNumber++;

            if (playerNumber == 4) {
                isPartyFull = true;
                playerXTurn();
//                notificationEvent();
            }
        }
        return new ObjectMapper().writeValueAsString(players);
    }

    public Player[] getPlayers() {
        return players;
    }

    /*
    sends a notification to all players, which player's turn it is now
    TODO message isn't sent away
     */
    @MessageMapping()
    @SendTo("/client/notification")
    private String notificationEvent() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString("Player" + getPlayers()[playerNumber - 1].getName() + " is on turn");
    }

    /*
    sends this message only to the player whose turn it is now, so that the buttons can be activated
     */
    public void playerXTurn() {
        sendMessage.sendToUser(SESSIONIDS[0], "/client/toggleDiceNumberBtn", "false");
    }

    //Define previous Player for everyone
    public void setPreviousPlayers() {
        for(int i = 0; i < playerNumber; i++){
            if(i!=0) {
                players[i].setPreviousPlayer(players[i-1]);
            }else{
                players[i].setPreviousPlayer(players[playerNumber -1]);
            }
        }
    }

    /*
    gets a message on successful connection and then tells the client if more players can join
    */
    @MessageMapping("/message")
    @SendToUser("/client/reply")
    public String processMessageFromClient() throws Exception {
        Thread.sleep(1000);

        return new ObjectMapper().writeValueAsString(isPartyFull);
    }
}
