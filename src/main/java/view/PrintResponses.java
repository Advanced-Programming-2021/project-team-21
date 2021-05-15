package view;

import controller.menu.Phases;
import module.Deck;
import module.Duel;
import module.User;
import module.card.Card;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

public class PrintResponses {
    public static void printInvalidFormat() {
        System.out.println(Responses.invalidFormat);
    }

    public static void printNoLoginYet() {
        System.out.println(Responses.noLoginYet);
    }

    public static void printMenuNavigationError() {
        System.out.println(Responses.menuNavigationError);
    }

    public static void printSuccessfulUserCreation() {
        System.out.println(Responses.successfulUserCreation);
    }

    public static void printNoUserExists() {
        System.out.println(Responses.noUserExists);
    }

    public static void printWrongPasswordInLogin() {
        System.out.println(Responses.wrongPasswordInLogin);
    }

    public static void printSuccessfulLogin() {
        System.out.println(Responses.successfulLogin);
    }

    public static void printSuccessfulLogout() {
        System.out.println(Responses.successfulLogout);
    }

    public static void printSuccessfulNicknameChange() {
        System.out.println(Responses.successfulNicknameChange);
    }

    public static void printSuccessfulPasswordChange() {
        System.out.println(Responses.successfulPasswordChange);
    }

    public static void printWrongPasswordInChange() {
        System.out.println(Responses.wrongPasswordInChange);
    }

    public static void printEqualityOfCurrentAndNewPassword() {
        System.out.println(Responses.equalityOfCurrentAndNewPassword);
    }

    public static void printSuccessfulDeckCreation() {
        System.out.println(Responses.successfulDeckCreation);
    }

    public static void printSuccessfulDeckDeletion() {
        System.out.println(Responses.successfulDeckDeletion);
    }

    public static void printSuccessfulDeckActivation() {
        System.out.println(Responses.successfulDeckActivation);
    }

    public static void printSuccessfulCardAddition() {
        System.out.println(Responses.successfulCardAddition);
    }

    public static void printSuccessfulCardRemoval() {
        System.out.println(Responses.successfulCardRemoval);
    }

    public static void printNoCardExistToBuy() {
        System.out.println(Responses.noCardExistToBuy);
    }

    public static void printLackOfMoney() {
        System.out.println(Responses.lackOfMoney);
    }

    public static void printNoUserExistToPlayWith() {
        System.out.println(Responses.noUserExistToPlayWith);
    }

    public static void printNonSupportiveRound() {
        System.out.println(Responses.nonSupportiveRound);
    }

    public static void printInvalidSelection() {
        System.out.println(Responses.invalidSelection);
    }

    public static void printSuccessfulCardSelection() {
        System.out.println(Responses.successfulCardSelection);
    }

    public static void printNoCardInPosition() {
        System.out.println(Responses.noCardInPosition);
    }

    public static void printNoCardSelected() {
        System.out.println(Responses.noCardSelected);
    }

    public static void printSuccessfulCardDeselection() {
        System.out.println(Responses.successfulCardDeselection);
    }

    public static void printUnableToSummonCard() {
        System.out.println(Responses.unableToSummonCard);
    }

    public static void printSummonInWrongPhase() {
        System.out.println(Responses.summonInWrongPhase);
    }

    public static void printFullnessOfMonsterCardZone() {
        System.out.println(Responses.fullnessOfMonsterCardZone);
    }

    public static void printUnableToSummonInTurn() {
        System.out.println(Responses.unableToSummonInTurn);
    }

    public static void printSuccessfulSummon() {
        System.out.println(Responses.successfulSummon);
    }

    public static void printNoCardToTribute() {
        System.out.println(Responses.noCardToTribute);
    }

    public static void printNoMonsterOnAddress() {
        System.out.println(Responses.noMonsterOnAddress);
    }

    public static void printNoMonsterOnAddresses() {
        System.out.println(Responses.noMonsterOnAddresses);
    }

    public static void printUnableToSetCard() {
        System.out.println(Responses.unableToSetCard);
    }

    public static void printSetCardInWrongPhase() {
        System.out.println(Responses.setCardInWrongPhase);
    }

    public static void printSuccessfulCardSetting() {
        System.out.println(Responses.successfulCardSetting);
    }

