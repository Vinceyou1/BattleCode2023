package Player1;

import battlecode.common.*;

import java.awt.*;

public class HQController {
    static private int[] robotsCreated = new int[5];
    // Lets HQ keep track of all the robots made, goes Carrier, Launcher,
    // Amplifiers, Destabilizers, and Boosters
    static void runHeadquarters(RobotController rc) throws GameActionException {
        // I'm assuming that Team A is South and Team B is North, we'll have to
        // implement something that finds side of the map
        int index;
        if(rc.getTeam() == Team.A) index = 0; else index = 4;
        // Here, I set index so Team A starts building to the north of their HQ,
        // and Team B starts building to the south of their HQ
        // This way, Launchers should be in front of carriers

        // Following code is for adamantium and mana construction
        MapLocation newLoc = rc.getLocation().add(RobotPlayer.directions[index]);
        if (RobotPlayer.turnCount != 1 && rc.canBuildRobot(RobotType.AMPLIFIER, newLoc)){
            // This should be pretty rare since 2 carriers of different types each
            // carrying 40kg coming back at the same time is pretty unlikely
            rc.buildRobot(RobotType.AMPLIFIER, newLoc);
            newLoc = rc.getLocation().add(RobotPlayer.directions[++index]);
        }
        while (rc.canBuildRobot(RobotType.LAUNCHER, newLoc)) {
            rc.buildRobot(RobotType.LAUNCHER, newLoc);
            newLoc = rc.getLocation().add(RobotPlayer.directions[++index]);
        }
        while (rc.canBuildRobot(RobotType.CARRIER, newLoc)) {
            rc.buildRobot(RobotType.CARRIER, newLoc);
            newLoc = rc.getLocation().add(RobotPlayer.directions[++index]);
        }

        //TODO Implement elixir crafting
    }
}
