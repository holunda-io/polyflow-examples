package io.holunda.polyflow.example.infrastructure.jpa

import org.hibernate.boot.model.TypeContributions
import org.hibernate.dialect.PostgreSQLDialect
import org.hibernate.service.ServiceRegistry
import org.hibernate.type.descriptor.sql.internal.DdlTypeImpl
import java.sql.Types

class NoToastPostgresSQLDialect : PostgreSQLDialect() {

  override fun registerColumnTypes(typeContributions: TypeContributions, serviceRegistry: ServiceRegistry) {
    super.registerColumnTypes(typeContributions, serviceRegistry)
    val ddlTypeRegistry = typeContributions.typeConfiguration.ddlTypeRegistry
    ddlTypeRegistry.addDescriptor(DdlTypeImpl(Types.BLOB, "bytea", this))
    ddlTypeRegistry.addDescriptor(DdlTypeImpl(Types.CLOB, "bytea", this))
    ddlTypeRegistry.addDescriptor(DdlTypeImpl(Types.NCLOB, "bytea", this))
  }
}
