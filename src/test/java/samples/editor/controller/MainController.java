package samples.editor.controller;

import java.io.File;

import samples.editor.view.EditView;

/**
 * The MainController class is responsible for initializing the main view of the
 * SdaEdit application.
 */
public final class MainController {
	
	private final EditView editView; // the main view of the edit application

	
	/**
	 * Constructs a new MainController with the given file. If the file is not null,
	 * it attempts to load it into a new SdaDocument and display it in the view.
	 */
	public MainController(File file) {

		editView = new EditView(file);
	}
	
	
	/**
	 * Returns the main EditView of the application.
	 *
	 * @return the EditView instance
	 */
	public EditView getEditView() {
		return editView;
	}

}
