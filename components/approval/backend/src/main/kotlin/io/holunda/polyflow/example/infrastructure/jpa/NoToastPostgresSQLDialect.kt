package io.holunda.polyflow.example.infrastructure.jpa

import org.hibernate.dialect.PostgreSQLDialect
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor
import java.sql.Types

class NoToastPostgresSQLDialect : PostgreSQLDialect() {
  init {
    this.registerColumnType(Types.BLOB, "BYTEA")
  }

  override fun remapSqlTypeDescriptor(sqlTypeDescriptor: SqlTypeDescriptor): SqlTypeDescriptor {
    return if (sqlTypeDescriptor.sqlType == Types.BLOB) {
      BinaryTypeDescriptor.INSTANCE
    } else super.remapSqlTypeDescriptor(sqlTypeDescriptor)
  }
}
