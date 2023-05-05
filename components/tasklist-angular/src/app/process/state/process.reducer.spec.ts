import { startableProcessDefinitionsLoaded } from './process.actions';
import { ProcessDefinition, processReducer, ProcessState } from './process.reducer';

describe('processReducer', () => {

  const initialState: ProcessState = {
    startableProcesses: []
  };

  it('updates available users', () => {
    // given:
    const definitions: ProcessDefinition[] = [
      {name: 'foo', description: '', url: ''},
      {name: 'bar', description: '', url: ''}
    ];
    const action = startableProcessDefinitionsLoaded({definitions});

    // when:
    const newState = processReducer(initialState, action);

    // then:
    expect(newState.startableProcesses).toBe(definitions);
  });
});
