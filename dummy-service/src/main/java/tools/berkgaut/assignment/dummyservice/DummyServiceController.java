package tools.berkgaut.assignment.dummyservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
public class DummyServiceController {

    @GetMapping("/health/{status}")
    @ResponseBody
    public ResponseEntity<String> response(@PathVariable String status, @RequestParam(name="delayMs", required = false) String delayMs) throws InterruptedException {
        int responseCode = Integer.parseInt(status);

        if (delayMs != null && !delayMs.isEmpty()) {
            long delayMilliseconds = Long.parseLong(delayMs);
            Thread.sleep(delayMilliseconds);
        }

        return new ResponseEntity<>(HttpStatus.resolve(responseCode));
    }

}
