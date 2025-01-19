package tech.edwyn.ledger.avro.types;

import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

public class IbanLogicalTypeFactory implements LogicalTypes.LogicalTypeFactory {
  public static final LogicalType IBAN = new LogicalType("iban");
  
  @Override
  public LogicalType fromSchema(Schema schema) {
    return IBAN;
  }
  
  @Override
  public String getTypeName() {
    return IBAN.getName();
  }
}
