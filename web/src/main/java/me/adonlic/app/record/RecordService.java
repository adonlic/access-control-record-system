//package me.adonlic.app.record;
//
//import me.adonlic.app.access_control.model.AccessLog;
//import me.adonlic.app.access_control.model.Card;
//import me.adonlic.app.controller.ControllerService;
//import me.adonlic.app.controller.model.Controller;
//import me.adonlic.app.controller.model.Door;
//import me.adonlic.uhppote.functions.implementations.QueryControllerFunction;
//import me.adonlic.uhppote.functions.implementations.record.GetRecordByIndexFunction;
//import me.adonlic.uhppote.types.Validation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class RecordService {
//    private final RecordRepository recordRepository;
//    private final ControllerService controllerService;
//
//    @Autowired
//    public RecordService(RecordRepository recordRepository, ControllerService controllerService) {
//        this.recordRepository = recordRepository;
//        this.controllerService = controllerService;
//    }
//
//    public List<AccessLog> getRecordsByControllerId(Long controllerId) {
//        Controller controller = controllerService.getController(controllerId);
//        QueryControllerFunction.QueryControllerResponse queryControllerResponse = controllerService
//                .queryController(controllerId);
//        int latestRecordIndex = queryControllerResponse.getLatestRecordIndex();
//        List<AccessLog> accessLogs = new ArrayList<>();
//
//        // Backwards (latest on top)
//        for (int i = latestRecordIndex; i >= 0; i-- ) {
//            GetRecordByIndexFunction.GetRecordResponse getRecordResponse = controllerService.getRecord(controllerId, i);
//            Card card = new Card(String.valueOf(getRecordResponse.getCardNumber()), null);
//            Door door = new Door(
//                    getRecordResponse.getDoorNumber().name(),
//                    getRecordResponse.getDoorNumber().getValue(),
//                    controller
//            );
//            AccessLog accessLog = new AccessLog(card, door, queryControllerResponse.getValidation().equals(Validation.PASS));
//            accessLog.setSwipeTime(LocalDateTime.parse(getRecordResponse.getSwipeTime()));
//
//            accessLogs.add(new AccessLog(card, door, queryControllerResponse.getValidation().equals(Validation.PASS)));
//        }
//
//        return accessLogs;
//    }
//}
