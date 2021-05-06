package module;

import module.card.Card;
import module.card.Monster;

import java.util.ArrayList;

public class Board {
   private User boardOwner;
   private Monster[] monsters;
   private Card[] spellsAndTraps;
   private Card fieldZone;

   {
      this.monsters = new Monster[5];
      this.spellsAndTraps = new Card[5];
   }

   public Board (User boardOwner) {
       this.boardOwner = boardOwner;
   }

   public void addMonsterFaceUp (int placeInBoard, Monster selectedMonsterCard) {
      this.monsters.add(placeInBoard, selectedMonsterCard);
   }

   public void addMonsterFaceDown (int placeInBoard) {

   }

   public void addSpellAndTrap (int placeInBoard) {

   }

   public void changeFacePosition (int placeInBoard) {

   }

   public void putCardToFieldZone (Card card) {

   }

   public void removeMonster (int placeInBoard) {

   }

   public void removeSpellAndTrap (int placeInBoard) {

   }

   public void removeFieldZone (Card card) {

   }

   public void disableSpellAndTrap (int placeInBoard) {

   }
   // monsterOrSpell takes either 'S' as Spell or 'M' as Monster
   public Card getCard(int placeInBoard, char monsterOrSpell){
      return null;
   }
}
