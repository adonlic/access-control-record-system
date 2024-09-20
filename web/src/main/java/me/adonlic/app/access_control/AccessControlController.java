package me.adonlic.app.access_control;

import me.adonlic.app.access_control.model.AccessPermission;
import me.adonlic.app.access_control.model.Card;
import me.adonlic.app.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/permissions")
public class AccessControlController {

    private final AccessControlService accessControlService;

    @Autowired
    public AccessControlController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    // TODO should use query parameter to distinct a variable type
    @GetMapping("/by-card-id/{cardId}")
    public List<AccessPermission> getAccessPermissionByCardId(@PathVariable Long cardId) {
        return accessControlService.getAccessPermissionByCardId(cardId);
    }

    // TODO should use query parameter to distinct a variable type
    @GetMapping("/by-card-number/{cardNO}")
    public List<AccessPermission> getAccessPermissionByCardNO(@PathVariable String cardNO) {
        return accessControlService.getAccessPermissionByCardNO(cardNO);
    }

    // TODO should use query parameter to distinct a variable type
    @GetMapping("/by-user-id/{cardId}")
    public List<AccessPermission> getAccessPermissionByUserId(@PathVariable Long userId) {
        return accessControlService.getAccessPermissionByUserId(userId);
    }

    @GetMapping("/cards")
    public List<Card> getAllCards() {
        return accessControlService.getCards();
    }

    @GetMapping("/cards/{id}")
    public Card getCard(@PathVariable Long id) {
        return accessControlService.getCard(id);
    }

    @PostMapping("/cards")
    public Card addCard(@RequestBody Card card) {
        return accessControlService.createCard(card);
    }

    @PutMapping("/cards/{id}")
    public Card addCard(@PathVariable Long id, @RequestBody Card card) {
        return accessControlService.updateCard(id, card);
    }

    @DeleteMapping("/cards/{id}")
    public void removeCard(@PathVariable Long id) {
        accessControlService.deleteCard(id);
    }
}