    public static void printUnableToChangePositionOfCard() {
        System.out.println(Responses.unableToChangePositionOfCard);
    }

    public static void printCardInWantedPosition() {
        System.out.println(Responses.cardInWantedPosition);
    }

    public static void printUnableToChangePositionInTurnTwice() {
        System.out.println(Responses.unableToChangePositionInTurnTwice);
    }

    public static void printChangeMonsterCardModeInWrongPhase() {
        System.out.println(Responses.changeMonsterCardModeInWrongPhase);
    }

    public static void printSuccessfulMonsterCardPositionChange() {
        System.out.println(Responses.successfulMonsterCardPositionChange);
    }

    public static void printUnableToFlipSummonCard() {
        System.out.println(Responses.unableToFlipSummonCard);
    }

    public static void printFlipSummonInWrongPhase() {
        System.out.println(Responses.flipSummonInWrongPhase);
    }

    public static void printSuccessfulFlipSummon() {
        System.out.println(Responses.successfulFlipSummon);
    }

    public static void printUnableToAttack() {
        System.out.println(Responses.unableToAttack);
    }

    public static void printAttackInWrongPhase() {
        System.out.println(Responses.attackInWrongPhase);
    }

    public static void printCardAttackedBefore() {
        System.out.println(Responses.cardAttackedBefore);
    }

    public static void printNoCardToAttackWith() {
        System.out.println(Responses.noCardToAttackWith);
    }

    public static void printBothCardsDestroyedInAttack() {
        System.out.println(Responses.destructionOfTwoSideCards);
    }

    public static void printOpponentMonsterInDefenceDestroyed() {
        System.out.println(Responses.destructionOfDefensePositionMonster);
    }


    public static void printNonSpellCardsToActivateEffect() {
        System.out.println(Responses.nonSpellCardsToActivateEffect);
    }

    public static void printUnableToActivateEffectOnTurn() {
        System.out.println(Responses.unableToActivateEffectOnTurn);
    }

    public static void printUnableToActivateCardTwice() {
        System.out.println(Responses.unableToActivateCardTwice);
    }

    public static void printFullnessOfSpellCardZone() {
        System.out.println(Responses.fullnessOfSpellCardZone);
    }

    public static void printUnfinishedPreparationOfSpell() {
        System.out.println(Responses.unfinishedPreparationOfSpell);
    }

    public static void printSuccessfulSpellActivation() {
        System.out.println(Responses.successfulSpellActivation);
    }

    public static void printSetSpellInWrongPhase() {
        System.out.println(Responses.setSpellInWrongPhase);
    }

    public static void printSuccessfulSpellSetting() {
        System.out.println(Responses.successfulSpellSetting);
    }

    public static void printAskToActivateSpellOrTrap() {
        System.out.println(Responses.askToActivateSpellOrTrap);
    }

    public static void printWrongTurnToActivateSpellOrTrap() {
        System.out.println(Responses.wrongTurnToActivateSpellOrTrap);
    }

    public static void printSuccessfulSpellOrTrapActivation() {
        System.out.println(Responses.successfulSpellOrTrapActivation);
    }

    public static void printUnableToRitualSummonMonster() {
        System.out.println(Responses.unableToRitualSummonMonster);
    }

    public static void printEmergencyRitualSummon() {
        System.out.println(Responses.emergencyRitualSummon);
    }

    public static void printInequalityOfLevelsOfSelectedAndRitualMonster() {
        System.out.println(Responses.inequalityOfLevelsOfSelectedAndRitualMonster);
    }

    public static void printUnableToSpecialSummonMonster() {
        System.out.println(Responses.unableToSpecialSummonMonster);
    }

    public static void printEmergencySpecialSummon() {
        System.out.println(Responses.emergencySpecialSummon);
    }

    public static void printEmptinessOfGraveyard() {
        System.out.println(Responses.emptinessOfGraveyard);
    }

    public static void printUnableToShowOpponentCard() {
        System.out.println(Responses.unableToShowOpponentCard);
    }

    public static void printLoginMenuShow() {
        System.out.println(Responses.loginMenuShow);
    }

    public static void printUserExistsWithUsername(String username) {
        System.out.println("user with username " + username + " already exists");
    }

