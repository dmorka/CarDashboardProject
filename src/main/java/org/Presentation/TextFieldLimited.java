package org.Presentation;

import javafx.scene.control.TextField;

/**
 * The type Text field limited.
 */
public class TextFieldLimited extends TextField {
    private int maxlength;

    /**
     * Instantiates a new Text field limited.
     */
    public TextFieldLimited() {
        this.maxlength = 3;
    }

    /**
     * Sets maxlength.
     *
     * @param maxlength the maxlength
     */
    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        // Delete or backspace user input.
        if (text.equals("")) {
            super.replaceText(start, end, text);
        } else if (getText().length() < maxlength) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        // Delete or backspace user input.
        if (text.equals("")) {
            super.replaceSelection(text);
        } else if (getText().length() < maxlength) {
            // Add characters, but don't exceed maxlength.
            if (text.length() > maxlength - getText().length()) {
                text = text.substring(0, maxlength - getText().length());
            }
            super.replaceSelection(text);
        }
    }
}
