package models

public class ExchangeRate {

    BigDecimal usd
    BigDecimal vnd

    @Override
    String toString() {
        return 'Employee(' +
           "usd='${usd}', " +
           "vnd='${vnd}'" +
           ')'
    }

}
