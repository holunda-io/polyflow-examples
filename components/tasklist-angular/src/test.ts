// This file is required by karma.conf.js and loads recursively all the .spec and framework files

import 'zone.js/testing';
import { TestBed, getTestBed } from '@angular/core/testing';
import { MockStore } from '@ngrx/store/testing';
import {BrowserTestingModule, platformBrowserTesting} from "@angular/platform-browser/testing";

// First, initialize the Angular testing environment.
getTestBed().initTestEnvironment(
  BrowserTestingModule,
  platformBrowserTesting(), {
  teardown: { destroyAfterEach: false }
}
);

afterEach(() => {
  TestBed.inject(MockStore, undefined, { optional: true })?.resetSelectors();
});
