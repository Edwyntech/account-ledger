package tech.edwyn.ledger.avro.types;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.iban4j.Iban;

import static tech.edwyn.ledger.avro.types.IbanLogicalTypeFactory.IBAN;

public class IbanConversion extends Conversion<Iban> {
  
  @Override
  public Class<Iban> getConvertedType() {
    return Iban.class;
  }
  
  @Override
  public String getLogicalTypeName() {
    return IBAN.getName();
  }
  
  @Override
  public Schema getRecommendedSchema() {
    return IBAN.addToSchema(Schema.create(Schema.Type.STRING));
  }
  
  @Override
  public Iban fromCharSequence(CharSequence value, Schema schema, LogicalType type) {
    return Iban.valueOf(value.toString());
  }
  
  @Override
  public CharSequence toCharSequence(Iban value, Schema schema, LogicalType type) {
    return value.toString();
  }
}
