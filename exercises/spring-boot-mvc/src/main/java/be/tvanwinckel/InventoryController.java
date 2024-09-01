package be.tvanwinckel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
    private final List<InventoryItem> items = new ArrayList<InventoryItem>(
            Arrays.asList(new InventoryItem("Sword", "epic", 100),
                    new InventoryItem("Shield", "common", 35)));

    @GetMapping(path = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<InventoryItem> getItemsFromInventory() {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        return items;
    }


    @PostMapping(path = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView addItemToInventory(@RequestBody final InventoryItem item) {
        items.add(item);

        final ModelAndView view = new ModelAndView("inventoryView");
        view.addObject("message", "Added: " + item.toString());
        return view;
    }

    @GetMapping (path = "/items/{index}")
    public InventoryItem getItemFromInventory(@PathVariable(name = "index") final int itemIndex) {
        return items.get(itemIndex);
    }

    @ModelAttribute
    public void addTotalItemsInInventoryAttribute(final Model model) {
        model.addAttribute("numberOfTotalItems", items.size());
    }

    private String inventoryItemsToString() {
        final StringBuilder inventoryItemList = new StringBuilder();
        for (final InventoryItem i : items) {
            inventoryItemList.append(" ").append(i.toString());
        }
        return inventoryItemList.toString();
    }
}
