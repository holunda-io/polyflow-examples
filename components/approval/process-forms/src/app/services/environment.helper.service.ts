import { Injectable, inject } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Environment } from 'process/models/environment';
import { EnvironmentService } from 'process/services/environment.service';

@Injectable()
export class EnvironmentHelperService {
  private environmentService = inject(EnvironmentService);


  private environmentSubject: BehaviorSubject<Environment> = new BehaviorSubject<Environment>(this.none());

  constructor() {
    this.environmentService.getEnvironment().subscribe({
      next: (environment) => {
        this.environmentSubject.next(environment);
      },
      error: (error) => {
        console.log('Error loading environment', error);
      }
    });
  }

  env() {
    return this.environmentSubject.asObservable();
  }


  none(): Environment {
    return {
      applicationName: 'loading...',
      tasklistUrl: '#',
      users: []
    };
  }
}
