package view;

import controller.ProgramController;

public class Responses {
    // Server log responses
    public static final String SERVER_IS_ON = "Server is running";
    public static final String SERVER_INITIALIZATION_FAILED = "Server initialization failed. Error details: ";
    public static final String NEW_CLIENT_CONNECTED = "New client successfully connected";
    public static final String CLIENT_DISCONNECTED = "A client disconnected";
    public static final String NEW_MESSAGE_FROM_CLIENT = "New message received from client. Message: ";
    public static final String WRONG_FORMAT = "Error: Wrong message format";
    public static final String MESSAGE_SENT = "Message sent: ";
    public static final String USER_CREATED = "User created successfully";
    public static final String LOGOUT = "User logged out and following token invalidated: ";
    public static final String ALREADY_LOGGED_IN = "User already logged in some other device.";
    public static final String DECK_ALREADY_EXISTS = "Error: Deck  with this name already exists";
    public static final String DECK_CREATED = "Deck successfully created.";
    public static final String INVALID_TOKEN = "Error: Token is invalid";
    public static final String ERROR = "Error: Unknown error";
    public static final String DECK_DELETED = "Deck deleted successfully.";
    public static final String DECK_ACTIVATED = "Deck activated successfully.";
    public static final String USER_NOT_FOUND = "Error: User does not exist";
    public static String changeToCurrentNickname = "This is your nickname";
    public static String emptyNicknameField = "Nickname field is empty";
    public static String invalidToken = "Token is invalid";
    public static String WRONG_PASSWORD = "Error: Password is wrong";
    public static String equalityOfNewAndOldPassword = "New and old password are equal";
    public static String SUCCESS = "Successful";
    public static String USER_EXISTS = "User already exists";
    public static String NICKNAME_EXISTS = "Nickname already exists";
    public static String LOGIN_ERROR = "Login error";
    public static String invalidFormat = "invalid command";
    public static String unableToSummonCard = "you can’t summon this card";
    public static String summonInWrongPhase = "action not allowed in this phase";
    public static String fullnessOfMonsterCardZone = "monster card zone is full";
    public static String unableToSummonInTurn = "you already summoned/set on this turn";
    public static String successfulSummon = "summoned successfully";
    public static String noCardToTribute = "there are not enough cards for tribute";
    public static String noMonsterOnAddress = "there no monsters on this address";
    public static String setCardInWrongPhase = "you can’t do this action in this phase";
    public static String successfulCardSetting = "set successfully";
    public static String unableToChangePositionInTurnTwice = "you already changed this card position in this turn";
    public static String unableToAttack = "you can’t attack with this card";
    public static String attackInWrongPhase = "you can’t do this action in this phase";
    public static String cardAttackedBefore = "this card already attacked";
    public static String destructionOfTwoSideCards = "both you and your opponent monster cards are destroyed and no one receives damage";
    public static String destructionOfDefensePositionMonster = "the defense position monster is destroyed";
    public static String noDestruction = "no card is destroyed";
    public static String unableToActivateEffectOnTurn = "you can’t activate an effect on this turn";
    public static String unableToActivateCardTwice = "you have already activated this card";
    public static String fullnessOfSpellCardZone = "spell card zone is full";
    public static String unfinishedPreparationOfSpell = "preparations of this spell are not done yet";
    public static String successfulSpellActivation = "spell activated";
    public static String unableToRitualSummonMonster = "there is no way you could ritual summon a monster";
    public static String emergencyRitualSummon = "you should ritual summon right now";
    public static String inequalityOfLevelsOfSelectedAndRitualMonster = "selected monsters levels don’t match with ritual monster";
    public static String unableToSpecialSummonMonster = "there is no way you could special summon a monster";
    public static String mainMenu = "Main menu";
    public static String duelMenu = "Duel Menu";
    public static String gameCreation = "Game has been started! Drawing a card in 3 seconds";
    public static String increaseLP = "CHEAT: LP increased successfully!";
    public static String forceSelectHand = "CHEAT: Given card successfully summoned!";
    public static String choiceOfDifferentTribute = "Pick a number to choose how many monsters to tribute";
    public static String disabledMonsterSummon = "You can not summon due to an Effect";
    public static String disabledSpellSummon = "you can not activate due to an Effect";
    public static String changeOfHeart = "choose the monster you want to control";
    public static String wrongChoiceControl = "you did not choose the monster correctly";
    public static String canNotControl = "you can Not control any thing";
    public static String noSpellFound = "no spell found on this location";
    public static String wrongSpell = "you have chosen a wrong place or a spell that can not be chained right now";
    public static String wrongSpellFormat = "you have to enter a number";
    public static String askToDiscard = "select the cards in your hand to discard";
    public static String askToGetCardName = "enter a card name";
    public static String wrongCardName = "there is no card with this name";
    public static String noSpecialEffect = "You can not use this Effect now";
    public static String addACardToHand = "CHEAT: Given card added to your hand!";
    public static String handIsFull = "CHEAT: Your hand is full. Could not add the card.";
    public static String askToRitual = "select Your Ritual Monster And then enter the ones to be killed at last summon it";
    public static String enterTribute = "enter the place of monster to be killed in order to summon and divide them by " +
            "space then decide the format of the monster";
    public static String cardNotFoundInHand = "CHEAT: Card not found in hand.";
    public static String canNotAttackDueToEffect = "You can't attack due to an effect.";
    public static String wrongTurn = "It's not your turn!";
    public static String surrenderConfirmation = "Are you sure you want to surrender?";
    static String askForChain = "do you want to activate your trap and spell?";

    public static void logToConsole(String logMessage) {
        System.out.println(ProgramController.getCurrentTimeAndDate() + logMessage);
    }


}
