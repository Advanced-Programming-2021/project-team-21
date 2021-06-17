package controller.menu;

import controller.ProgramController;
import view.PrintResponses;

public class ImportAndExport implements Menuable {

    @Override
    public void showMenu() {
        PrintResponses.printExportAndImportMenuShow();
    }

}
