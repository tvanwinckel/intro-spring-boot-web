package be.tvanwinckel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/currency")
public class CurrencyController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyController(final CurrencyService currencyService, final CurrencyRepository currencyRepository) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    public String getTotalCurrency(final Model model) {
        LOGGER.info("GET total currency.");
        model.addAttribute("message", "Total amount of currency: " + currencyRepository.getAll());
        return "inventoryView";
    }

    @PostMapping
    public String addOrSubtractGoldToInventory(@RequestBody final Currency currency,
                                               @RequestParam(name = "action", required = false, defaultValue = "add") final String action,
                                               @RequestHeader(name = "key") final String key,
                                               final Model model) throws Exception {
        if (key.equals("secret")) {
            if (action.equals("add")) {
                final Currency current_currency = currencyRepository.getAll();
                final Currency newCurrency = currencyService.add(current_currency, currency);
                model.addAttribute("message", "Amount of currency: " + newCurrency);
            } else if (action.equals("subtract")) {
                final Currency current_currency = currencyRepository.getAll();
                final Currency newCurrency = currencyService.subtract(current_currency, currency);
                model.addAttribute("message", "Amount of currency: " + newCurrency);
            } else {
                throw new UnknownCurrencyOpperationException();
            }
        } else {
            model.addAttribute("message", "Sorry, wallet is locked");
        }

        return "inventoryView";
    }
}
