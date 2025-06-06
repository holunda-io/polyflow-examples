create table plf_data_entry_correlations
(
	owning_entry_type varchar(255) not null,
	owning_entry_id varchar(64) not null,
	entry_type varchar(255) not null,
	entry_id varchar(64) not null,
	primary key (owning_entry_type, owning_entry_id, entry_type, entry_id)
)