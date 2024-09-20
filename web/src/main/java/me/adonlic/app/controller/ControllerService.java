package me.adonlic.app.controller;

import me.adonlic.app.controller.exception.ControllerAlreadyExistsException;
import me.adonlic.app.controller.exception.ControllerFunctionExecutionFailedException;
import me.adonlic.app.controller.exception.ControllerNotFoundException;
import me.adonlic.app.controller.model.Controller;
import me.adonlic.app.controller.model.Door;
import me.adonlic.communication.DefaultUDPClient;
import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.implementations.*;
import me.adonlic.uhppote.functions.implementations.access_privilege.AddAccessPrivilegeFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.ClearAllAccessPrivilegesFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.DeleteAccessPrivilegeFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.QueryAccessPrivilegeByCardFunction;
import me.adonlic.uhppote.functions.implementations.record.GetRecordByIndexFunction;
import me.adonlic.uhppote.functions.implementations.record.GetRecordIndexFunction;
import me.adonlic.uhppote.functions.implementations.record.SetRecordIndexFunction;
import me.adonlic.uhppote.types.DoorNumber;
import me.adonlic.uhppote.types.DoorStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ControllerService {
    private final ControllerRepository controllerRepository;

    @Autowired
    public ControllerService(ControllerRepository controllerRepository) {
        this.controllerRepository = controllerRepository;
    }

    private Controller loadControllerWithFullData(Controller controller) {
        QueryControllerFunction.QueryControllerResponse queryControllerResponse = queryController(
                controller.getId()
        );
        if (controller.getDoors().isEmpty()) {
            DoorNumber door1 = queryControllerResponse.getDoorNumber();
            DoorStatus door1Status = queryControllerResponse.getDoorStatuses()[0];
            controller.getDoors().add(new Door(door1.name(), door1.getValue(), controller));
            controller.getDoors().getFirst().setDoorStatus(door1Status);
            // controller.getDoors().getFirst().setDoorStatus(queryControllerResponse.getDoorStatuses()[0]);
        }

        // Door door = new Door(DoorNumber.DOOR_1);
        // door.setDoorStatus(queryControllerResponse.getDoorStatuses()[0]);
        // controller.setDoors(List.of(door));

        return controller;
    }

    public List<Controller> getControllers() {
        // return controllerRepository.findAll().stream().map(this::loadControllerWithFullData).toList();
        return controllerRepository.findAll();
    }

    public Controller getController(Long id) {
        // return controllerRepository.findById(id).map(this::loadControllerWithFullData)
        //         .orElseThrow(() -> new ControllerNotFoundException(id));
        return controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));
    }

    public Controller createController(Controller controller) {
        boolean exists = controllerRepository.existsByControllerSN(controller.getControllerSN());

        if (exists) {
            throw new ControllerAlreadyExistsException(controller.getControllerSN());
        }

        return controllerRepository.save(controller);
    }

    public Controller updateController(Long id, Controller controller) {
        controllerRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException(id));
        return controllerRepository.save(controller);
    }

    public void deleteController(Long id) {
        controllerRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException(id));
        controllerRepository.deleteById(id);
    }

    public List<SearchFunction.SearchResponse> searchInNetwork() {
        List<SearchFunction.SearchResponse> controllers = new ArrayList<>();

        SearchFunction searchFunction = new SearchFunction();
        byte[] searchFunctionBinary = searchFunction.toPacket().getRawData();
        try {
            DefaultUDPClient.broadcastAndReceive(
                    searchFunctionBinary,
                    Controller.PORT,
                    5000, // Search for 5 seconds
                    (data) -> {
                        ProtocolPacket protocolPacket = new ProtocolPacket(data);
                        SearchFunction.SearchResponse searchResponse = searchFunction.fromPacket(protocolPacket);
                        controllers.add(searchResponse);
                    }
            );

            return controllers;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, SearchFunction.class.getSimpleName());
        }
    }

    public QueryControllerFunction.QueryControllerResponse queryController(Long id) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            QueryControllerFunction queryControllerFunction = new QueryControllerFunction(
                    Integer.parseInt(controller.getControllerSN())
            );

            return queryControllerFunction.execute(udpClient);
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, QueryControllerFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public void remoteOpenDoor(Long id, Integer doorNumber) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            OpenDoorFunction openDoorFunction = new OpenDoorFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    doorNumber
            );
            openDoorFunction.execute(udpClient);
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, OpenDoorFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public QueryAccessPrivilegeByCardFunction.QueryAccessPrivilegeResponse queryAccessPrivilegeByCard(
            Long id,
            int cardNumber
    ) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            QueryAccessPrivilegeByCardFunction queryAccessPrivilegeByCardFunction = new QueryAccessPrivilegeByCardFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    cardNumber
            );
            QueryAccessPrivilegeByCardFunction.QueryAccessPrivilegeResponse queryAccessPrivilegeByCardResponse = queryAccessPrivilegeByCardFunction
                    .execute(udpClient);

            return queryAccessPrivilegeByCardResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, QueryAccessPrivilegeByCardFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public AddAccessPrivilegeFunction.AddAccessPrivilegeResponse addAccessPrivilege(
            Long id,
            Integer cardNumber,
            LocalDate activateDate,
            LocalDate deactivateDate,
            boolean door1AccessRight
    ) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            AddAccessPrivilegeFunction addAccessPrivilegeFunction = new AddAccessPrivilegeFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    cardNumber,
                    // REF: https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
                    activateDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                    deactivateDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                    new boolean[]{door1AccessRight, false, false, false}
            );
            AddAccessPrivilegeFunction.AddAccessPrivilegeResponse addAccessPrivilegeResponse = addAccessPrivilegeFunction
                    .execute(udpClient);

            return addAccessPrivilegeResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, AddAccessPrivilegeFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public DeleteAccessPrivilegeFunction.DeleteAccessPrivilegeResponse deleteAccessPrivilege(
            Long id,
            Integer cardNumber
    ) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            DeleteAccessPrivilegeFunction addAccessPrivilegeFunction = new DeleteAccessPrivilegeFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    cardNumber
            );
            DeleteAccessPrivilegeFunction.DeleteAccessPrivilegeResponse deleteAccessPrivilegeResponse = addAccessPrivilegeFunction
                    .execute(udpClient);

            return deleteAccessPrivilegeResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, DeleteAccessPrivilegeFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public ClearAllAccessPrivilegesFunction.ClearAllAccessPrivilegesResponse clearAllAccessPrivileges(
            Long id
    ) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            ClearAllAccessPrivilegesFunction clearAllAccessPrivilegesFunction = new ClearAllAccessPrivilegesFunction(
                    Integer.parseInt(controller.getControllerSN())
            );
            ClearAllAccessPrivilegesFunction.ClearAllAccessPrivilegesResponse clearAllAccessPrivilegesResponse = clearAllAccessPrivilegesFunction
                    .execute(udpClient);

            return clearAllAccessPrivilegesResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, ClearAllAccessPrivilegesFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public GetRecordIndexFunction.GetRecordIndexResponse getRecordIndex(
            Long id
    ) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            GetRecordIndexFunction getRecordIndexFunction = new GetRecordIndexFunction(
                    Integer.parseInt(controller.getControllerSN())
            );
            GetRecordIndexFunction.GetRecordIndexResponse getRecordIndexResponse = getRecordIndexFunction
                    .execute(udpClient);

            return getRecordIndexResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, GetRecordIndexFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public GetRecordByIndexFunction.GetRecordResponse getRecord(Long id, Integer recordIndex) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            GetRecordByIndexFunction getRecordByIndexFunction = new GetRecordByIndexFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    recordIndex
            );
            GetRecordByIndexFunction.GetRecordResponse getRecordByIndexResponse = getRecordByIndexFunction
                    .execute(udpClient);

            return getRecordByIndexResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, GetRecordByIndexFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public SetRecordIndexFunction.SetRecordIndexResponse setRecordIndex(Long id, Integer recordIndex) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            SetRecordIndexFunction setRecordIndexFunction = new SetRecordIndexFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    recordIndex
            );
            SetRecordIndexFunction.SetRecordIndexResponse setRecordIndexResponse = setRecordIndexFunction
                    .execute(udpClient);

            return setRecordIndexResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, GetRecordByIndexFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public void configureIP(Long id, String controllerIP, String controllerMask, String controllerGateway) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            ConfigureIPFunction configureIPFunction = new ConfigureIPFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    InetAddress.getByName(controllerIP),
                    InetAddress.getByName(controllerMask),
                    InetAddress.getByName(controllerGateway)
            );

            configureIPFunction.execute(udpClient);
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, GetRecordByIndexFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }

    public SetTimeFunction.SetTimeResponse setTime(Long id) {
        Controller controller = controllerRepository.findById(id)
                .orElseThrow(() -> new ControllerNotFoundException(id));

        UDPClient udpClient = null;
        try {
            udpClient = new DefaultUDPClient(controller.getIpAddress(), Controller.PORT);
            SetTimeFunction setTimeFunction = new SetTimeFunction(
                    Integer.parseInt(controller.getControllerSN()),
                    Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)) // TODO check
            );
            SetTimeFunction.SetTimeResponse setTimeResponse = setTimeFunction.execute(udpClient);

            return setTimeResponse;
        } catch (IOException e) {
            throw new ControllerFunctionExecutionFailedException(e, GetRecordByIndexFunction.class.getSimpleName());
        } finally {
            assert udpClient != null;
            udpClient.close();
        }
    }
}
