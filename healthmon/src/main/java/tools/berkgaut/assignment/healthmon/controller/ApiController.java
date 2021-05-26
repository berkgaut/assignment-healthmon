package tools.berkgaut.assignment.healthmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tools.berkgaut.assignment.healthmon.apiobjects.Service;
import tools.berkgaut.assignment.healthmon.components.ServiceManager;
import tools.berkgaut.assignment.healthmon.exceptions.HealthmonException;

import java.util.List;

@Controller
public class ApiController {
    @Autowired
    ServiceManager serviceManager;

    @GetMapping("/v1/services")
    @ResponseBody
    public List<Service> getServices() throws HealthmonException {
        return serviceManager.getServices();
    }

    @PutMapping("/v1/services")
    @ResponseBody
    public Service createService(@RequestBody Service createData) throws HealthmonException {
        return serviceManager.createService(createData);
    }

    @PostMapping("/v1/services/{serviceId}")
    @ResponseBody
    public Service updateService(@PathVariable(name = "serviceId") String serviceIdString,
                                 @RequestBody Service service) throws HealthmonException {
        long serviceId = toId(serviceIdString);
        return serviceManager.updateService(serviceId, service);
    }

    @DeleteMapping("/v1/services/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteService(@PathVariable(name = "serviceId") String serviceIdString) throws HealthmonException {
        long serviceId = toId(serviceIdString);
        serviceManager.deleteService(serviceId);
    }

    private long toId(String s) throws HealthmonException {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new HealthmonException(400, "Invalid id format");
        }
    }
}
