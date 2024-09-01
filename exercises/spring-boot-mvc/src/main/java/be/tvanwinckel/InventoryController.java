package be.tvanwinckel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    @PostMapping
    public String postMapping(final Model model) {
        model.addAttribute("message", "POST mapping");
        return "inventoryView";
    }

    @RequestMapping(path = "/old-post", method = RequestMethod.POST)
    public String oldPostMapping(final Model model) {
        model.addAttribute("message", "old POST mapping");
        return "inventoryView";
    }

    @GetMapping
    public String getInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", "GET mapping");
        return "inventoryView";
    }

    @PutMapping
    public String putMapping(final Model model) {
        model.addAttribute("message", "PUT mapping");
        return "inventoryView";
    }

    @DeleteMapping
    public String deleteMapping(final Model model) {
        model.addAttribute("message", "DELETE mapping");
        return "inventoryView";
    }

    @PatchMapping
    public String patchMapping(final Model model) {
        model.addAttribute("message", "PATCH mapping");
        return "inventoryView";
    }

}
