//package me.adonlic.app.record;
//
//import me.adonlic.app.access_control.model.AccessLog;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(path = "api/v1/controllers")
//public class RecordController {
//    private final RecordService recordService;
//
//    @Autowired
//    public RecordController(RecordService recordService) {
//        this.recordService = recordService;
//    }
//
//    @GetMapping("/by-controller-id")
//    public List<AccessLog> getRecordsByControllerId(@RequestParam Long controllerId) {
//        return recordService.getRecordsByControllerId(controllerId);
//    }
//}
