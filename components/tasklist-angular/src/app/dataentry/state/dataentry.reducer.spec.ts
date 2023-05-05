import { dataEntriesLoaded } from './dataentry.actions';
import { DataEntry, dataentryReducer, DataEntryState } from './dataentry.reducer';

describe('dataEntryReducer', () => {

  const initialState: DataEntryState = {
    dataEntries: []
  };

  it('updates available users', () => {
    // given:
    const dataEntries: DataEntry[] = [
      {name: 'foo', description: '', url: '', type: 'type', payload: {}, currentState: 'MY STATE', currentStateType: '', protocol: []},
      {name: 'bar', description: '', url: '', type: 'type2', payload: {}, currentState: 'MY STATE2', currentStateType: '', protocol: []}
    ];
    const action = dataEntriesLoaded({dataEntries});

    // when:
    const newState = dataentryReducer(initialState, action);

    // then:
    expect(newState.dataEntries).toBe(dataEntries);
  });
});
