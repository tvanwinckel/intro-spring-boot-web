package be.tvanwinckel;

import org.springframework.stereotype.Repository;

@Repository
public class DefaultCurrencyRepository implements CurrencyRepository {

    @Override
    public Currency getAll() {
        return new Currency(10, 23, 67);
    }
}
