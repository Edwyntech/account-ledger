package tech.edwyn.ledger.documentation.infrastructure;

import org.javamoney.moneta.convert.ExchangeRateBuilder;
import org.javamoney.moneta.spi.AbstractRateProvider;
import org.javamoney.moneta.spi.DefaultNumberValue;

import javax.money.NumberValue;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ProviderContextBuilder;
import javax.money.convert.RateType;
import java.util.Map;

public class FakeExchangeRateProvider extends AbstractRateProvider {
  private static final Map<String, Map<String, NumberValue>> RATES = Map.of(
    "EUR", Map.of(
      "USD", DefaultNumberValue.of(1.2)
    ),
    "USD", Map.of(
      "EUR", DefaultNumberValue.of(0.8)
    )
  );
  
  public FakeExchangeRateProvider() {
    super(ProviderContextBuilder.of("FAKE", RateType.OTHER)
                                .set("providerDescription", "Fake Rates Provider")
                                .build());
  }
  
  @Override
  public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
    return new ExchangeRateBuilder(getExchangeContext("fake"))
      .setBase(conversionQuery.getBaseCurrency())
      .setTerm(conversionQuery.getCurrency())
      .setFactor(RATES.get(conversionQuery.getBaseCurrency()
                                          .getCurrencyCode())
                      .get(conversionQuery.getCurrency()
                                          .getCurrencyCode()))
      .build();
  }
  
}
