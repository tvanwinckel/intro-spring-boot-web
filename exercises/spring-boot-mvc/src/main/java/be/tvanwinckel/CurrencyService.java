package be.tvanwinckel;

import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    public Currency add(final Currency c1, final Currency c2) {
        int newAmountOfCopper = 0;
        int newAmountOfSilver = 0;
        int newAmountOfGold = 0;

        if (c1.copper() + c2.copper() < 100) {
            newAmountOfCopper = c1.copper() + c2.copper() + newAmountOfCopper;
        } else {
            newAmountOfSilver += 1;
            newAmountOfCopper = c1.copper() + c2.copper() - 100;
        }

        if (c1.silver() + c2.silver() + newAmountOfSilver < 100) {
            newAmountOfSilver = newAmountOfSilver + c1.silver() + c2.silver();
        } else {
            newAmountOfGold += 1;
            newAmountOfSilver = newAmountOfSilver + c1.silver() + c2.silver() - 100;
        }

        newAmountOfGold = c1.gold() + c2.gold() + newAmountOfGold;

        return new Currency(newAmountOfGold, newAmountOfSilver, newAmountOfCopper);
    }

    public Currency subtract(final Currency c1, final Currency c2) throws NotEnoughCurrencyException {
        int newAmountOfCopper = 0;
        int newAmountOfSilver = 0;
        int newAmountOfGold = 0;

        if (c1.gold() - c2.gold() < 0) {
            throw new NotEnoughCurrencyException();
        }
        newAmountOfGold = c1.gold() - c2.gold();

        if (c1.silver() - c2.silver() >= 0) {
            newAmountOfSilver = c1.silver() - c2.silver();
        } else {
            newAmountOfGold -= 1;
            newAmountOfSilver = c1.silver() - c2.silver() + 100;
        }

        if (c1.copper() - c2.copper() >= 0) {
            newAmountOfCopper = c1.copper() - c2.copper();
        } else {
            if (newAmountOfSilver > 0) {
                newAmountOfSilver -= 1;
                newAmountOfCopper = c1.copper() - c2.copper() + 100;
            } else {
                if (newAmountOfGold > 0) {
                    newAmountOfGold -= 1;
                    newAmountOfSilver += 99;
                    newAmountOfCopper = c1.copper() - c2.copper() + 100;
                } else {
                    throw new NotEnoughCurrencyException();
                }

            }
        }

        return new Currency(newAmountOfGold, newAmountOfSilver, newAmountOfCopper);
    }
}
