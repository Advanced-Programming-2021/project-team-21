package view;

public class Responses {
    public static String invalidFormat = "invalid command";
    public static String noLoginYet = "please login first";
    public static String menuNavigationError = "menu navigation is not possible";
    public static String successfulUserCreation = "user created successfully!";
    public static String successfulLogin = "user logged in successfully!";
    public static String noUserExists = "Username and password didn’t match!";
    public static String wrongPasswordInLogin = "Username and password didn’t match!";
    public static String successfulLogout = "user logged out successfully!";
    public static String successfulNicknameChange = "nickname changed successfully!";
    public static String successfulPasswordChange = "password changed successfully!";
    public static String wrongPasswordInChange = "current password is invalid";
    public static String equalityOfCurrentAndNewPassword = "please enter a new password";
    public static String successfulDeckCreation = "deck created successfully!";
    public static String successfulDeckDeletion = "deck deleted successfully";
    public static String successfulDeckActivation = "deck activated successfully";
    public static String successfulCardAddition = "card added to deck successfully";
    public static String successfulCardRemoval = "card removed form deck successfully";
    public static String noCardExistToBuy = "there is no card with this name";
    public static String lackOfMoney = "not enough money";
    public static String noUserExistToPlayWith = "there is no player with this username";
    public static String nonSupportiveRound = "number of rounds is not supported";
    public static String invalidSelection = "invalid selection";
    public static String successfulCardSelection = "card selected";
    public static String noCardInPosition = "no card found in the given position";
    public static String noCardSelected = "no card is selected yet";
    public static String successfulCardDeselection = "card deselected";
    public static String unableToSummonCard = "you can’t summon this card";
    public static String summonInWrongPhase = "action not allowed in this phase";
    public static String fullnessOfMonsterCardZone = "monster card zone is full";
    public static String unableToSummonInTurn = "you already summoned/set on this turn";
    public static String successfulSummon = "summoned successfully";
    public static String noCardToTribute = "there are not enough cards for tribute";
    public static String noMonsterOnAddress = "there no monsters on this address";
    public static String noMonsterOnAddresses = "there no monsters on one of this addresses";
    public static String unableToSetCard = "you can’t set this card";
    public static String setCardInWrongPhase = "you can’t do this action in this phase";
    public static String successfulCardSetting = "set successfully";
    public static String unableToChangePositionOfCard = "you can’t change this card position";
    public static String cardInWantedPosition = "this card is already in the wanted position";
    public static String unableToChangePositionInTurnTwice = "you already changed this card position in this turn";
    public static String changeMonsterCardModeInWrongPhase = "you can’t do this action in this phase";
    public static String successfulMonsterCardPositionChange = "monster card position changed successfully";
    public static String unableToFlipSummonCard = "you can’t flip summon this card";
    public static String flipSummonInWrongPhase = "you can’t do this action in this phase";
    public static String successfulFlipSummon = "flip summoned successfully";
    public static String unableToAttack = "you can’t attack with this card";
    public static String attackInWrongPhase = "you can’t do this action in this phase";
    public static String cardAttackedBefore = "this card already attacked";
    public static String noCardToAttackWith = "there is no card to attack here";
    public static String destructionOfTwoSideCards = "both you and your opponent monster cards are destroyed and no one receives damage";
    public static String destructionOfDefensePositionMonster = "the defense position monster is destroyed";
    public static String noDestruction = "no card is destroyed";
    public static String nonSpellCardsToActivateEffect = "activate effect is only for spell cards.";
    public static String unableToActivateEffectOnTurn = "you can’t activate an effect on this turn";
    public static String unableToActivateCardTwice = "you have already activated this card";
    public static String fullnessOfSpellCardZone = "spell card zone is full";
    public static String unfinishedPreparationOfSpell = "preparations of this spell are not done yet";
    public static String successfulSpellActivation = "spell activated";
    public static String setSpellInWrongPhase = "you can’t do this action in this phase";
    public static String successfulSpellSetting = "set successfully";
    public static String askToActivateSpellOrTrap = "do you want to activate your trap and spell";
    public static String wrongTurnToActivateSpellOrTrap = "it’s not your turn to play this kind of moves";
    public static String successfulSpellOrTrapActivation = "spell/trap activated";
    public static String unableToRitualSummonMonster = "there is no way you could ritual summon a monster";
    public static String emergencyRitualSummon = "you should ritual summon right now";
    public static String inequalityOfLevelsOfSelectedAndRitualMonster = "selected monsters levels don’t match with ritual monster";
    public static String unableToSpecialSummonMonster = "there is no way you could special summon a monster";
    public static String emergencySpecialSummon = "you should special summon right now";
    public static String emptinessOfGraveyard = "graveyard empty";
    public static String unableToShowOpponentCard = "card is not visible";
    public static String loginMenuShow = "Login Menu";
    public static String profileMenu = "Profile Menu";
    public static String mainMenu = "Main menu";
    public static String deckMenu = "Deck Menu";
    public static String duelMenu = "Duel Menu";
    public static String exportAndImportMenu = "Export/Import Menu";
    public static String scoreBoard = "ScoreBoard";
    public static String shopMenu = "Shop Menu";
    public static String activeDeck = "Decks:\n" + "Active deck:";
    public static String otherDeck = "Other decks:";
    public static String gameCreation = "Game has been started! Drawing a card in 3 seconds";
    public static String moneyIncreased = "CHEAT: Money increased successfully!";
    public static String increaseLP = "CHEAT: LP increased successfully!";
    public static String forceSelectHand = "CHEAT: Given card successfully summoned!";
    public static String cardExportedSuccessfully = "Card exported successfully.";
    public static String cardImportedSuccessfully = "Card imported successfully.";
    public static String unableToAttackDirectly = "you can’t attack the opponent directly";
    public static String alreadyInGame = "You are already in a game!";
    public static String wrongChoose = "choose a right number from the shown cards to special summon";
    public static String choiceOfDifferentTribute = "Pick a number to choose how many monsters to tribute";
    public static String wrongChoiceOfTribute = "choose a suitable number for tribute";
    public static String disabledMonsterSummon = "You can not summon due to an Effect";
    public static String disabledTrapSummon = "You can not activate due to an Effect";
    public static String disabledSpellSummon = "you can not activate due to an Effect";
    public static String activateEffectMonster = "Do you want to activate the Effect?[Yes/No]";
    public static String changeOfHeart = "choose the monster you want to control";
    public static String wrongChoiceControl = "you did not choose the monster correctly";
    public static String canNotControl = "you can Not control any thing";
    public static String noSpellFound = "no spell found on this location";
    public static String disabledAttack = "You can not attack due to effect of an card";
    public static String chooseEquip = "Choose a Monster to Equip it with this Spell";
    public static String askForChain = "do you want to activate your trap and spell?";
    public static String wrongSpell = "you have chosen a wrong place or a spell that can not be chained right now";
    public static String wrongSpellFormat = "you have to enter a number";
    public static String askToDiscard = "select the cards in your hand to discard";
    public static String askToGetCardName = "enter a card name";
    public static String wrongCardName = "there is no card with this name";
    public static String noSelect = "There is no selected card";
    public static String noSpecialEffect = "You can not use this Effect now";
    public static String addACardToHand = "CHEAT: Given card added to your hand!";
    public static String handIsFull = "CHEAT: Your hand is full. Could not add the card.";
    public static String askToRitual = "select Your Ritual Monster And then enter the ones to be killed at last summon it";
    public static String enterTribute = "enter the place of monster to be killed in order to summon and divide them by " +
            "space then decide the format of the monster";
    public static String cardNotFoundInHand = "CHEAT: Card not found in hand.";
}
