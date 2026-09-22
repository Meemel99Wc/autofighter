package autofighter;

import net.runelite.api.Skill;

import java.util.ArrayList;
import java.util.List;

public class PotionHandler {

    public static class Potion {
        public final String name;

        public final List<Skill> boostedSkills;

        public Potion(String name, List<Skill> boostedSkills) {
            this.name = name;
            this.boostedSkills = boostedSkills;
        }
    }

    public static List<Potion> potions;

    public static List<Potion> mixedPotions ;

    public static void init() {
        potions = new ArrayList<>();
        mixedPotions = new ArrayList<>();

        // Attack
        potions.add(new Potion("Attack potion", List.of(Skill.ATTACK)));
        potions.add(new Potion("Attack mix", List.of(Skill.ATTACK)));
        potions.add(new Potion("Super attack", List.of(Skill.ATTACK)));
        potions.add(new Potion("Super attack mix", List.of(Skill.ATTACK)));
        potions.add(new Potion("Divine super attack potion", List.of(Skill.ATTACK)));

        // Strength
        potions.add(new Potion("Strength potion", List.of(Skill.STRENGTH)));
        potions.add(new Potion("Strength mix", List.of(Skill.STRENGTH)));
        potions.add(new Potion("Super strength", List.of(Skill.STRENGTH)));
        potions.add(new Potion("Super strength mix", List.of(Skill.STRENGTH)));
        potions.add(new Potion("Divine super strength potion", List.of(Skill.STRENGTH)));

        // Defence
        potions.add(new Potion("Defence potion", List.of(Skill.DEFENCE)));
        potions.add(new Potion("Defence mix", List.of(Skill.DEFENCE)));
        potions.add(new Potion("Super defence", List.of(Skill.DEFENCE)));
        potions.add(new Potion("Super defence mix", List.of(Skill.DEFENCE)));
        potions.add(new Potion("Divine super defence potion", List.of(Skill.DEFENCE)));

        // Magic
        potions.add(new Potion("Magic potion", List.of(Skill.MAGIC)));
        potions.add(new Potion("Magic mix", List.of(Skill.MAGIC)));
        potions.add(new Potion("Divine magic potion", List.of(Skill.MAGIC)));

        // Range
        potions.add(new Potion("Ranging potion", List.of(Skill.RANGED)));
        potions.add(new Potion("Ranging mix", List.of(Skill.RANGED)));
        potions.add(new Potion("Divine ranging potion", List.of(Skill.RANGED)));

        // Mixed
        mixedPotions.add(new Potion("Combat potion", List.of(Skill.ATTACK, Skill.STRENGTH)));
        mixedPotions.add(new Potion("Combat mix", List.of(Skill.ATTACK, Skill.STRENGTH)));
        mixedPotions.add(new Potion("Super combat potion", List.of(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE)));
        mixedPotions.add(new Potion("Divine super combat potion", List.of(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE)));

        mixedPotions.add(new Potion("Bastion potion", List.of(Skill.DEFENCE, Skill.RANGED)));
        mixedPotions.add(new Potion("Divine bastion potion", List.of(Skill.DEFENCE, Skill.RANGED)));

        mixedPotions.add(new Potion("Battlemage potion", List.of(Skill.DEFENCE, Skill.MAGIC)));
        mixedPotions.add(new Potion("Divine battlemage potion", List.of(Skill.DEFENCE, Skill.MAGIC)));

    }
}
