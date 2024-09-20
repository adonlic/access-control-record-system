package me.adonlic.app.access_control;

import me.adonlic.app.access_control.exception.CardNotFoundException;
import me.adonlic.app.access_control.model.AccessPermission;
import me.adonlic.app.access_control.model.Card;
import me.adonlic.app.controller.ControllerService;
import me.adonlic.app.controller.model.Controller;
import me.adonlic.app.controller.model.Door;
import me.adonlic.app.user.UserRepository;
import me.adonlic.app.user.UserService;
import me.adonlic.app.user.model.User;
import me.adonlic.communication.DefaultUDPClient;
import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.functions.implementations.access_privilege.AddAccessPrivilegeFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.QueryAccessPrivilegeByCardFunction;
import me.adonlic.uhppote.types.DoorNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccessControlService {
    private final AccessPermissionRepository accessPermissionRepository;
    private final AccessLogRepository accessLogRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final ControllerService controllerService;
    private final UserRepository userRepository;

    @Autowired
    public AccessControlService(
            AccessPermissionRepository accessPermissionRepository,
            AccessLogRepository accessLogRepository,
            CardRepository cardRepository,
            UserService userService,
            ControllerService controllerService,
            UserRepository userRepository) {
        this.accessPermissionRepository = accessPermissionRepository;
        this.accessLogRepository = accessLogRepository;
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.controllerService = controllerService;
        this.userRepository = userRepository;
    }

    public List<AccessPermission> getAccessPermissions() {
        return accessPermissionRepository.findAll();
    }

    private List<AccessPermission> getAccessPermissionsFromController(Controller controller, Card card) {
        QueryAccessPrivilegeByCardFunction.QueryAccessPrivilegeResponse queryAccessPrivilegeResponse = controllerService
                .queryAccessPrivilegeByCard(controller.getId(), Integer.parseInt(card.getCardNO()));
        List<AccessPermission> accessPermissions = new ArrayList<>();

        int i = 1;
        for (boolean isAllowed : queryAccessPrivilegeResponse.getDoorAccessRights()) {
            LocalDate dateNow = LocalDate.now();
            LocalDate dateIsAllowedFrom = LocalDate.parse(queryAccessPrivilegeResponse.getActivateDate());
            LocalDate dateIsAllowedTo = LocalDate.parse(queryAccessPrivilegeResponse.getDeactivateDate());

            if (isAllowed && dateNow.isAfter(dateIsAllowedFrom.minusDays(1))
                    && dateNow.isBefore(dateIsAllowedTo.plusDays(1))) {
                int finalI = i;
                controller.getDoors().forEach(door -> {
                    door.setDoorName(DoorNumber.fromInt(finalI).name()); // This is FIX
                    door.setDoorNumber(DoorNumber.fromInt(finalI).getValue()); // This is FIX
                    accessPermissions.add(new AccessPermission(door, card, true));
                });
            } else {
                controller.getDoors().forEach(door -> {
                    accessPermissions.add(new AccessPermission(door, card, false));
                });
            }

            i++;
        }

        return accessPermissions;
    }

    public List<AccessPermission> getAccessPermissionByCardId(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        List<AccessPermission> permissions = accessPermissionRepository.findAccessPermissionsByCardId(cardId);
        List<Controller> controllers = controllerService.getControllers();

        controllers.forEach(controller -> {
            List<AccessPermission> permissionsFromController = getAccessPermissionsFromController(controller, card);
            permissions.addAll(permissionsFromController); // TODO do not add, eliminate duplicates before add
        });

        return permissions;
    }

    public List<AccessPermission> getAccessPermissionByCardNO(String cardNO) {
        Card card = cardRepository.findCardByCardNO(cardNO);
        if (null == card) {
            throw new CardNotFoundException(cardNO);
        }
        List<AccessPermission> permissions = accessPermissionRepository.findAccessPermissionsByCardCardNO(cardNO);
        List<Controller> controllers = controllerService.getControllers();

        controllers.forEach(controller -> {
            List<AccessPermission> permissionsFromController = getAccessPermissionsFromController(controller, card);
            permissions.addAll(permissionsFromController); // TODO do not add, eliminate duplicates before add
        });

        return permissions;
    }

    public List<AccessPermission> getAccessPermissionByUserId(Long userId) {
        List<Card> cards = cardRepository.findCardsByUserId(userId);
        if (cards.isEmpty()) {
            throw new CardNotFoundException(); // TODO maybe another type of error?
        }
        // Take only the first card for single user, for now!!!
        List<AccessPermission> permissions = accessPermissionRepository
                .findAccessPermissionsByCardCardNO(cards.getFirst().getCardNO());
        List<Controller> controllers = controllerService.getControllers();

        controllers.forEach(controller -> {
            List<AccessPermission> permissionsFromController = getAccessPermissionsFromController(controller, cards.getFirst());
            permissions.addAll(permissionsFromController); // TODO do not add, eliminate duplicates before add
        });

        // return accessPermissionRepository.findAccessPermissionsByCardUserId(userId);
        return permissions;
    }

    public List<AccessPermission> getAccessPermissionByDoorId(Long doorId) {
        return accessPermissionRepository.findAccessPermissionsByCardUserId(doorId);
    }

    public boolean userIsAllowed(Long userId, Long controllerId) {
        User user = userService.getUser(userId);
        Controller controller = controllerService.getController(controllerId);
        // con

        // if (user.getCards().stream().anyMatch(card -> {
        //     controller.
        // }))

        // controllerService.queryAccessPrivilegeByCard()
        // see if controller service can tell what controller allows him
        return false;
    }

    /*public AddAccessPrivilegeFunction.AddAccessPrivilegeResponse addAccessPrivilege() {
        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
        } catch (IOException e) {

        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }*/

    public List<Card> getCards() {
        return cardRepository.findAll();
    }

    public Card getCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    public Card updateCard(Long id, Card card) {
        cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardRepository.save(card);
    }

    public void deleteCard(Long id) {
        cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        cardRepository.deleteById(id);
    }
}
