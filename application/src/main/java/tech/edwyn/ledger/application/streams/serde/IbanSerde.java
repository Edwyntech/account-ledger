package tech.edwyn.ledger.application.streams.serde;

import org.apache.kafka.common.serialization.Serdes;
import org.iban4j.Iban;

public class IbanSerde extends Serdes.WrapperSerde<Iban> {
  public IbanSerde() {
    super(new IbanSerializer(), new IbanDeserializer());
  }
}
