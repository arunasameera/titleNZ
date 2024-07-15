// playwright.config.js
// @ts-check

/** @type {import('@playwright/test').PlaywrightTestConfig} */
const config = {
  timeout: 30000,
  globalTimeout: 600000,
  reporter: 'list',
  testDir: './tests',
  use: {
    headless: false,
    baseURL: 'http://localhost:3000',
  },
};

module.exports = config;