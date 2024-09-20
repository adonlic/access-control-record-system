package me.adonlic.app.access_control.ws;

import me.adonlic.app.access_control.CardService;
import me.adonlic.app.access_control.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequestMapping("/api/cards")
public class CardWebSocketController {
    private final CardService cardService;

    @Autowired
    public CardWebSocketController(CardService cardService) {
        this.cardService = cardService;
    }

    // When the card is passed, this endpoint will send a message to the "/topic/cards"
    @MessageMapping("/card")  // WebSocket endpoint where the message comes in
    @SendTo("/topic/cards")   // Broadcasts to all clients subscribed to "/topic/cards"
    public Card sendCard(Card card) throws Exception {
        return card;
    }
}
