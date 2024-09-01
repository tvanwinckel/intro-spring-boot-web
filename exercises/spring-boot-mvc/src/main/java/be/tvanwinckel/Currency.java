package be.tvanwinckel;

import com.fasterxml.jackson.annotation.JsonProperty;

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
