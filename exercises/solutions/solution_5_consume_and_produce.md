# Solution Consume and Produce

```java
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
    public String addItemToInventory(@RequestBody final InventoryItem item, Model model) {
        items.add(item);
        model.addAttribute("message", inventoryItemsToString());
        return "inventoryView";
    }

    @GetMapping (path = "/items/{index}")
    public InventoryItem getItemFromInventory(@PathVariable(name = "index") final int itemIndex) {
        return items.get(itemIndex);
    }

    private String inventoryItemsToString() {
        final StringBuilder inventoryItemList = new StringBuilder();
        for (final InventoryItem i : items) {
            inventoryItemList.append(" ").append(i.toString());
        }
        return inventoryItemList.toString();
    }
}
```

```java
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
```

```java
public record Currency(int gold, int silver, int copper) {

    public Currency(@JsonProperty(value = "gold") int gold,
                    @JsonProperty(value = "silver") int silver,
                    @JsonProperty(value = "copper") int copper) {
        this.gold = gold;
        this.silver = silver;
        this.copper = copper;
    }

    public static Currency empty() {
        return new Currency(0, 0, 0);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "gold=" + gold +
                ", silver=" + silver +
                ", copper=" + copper +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return gold == currency.gold && silver == currency.silver && copper == currency.copper;
    }

}
```