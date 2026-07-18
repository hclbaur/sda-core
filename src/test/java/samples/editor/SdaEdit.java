package samples.editor;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

import samples.editor.controller.MainController;

public final class SdaEdit {
	
	/**
	 * Main method to start the application.
	 * 
	 * @param args Command line arguments, where the first argument can be a file
	 *             path to open.
	 */
	public static void main(String[] args) {
		
        try { // Set look and feel or continue with default (Metal) 
        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {}
        
		final File file = args.length > 0 ? new File(args[0]) : null;
		final MainController controller = new MainController(file);
		
		EventQueue.invokeLater(() -> {
			JFrame frame = controller.getEditView();
			frame.setVisible(true);
		});
	}
}
