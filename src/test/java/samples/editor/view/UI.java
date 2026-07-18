package samples.editor.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * The {@code UI} class provides static utility methods for creating and
 * manipulating Swing components and dimensions. It serves as a centralized
 * helper for consistent UI scaling and component initialization.
 */
public final class UI {

	/** The size of the primary screen. */
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

	
	private UI() { /* cannot instantiate */ }


	/**
	 * Creates a new {@code Dimension} object based on a source dimension scaled by
	 * specific width and height factors.
	 * 
	 * @param dimension   The base dimension to scale.
	 * @param scaleWidth  The factor to multiply the width (e.g. 0.5 for 50%).
	 * @param scaleHeight The factor to multiply the height.
	 * @return A new {@code Dimension} instance with scaled values.
	 */
	public static Dimension scaledDim(Dimension dimension, Double scaleWidth, Double scaleHeight) {
		return new Dimension(
			(int) (dimension.width * scaleWidth), (int) (dimension.height * scaleHeight)
		);
	}


	/**
	 * Creates a new {@code Dimension} object scaled uniformly by a single factor.
	 * 
	 * @param dimension The base dimension to scale.
	 * @param scale     The factor to multiply both width and height.
	 * @return A new {@code Dimension} instance with scaled values.
	 */
	public static Dimension scaledDim(Dimension dimension, Double scale) {
		return scaledDim(dimension, scale, scale);
	}


	/**
	 * Factory method to initialize a {@code JTextArea} with common configuration.
	 * 
	 * @param edit True if the text area should be user-editable.
	 * @param wrap True if line wrapping should be enabled.
	 * @return A configured {@code JTextArea} instance.
	 */
	public static JTextArea newTextArea(boolean edit, boolean wrap) {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(edit);	textArea.setLineWrap(wrap);
		textArea.setWrapStyleWord(true); 
		return textArea;
	}


	/**
	 * Displays an error message dialog containing a scrollable text area. This is
	 * useful for displaying long stack traces or detailed error descriptions.
	 * 
	 * @param parent The parent component used to position the dialog.
	 * @param title  The title string for the dialog.
	 * @param text   The error message text to display.
	 */
	public static void showErrorDialog(Component parent, String title, String text) {
		
		JTextArea textArea = newTextArea(false, true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(scaledDim(parent.getSize(), 0.5));
		textArea.setText(text);
		JOptionPane.showMessageDialog(parent, 
			scrollPane, title, JOptionPane.ERROR_MESSAGE);
	}


	/**
	 * Displays an error message dialog for a given exception, including its cause
	 * if available.
	 * 
	 * @param parent The parent component used to position the dialog.
	 * @param title  The title string for the dialog.
	 * @param e      The exception whose message and cause will be displayed.
	 */
	public static void showExceptionDialog(Component parent, String title, Exception exception) {
		
        String message = exception.toString();
        var cause = exception.getCause();
        if (cause != null)
            message = message + "\n\n(caused by)\n\n" + cause.toString();
        showErrorDialog(parent, title, message);
	}
	

	/**
	 * Displays an informational message dialog with the specified title and text.
	 * 
	 * @param parent The parent component used to position the dialog.
	 * @param title  The title string for the dialog.
	 * @param text   The informational message text to display.
	 */
	public static void showInfoDialog(Component parent, String title, String text) {

		JOptionPane.showMessageDialog(parent, 
			text, title, JOptionPane.INFORMATION_MESSAGE);
	}

	
	/**
	 * Displays a warning message dialog with the specified title and text.
	 * 
	 * @param parent The parent component used to position the dialog.
	 * @param title  The title string for the dialog.
	 * @param text   The warning message text to display.
	 */
	public static void showWarningDialog(Component parent, String title, String text) {

		JOptionPane.showMessageDialog(parent, 
			text, title, JOptionPane.WARNING_MESSAGE);
	}
}
