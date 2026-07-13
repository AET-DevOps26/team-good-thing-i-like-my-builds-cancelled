# Client

The client is built using [Angular](https://angular.dev/) and can be found in `/apps/client/`.

## Content

## External UI Components

## Testing

### Component Testing

Angular natively comes with a testing suite. Each component is being tested if it can be created to ensure correct imports, exports and dependencies. The two pages of the app (plan and log) have additional test cases that test the most important parts of the user workflow:
- `src/app/page/log/log.spec.ts`
- `src/app/page/plan/plan.spec.ts`

All tests can be executed using:

```shell
npm run test
```

### Linting

This project uses ESLINT with a custom config (`eslint.config.js`) to run linting on the TypeScript code. Linting can be executed locally using:

```shell
npm run lint
```
