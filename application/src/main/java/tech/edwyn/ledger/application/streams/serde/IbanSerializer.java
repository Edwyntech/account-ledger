package tech.edwyn.ledger.application.streams.serde;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.iban4j.Iban;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class IbanSerializer implements Serializer<Iban> {
  private String encoding = StandardCharsets.UTF_8.name();
  
  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    String propertyName = isKey ? "key.serializer.encoding" : "value.serializer.encoding";
    Object encodingValue = configs.get(propertyName);
    if (encodingValue == null)
      encodingValue = configs.get("serializer.encoding");
    if (encodingValue instanceof String)
      encoding = (String) encodingValue;
  }
  
  @Override
  public byte[] serialize(String topic, Iban data) {
    try {
      if (data == null)
        return null;
      else
        return data.toString()
                   .getBytes(encoding);
    } catch (UnsupportedEncodingException e) {
      throw new SerializationException("Error when serializing Iban to byte[] due to unsupported encoding " + encoding);
    }
  }
}
