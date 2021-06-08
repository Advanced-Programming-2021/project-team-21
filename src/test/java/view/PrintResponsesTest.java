package view;

import controller.menu.LoginMenu;
import controller.menu.Phases;
import javafx.util.Pair;
import module.Board;
import module.Deck;
import module.Duel;
import module.User;
import module.card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PrintResponsesTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void printInvalidFormat() {
        PrintResponses.printInvalidFormat();
        assertEquals(Responses.invalidFormat + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoLoginYet() {
        PrintResponses.printNoLoginYet();
        assertEquals(Responses.noLoginYet + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printMenuNavigationError() {
        PrintResponses.printMenuNavigationError();
        assertEquals(Responses.menuNavigationError + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulUserCreation() {
        PrintResponses.printSuccessfulUserCreation();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoUserExists() {
        PrintResponses.printNoUserExists();
        assertEquals(Responses.noUserExists + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printWrongPasswordInLogin() {
        PrintResponses.printWrongPasswordInLogin();
        assertEquals(Responses.wrongPasswordInLogin + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulLogin() {
        PrintResponses.printSuccessfulLogin();
        assertEquals(Responses.successfulLogin + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulLogout() {
        PrintResponses.printSuccessfulLogout();
        assertEquals(Responses.successfulLogout + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulNicknameChange() {
        PrintResponses.printSuccessfulNicknameChange();
        assertEquals(Responses.successfulNicknameChange + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulPasswordChange() {
        PrintResponses.printSuccessfulPasswordChange();
        assertEquals(Responses.successfulPasswordChange + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printWrongPasswordInChange() {
        PrintResponses.printWrongPasswordInChange();
        assertEquals(Responses.wrongPasswordInChange + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printEqualityOfCurrentAndNewPassword() {
        PrintResponses.printEqualityOfCurrentAndNewPassword();
        assertEquals(Responses.equalityOfCurrentAndNewPassword + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulDeckCreation() {
        PrintResponses.printSuccessfulDeckCreation();
        assertEquals(Responses.successfulDeckCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulDeckDeletion() {
        PrintResponses.printSuccessfulDeckDeletion();
        assertEquals(Responses.successfulDeckDeletion + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulDeckActivation() {
        PrintResponses.printSuccessfulDeckActivation();
        assertEquals(Responses.successfulDeckActivation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulCardAddition() {
        PrintResponses.printSuccessfulCardAddition();
        assertEquals(Responses.successfulCardAddition + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulCardRemoval() {
        PrintResponses.printSuccessfulCardRemoval();
        assertEquals(Responses.successfulCardRemoval + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardExistToBuy() {
        PrintResponses.printNoCardExistToBuy();
        assertEquals(Responses.noCardExistToBuy + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printLackOfMoney() {
        PrintResponses.printLackOfMoney();
        assertEquals(Responses.lackOfMoney + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoUserExistToPlayWith() {
        PrintResponses.printNoUserExistToPlayWith();
        assertEquals(Responses.noUserExistToPlayWith + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNonSupportiveRound() {
        PrintResponses.printNonSupportiveRound();
        assertEquals(Responses.nonSupportiveRound + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printInvalidSelection() {
        PrintResponses.printInvalidSelection();
        assertEquals(Responses.invalidSelection + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulCardSelection() {
        PrintResponses.printSuccessfulCardSelection();
        assertEquals(Responses.successfulCardSelection + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardInPosition() {
        PrintResponses.printNoCardInPosition();
        assertEquals(Responses.noCardInPosition + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardSelected() {
        PrintResponses.printNoCardSelected();
        assertEquals(Responses.noCardSelected + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulCardDeselection() {
        PrintResponses.printSuccessfulCardDeselection();
        assertEquals(Responses.successfulCardDeselection + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToSummonCard() {
        PrintResponses.printUnableToSummonCard();
        assertEquals(Responses.unableToSummonCard + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSummonInWrongPhase() {
        PrintResponses.printSummonInWrongPhase();
        assertEquals(Responses.summonInWrongPhase + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printFullnessOfMonsterCardZone() {
        PrintResponses.printFullnessOfMonsterCardZone();
        assertEquals(Responses.fullnessOfMonsterCardZone + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToSummonInTurn() {
        PrintResponses.printUnableToSummonInTurn();
        assertEquals(Responses.unableToSummonInTurn + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulSummon() {
        PrintResponses.printSuccessfulSummon();
        assertEquals(Responses.successfulSummon + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardToTribute() {
        PrintResponses.printNoCardToTribute();
        assertEquals(Responses.noCardToTribute + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoMonsterOnAddress() {
        PrintResponses.printNoMonsterOnAddress();
        assertEquals(Responses.noMonsterOnAddress + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoMonsterOnAddresses() {
        PrintResponses.printNoMonsterOnAddresses();
        assertEquals(Responses.noMonsterOnAddresses + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToSetCard() {
        PrintResponses.printUnableToSetCard();
        assertEquals(Responses.unableToSetCard + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSetCardInWrongPhase() {
        PrintResponses.printSetCardInWrongPhase();
        assertEquals(Responses.setCardInWrongPhase + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulCardSetting() {
        PrintResponses.printSuccessfulCardSetting();
        assertEquals(Responses.successfulCardSetting + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToChangePositionOfCard() {
        PrintResponses.printUnableToChangePositionOfCard();
        assertEquals(Responses.unableToChangePositionOfCard + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printCardInWantedPosition() {
        PrintResponses.printCardInWantedPosition();
        assertEquals(Responses.cardInWantedPosition + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToChangePositionInTurnTwice() {
        PrintResponses.printUnableToChangePositionInTurnTwice();
        assertEquals(Responses.unableToChangePositionInTurnTwice + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printChangeMonsterCardModeInWrongPhase() {
        PrintResponses.printChangeMonsterCardModeInWrongPhase();
        assertEquals(Responses.changeMonsterCardModeInWrongPhase + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulMonsterCardPositionChange() {
        PrintResponses.printSuccessfulMonsterCardPositionChange();
        assertEquals(Responses.successfulMonsterCardPositionChange + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToFlipSummonCard() {
        PrintResponses.printUnableToFlipSummonCard();
        assertEquals(Responses.unableToFlipSummonCard + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printFlipSummonInWrongPhase() {
        PrintResponses.printFlipSummonInWrongPhase();
        assertEquals(Responses.flipSummonInWrongPhase + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulFlipSummon() {
        PrintResponses.printSuccessfulFlipSummon();
        assertEquals(Responses.successfulFlipSummon + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToAttack() {
        PrintResponses.printUnableToAttack();
        assertEquals(Responses.unableToAttack + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printAttackInWrongPhase() {
        PrintResponses.printAttackInWrongPhase();
        assertEquals(Responses.attackInWrongPhase + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printCardAttackedBefore() {
        PrintResponses.printCardAttackedBefore();
        assertEquals(Responses.cardAttackedBefore + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardToAttackWith() {
        PrintResponses.printNoCardToAttackWith();
        assertEquals(Responses.noCardToAttackWith + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printBothCardsDestroyedInAttack() {
        PrintResponses.printBothCardsDestroyedInAttack();
        assertEquals(Responses.destructionOfTwoSideCards + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printOpponentMonsterInDefenceDestroyed() {
        PrintResponses.printOpponentMonsterInDefenceDestroyed();
        assertEquals(Responses.destructionOfDefensePositionMonster + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNonSpellCardsToActivateEffect() {
        PrintResponses.printNonSpellCardsToActivateEffect();
        assertEquals(Responses.nonSpellCardsToActivateEffect + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToActivateEffectOnTurn() {
        PrintResponses.printUnableToActivateEffectOnTurn();
        assertEquals(Responses.unableToActivateEffectOnTurn + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToActivateCardTwice() {
        PrintResponses.printUnableToActivateCardTwice();
        assertEquals(Responses.unableToActivateCardTwice + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printFullnessOfSpellCardZone() {
        PrintResponses.printFullnessOfSpellCardZone();
        assertEquals(Responses.fullnessOfSpellCardZone + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnfinishedPreparationOfSpell() {
        PrintResponses.printUnfinishedPreparationOfSpell();
        assertEquals(Responses.unfinishedPreparationOfSpell + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulSpellActivation() {
        PrintResponses.printSuccessfulSpellActivation();
        assertEquals(Responses.successfulSpellActivation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSetSpellInWrongPhase() {
        PrintResponses.printSetSpellInWrongPhase();
        assertEquals(Responses.setSpellInWrongPhase + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulSpellSetting() {
        PrintResponses.printSuccessfulSpellSetting();
        assertEquals(Responses.successfulSpellSetting + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printAskToActivateSpellOrTrap() {
        PrintResponses.printAskToActivateSpellOrTrap();
        assertEquals(Responses.askToActivateSpellOrTrap + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printWrongTurnToActivateSpellOrTrap() {
        PrintResponses.printWrongTurnToActivateSpellOrTrap();
        assertEquals(Responses.wrongTurnToActivateSpellOrTrap + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSuccessfulSpellOrTrapActivation() {
        PrintResponses.printSuccessfulSpellOrTrapActivation();
        assertEquals(Responses.successfulSpellOrTrapActivation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToRitualSummonMonster() {
        PrintResponses.printUnableToRitualSummonMonster();
        assertEquals(Responses.unableToRitualSummonMonster + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printEmergencyRitualSummon() {
        PrintResponses.printEmergencyRitualSummon();
        assertEquals(Responses.emergencyRitualSummon + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printInequalityOfLevelsOfSelectedAndRitualMonster() {
        PrintResponses.printInequalityOfLevelsOfSelectedAndRitualMonster();
        assertEquals(Responses.inequalityOfLevelsOfSelectedAndRitualMonster + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToSpecialSummonMonster() {
        PrintResponses.printUnableToSpecialSummonMonster();
        assertEquals(Responses.unableToSpecialSummonMonster + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printEmergencySpecialSummon() {
        PrintResponses.printEmergencySpecialSummon();
        assertEquals(Responses.emergencySpecialSummon + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printEmptinessOfGraveyard() {
        PrintResponses.printEmptinessOfGraveyard();
        assertEquals(Responses.emptinessOfGraveyard + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUnableToShowOpponentCard() {
        PrintResponses.printUnableToShowOpponentCard();
        assertEquals(Responses.unableToShowOpponentCard + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printLoginMenuShow() {
        PrintResponses.printLoginMenuShow();
        assertEquals(Responses.loginMenuShow + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUserExistsWithUsername() {
        PrintResponses.printUserExistsWithUsername("aaa");
        assertEquals("user with username aaa already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printUserExistsWithNickname() {
        PrintResponses.printUserExistsWithNickname("aaa");
        assertEquals("user with nickname aaa already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printProfileMenuShow() {
        PrintResponses.printProfileMenuShow();
        assertEquals(Responses.profileMenu + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printMainMenuShow() {
        PrintResponses.printMainMenuShow();
        assertEquals(Responses.mainMenu + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printDeckMenuShow() {
        PrintResponses.printDeckMenuShow();
        assertEquals(Responses.deckMenu + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printDuelMenuShow() {
        PrintResponses.printDuelMenuShow();
        assertEquals(Responses.duelMenu + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printExportAndImportMenuShow() {
        PrintResponses.printExportAndImportMenuShow();
        assertEquals(Responses.exportAndImportMenu + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printScoreboardShow() {
        PrintResponses.printScoreboardShow();
        assertEquals(Responses.scoreBoard + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printShopMenuShow() {
        PrintResponses.printShopMenuShow();
        assertEquals(Responses.shopMenu + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printCardsInShop() {
        PrintResponses.printCardsInShop("Taylor", 1000);
        assertEquals("Taylor: 1000" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printACard() {
        Card card = new Card();
        PrintResponses.printACard(card);
        assertEquals(card + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printDeckExist() {
        PrintResponses.printDeckExist("aaa");
        assertEquals("deck with name aaa already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printDeckNotExist() {
        PrintResponses.printDeckNotExist("aaa");
        assertEquals("deck with name aaa does not exist" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printCardNotExist() {
        PrintResponses.printCardNotExist("aaa");
        assertEquals("card with name aaa does not exist" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printDeckFull() {
        PrintResponses.printDeckFull("aaa");
        assertEquals("aaa deck is full" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printInvalidAdd() {
        PrintResponses.printInvalidAdd("aaa", "bbb");
        assertEquals("there are already three cards with name aaa in deck bbb" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardTodDelete() {
        PrintResponses.printNoCardTodDelete("aaa", "bbb");
        assertEquals("card with name aaa does not exist in bbb deck" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printDeckShow() {
        Deck deck = new Deck("aaa");
        PrintResponses.printDeckShow(deck);
        assertEquals(deck.deckShow() + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printActive() {
        PrintResponses.printActive();
        assertEquals(Responses.activeDeck + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printOther() {
        PrintResponses.printOther();
        assertEquals(Responses.otherDeck + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printADeck() {
        Deck deck = new Deck("aaa");
        PrintResponses.printADeck(deck, "bbb");
        assertEquals(deck.toString("bbb") + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printAllCard() {
        Card card = new Card();
        PrintResponses.printAllCard(card);
        assertEquals(card.getName() + ":" + card.getDescription() + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printScoreboard() {
        int[] rank = {1, 2, 3};
        ArrayList<User> users = new ArrayList<>();
        new LoginMenu().run("user create --username printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers1 --nickname printScoreboardT1yl0rSw1ftIs179C3nt1m3t3ers1 --password 1989");
        new LoginMenu().run("user create --username printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers2 --nickname printScoreboardT1yl0rSw1ftIs179C3nt1m3t3ers2 --password 1989");
        new LoginMenu().run("user create --username printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers3 --nickname printScoreboardT1yl0rSw1ftIs179C3nt1m3t3ers3 --password 1989");
        users.add(User.getUserByUsername("printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers1"));
        users.add(User.getUserByUsername("printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers2"));
        users.add(User.getUserByUsername("printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers3"));
        new File("src/main/resources/users/printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers1.user.json").delete();
        new File("src/main/resources/users/printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers2.user.json").delete();
        new File("src/main/resources/users/printScoreboard1T1yl0rSw1ftIs179C3nt1m3t3ers3.user.json").delete();
        PrintResponses.printScoreboard(rank, users);
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulUserCreation + System.lineSeparator() + "1- printScoreboardT1yl0rSw1ftIs179C3nt1m3t3ers1: 0" + System.lineSeparator() + "2- printScoreboardT1yl0rSw1ftIs179C3nt1m3t3ers2: 0" + System.lineSeparator() + "3- printScoreboardT1yl0rSw1ftIs179C3nt1m3t3ers3: 0" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printHasNoActiveDeck() {
        new LoginMenu().run("user create --username printHasNoActiveDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1 --nickname printHasNoActiveDeckT1yl0rSw1ftIs179C3nt1m3t3ers1 --password 1989");
        PrintResponses.printHasNoActiveDeck(User.getUserByUsername("printHasNoActiveDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1"));
        new File("src/main/resources/users/printHasNoActiveDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + "printHasNoActiveDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1 hasNoActiveDeck" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printInvalidDeck() {
        new LoginMenu().run("user create --username printInvalidDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1 --nickname printInvalidDeckT1yl0rSw1ftIs179C3nt1m3t3ers1 --password 1989");
        PrintResponses.printInvalidDeck(User.getUserByUsername("printInvalidDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1"));
        new File("src/main/resources/users/printInvalidDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + "printInvalidDeck1T1yl0rSw1ftIs179C3nt1m3t3ers1’s deck is invalid" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printPhaseName() {
        PrintResponses.printPhaseName(Phases.BATTLE_PHASE);
        assertEquals("phase: " + Phases.BATTLE_PHASE.toString().toLowerCase().replace("_", " ") + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printDrawnCard() {
        Card card = new Card();
        PrintResponses.printDrawnCard(card);
        assertEquals("new card added to the hand : " + card.getName() + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void showTurn() {
        new LoginMenu().run("user create --username showTurn1T1yl0rSw1ftIs179C3nt1m3t3ers1 --nickname showTurnT1yl0rSw1ftIs179C3nt1m3t3ers1 --password 1989");
        PrintResponses.showTurn(User.getUserByUsername("showTurn1T1yl0rSw1ftIs179C3nt1m3t3ers1"));
        new File("src/main/resources/users/showTurn1T1yl0rSw1ftIs179C3nt1m3t3ers1.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + "its " + User.getUserByUsername("showTurn1T1yl0rSw1ftIs179C3nt1m3t3ers1").getNickname() + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printOpponentMonsterDestroyedWithDamage() {
        PrintResponses.printOpponentMonsterDestroyedWithDamage(1);
        assertEquals("your opponent’s monster is destroyed and your opponent receives 1 battle damage" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printOwnMonsterDestroyedInAttackWithDamage() {
        PrintResponses.printOwnMonsterDestroyedInAttackWithDamage(1);
        assertEquals("Your monster card is destroyed and you received 1 battle damage" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardDestroyedInDefence() {
        PrintResponses.printNoCardDestroyedInDefence();
        assertEquals(Responses.noDestruction + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printNoCardDestroyedButReceivedDamage() {
        PrintResponses.printNoCardDestroyedButReceivedDamage(1);
        assertEquals("no card is destroyed and you received 1 battle damage" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printCardNameInAttackIfIsDefenceHide() {
        PrintResponses.printCardNameInAttackIfIsDefenceHide("aaa");
        assertEquals("opponent’s monster card was aaa and ", outputStreamCaptor.toString());
    }

    @Test
    public void printDamageInAttackDirectly() {
        PrintResponses.printDamageInAttackDirectly(1);
        assertEquals("you opponent receives 1 battle damage" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void showGraveyard() {
        PrintResponses.showGraveyard("aaa");
        assertEquals("aaa" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printSelectedCard() {
        PrintResponses.printSelectedCard("aaa");
        assertEquals("aaa" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    //public void printEndingTheGame() {
      //  Pair pair = new Pair("aaa", "bbb");
      //  PrintResponses.printEndingTheGame("aaa","bbb" pair);
      //  assertEquals(pair.getFirst() + " won the game and the score is: " + pair.getSecond() + System.lineSeparator(), outputStreamCaptor.toString());
   // }

    @Test
    public void printGameSuccessfullyCreated() {
        PrintResponses.printGameSuccessfullyCreated();
        assertEquals(Responses.gameCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printBoard() {
        new LoginMenu().run("user create --username printBoard1T1yl0rSw1ftIs179C3nt1m3t3ers1 --nickname printBoardT1yl0rSw1ftIs179C3nt1m3t3ers1 --password 1989");
        new LoginMenu().run("user create --username printBoard1T1yl0rSw1ftIs179C3nt1m3t3ers2 --nickname printBoardT1yl0rSw1ftIs179C3nt1m3t3ers2 --password 1989");
        Duel duel = new Duel(User.getUserByUsername("printBoard1T1yl0rSw1ftIs179C3nt1m3t3ers1"), User.getUserByUsername("printBoard1T1yl0rSw1ftIs179C3nt1m3t3ers2"));
        PrintResponses.printBoard(duel);
        new File("src/main/resources/users/printBoard1T1yl0rSw1ftIs179C3nt1m3t3ers1.user.json").delete();
        new File("src/main/resources/users/printBoard1T1yl0rSw1ftIs179C3nt1m3t3ers2.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulUserCreation + System.lineSeparator() + duel + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void printRoundNumber() {
        int remainingRounds = 1;
        PrintResponses.printRoundNumber(remainingRounds);
        assertEquals("Round " + (remainingRounds - 3) + " started" + System.lineSeparator(), outputStreamCaptor.toString());
    }








}