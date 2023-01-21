package Player1;

import battlecode.common.*;

public class LauncherController {
    private static RobotInfo temp;
    private static int value = -1;
    static void attackPriorityFilter(RobotInfo robot){
        // Priority Filter(by cost): Destabilizers, Boosters, Amplifiers, Launchers, Carriers
        // Launchers could be higher priority if you can quickly eliminate all then attack the rest
        int t = -1;
        switch(robot.getType()) {
            case DESTABILIZER:
                t = 4;
                break;
            case BOOSTER:
                t = 3;
                break;
            case AMPLIFIER:
                t = 2;
                break;
            case LAUNCHER:
                t = 1;
                break;
            case CARRIER:
                t=0;
                break;
        }
// Alternate strategy to prioritize launchers
//        switch(robot.getType()) {
//            case LAUNCHER:
//                t = 4;
//                break;
//            case DESTABILIZER:
//                t = 3;
//                break;
//            case BOOSTER:
//                t = 2;
//                break;
//            case AMPLIFIER:
//                t = 1;
//                break;
//            default:
//                t = 0;
//        }

        if(t>value) {
            value = t;
            temp = robot;
        }

    }
    static void movePriorityFilter(RobotInfo robot){
        // Priority Filter(by cost): Destabilizers, Boosters, Amplifiers, Launchers, Carriers
        // Launchers could be higher priority if you can quickly eliminate all then attack the rest
        int t;
        switch(robot.getType()){
            case HEADQUARTERS:
                t = 5;
                break;
            case DESTABILIZER:
                t = 4;
                break;
            case BOOSTER:
                t=3;
                break;
            case AMPLIFIER:
                t=2;
                break;
            case CARRIER:
                t=1;
                break;
            case LAUNCHER:
                t=0;
                break;
            default:
                t = -1;
        }
        if(t>value) {
            value = t;
            temp = robot;
        }
    }

    static void resetPriorityFilter(){
        value = -1;
        temp = null;
    }
    static void runLauncher(RobotController rc) throws GameActionException {
        // Try to attack while cooldown < 10
        while (rc.getActionCooldownTurns()<GameConstants.COOLDOWN_LIMIT){
            int radius = rc.getType().actionRadiusSquared;
            Team opponent = rc.getTeam().opponent();
            RobotInfo[] attackableEnemies = rc.senseNearbyRobots(radius, opponent);
            if (attackableEnemies.length >= 1) {
                resetPriorityFilter();
                for (RobotInfo robot : attackableEnemies) {
                    // If Launcher can kill
                    if (robot.getHealth() <= RobotType.LAUNCHER.damage) {
                        attackPriorityFilter(robot);
                    }
                }
                if (rc.canAttack(temp.location)) {
                    rc.setIndicatorString("Attacking");
                    rc.attack(temp.location);
                } else {
                    resetPriorityFilter();
                    for (RobotInfo robot : attackableEnemies) {
                        attackPriorityFilter(robot);
                    }
                    if (rc.canAttack(temp.location)) {
                        rc.setIndicatorString("Attacking");
                        rc.attack(temp.location);
                    }
                }
            }
        }
        while (rc.getMovementCooldownTurns() < GameConstants.COOLDOWN_LIMIT){
            RobotInfo[] viewableEnemies = rc.senseNearbyRobots();
            if(viewableEnemies.length > 0){
                resetPriorityFilter();
                for (RobotInfo robot: viewableEnemies){
                    movePriorityFilter(robot); // maybe implement detection of destabilized squares if possible
                }
                Direction toRobot = rc.getLocation().directionTo(temp.location);
                if (rc.canMove(toRobot)){
                    rc.move(toRobot);
                }
            }
            else {
                Direction toCenter = rc.getLocation().directionTo(RobotPlayer.getCenter(rc));
                // TODO implement moving towards enemy's hqs
                if(rc.canMove(toCenter)){
                    rc.move(toCenter);
                } else {
                    rc.move(RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)]);
                }
            }
        }
    }
}
