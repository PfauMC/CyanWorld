package ru.cyanworld.cyanuniverse;

import ru.cyanworld.cyanuniverse.menus.*;
import ru.cyanworld.cyanuniverse.menus.decorations.DecoHeads;
import ru.cyanworld.cyanuniverse.menus.decorations.DecoMain;

public class MenusList {
    public static WorldsMenu worldsMenu = new WorldsMenu();
    public static BuildingType buildingType = new BuildingType();
    public static DeleteWorld deleteWorld = new DeleteWorld();
    public static MyWorldMenu myWorldMenu = new MyWorldMenu();
    public static MyWorldTime myWorldTime = new MyWorldTime();
    public static KitPvpMenu kitPvpMenu = new KitPvpMenu();
    public static VoteMenu voteMenu = new VoteMenu();

    public static DecoMain decoMain = new DecoMain();
    public static DecoHeads decoHeads = new DecoHeads();

}
