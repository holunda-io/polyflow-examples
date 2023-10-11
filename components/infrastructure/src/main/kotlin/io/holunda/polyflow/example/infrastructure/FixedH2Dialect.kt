package io.holunda.polyflow.example.infrastructure

import org.hibernate.boot.model.TypeContributions
import org.hibernate.dialect.H2Dialect
import org.hibernate.service.ServiceRegistry
import org.hibernate.type.descriptor.sql.internal.DdlTypeImpl
import java.sql.Types

class FixedH2Dialect : H2Dialect() {

  override fun registerColumnTypes(typeContributions: TypeContributions, serviceRegistry: ServiceRegistry) {
    super.registerColumnTypes(typeContributions, serviceRegistry)
    val ddlTypeRegistry = typeContributions.typeConfiguration.ddlTypeRegistry
    ddlTypeRegistry.addDescriptor(DdlTypeImpl(Types.BLOB, "bytea", this))
    ddlTypeRegistry.addDescriptor(DdlTypeImpl(Types.CLOB, "bytea", this))
    ddlTypeRegistry.addDescriptor(DdlTypeImpl(Types.NCLOB, "bytea", this))
  }

}
