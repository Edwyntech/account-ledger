package tech.edwyn.ledger.avro.types;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.javamoney.moneta.Money;

import static tech.edwyn.ledger.avro.types.MoneyLogicalTypeFactory.MONEY;

public class MoneyConversion extends Conversion<Money> {
  
  @Override
  public Class<Money> getConvertedType() {
    return Money.class;
  }
  
  @Override
  public String getLogicalTypeName() {
    return MONEY.getName();
  }
  
  @Override
  public Schema getRecommendedSchema() {
    return MONEY.addToSchema(Schema.create(Schema.Type.STRING));
  }
  
  @Override
  public Money fromCharSequence(CharSequence value, Schema schema, LogicalType type) {
    return Money.parse(value.toString());
  }
  
  @Override
  public CharSequence toCharSequence(Money value, Schema schema, LogicalType type) {
    return value.toString();
  }
}
