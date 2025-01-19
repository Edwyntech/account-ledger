package tech.edwyn.ledger.application.streams.serde;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.utils.Utils;
import org.iban4j.Iban;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class IbanDeserializer implements Deserializer<Iban> {
  private String encoding = StandardCharsets.UTF_8.name();
  
  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    String propertyName  = isKey ? "key.deserializer.encoding" : "value.deserializer.encoding";
    Object encodingValue = configs.get(propertyName);
    if (encodingValue == null)
      encodingValue = configs.get("deserializer.encoding");
    if (encodingValue instanceof String)
      encoding = (String) encodingValue;
  }
  
  @Override
  public Iban deserialize(String topic, byte[] data) {
    try {
      if (data == null)
        return null;
      else
        return Iban.valueOf(new String(data, encoding));
    } catch (UnsupportedEncodingException e) {
      throw new SerializationException("Error when deserializing byte[] to Iban due to unsupported encoding " + encoding, e);
    } catch (IllegalArgumentException e) {
      throw new SerializationException("Error parsing data into Iban", e);
    }
  }
  
  @Override
  public Iban deserialize(String topic, Headers headers, ByteBuffer data) {
    try {
      if (data == null) {
        return null;
      }
      
      if (data.hasArray()) {
        return Iban.valueOf(new String(data.array(), data.arrayOffset() + data.position(), data.remaining(), encoding));
      }
      return Iban.valueOf(new String(Utils.toArray(data), encoding));
    } catch (UnsupportedEncodingException e) {
      throw new SerializationException("Error when deserializing ByteBuffer to Iban due to unsupported encoding " + encoding, e);
    } catch (IllegalArgumentException e) {
      throw new SerializationException("Error parsing data into Iban", e);
    }
  }
}
