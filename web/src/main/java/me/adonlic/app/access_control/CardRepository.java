package me.adonlic.app.access_control;

import me.adonlic.app.access_control.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    public Card findCardByCardNO(String cardNO);
    public List<Card> findCardsByUserId(Long id); // TODO there are more cards per user
}
