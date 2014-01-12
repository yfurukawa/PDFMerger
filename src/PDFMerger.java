ockage gui;

import java.io.File;

/**
*
* @author YFurukawa
*
*/
public class MainWindow {

	private static Text outputFile; /** 出力ファイル名. */
	private static Text inputDirectory; /** 元のPDFファイルの格納フォルダ
名. */

	/**
	 * Launch the application.
	 * @param args コマンドライン引数
	 */
	public static void main(final String[] args) {
		final int shellX = 450;
		final int shellY = 175;

		final int buttonX = 72;
		final int buttonY = 22;

		final int combineButtonPositionX = 282;
		final int cancelButtonPositionX = 360;
		final int buttonPositionY = 115;

		final int refButtonX = 45;
		final int refButtonY = 22;
		final int refButtonPositionX = 370;
		final int folderRefButtonPositionY = 34;
		final int fileRefButtonPositionY = 66;
		final int lavelX = 91;
		final int lavelY = 12;
		final int lavelPositionX = 10;
		final int folderLavelPositionY = 35;
		final int fileLavelPositionY = 71;
		final int textBoxX = 250;
		final int textBoxY = 22;
		final int textBoxPositionX = 106;
		final int folderTextBoxPositionY = 32;
		final int fileTextBoxPositionY = 68;


		Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setSize(shellX, shellY);
		shell.setText("SWT Application");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(cancelButtonPositionX, buttonPositionY,
				buttonX, buttonY);
		btnCancel.setText("キャンセル");

		Button btnCombine = new Button(shell, SWT.NONE);
		btnCombine.setSelection(true);
		btnCombine.setBounds(combineButtonPositionX, buttonPositionY,
				buttonX, buttonY);
		btnCombine.setText("統合");

		Label lblOutputfilename = new Label(shell, SWT.NONE);
		lblOutputfilename.setBounds(lavelPositionX, fileLavelPositionY,
				lavelX, lavelY);
		lblOutputfilename.setText("出力ファイル名");

		outputFile = new Text(shell, SWT.BORDER);
		outputFile.setBounds(textBoxPositionX, fileTextBoxPositionY,
				textBoxX, textBoxY);

		Button btnReferoutout = new Button(shell, SWT.NONE);
		btnReferoutout.setBounds(refButtonPositionX, fileRefButtonPositionY,
				refButtonX, refButtonY);
		btnReferoutout.setText("参照");

		Label lblPartfile = new Label(shell, SWT.NONE);
		lblPartfile.setBounds(lavelPositionX, folderLavelPositionY,
				lavelX, lavelY);
		lblPartfile.setText("フォルダ");

		Button btnReferinput = new Button(shell, SWT.NONE);
		btnReferinput.setBounds(refButtonPositionX, folderRefButtonPositionY,
				refButtonX, refButtonY);
		btnReferinput.setText("参照");

		inputDirectory = new Text(shell, SWT.BORDER | SWT.MULTI);
		inputDirectory.setTouchEnabled(true);
		inputDirectory.setBounds(textBoxPositionX, folderTextBoxPositionY,
				textBoxX, textBoxY);

		btnCancel.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				 shell.dispose();
			}
		});

		btnCombine.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				PDFMergerUtility mergePdf = new PDFMergerUtility();
				String inputFolder = inputDirectory.getText();
				String outFile = outputFile.getText();

				try {
					String[] files = getFiles(inputFolder);

					for (int i = 0; i < files.length; i++) {
						mergePdf.addSource(inputFolder + "\\" + files[i]);
					}
					mergePdf.setDestinationFileName(outFile);
					mergePdf.mergeDocuments();
					MessageBox finishMessage = new MessageBox (shell, SWT.OK);
					finishMessage.setMessage("ファイルの統合が完了しました");
					finishMessage.open();
					shell.dispose();
				}
				catch (IOException e) {
					System.out.println(e.toString());
				}
				catch (COSVisitorException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		});

		btnReferinput.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				DirectoryDialog dialog = new DirectoryDialog (shell);
				String folderPath = dialog.open();
				inputDirectory.setText(folderPath);
				outputFile.setText(folderPath + "\\" + "Output.pdf");
			}
		});

		btnReferoutout.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
				outputFile.setText(fileDialog.open());
			}
		});

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	/**
	 *
	 * @param folder マージ元のPDFファイルが格納されているフォルダ
	 * @return PDFファイルのリスト
	 * @throws IOException 引数がフォルダでない場合の例外
	 */
	private static String[] getFiles(final String folder) throws IOException {
		File inFolder = new File(folder);
		String[] filesInFolder;
		if (inFolder.isDirectory()) {
			filesInFolder = inFolder.list();
			return filesInFolder;
		}
		else {
			throw new IOException("Path is not a directory");
		}
	}
}

