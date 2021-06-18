package controller.menu;

import controller.Effects.SelectEffect;
import controller.ProgramController;
import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;
import module.card.Spell;
import module.card.enums.CardType;
import org.apache.commons.math3.util.Pair;
import view.PrintResponses;
import view.Regex;
import view.Responses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;

public class DuelMenu implements Menuable {
    public static ArrayList<Monster> specialSummonsedCards;
    public static boolean isForScan;
    public static boolean isGetFromGY;
    public static boolean isGetFromDeck;
    public static boolean isGetFromHand;
    public static boolean addToHand;
    public static boolean isForSet;
    public static boolean isGetFroOpponentGY;
    private Duel currentDuel;
    private Phases phase;
    private boolean isInGame;
    private int remainingRounds;
    private int initialRounds;
    private boolean isFirstRound = true;

    {
        isInGame = true;
    }

    public static Monster getMonsterForEquip(Duel duel, Spell spell) {
        PrintResponses.printChooseEquip();
        int number;
        while (true) {
            try {
                number = Integer.parseInt(ProgramController.scanner.nextLine());
                if (number > 5 || number < 1) {
                    PrintResponses.printWrongChoice();
                    continue;
                }
                if (duel.getUserWhoPlaysNow().getBoard().getCard(number, 'm') == null) continue;
            } catch (Exception e) {
                PrintResponses.printWrongChoice();
                continue;
            }
            break;
        }
        spell.setEquippedPlace(number);
        return (Monster) duel.getUserWhoPlaysNow().getBoard().getCard(number, 'm');
    }


