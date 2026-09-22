package autofighter;

import com.osrsbots.orb.api.interactables.world.locations.Banks;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.coords.WorldPoint;
import autofighter.ui.util.CombatTarget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

@Getter
public class FightProfile {

    final String name;

    // LOOT
    @Setter
    @Nullable
    ArrayList<String> lootList;

    // BANK
    @Setter
    @Nullable
    ArrayList<String> bankList;

    @Setter
    @Nullable
    ArrayList<String> prayerList;

    @Setter
    @Nullable
    Banks.Location bank;

    boolean lootInCombat;

    boolean usePotions;

    @Setter
    boolean useBankFullInv;

    // COMBAT
    int distance;

     /*
        0 | Disabled
        1 | Full Inventory
        2 | Before Combat
        3 | During Combat
        4 | After Combat
        5 | Random
     */

    int buryBones;

    @Nonnull
    WorldPoint location;

    @Nonnull
    ArrayList<CombatTarget> targetList;

    @Setter
    int hopIfXPlayers = 5, hopIfXStolen =10, hopIfXNotFound = 60, hopIfXTime = 60;

    @Setter
    boolean hopIfTooManyPlayers, hopIfStolenTargets, hopIfNoTargets, hopIfTimeHasPassed;

    public FightProfile(
            String name,
            boolean usePotions,
            boolean lootInCombat, int buryBones, int distance,
            WorldPoint location, ArrayList<CombatTarget> targetList
    ) {
        this.name = name;
        lootList = null;
        this.usePotions = usePotions;
        this.lootInCombat = lootInCombat;
        this.buryBones = buryBones;
        bankList = null;
        bank = null;
        useBankFullInv = false;
        this.distance = distance;

        this.location = location;
        this.targetList = targetList;
    }

    @Override
    public String toString() {
        return name;
    }

}
