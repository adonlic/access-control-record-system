package me.adonlic.app.controller;

import me.adonlic.app.controller.model.Controller;
import me.adonlic.uhppote.functions.implementations.QueryControllerFunction;
import me.adonlic.uhppote.functions.implementations.SearchFunction;
import me.adonlic.uhppote.functions.implementations.SetTimeFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.AddAccessPrivilegeFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.ClearAllAccessPrivilegesFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.DeleteAccessPrivilegeFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.QueryAccessPrivilegeByCardFunction;
import me.adonlic.uhppote.functions.implementations.record.GetRecordByIndexFunction;
import me.adonlic.uhppote.functions.implementations.record.GetRecordIndexFunction;
import me.adonlic.uhppote.functions.implementations.record.SetRecordIndexFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/controllers")
public class ControllerController {

    private final ControllerService controllerService;

    @Autowired
    public ControllerController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping
    public List<Controller> getControllers() {
        return controllerService.getControllers();
    }

    @GetMapping("/{id}")
    public Controller getController(@PathVariable Long id) {
        return controllerService.getController(id);
    }

    @PostMapping()
    public Controller addController(@RequestBody Controller controller) {
        return controllerService.createController(controller);
    }

    @PutMapping("/{id}")
    public Controller updateController(@PathVariable Long id, @RequestBody Controller controller) {
        return controllerService.updateController(id, controller);
    }

    @DeleteMapping("/{id}")
    public void removeController(@PathVariable Long id) {
        controllerService.deleteController(id);
    }

    @GetMapping("/function/search")
    public List<SearchFunction.SearchResponse> searchControllers() {
        return controllerService.searchInNetwork();
    }

    @GetMapping("/{id}/function/query")
    public QueryControllerFunction.QueryControllerResponse queryController(@PathVariable  Long id) {
        return controllerService.queryController(id);
    }

    @GetMapping("/{id}/function/open-door")
    public void remoteOpenDoor(@PathVariable Long id, @RequestParam Integer doorId) {
        controllerService.remoteOpenDoor(id, doorId);
    }

    @GetMapping("/{id}/function/query-access-privilege-by-card")
    public QueryAccessPrivilegeByCardFunction.QueryAccessPrivilegeResponse queryAccessPrivilegeByCard(
            @PathVariable Long id,
            @RequestParam String cardNumber
    ) {
        return controllerService.queryAccessPrivilegeByCard(id, Integer.parseInt(cardNumber));
    }

    @GetMapping("/{id}/function/add-access-privilege")
    public AddAccessPrivilegeFunction.AddAccessPrivilegeResponse addAccessPrivilege(
            @PathVariable Long id,
            @RequestParam String cardNumber,
            @RequestParam LocalDate activateDate,
            @RequestParam LocalDate deactivateDate,
            @RequestParam boolean door1AccessRight
    ) {
        return controllerService.addAccessPrivilege(id, Integer.parseInt(cardNumber), activateDate, deactivateDate, door1AccessRight);
    }

    @GetMapping("/{id}/function/delete-access-privilege")
    public DeleteAccessPrivilegeFunction.DeleteAccessPrivilegeResponse deleteAccessPrivilege(
            @PathVariable Long id,
            @RequestParam String cardNumber
    ) {
        return controllerService.deleteAccessPrivilege(id, Integer.parseInt(cardNumber));
    }

    @GetMapping("/{id}/function/clear-all-access-privileges")
    public ClearAllAccessPrivilegesFunction.ClearAllAccessPrivilegesResponse clearAllAccessPrivileges(
            @PathVariable Long id
    ) {
        return controllerService.clearAllAccessPrivileges(id);
    }

    @GetMapping("/{id}/function/get-record-index")
    public GetRecordIndexFunction.GetRecordIndexResponse getRecordIndex(@PathVariable Long id) {
        return controllerService.getRecordIndex(id);
    }

    @GetMapping("/{id}/function/get-record-by-index")
    public GetRecordByIndexFunction.GetRecordResponse getRecord(
            @PathVariable Long id,
            @RequestParam Integer recordIndex
    ) {
        return controllerService.getRecord(id, recordIndex);
    }

    @GetMapping("/{id}/function/set-record-index")
    public SetRecordIndexFunction.SetRecordIndexResponse setRecordIndex(
            @PathVariable Long id,
            @RequestParam Integer recordIndex
    ) {
        return controllerService.setRecordIndex(id, recordIndex);
    }

    @GetMapping("/{id}/function/configure-ip")
    public void configureIP(
            @PathVariable Long id,
            @RequestParam String controllerIP,
            @RequestParam String controllerMask,
            @RequestParam String controllerGateway
    ) {
        controllerService.configureIP(id, controllerIP, controllerMask, controllerGateway);
    }

    @GetMapping("/{id}/function/sync-time")
    public SetTimeFunction.SetTimeResponse syncTime(@PathVariable Long id) {
        return controllerService.setTime(id);
    }
}