    public void run(String command) {
        if (checkSpecialSummon(command, currentDuel)) return;
        HashMap<String, Consumer<Matcher>> commandMap = createCommandMap();
        boolean isValidCommand = false;

        try {
            for (String string : commandMap.keySet()) {
                Matcher matcher = Regex.getMatcher(command, string);
                if (currentDuel != null && currentDuel.isGameEnded()) {
                    endTheGame();
                    return;
                } else if (matcher.find() && isInGame) {
                    isValidCommand = true;
                    commandMap.get(string).accept(matcher);
                } else if (!isInGame) {
                    if (command.equals("back")) {
                        back();
                        isValidCommand = true;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        if (!isValidCommand)
            PrintResponses.printInvalidFormat();
    }

    public static boolean checkSpecialSummon(String command, Duel currentDuel) {
        Matcher matcher;
        if (specialSummonsedCards != null) {
            PrintResponses.printSpecialSummonCards(specialSummonsedCards);
            if (!(matcher = Regex.getMatcher(command, Regex.specialSummon)).matches()) {
                PrintResponses.printEmergencySpecialSummon();
                return true;
            }
            if (specialSummonsedCards.size() == 0) {
                PrintResponses.printUnableToSpecialSummonMonster();
                return true;
            }
            int number = Integer.parseInt(matcher.group("cardNumber"));
            if (number < 0 || number > specialSummonsedCards.size()) {
                PrintResponses.printWrongChoice();
                return true;
            }
            Monster monster = specialSummonsedCards.get(number);
            if (isForScan) {
                if (SelectEffect.scannerHolder.isATK())
                    currentDuel.getUserWhoPlaysNow().getBoard().addMonsterFaceUp(SelectEffect.scannerPlace, monster);
                else currentDuel.getUserWhoPlaysNow().getBoard().addMonsterFaceDown(SelectEffect.scannerPlace, monster);
                isForScan = false;
            } else if (addToHand) {
                currentDuel.getUserWhoPlaysNow().getHand().addCardToHand(monster);
                addToHand = false;
                specialSummonsedCards = null;
            } else if (isForSet) {
                currentDuel.setSelectedCard(monster);
                if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
                    PrintResponses.printUnableToSpecialSummonMonster();
                    return true;
                }
                removeTheSpecialSummoned(monster, currentDuel);
                int place = currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon();
                currentDuel.setMonster();
                currentDuel.flipSetForMonsters(place);
            } else {
                currentDuel.setSelectedCard(monster);
                if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
                    PrintResponses.printUnableToSpecialSummonMonster();
                    return true;
                }
                removeTheSpecialSummoned(monster, currentDuel);
                currentDuel.summonMonster();
            }
            specialSummonsedCards = null;
            return true;
        }
        return false;
    }

    public static void removeTheSpecialSummoned(Monster monster, Duel currentDuel) {
        boolean found = false;
        if (isGetFromGY) {
            for (Card card : currentDuel.getUserWhoPlaysNow().getBoard().getGraveyard()) {
                if (card.getName().equals(monster.getName())) {
                    currentDuel.getUserWhoPlaysNow().getBoard().removeFromGY(card.getName());
                    found = true;
                    break;
                }
            }
        }
        if (isGetFromDeck && !found) {
            for (Card mainDeckCard : currentDuel.getUserWhoPlaysNow().getHand().getDeckToDraw().getMainDeckCards()) {
                if (mainDeckCard.getName().equals(monster.getName())) {
                    currentDuel.getUserWhoPlaysNow().getHand().removeFromDeck(monster.getName());
                    found = true;
                    break;
                }
            }
        }
        if (isGetFromHand && !found) {
            Card[] cards = currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand();
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].getName().equals(monster.getName())) {
                    currentDuel.getUserWhoPlaysNow().getHand().removeCardFromHand(i);
                    break;
                }
            }
        }
        isGetFromHand = false;
        isGetFromDeck = false;
        isGetFromGY = false;
    }

    private void back() {
        isInGame = true;
    }


    private HashMap<String, Consumer<Matcher>> createCommandMap() {
        HashMap<String, Consumer<Matcher>> commandMap = new HashMap<>();
        commandMap.put(Regex.createNewDuel, this::createNewDuel);
        commandMap.put(Regex.createNewDuelWithAI, this::createNewDuelWithAI);
        commandMap.put(Regex.selectFromOwn, this::selectCardFromOwn);
        commandMap.put(Regex.selectFromOpponent, this::selectCardFromOpponent);
        commandMap.put(Regex.deselectCard, this::deselectCard);
        commandMap.put(Regex.nextPhase, this::goToNextPhase);
        commandMap.put(Regex.summon, this::summon);
        commandMap.put(Regex.set, this::set);
        commandMap.put(Regex.setPosition, this::setPosition);
        commandMap.put(Regex.flipSummon, this::flipSummon);
        commandMap.put(Regex.attack, this::attack);
        commandMap.put(Regex.attackDirectly, this::attackDirectly);
        commandMap.put(Regex.activateSpell, this::activateSpell);
        commandMap.put(Regex.showGraveyard, this::showGraveyard);
        commandMap.put(Regex.showSelectedCard, this::showSelectedCard);
        commandMap.put(Regex.surrender, this::surrender);
        commandMap.put(Regex.increaseLP, this::increaseLP);
        commandMap.put(Regex.setWinner, this::setWinner);
        commandMap.put(Regex.forceSelectHand, this::forceSelectHand);
        return commandMap;
    }


    private void createNewDuel(Matcher matcher) {
        if (currentDuel != null) {
            PrintResponses.print(Responses.alreadyInGame);
            return;
        }
        User secondPlayer = User.getUserByUsername(matcher.group("player2Username"));
        remainingRounds = Integer.parseInt(matcher.group("rounds"));
        initialRounds = remainingRounds;
        if (secondPlayer == null) {
            PrintResponses.printNoUserExistToPlayWith();
        } else if (ProgramController.userInGame.getActiveDeck() == null) {
            PrintResponses.printHasNoActiveDeck(ProgramController.userInGame);
        } else if (secondPlayer.getActiveDeck() == null) {
            PrintResponses.printHasNoActiveDeck(secondPlayer);
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(ProgramController.userInGame);
        } else if (!secondPlayer.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(secondPlayer);
        } else if (!(remainingRounds == 1 || remainingRounds == 3)) {
            PrintResponses.printNonSupportiveRound();
        } else {
            handleSuccessfulGameCreation(secondPlayer);
        }
    }

    private void createNewDuelWithAI(Matcher matcher) {

    }

    private void selectCardFromOwn(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("field")
                || whereToSelectFrom.equals("hand"))) {
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "own");
            if (currentDuel.getSelectedCard() == null) {
                PrintResponses.printNoCardInPosition();
            } else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void selectCardFromOpponent(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("field"))) {
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "opponent");
            if (currentDuel.getSelectedCard() == null) {
                PrintResponses.printNoCardInPosition();
            } else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void deselectCard(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else {
            currentDuel.deselectACard();
            PrintResponses.printSuccessfulCardDeselection();
        }
    }

    private void goToNextPhase(Matcher matcher) {
        if (currentDuel == null) {
            PrintResponses.printInvalidFormat();
            return;
        }
        if (phase.equals(Phases.DRAW_PHASE)) {
            phase = Phases.STANDBY_PHASE;
        } else if (phase.equals(Phases.STANDBY_PHASE)) {
            phase = Phases.MAIN_PHASE1;
        } else if (phase.equals(Phases.MAIN_PHASE1)) {
            if (currentDuel.getNumberOfTurnsPlayedUpToNow() != 0)
                phase = Phases.BATTLE_PHASE;
            else {
                handleTransitionFromEndPhaseToDrawPhase();
                handleDrawingACard(currentDuel.drawACard());
                return;
            }
        } else if (phase.equals(Phases.BATTLE_PHASE)) {
            phase = Phases.MAIN_PHASE2;
        } else if (phase.equals(Phases.MAIN_PHASE2)) {
            handleTransitionFromEndPhaseToDrawPhase();
            handleDrawingACard(currentDuel.drawACard());
            return;
        }
        PrintResponses.printPhaseName(phase);
    }

    private void endTheGame() {
        remainingRounds--;
        if (remainingRounds == 0) {
            if (initialRounds == 1)
                PrintResponses.printEndingTheGame(currentDuel.handleEndingGame());
            else
                PrintResponses.printEndingTheWholeMatch(currentDuel.handleEndingTheWholeMatch());
            currentDuel = null;
        } else {
            User winner = currentDuel.handleEndingARound();
            PrintResponses.printWinnerInRound(winner);
            if (winner.getWinsInAMatch() == 2) {
                endTheGame();
            } else
                handleSuccessfulGameCreation(currentDuel.getSECOND_USER());
        }
    }

    private void summon(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (currentDuel.canNotSummonSelectedCard()) {
            PrintResponses.printUnableToSummonCard();
        } else if (isNotInMainPhases()) {
            PrintResponses.printSummonInWrongPhase();
        } else if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
            PrintResponses.printFullnessOfMonsterCardZone();
        } else if (currentDuel.isHasSummonedOrSetOnce()) {
            PrintResponses.printUnableToSummonInTurn();
        } else if (currentDuel.getUserWhoPlaysNow().isCanSummonMonster()) {
            PrintResponses.printDisabledSummonMonster();
        } else if (((Monster) currentDuel.getSelectedCard()).getLevel() > 4) {
            Monster monster = (Monster) currentDuel.getSelectedCard();
            if (monster.isCanHaveDifferentTribute()) {
                PrintResponses.printChooseTribute();
                int number = Integer.parseInt(ProgramController.scanner.nextLine());
                if (number > 3 || number < 0) {
                    PrintResponses.printWrongTribute();
                    return;
                }
                if (number == 0 && monster.getCanBeNotTribute().hasEffect()) {
                    monster.setAtk(monster.getAtk() - monster.getCanBeNotTribute().getEffectNumber());
                    currentDuel.summonMonster();
                    PrintResponses.printSuccessfulSummon();
                    return;
                }
                if (number == 0 && monster.getDiscardToSpecialSummon().hasEffect()) {
                    Card card = currentDuel.getUserWhoPlaysNow().getHand().selectARandomCardFromHand();
                    int i;
                    for (i = 0; i < currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand().length; i++) {
                        if (card == currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand()[i])
                            break;
                    }
                    currentDuel.getUserWhoPlaysNow().getHand().discardACard(i);
                    currentDuel.summonMonster();
                    PrintResponses.printSuccessfulSummon();
                    return;
                }
                monster.setRequiredCardsFOrTribute(number);
            }
            if (monster.getLevel() < 7) {
                if (isNotEnoughCardsForTribute(monster.getRequiredCardsFOrTribute())) return;
            } else if (monster.getLevel() < 10) {
                if (isNotEnoughCardsForTribute(monster.getRequiredCardsFOrTribute())) return;
            } else {
                if (isNotEnoughCardsForTribute(monster.getRequiredCardsFOrTribute())) return;
            }
            int[] cardToTributeAddress = Arrays.stream(ProgramController.scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (areCardAddressesEmpty(cardToTributeAddress)) return;
            currentDuel.tribute(cardToTributeAddress);
            if (monster.getTributeToKillAllMonsterOfOpponent().hasEffect() && monster.getRequiredCardsFOrTribute() == monster.getTributeToKillAllMonsterOfOpponent().getEffectNumber()) {
                for (int i = 1; i < 6; i++) {
                    Card card = currentDuel.getRival().getBoard().getCard(i, 'm');
                    if (card != null) currentDuel.addCardToGraveyard(card, i, currentDuel.getRival());
                }
            }
            currentDuel.summonMonster();
            PrintResponses.printSuccessfulSummon();
        } else {
            currentDuel.summonMonster();
            PrintResponses.printSuccessfulSummon();
        }


    }

        private void set (Matcher matcher){
            if (currentDuel.isNoCardSelected()) {
                PrintResponses.printNoCardSelected();
            } else if (!currentDuel.getUserWhoPlaysNow().getHand().isCardInHand(currentDuel.getSelectedCard())) {
                PrintResponses.printUnableToSetCard();
            } else if (isNotInMainPhases()) {
                PrintResponses.printSetCardInWrongPhase();
            } else if (currentDuel.getSelectedCard() instanceof Monster) {
                handleMonsterSet();
            } else {
                handleSpellAndTrapSet();
            }
        }

        private void setPosition (Matcher matcher){
            String position = matcher.group("position");
            if (currentDuel.isNoCardSelected()) {
                PrintResponses.printNoCardSelected();
            } else if (isSelectedCardNotInMonsterZone()) {
                PrintResponses.printUnableToChangePositionOfCard();
            } else if (isNotInMainPhases()) {
                PrintResponses.printSetCardInWrongPhase();
            } else if (isPositionTheSameAsBefore(position)) {
                PrintResponses.printCardInWantedPosition();
            } else if (currentDuel.isHasChangedPositionOnce()) {
                PrintResponses.printUnableToChangePositionInTurnTwice();
            } else {
                if (position.equals("attack")) {
                    currentDuel.changeToAttackPosition();
                } else {
                    currentDuel.changeToDefensePosition();
                }
                PrintResponses.printSuccessfulMonsterCardPositionChange();
            }
        }

        private void flipSummon (Matcher matcher){
            if (currentDuel.isNoCardSelected()) {
                PrintResponses.printNoCardSelected();
            } else if (isSelectedCardNotInMonsterZone()) {
                PrintResponses.printUnableToChangePositionOfCard();
            } else if (isNotInMainPhases()) {
                PrintResponses.printFlipSummonInWrongPhase();
            } else if (currentDuel.isSelectedCardSummonedInThisTurn() || !(!currentDuel.getSelectedCard().isFaceUp()
                    && currentDuel.getSelectedCard().isFaceUp())) {
                PrintResponses.printUnableToFlipSummonCard();
            } else {
                currentDuel.flipSummon();
                PrintResponses.printSuccessfulFlipSummon();
            }

        }

        private void attack (Matcher matcher){
            int address = Integer.parseInt(matcher.group("number"));
            Monster monsterToAttack = (Monster) currentDuel.getRival().getBoard().
                    getCard(currentDuel.getPlaceOfSelectedCard(), 'M');
            if (currentDuel.isNoCardSelected()) {
                PrintResponses.printNoCardSelected();
            } else if (isSelectedCardNotInMonsterZone()) {
                PrintResponses.printUnableToAttack();
            } else if (isNotInBattlePhase()) {
                PrintResponses.printAttackInWrongPhase();
            } else if (((Monster) currentDuel.getSelectedCard()).isHasAttackedOnceInTurn()) {
                PrintResponses.printCardAttackedBefore();
            } else if (monsterToAttack == null) {
                PrintResponses.printNoCardToAttackWith();
            } else {
                handleSuccessfulAttack(address, monsterToAttack);
                PrintResponses.printBoard(currentDuel);
            }
        }

        private void attackDirectly (Matcher matcher){
            if (currentDuel.isNoCardSelected()) {
                PrintResponses.printNoCardSelected();
            } else if (isSelectedCardNotInMonsterZone()) {
                PrintResponses.printUnableToAttack();
            } else if (isNotInBattlePhase()) {
                PrintResponses.printAttackInWrongPhase();
            } else if (((Monster) currentDuel.getSelectedCard()).isHasAttackedOnceInTurn()) {
                PrintResponses.printCardAttackedBefore();
            } else if (currentDuel.getRival().getBoard().getMonsters().length != 0) {
                PrintResponses.print(Responses.unableToAttackDirectly);
            } else {
                int damage = currentDuel.attackDirectly();
                PrintResponses.printDamageInAttackDirectly(damage);
                currentDuel.deselectACard();
            }
        }

    private void activateSpell(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (!(currentDuel.getSelectedCard() instanceof Spell)) {
            PrintResponses.printNonSpellCardsToActivateEffect();
        } else if (isNotInMainPhases()) {
            PrintResponses.printUnableToActivateEffectOnTurn();
        } else if (currentDuel.getSelectedCard().isFaceUp()) {
            PrintResponses.printUnableToActivateCardTwice();
        } else if (isSpellZoneFullAndNeedsToBeOnBoard()) {
            PrintResponses.printFullnessOfSpellCardZone();
        } else if (isSpellPreparedToBeActivated()) {
            PrintResponses.printUnfinishedPreparationOfSpell();
        } else if (currentDuel.getUserWhoPlaysNow().isCanSummonSpell()) {
            PrintResponses.printDisabledSummonSpell();
        } else {
            currentDuel.activateEffects();
            PrintResponses.printSuccessfulSpellActivation();
        }
    }

        private void showGraveyard (Matcher matcher){
            isInGame = false;
            PrintResponses.showGraveyard(currentDuel.showGraveyard());
        }

        private void showSelectedCard (Matcher matcher){
            PrintResponses.printSelectedCard(currentDuel.showSelectedCard());
        }

        private void surrender (Matcher matcher){
            PrintResponses.printEndingTheGame(currentDuel.surrender(remainingRounds, initialRounds));
            currentDuel = null;
        }


        private void increaseLP (Matcher matcher){
            int amount = Integer.parseInt(matcher.group("amount"));
            currentDuel.getUserWhoPlaysNow().setLifePoints(currentDuel.getUserWhoPlaysNow().getLifePoints() + amount);
            PrintResponses.print(Responses.increaseLP);
        }

        private void setWinner (Matcher matcher){
            String nickname = matcher.group("nickname");
            User winner, loser;
            if (currentDuel.getUserWhoPlaysNow().getNickname().equals(nickname)) {
                winner = currentDuel.getUserWhoPlaysNow();
                loser = currentDuel.getRival();
            } else {
                winner = currentDuel.getRival();
                loser = currentDuel.getUserWhoPlaysNow();
            }
            if (initialRounds == 1)
                PrintResponses.printEndingTheGame(currentDuel.handleEndingOneRoundGames(winner, loser));
            else
                PrintResponses.printEndingTheWholeMatch(currentDuel.handleEndingThreeRoundGames(winner, loser));
            currentDuel = null;

        }

        // when we want to summon unconditionally.
        private void forceSelectHand (Matcher matcher){
            String cardName = matcher.group("cardName");
            Card[] cardsInHand = currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand();
            int i = 0, cardsInHandLength = cardsInHand.length;
            while (true) {
                if (i >= cardsInHandLength) break;
                Card card1 = cardsInHand[i];
                if (card1.getName().equals(cardName)) {
                    currentDuel.setSelectedCard(card1);
                    currentDuel.setPlaceOfSelectedCard(i + 1);
                    break;
                }
                i++;
            }
            currentDuel.summonMonster();
            PrintResponses.print(Responses.forceSelectHand);
            PrintResponses.print(currentDuel);
        }

        private boolean isRitualSummon () {
            if (!currentDuel.getUserWhoPlaysNow().getBoard().isThereAnyCardWithGivenTypeInMonsters(CardType.RITUAL)
                    || currentDuel.getUserWhoPlaysNow().getBoard()
                    .isThereASubsetOfMonstersWithSumOfLevelsGreaterThanGivenLevel(currentDuel.getUserWhoPlaysNow().getHand().getMinLevelOfRitualMonstersInHand())) {
                PrintResponses.printUnableToRitualSummonMonster();
                return false;
            }
            handleSelectionForRitualSummon();
            handleSummonForRitualSummon();
            return true;
        }


        private boolean isNotEnoughCardsForTribute ( int requiredCardsAmount){
            if (currentDuel.getUserWhoPlaysNow().getBoard().getMonsters().length == requiredCardsAmount) {
                PrintResponses.printNoCardToTribute();
                return true;
            }
            return false;
        }

        private boolean areCardAddressesEmpty ( int[] cardsAddresses){
            for (int toTributeAddress : cardsAddresses) {
                if (currentDuel.getUserWhoPlaysNow().getBoard().getCard(toTributeAddress, 'M') == null) {
                    PrintResponses.printNoMonsterOnAddress();
                    return true;
                }
            }
            return false;
        }

        private boolean isPositionTheSameAsBefore (String position){
            if (position.equals("attack")) {
                return currentDuel.getSelectedCard().isFaceUp();
            } else {
                return !currentDuel.getSelectedCard().isATK() && !currentDuel.getSelectedCard().isFaceUp();
            }
        }

        private boolean isNotInMainPhases () {
            return !(phase.equals(Phases.MAIN_PHASE1) || phase.equals(Phases.MAIN_PHASE2));
        }

        private boolean isSelectedCardNotInMonsterZone () {
            return !currentDuel.getUserWhoPlaysNow().getBoard().isCardOnMonsterZone(currentDuel.getSelectedCard());
        }

        private boolean isNotInBattlePhase () {
            return !phase.equals(Phases.BATTLE_PHASE);
        }

        private boolean isSpellZoneFullAndNeedsToBeOnBoard () {
            //TODO implement if the card **needs** to be on board
            return currentDuel.getUserWhoPlaysNow().getBoard().getAddressToPutSpell() == 0;
        }

        //TODO implement this method
        private boolean isSpellPreparedToBeActivated () {
            return true;
        }

        private void handleSuccessfulGameCreation (User secondPlayer){
            currentDuel = new Duel(ProgramController.userInGame, secondPlayer);
            phase = Phases.DRAW_PHASE;
            if (isFirstRound) {
                PrintResponses.printGameSuccessfullyCreated();
                isFirstRound = false;
                currentDuel.getSECOND_USER().setWinsInAMatch(0);
                currentDuel.getFIRST_USER().setWinsInAMatch(0);
            } else {
                PrintResponses.printRoundNumber(remainingRounds);
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handleDrawingACard(currentDuel.drawACard());
        }

        private void handleTransitionFromEndPhaseToDrawPhase () {
            phase = Phases.END_PHASE;
            PrintResponses.printPhaseName(phase);
            currentDuel.changeTurn();
            currentDuel.setNumberOfTurnsPlayedUpToNow(currentDuel.getNumberOfTurnsPlayedUpToNow() + 1);
            PrintResponses.showTurn(currentDuel.getUserWhoPlaysNow());
            phase = Phases.DRAW_PHASE;
            PrintResponses.printPhaseName(phase);
        }

        private void handleDrawingACard (Card card){
            if (card == null) {
                endTheGame();
            } else {
                PrintResponses.printDrawnCard(card);
                PrintResponses.printBoard(currentDuel);
            }
        }

        private void handleSuccessfulAttack ( int address, Monster monsterToAttack){
            Pair<Integer, Integer> pair = currentDuel.attack(address);
            int key = pair.getKey();
            if (key > 6) {
                key -= 3;
                PrintResponses.printCardNameInAttackIfIsDefenceHide(monsterToAttack.getName());
            }
            switch (key) {
                case 0:
                    //print sth if you want!
                    break;
                case 1:
                    PrintResponses.printOpponentMonsterDestroyedWithDamage(pair.getValue());
                    break;
                case 2:
                    PrintResponses.printBothCardsDestroyedInAttack();
                    break;
                case 3:
                    PrintResponses.printOwnMonsterDestroyedInAttackWithDamage(pair.getValue());
                    break;
                case 4:
                    PrintResponses.printOpponentMonsterInDefenceDestroyed();
                case 5:
                    PrintResponses.printNoCardDestroyedInDefence();
                    break;
                case 6:
                    PrintResponses.printNoCardDestroyedButReceivedDamage(pair.getValue());
                    break;
            }
        }

        private void handleSelectionForRitualSummon () {
            while (true) {
                Matcher matcher = Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.selectFromOwn);
                if (matcher.find()) {
                    selectCardFromOwn(Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.selectFromOwn));
                    break;
                } else {
                    PrintResponses.printEmergencyRitualSummon();
                }
            }
        }

        private void handleSummonForRitualSummon () {
            while (true) {
                Matcher matcher = Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.summon);
                if (matcher.find()) {
                    while (true) {
                        int[] cardAddresses = Arrays.stream(ProgramController.scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                        if (currentDuel.getUserWhoPlaysNow().getBoard().areGivenCardsEnoughForRitualSummon(cardAddresses, currentDuel.getSelectedCard())) {
                            currentDuel.tribute(cardAddresses);
                            break;
                        } else {
                            PrintResponses.printInequalityOfLevelsOfSelectedAndRitualMonster();
                        }
                    }
                    boolean isAttacking;
                    while (true) {
                        String input = ProgramController.scanner.nextLine();
                        if (input.equals("attacking")) {
                            isAttacking = true;
                            break;
                        } else if (input.equals("defensive")) {
                            isAttacking = false;
                            break;
                        } else {
                            PrintResponses.printInvalidFormat();
                        }
                    }
                    if (isAttacking)
                        summon(Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.summon));
                    else {
                        currentDuel.setMonster();
                        currentDuel.changeToDefensePosition();
                        PrintResponses.printSuccessfulSummon();
                    }
                    break;
                } else {
                    PrintResponses.printEmergencyRitualSummon();
                }
            }
        }

        private void handleMonsterSet () {
            if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
                PrintResponses.printFullnessOfMonsterCardZone();
            } else if (currentDuel.isHasSummonedOrSetOnce()) {
                PrintResponses.printUnableToSummonInTurn();
            } else {
                currentDuel.setMonster();
                PrintResponses.printSuccessfulCardSetting();
                PrintResponses.print(currentDuel);
            }
        }

        private void handleSpellAndTrapSet () {
            if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToPutSpell() == 0) {
                PrintResponses.printFullnessOfSpellCardZone();
            } else if (currentDuel.isHasSummonedOrSetOnce()) {
                PrintResponses.printUnableToSummonInTurn();
            } else {
                currentDuel.setSpellOrTrap();
                PrintResponses.printSuccessfulCardSetting();
                PrintResponses.print(currentDuel);
            }
        }


    @Override
    public void showMenu() throws IOException {

    }
}

