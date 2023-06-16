package io.holunda.polyflow.example.process.platform.infrastructure

import org.hibernate.dialect.PostgreSQL94Dialect
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor
import java.sql.Types

class NoToastPostgresSQLDialect : PostgreSQL94Dialect() {
  init {
    this.registerColumnType(Types.BLOB, "BYTEA")
  }

  override fun remapSqlTypeDescriptor(sqlTypeDescriptor: SqlTypeDescriptor): SqlTypeDescriptor {
    return if (sqlTypeDescriptor.sqlType == Types.BLOB) {
      BinaryTypeDescriptor.INSTANCE
    } else super.remapSqlTypeDescriptor(sqlTypeDescriptor)
  }
}
