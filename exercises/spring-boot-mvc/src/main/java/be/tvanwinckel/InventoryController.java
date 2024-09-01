package be.tvanwinckel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
    private final List<String> items = List.of("Sword", "Shield", "Mana Potion");
    private int gold = 0;

    @GetMapping(path = "/items")
    public String getInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        final StringBuilder inventoryItemList = new StringBuilder();
        for (final String item : items) {
            inventoryItemList.append(" ").append(item);
        }

        model.addAttribute("message", inventoryItemList.toString());
        return "inventoryView";
    }

    @GetMapping(path = "/gold")
    public String getGoldFromInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", "Amount of gold: " + gold);
        return "inventoryView";
    }
}