    public static void printUserExistsWithNickname(String nickname) {
        System.out.println("user with nickname " + nickname + " already exists");
    }

    public static void printProfileMenuShow() {
        System.out.println(Responses.profileMenu);
    }

    public static void printMainMenuShow() {
        System.out.println(Responses.mainMenu);
    }

    public static void printDeckMenuShow() {
        System.out.println(Responses.deckMenu);
    }

    public static void printDuelMenuShow() {
        System.out.println(Responses.duelMenu);
    }

    public static void printExportAndImportMenuShow() {
        System.out.println(Responses.exportAndImportMenu);
    }

    public static void printScoreboardShow() {
        System.out.println(Responses.scoreBoard);
    }

    public static void printShopMenuShow() {
        System.out.println(Responses.shopMenu);
    }

    public static void printCardsInShop(String name, int price) {
        System.out.println(name + ": " + price);
    }

    public static void printACard(Card card) {
        System.out.println(card);
    }

    public static void printDeckExist(String deckName) {
        System.out.println("deck with name " + deckName + " already exists");
    }

    public static void printDeckNotExist(String deckName) {
        System.out.println("deck with name " + deckName + " does not exist");
    }

    public static void printCardNotExist(String cardName) {
        System.out.println("card with name " + cardName + " does not exist");
    }

    public static void printDeckFull(String type) {
        System.out.println(type + " deck is full");
    }

    public static void printInvalidAdd(String cardName, String deckName) {
        System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
    }

    public static void printNoCardTodDelete(String cardName, String type) {
        System.out.println("card with name " + cardName + " does not exist in " + type + " deck");
    }

    public static void printDeckShow(Deck deck) {
        System.out.println(deck.deckShow());
    }

    public static void printActive() {
        System.out.println(Responses.activeDeck);
    }

    public static void printOther() {
        System.out.println(Responses.otherDeck);
    }

    public static void printADeck(Deck deck, String type) {
        System.out.println(deck.toString(type));
    }

    public static void printAllCard(Card card) {
        System.out.println(card.getName() + ":" + card.getDescription());
    }

    public static void printScoreboard(int[] ranks, ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++) {
            System.out.println(ranks[i] + "- " + users.get(i).getNickname() + ": " + users.get(i).getScore());
        }
    }

    public static void printHasNoActiveDeck(User user) {
        System.out.println(user.getUsername() + " has no active deck");
    }

    public static void printInvalidDeck(User user) {
        System.out.println(user.getUsername() + "’s deck is invalid");
    }

    public static void printPhaseName(Phases phase) {
        System.out.println("phase: " + phase.toString().toLowerCase().replace("_", " "));
    }

    public static void printDrawnCard(Card card) {
        System.out.println("new card added to the hand : " + card.getName());
    }

    public static void showTurn(User user) {
        System.out.println("its " + user.getNickname() + "’s turn");
    }

    public static void printOpponentMonsterDestroyedWithDamage(int damage) {
        System.out.println("your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage");
    }

    public static void printOwnMonsterDestroyedInAttackWithDamage(int damage) {
        System.out.println("Your monster card is destroyed and you received " + damage + " battle damage");
    }

    public static void printNoCardDestroyedInDefence() {
        System.out.println(Responses.noDestruction);
    }

    public static void printNoCardDestroyedButReceivedDamage(int damage) {
        System.out.println("no card is destroyed and you received " + damage + " battle damage");
    }

    public static void printCardNameInAttackIfIsDefenceHide(String cardName){
        System.out.print("opponent’s monster card was "+ cardName +" and ");
    }

    public static void printDamageInAttackDirectly(int damage){
        System.out.println("you opponent receives " + damage + " battle damage");
    }

    public static void showGraveyard(String graveyardToShow){
        System.out.println(graveyardToShow);
    }

    public static void printSelectedCard(String selectedCardToShow){
        System.out.println(selectedCardToShow);
    }

    public static void printEndingTheGame(Pair<String, String> pair){
        System.out.println(pair.getFirst() + " won the game and the score is: " + pair.getSecond());
    }

    public static void printGameSuccessfullyCreated(){
        System.out.println(Responses.gameCreation);
    }

    public static void printBoard(Duel currentDuel){
        System.out.println(currentDuel);
    }
}
