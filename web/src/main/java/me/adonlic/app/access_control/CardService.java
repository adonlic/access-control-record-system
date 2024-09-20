package me.adonlic.app.access_control;

import me.adonlic.app.access_control.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final SimpMessagingTemplate template;

    @Autowired
    public CardService(CardRepository cardRepository, SimpMessagingTemplate template) {
        this.cardRepository = cardRepository;
        this.template = template;
    }

    public void swipeCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        template.convertAndSend("/topic/cards", card);
        System.out.println("Card swiped");
    }
}
