create view PLF_VIEW_TASK_AND_DATA_ENTRY_PAYLOAD as
((select pc.TASK_ID, dea.PATH, dea.VALUE
  from PLF_TASK_CORRELATIONS pc
         join PLF_DATA_ENTRY_PAYLOAD_ATTRIBUTES dea on pc.ENTRY_ID = dea.ENTRY_ID and pc.ENTRY_TYPE = dea.ENTRY_TYPE)
union
select * from PLF_TASK_PAYLOAD_ATTRIBUTES);

create view PLF_VIEW_DATA_ENTRY_PAYLOAD as
(
select *
from PLF_DATA_ENTRY_PAYLOAD_ATTRIBUTES
union
(select ec.OWNING_ENTRY_ID   as ENTRY_ID,
        ec.OWNING_ENTRY_TYPE as ENTRY_TYPE,
        ep.path              as PATH,
        ep.value             as VALUE
 from PLF_DATA_ENTRY_CORRELATIONS ec
   join PLF_DATA_ENTRY_PAYLOAD_ATTRIBUTES ep
 on
   ec.ENTRY_ID = ep.ENTRY_ID and ec.ENTRY_TYPE = ep.ENTRY_TYPE)
);
