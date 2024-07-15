const { test, expect } = require('@playwright/test');

test.describe('Landing page', () => {
  test('shows heading and help text', async ({ page }) => {
    await page.goto('/');
    await expect(page.locator('h3')).toHaveText('Welcome to LandOnLite');
    await expect(page.locator('p')).toHaveText('You can enter a title number (e.g. "1") to view it.');
  });

  test('shows title search box', async ({ page }) => {
    await page.goto('/');
    expect(await page.locator('input').getAttribute('placeholder')).toEqual('Enter a title number');
    await expect(page.locator('button')).toHaveText('Go');
  });
})