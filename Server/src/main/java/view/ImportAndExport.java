package view;

public class ImportAndExport implements Menuable{
    @Override
    public String run(String command) {
        return null;
    }

   /* public void exportCard() {
        if (cardToExport == null) {
            return;
        }
        Card card = Card.getCardByName(cardToExport);
        DataController.saveData(card);
        cardToExport = null;
    }

    public void importCard() {
        if (cardToImport == null) {
            return;
        }
        Card card = DataController.importCardFromJson(cardToImport.getPath());
        ProgramController.userInGame.addCard(card);
    }*/

}