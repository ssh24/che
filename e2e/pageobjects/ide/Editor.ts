/*********************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/
import 'reflect-metadata';
import { injectable, inject } from 'inversify';
import { DriverHelper } from '../../utils/DriverHelper';
import { CLASSES } from '../../inversify.types';
import { TestConstants } from '../../TestConstants';
import { By, Key, error } from 'selenium-webdriver';

@injectable()
export class Editor {
    private static readonly SUGGESTION_WIDGET_BODY_CSS: string = 'div.visible[widgetId=\'editor.widget.suggestWidget\']';

    constructor(@inject(CLASSES.DriverHelper) private readonly driverHelper: DriverHelper) { }

    public async waitSuggestionContainer(timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.driverHelper.waitVisibility(By.css(Editor.SUGGESTION_WIDGET_BODY_CSS), timeout);
    }

    public async waitSuggestionContainerClosed() {
        await this.driverHelper.waitDisappearanceTestWithTimeout(By.css(Editor.SUGGESTION_WIDGET_BODY_CSS));
    }

    public async waitSuggestion(editorTabTitle: string,
        suggestionText: string,
        timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {

        const suggestionLocator: By = By.xpath(this.getSuggestionLineXpathLocator(suggestionText));

        await this.driverHelper.getDriver().wait(async () => {
            await this.waitSuggestionContainer();
            try {
                await this.driverHelper.waitVisibility(suggestionLocator, 5000);
                return true;
            } catch (err) {
                const isTimeoutError: boolean = err instanceof error.TimeoutError;
                if (!isTimeoutError) {
                    throw err;
                }

                await this.pressEscapeButton(editorTabTitle);
                await this.waitSuggestionContainerClosed();
                await this.pressControlSpaceCombination(editorTabTitle);
            }
        }, timeout);
    }

    public async pressControlSpaceCombination(editorTabTitle: string) {
        await this.performKeyCombination(editorTabTitle, Key.chord(Key.CONTROL, Key.SPACE));
    }

    public async pressEscapeButton(editorTabTitle: string) {
        await this.performKeyCombination(editorTabTitle, Key.ESCAPE);
    }

    public async clickOnSuggestion(suggestionText: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.driverHelper.waitAndClick(By.xpath(this.getSuggestionLineXpathLocator(suggestionText)), timeout);
    }

    public async waitTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.driverHelper.waitVisibility(By.xpath(this.getTabXpathLocator(tabTitle)), timeout);
    }

    public async waitTabDisappearance(tabTitle: string,
        attempt: number = TestConstants.TS_SELENIUM_DEFAULT_ATTEMPTS,
        polling: number = TestConstants.TS_SELENIUM_DEFAULT_POLLING) {
        await this.driverHelper.waitDisappearance(By.xpath(this.getTabXpathLocator(tabTitle)), attempt, polling);
    }

    public async clickOnTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.driverHelper.waitAndClick(By.xpath(this.getTabXpathLocator(tabTitle)), timeout);
    }

    public async waitTabFocused(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const focusedTabLocator: By = By.xpath(`//li[contains(@class, 'p-TabBar-tab') and contains(@class, 'theia-mod-active')]//div[text()='${tabTitle}']`);

        await this.driverHelper.waitVisibility(focusedTabLocator, timeout);

        // wait for increasing stability
        await this.driverHelper.wait(2000);
    }

    async closeTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const tabCloseButtonLocator: By = By.xpath(`//div[text()='${tabTitle}']/parent::li//div[contains(@class, 'p-TabBar-tabCloseIcon')]`);

        await this.driverHelper.waitAndClick(tabCloseButtonLocator, timeout);
    }

    async waitEditorOpened(editorTabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const firstEditorLineLocator: By = By.xpath(this.getEditorLineXpathLocator(1));

        await this.driverHelper.waitPresence(this.getEditorBodyLocator(editorTabTitle), timeout);
        await this.driverHelper.waitPresence(firstEditorLineLocator, timeout);
    }

    async waitEditorAvailable(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.waitTab(tabTitle, timeout);
        await this.waitEditorOpened(tabTitle, timeout);
    }

    async getLineText(lineNumber: number): Promise<string> {
        const lineIndex: number = lineNumber - 1;
        const editorText: string = await this.getEditorVisibleText();
        const editorLines: string[] = editorText.split('\n');
        const editorLine = editorLines[lineIndex] + '\n';

        return editorLine;
    }

    async getEditorVisibleText(): Promise<string> {
        const editorBodyLocator: By = By.xpath('//div[@class=\'view-lines\']');
        const editorText: string = await this.driverHelper.waitAndGetText(editorBodyLocator);
        return editorText;
    }

    async waitText(expectedText: string,
        timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT,
        polling: number = TestConstants.TS_SELENIUM_DEFAULT_POLLING) {
        await this.driverHelper.getDriver().wait(async () => {
            const editorText: string = await this.getEditorVisibleText();
            const isEditorContainText: boolean = editorText.includes(expectedText);

            if (isEditorContainText) {
                return true;
            }

            this.driverHelper.wait(polling);
        }, timeout);
    }

    async followAndWaitForText(editorTabTitle: string,
        expectedText: string,
        timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT,
        polling: number = TestConstants.TS_SELENIUM_DEFAULT_POLLING) {

        await this.driverHelper.getDriver().wait(async () => {
            await this.performKeyCombination(editorTabTitle, Key.chord(Key.CONTROL, Key.END));
            const editorText: string = await this.getEditorVisibleText();

            const isEditorContainText: boolean = editorText.includes(expectedText);

            if (isEditorContainText) {
                return true;
            }

            await this.driverHelper.wait(polling);
        }, timeout);
    }

    async moveCursorToLineAndChar(editorTabTitle: string, line: number, char: number) {
        // set cursor to the 1:1 position
        await this.performKeyCombination(editorTabTitle, Key.chord(Key.CONTROL, Key.HOME));

        // for ensuring that cursor has been set to the 1:1 position
        await this.driverHelper.wait(1000);

        // move cursor to line
        for (let i = 1; i < line; i++) {
            await this.performKeyCombination(editorTabTitle, Key.ARROW_DOWN);
        }

        // move cursor to char
        for (let i = 1; i < char; i++) {
            await this.performKeyCombination(editorTabTitle, Key.ARROW_RIGHT);
        }
    }

    public async performKeyCombination(editorTabTitle: string, text: string) {
        const interactionContainerLocator: By = this.getEditorActionArreaLocator(editorTabTitle);

        await this.driverHelper.type(interactionContainerLocator, text);
    }

    async type(editorTabTitle: string, text: string, line: number) {
        await this.moveCursorToLineAndChar(editorTabTitle, line, 1);
        await this.performKeyCombination(editorTabTitle, text);
    }

    private getEditorBodyLocator(editorTabTitle: string): By {
        const editorXpathLocator: string = `//div[@id='theia-main-content-panel']//div[contains(@class, 'monaco-editor')` +
            ` and contains(@data-uri, '${editorTabTitle}')]//*[contains(@class, 'lines-content')]`;

        return By.xpath(editorXpathLocator);
    }

    private getEditorActionArreaLocator(editorTabTitle: string): By {
        const editorActionArreaXpathLocator: string = `//div[@id='theia-main-content-panel']//div[contains(@class, 'monaco-editor')` +
            ` and contains(@data-uri, '${editorTabTitle}')]//textarea`;

        return By.xpath(editorActionArreaXpathLocator);
    }

    private getEditorLineXpathLocator(lineNumber: number): string {
        return `(//div[contains(@class,'lines-content')]//div[@class='view-lines']/div[@class='view-line'])[${lineNumber}]`;
    }

    private getSuggestionLineXpathLocator(suggestionText: string): string {
        return `//div[@widgetid='editor.widget.suggestWidget']//div[@class='monaco-list-row']//span[text()='${suggestionText}']`;
    }

    private getTabXpathLocator(tabTitle: string): string {
        return `//li[contains(@class, 'p-TabBar-tab')]//div[text()='${tabTitle}']`;
    }
}
