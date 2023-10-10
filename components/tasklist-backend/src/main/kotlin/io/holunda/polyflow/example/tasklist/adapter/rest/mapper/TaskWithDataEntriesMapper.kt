package io.holunda.polyflow.example.tasklist.adapter.rest.mapper

import io.holunda.polyflow.example.tasklist.adapter.rest.model.DataEntryDto
import io.holunda.polyflow.example.tasklist.adapter.rest.model.ProtocolEntryDto
import io.holunda.polyflow.example.tasklist.adapter.rest.model.TaskDto
import io.holunda.polyflow.example.tasklist.adapter.rest.model.TaskWithDataEntriesDto
import io.holunda.polyflow.view.*
import org.mapstruct.*
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import jakarta.validation.Valid

/**
 * DTO mapper.
 */
@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.ERROR,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
abstract class TaskWithDataEntriesMapper {

  @Suppress("unused")
  @Autowired
  lateinit var formUrlResolver: FormUrlResolver

  @Mappings(
    Mapping(target = "processName", source = "sourceReference.name"),
    Mapping(target = "url", expression = "java(formUrlResolver.resolveUrl(task))"),
    Mapping(target = "createTime", expression = "java(toOffsetDateTime(task.getCreateTime()))"),
    Mapping(target = "dueDate", expression = "java(toOffsetDateTime(task.getDueDate()))"),
    Mapping(target = "followUpDate", expression = "java(toOffsetDateTime(task.getFollowUpDate()))")
  )
  abstract fun dto(task: Task): TaskDto

  @Mappings(
    Mapping(target = "payload", source = "payload"),
    Mapping(target = "url", expression = "java(formUrlResolver.resolveUrl(dataEntry))"),
    Mapping(target = "currentState", source = "state.state"),
    Mapping(target = "currentStateType", source = "state.processingType"),
    Mapping(target = "protocol", source = "protocol")
  )
  abstract fun dto(dataEntry: DataEntry): DataEntryDto

  @Mappings(
    Mapping(target = "timestamp", expression = "java(toOffsetDateTime(entry.getTime()))"),
    Mapping(target = "user", source = "username"),
    Mapping(target = "state", source = "state.state"),
    Mapping(target = "stateType", source = "state.processingType"),
    Mapping(target = "log", source = "logMessage"),
    Mapping(target = "logDetails", source = "logDetails")
  )
  abstract fun dto(entry: ProtocolEntry): ProtocolEntryDto

  @Mappings(
    Mapping(target = "task", source = "task"),
    Mapping(target = "dataEntries", source = "dataEntries")
  )
  abstract fun dto(taskWithDataEntries: TaskWithDataEntries): TaskWithDataEntriesDto

  fun toOffsetDateTime(@Valid time: Instant?): OffsetDateTime? =
    if (time == null) null else OffsetDateTime.ofInstant(time, ZoneOffset.UTC)

}
