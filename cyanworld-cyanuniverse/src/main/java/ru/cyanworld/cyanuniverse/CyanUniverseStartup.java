package ru.cyanworld.cyanuniverse;

import static ru.cyanworld.cyanuniverse.CyanUniverse.itemPrice;

public class CyanUniverseStartup {
    public CyanUniverseStartup(CyanUniverse main) {


        setupPrice();
    }

    public void setupPrice() {
        itemPrice.put("ru.cyanworld.cyanuniverse.block.dragon_egg", 100);
        itemPrice.put("ru.cyanworld.cyanuniverse.block.spawner", 300);
        itemPrice.put("ru.cyanworld.cyanuniverse.block.barrier", 500);
        itemPrice.put("ru.cyanworld.cyanuniverse.block.structure_void", 200);
    }
}
