create view plf_view_task_and_data_entry_payload as
(
(select pc.task_id, dea.path, dea.value
 from plf_task_correlations pc
          join plf_data_entry_payload_attributes dea on pc.entry_id = dea.entry_id AND pc.entry_type = dea.entry_type)
union
select *
from plf_task_payload_attributes);

create view plf_view_data_entry_payload as
(
select *
from plf_data_entry_payload_attributes
union
(select ec.owning_entry_id   as entry_id,
        ec.owning_entry_type as entry_type,
        ep.path              as path,
        ep.value             as value
 from plf_data_entry_correlations ec
          join plf_data_entry_payload_attributes ep
               on
                   ec.entry_id = ep.entry_id and ec.entry_type = ep.entry_type)
);