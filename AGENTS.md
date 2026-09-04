# AGENTS.md

## 1. Overview

Polyflow Examples is a Maven reactor containing runnable backend, frontend, and deployment-scenario examples for the Polyflow task-management platform. The repository is an integration surface: changes to a shared component must remain usable by the scenarios that compose it.

## 2. Folder Structure

- `components/`: reusable example applications and shared capabilities.
  - `tasklist-angular/`: Angular tasklist frontend; generated OpenAPI client lives in `src-gen/tasklist`.
  - `approval/process-forms/`: Angular approval-form frontend; generated OpenAPI client lives in `src-gen/process`.
  - `*-backend/`, `infrastructure/`, `users/`: Kotlin/Java services and shared backend support.
- `scenarios/`: deployable compositions of the components.
  - `single-node-jpa/`: the principal all-in-one JPA scenario and integration-test target; database upgrade migrations are in `src/main/resources/db/migrations/h2-postgresql`.
  - `distributed-*/`: distributed Axon Server and Kafka examples.
- `pom.xml`: root reactor, common dependency/plugin configuration, Node/npm versions, and the `frontend` and `itest` profiles.

## 3. Working Agreements

- Use English for communication and keep source-code identifiers, commands, and code blocks unchanged.
- Before changing a component, inspect its consumers and the corresponding scenario; keep edits focused and avoid speculative compatibility layers or refactors.
- Run the narrowest relevant verification first, then `mvn clean install` for reactor-wide changes. The root build enables frontend modules unless `-DskipFrontend` is set.

### Integration tests

- Run the focused single-node integration suite with:

  ```sh
  mvn -pl scenarios/single-node-jpa -am clean install -Pitest failsafe:verify -DskipFrontend
  ```

  `itest` activates Failsafe and defaults `skipFrontend` to true. `FrontendIT` is intentionally disabled when that system property is true, so this command validates the backend/JPA integration setup without requiring compiled SPAs.
- To exercise the Playwright frontend integration test, omit `-DskipFrontend` and ensure the frontend modules build first.
- When updating Camunda engine versions, keep the Flyway chain in `scenarios/single-node-jpa/src/main/resources/db/migrations/h2-postgresql` continuous. Take the matching upgrade SQL from Camunda's canonical Liquibase migration scripts; do not hand-write an approximate schema migration.

### Focused frontend verification

- The Maven frontend plugin provisions the pinned runtime into each frontend module's `node/` directory. Prefer that runtime over a globally installed Node/npm.
- `mvn clean` does not delete `node/` or `node_modules/`. To reproduce CI's clean dependency install, remove exactly those two generated directories from the affected frontend module, then run its Maven test lifecycle:

  ```sh
  mvn -pl components/tasklist-angular clean test
  mvn -pl components/approval/process-forms clean test
  ```

  The module POM runs `npm ci --no-audit` when `node_modules/` is absent, then generates clients, builds, lints, and runs Karma tests.
- For a faster compile-only check, use `mvn -pl <module> compile`; use `test` when changing dependencies or frontend code.
- Angular 22 uses TypeScript 6 in Tasklist. Its `tsconfig.json` explicitly preserves this application's existing non-strict TypeScript semantics and sets `ignoreDeprecations` for the retained `baseUrl` path mapping. Do not remove those settings without first migrating the application and tests to strict typing. Keep `istanbul-lib-instrument` installed because Angular's coverage builder declares it as a peer dependency.

### Frontend dependency updates

- Update `package.json` and its `package-lock.json` together. A clean `npm ci` fails if either lockfile omits newly required transitive packages.
- After Maven has provisioned `node/`, regenerate a frontend lockfile with the module-local npm, then verify a clean install and test:

  ```sh
  cd components/tasklist-angular
  node/node node/node_modules/npm/bin/npm-cli.js install --package-lock-only --ignore-scripts --no-audit
  node/node node/node_modules/npm/bin/npm-cli.js ci --no-audit --ignore-scripts
  node/node node/node_modules/npm/bin/npm-cli.js run test
  ```

  Apply the same sequence in `components/approval/process-forms` when updating its dependencies.
- Use the `npm-update` Maven profile only for intentionally resolver-driven updates. For a targeted dependency update, regenerate the lockfile as above so unrelated packages are not unnecessarily churned.
- Keep the root `node.version` compatible with both frontend dependency engines. A dependency's `engines` requirement is a build requirement, not merely a warning to ignore.
- Do not add tests, lint rules, or external dependencies unless the change requires them. Use comments only to capture non-obvious invariants.
