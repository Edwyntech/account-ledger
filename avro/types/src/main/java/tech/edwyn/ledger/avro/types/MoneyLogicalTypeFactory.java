package tech.edwyn.ledger.avro.types;

import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

public class MoneyLogicalTypeFactory implements LogicalTypes.LogicalTypeFactory {
  public static final LogicalType MONEY = new LogicalType("money");
  
  @Override
  public LogicalType fromSchema(Schema schema) {
    return MONEY;
  }
  
  @Override
  public String getTypeName() {
    return MONEY.getName();
  }
}
