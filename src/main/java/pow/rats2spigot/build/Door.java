package pow.rats2spigot.build;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.ArrayList;

public abstract class Door {

    private MainManager mainManager;
    private final Location locationToBuildFrom;
    private final Location locationToRotateFrom;
    private final Location templateLocation;
    private final BlockFace rootBlockFace;
    private boolean opensOutwards;
    private BlockFace currentBlockFace;
    private boolean open;
    private final int doorWidth;
    private ArrayList<Material> doorMaterials = new ArrayList<>();
    private boolean doorKnobRight;

    public Door(Location rootLocation, Location templateLocation, BlockFace rootBlockFace, int doorWidth, boolean opensOutwards, boolean doorKnobRight, MainManager mainManager) {
        this.locationToBuildFrom = rootLocation;
        this.templateLocation = templateLocation;
        this.rootBlockFace = rootBlockFace;
        this.currentBlockFace = rootBlockFace;
        open=false;
        this.opensOutwards = opensOutwards;
        this.doorWidth = doorWidth;
        this.mainManager = mainManager;
        this.doorKnobRight = doorKnobRight;
        doorMaterials = getDoorBlockTypes();

        if(!doorKnobRight){
            if(rootBlockFace==BlockFace.NORTH){
                this.locationToRotateFrom = rootLocation.clone().add(doorWidth-1,0,0);
            }else if(rootBlockFace==BlockFace.SOUTH){
                this.locationToRotateFrom = rootLocation.clone().add(-doorWidth+1,0,0);
            }else if(rootBlockFace==BlockFace.EAST){
                this.locationToRotateFrom = rootLocation.clone().add(0,0,doorWidth-1);
            }else{
                this.locationToRotateFrom = rootLocation.clone().add(0,0,-doorWidth+1);
            }
        }else{
            this.locationToRotateFrom = rootLocation;
        }


    }

    public void createClosedDoor(){
        clear();

        //Iterates over every block in template
        for (int width = 0; width < doorWidth; width++) {
            for (int height = 0; height < 7; height++) {
                for (int depth = -1; depth <=1 ; depth++) {
                    //Takes the template location
                    Location tempCopyLocation = templateLocation.clone();
                    tempCopyLocation.add(width,height,depth);


                    //And gets the target paste destination ready
                    Location tempPasteLocation = locationToBuildFrom.clone();
                    tempPasteLocation.add(width,height,depth);

                    //Before pasting however, it makes sure to rotate the coordinate, around the root location,
                    // centered on north (Which is where all templates are built from)
                    // to its new rootBlockFace
                    tempPasteLocation = rotateCoordinate(tempPasteLocation,locationToBuildFrom,BlockFace.NORTH,rootBlockFace);

                    if((tempPasteLocation.getBlock().getType()== Material.AIR||tempPasteLocation.getBlock().getType()==Material.LIGHT) && tempCopyLocation.getBlock().getType()!=Material.AIR){
                        //If the area we're pasting is air, and the area we're copying from isn't air
                        //Make the block at the new paste location the copied block
                        tempPasteLocation.getBlock().setType(tempCopyLocation.getBlock().getType());

                        //Get the blocks data,
                        BlockData tempCopyBlockData = tempCopyLocation.getBlock().getBlockData();
                        //And if it is something that has direction:
                        if(currentBlockFace!=BlockFace.NORTH && tempCopyBlockData instanceof Directional){

                            // Cast it as Directional,
                            Directional tempCopyBlockDataDirectional = (Directional)tempCopyBlockData;
                            BlockFace blockFacingDirection = tempCopyBlockDataDirectional.getFacing();
                            //And set its facing (Which could be anything)
                            // qual to the blockface value that is spit out by the getBlockFaceAfterRotation method
                            //To do this, we need to know the difference between North (Where all templates are built from),
                            //and the direction this door will be facing
                            tempCopyBlockDataDirectional.setFacing(getBlockFaceAfterRotation(blockFacingDirection,getDifferenceInRotation(BlockFace.NORTH,rootBlockFace)));
                        }
                        //Then set the block data for that new location equal to the block data of the tempBlock from the template
                        tempPasteLocation.getBlock().setBlockData(tempCopyBlockData);

                        //If the block from the template is a crimson wall sign,
                        if(tempCopyLocation.getBlock().getType()==Material.CRIMSON_WALL_SIGN){
                            //and the door should have the knob on the right
                            if(doorKnobRight){
                                //Otherwise place an oak button
                                tempPasteLocation.getBlock().setType(Material.OAK_BUTTON);
                                //Utility.sendMessageToAllAdmins(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing().toString());
                                //Get the block data of the just placed button
                                Directional buttonData = ((Directional)tempPasteLocation.getBlock().getBlockData());
                                //Get the difference between north and the way that the copy block location is facing
                                int differenceInOriginal = getDifferenceInRotation(BlockFace.NORTH,((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing());
                                //Calculate what that rotation from the rootFace of this door is
                                BlockFace correctFaceForPastedLocation = getBlockFaceAfterRotation(rootBlockFace,differenceInOriginal);
                                //Set it facing equal to the warped signs facing
                                buttonData.setFacing(correctFaceForPastedLocation);
                                //buttonData.setFacing(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing());
                                //And update the paste location with that data
                                tempPasteLocation.getBlock().setBlockData(buttonData);
                            }else{
                                //Otherwise just place air
                                tempPasteLocation.getBlock().setType(Material.AIR);
                            }
                        }else if(tempCopyLocation.getBlock().getType()==Material.WARPED_WALL_SIGN){
                            //If it was a warped sign and the doorknob should be on the left
                            if(doorKnobRight){
                                //Place air
                                tempPasteLocation.getBlock().setType(Material.AIR);
                            }else{
                                //Otherwise place an oak button
                                tempPasteLocation.getBlock().setType(Material.OAK_BUTTON);
                                //Utility.sendMessageToAllAdmins(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing().toString());
                                //Get the block data of the just placed button
                                Directional buttonData = ((Directional)tempPasteLocation.getBlock().getBlockData());
                                //Get the difference between north and the way that the copy block location is facing
                                int differenceInOriginal = getDifferenceInRotation(BlockFace.NORTH,((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing());
                                //Calculate what that rotation from the rootFace of this door is
                                BlockFace correctFaceForPastedLocation = getBlockFaceAfterRotation(rootBlockFace,differenceInOriginal);
                                //Set it facing equal to the warped signs facing
                                buttonData.setFacing(correctFaceForPastedLocation);
                                //buttonData.setFacing(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing());
                                //And update the paste location with that data
                                tempPasteLocation.getBlock().setBlockData(buttonData);

                            }
                        }
                    }
                }
            }
        }

        clearButtons();
    }

    public void createOpenDoor(){
        //First we clear the area
        clear();

        //we iterate over every position in the template location
        for (int width = 0; width < doorWidth; width++) {
            for (int height = 0; height < 7; height++) {
                for (int depth = -1; depth <=1 ; depth++) {
                    //We get the location data of the copy region
                    Location tempCopyLocation = templateLocation.clone();
                    tempCopyLocation.add(width,height,depth);

                    //And we get the location data of the paste region
                    Location tempPasteLocation = locationToRotateFrom.clone();
                    int newWidth = width;
                    int newDepth = depth;
                    if(!doorKnobRight){
                        newWidth=(doorWidth-1)-width;
                        newDepth=depth*-1;
                    }
                    tempPasteLocation.add(newWidth,height,newDepth);

                    //Before pasting, we rotate the pasteLocation, around the location to rotate from,
                    //equal to a number that is spat out by getDifferenceInRotation.
                    //To get that number, we use the difference between the rootBlockFace (How the door started)
                    //And it's current blockface


                    tempPasteLocation = rotateCoordinate(tempPasteLocation,locationToRotateFrom,getInwardsOrOutwards()+getDifferenceInRotation(BlockFace.NORTH,rootBlockFace));

                    //If the paste location is air, and the copy location is not air,
                    if(tempPasteLocation.getBlock().getType()== Material.AIR && tempCopyLocation.getBlock().getType()!=Material.AIR){
                        //Paste the block
                        tempPasteLocation.getBlock().setType(tempCopyLocation.getBlock().getType());
                        tempPasteLocation.getBlock().setBlockData(tempCopyLocation.getBlock().getBlockData());

                        //We then get the blockdata of the block we just pasted
                        BlockData blockData = tempPasteLocation.getBlock().getBlockData();


                        //If the block can be directional
                        if(blockData instanceof Directional){
                            //We cast it to be directional
                            Directional directional = (Directional) blockData;


                            int difference;
                            if(doorKnobRight){
                                difference =getDifferenceInRotation(BlockFace.NORTH,((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing())+ getInwardsOrOutwards();
                            }else{
                                difference =getDifferenceInRotation(BlockFace.NORTH,((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing())- getInwardsOrOutwards();
                            }
                            //Calculate what that rotation from the rootFace of this door is
                            BlockFace correctFaceForPastedLocation = getBlockFaceAfterRotation(rootBlockFace,difference);
                            //Set it facing equal to the warped signs facing
                            directional.setFacing(correctFaceForPastedLocation);
                            //buttonData.setFacing(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing());
                            //And update the paste location with that data
                            tempPasteLocation.getBlock().setBlockData(directional);

                        }

                        //If the block from the template is a crimson wall sign,
                        if(tempCopyLocation.getBlock().getType()==Material.CRIMSON_WALL_SIGN){
                            //and the door should have the knob on the right
                            if(doorKnobRight){
                                //Otherwise place an oak button
                                tempPasteLocation.getBlock().setType(Material.OAK_BUTTON);
                                //Utility.sendMessageToAllAdmins(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing().toString());
                                //Get the block data of the just placed button
                                Directional buttonData = ((Directional)tempPasteLocation.getBlock().getBlockData());
                                //Get the difference between north and the way that the copy block location is facing
                                int differenceInOriginalPlus =getDifferenceInRotation(BlockFace.NORTH,((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing())+ getInwardsOrOutwards();
                                //Calculate what that rotation from the rootFace of this door is
                                BlockFace correctFaceForPastedLocation = getBlockFaceAfterRotation(rootBlockFace,differenceInOriginalPlus);
                                //Set it facing equal to the warped signs facing
                                buttonData.setFacing(correctFaceForPastedLocation);
                                //buttonData.setFacing(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing());
                                //And update the paste location with that data
                                tempPasteLocation.getBlock().setBlockData(buttonData);
                            }else{
                                //Otherwise just place air
                                tempPasteLocation.getBlock().setType(Material.AIR);
                            }
                        }else if(tempCopyLocation.getBlock().getType()==Material.WARPED_WALL_SIGN){
                            //If it was a warped sign and the doorknob should be on the left
                            if(doorKnobRight){
                                //Place air
                                tempPasteLocation.getBlock().setType(Material.AIR);
                            }else{
                                //Otherwise place an oak button
                                tempPasteLocation.getBlock().setType(Material.OAK_BUTTON);
                                //Utility.sendMessageToAllAdmins(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing().toString());
                                //Get the block data of the just placed button
                                Directional buttonData = ((Directional)tempPasteLocation.getBlock().getBlockData());
                                //Get the difference between north and the way that the copy block location is facing
                                int differenceInOriginalMinus = getDifferenceInRotation(BlockFace.NORTH,((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing())-getInwardsOrOutwards();
                                //Calculate what that rotation from the rootFace of this door is
                                BlockFace correctFaceForPastedLocation = getBlockFaceAfterRotation(rootBlockFace,differenceInOriginalMinus);
                                //Set it facing equal to the warped signs facing
                                buttonData.setFacing(correctFaceForPastedLocation);
                                //buttonData.setFacing(((Directional)tempCopyLocation.getBlock().getBlockData()).getFacing());
                                //And update the paste location with that data
                                tempPasteLocation.getBlock().setBlockData(buttonData);
                            }
                        }

                    }
                }
            }
        }

        clearButtons();

    }

    public void openDoor(){
        //We change the currentBlockFace of this door to be equal to what it should be after rotating
        //the door clockwise by 90 degrees.

        currentBlockFace = getBlockFaceAfterRotation(rootBlockFace, getInwardsOrOutwards());
        //Utility.sendMessageToAllAdmins(String.valueOf(getInwardsOrOutwards()));
        //With this one simple change, we then call updateDoor.
        createOpenDoor();
        open=true;
    }

    public int getInwardsOrOutwards(){
        if(opensOutwards){
            return -90;
        }else{
            return 90;
        }
    }


    public ArrayList<Material> getDoorBlockTypes(){
        ArrayList<Material> materials = new ArrayList<>();

        for (int width = 0; width < doorWidth; width++) {
            for (int height = 0; height < 7; height++) {
                for (int depth = -1; depth <= 1; depth++) {
                    Location templateLocationClone = templateLocation.clone();
                    templateLocationClone.add(width,height,depth);
                    if(!materials.contains(templateLocationClone.getBlock().getType())){
                        materials.add(templateLocationClone.getBlock().getType());
                    }
                }
            }
        }
        materials.add(Material.OAK_BUTTON);
        materials.add(Material.DIAMOND_BLOCK);
        return materials;
    }

    public boolean isOpen() {
        return open;
    }

    public void closeDoor(){
        currentBlockFace=rootBlockFace;
        //updateDoor();
        createClosedDoor();
        open=false;
    }

    //Takes a direction to face, and a rotation as a number, and outputs the direction that it should face after applying that rotation
    public BlockFace getBlockFaceAfterRotation(BlockFace givenBlockFace, int rotationAngle){
        if(rotationAngle==0){
            return givenBlockFace;
        }else if(rotationAngle==90||rotationAngle==-270){
            switch (givenBlockFace) {
                case NORTH:
                    return BlockFace.EAST;
                case EAST:
                    return BlockFace.SOUTH;
                case SOUTH:
                    return BlockFace.WEST;
                case WEST:
                    return BlockFace.NORTH;
            }
        }else if(rotationAngle==180||rotationAngle==-180){
            switch (givenBlockFace) {
                case NORTH:
                    return BlockFace.SOUTH;
                case EAST:
                    return BlockFace.WEST;
                case SOUTH:
                    return BlockFace.NORTH;
                case WEST:
                    return BlockFace.EAST;
            }
        }else if(rotationAngle==270||rotationAngle==-90){
            switch (givenBlockFace) {
                case NORTH:
                    return BlockFace.WEST;
                case EAST:
                    return BlockFace.NORTH;
                case SOUTH:
                    return BlockFace.EAST;
                case WEST:
                    return BlockFace.SOUTH;
            }
        }
        Utility.sendMessageToAllAdmins("ERROR: no angle for:" + rotationAngle);
        return null;
    }







    public void clearButtons(){
        for(Entity entity : mainManager.getWorld().getEntities()){
            if(entity instanceof Item){
                if(((Item)entity).getItemStack().getType() == Material.OAK_BUTTON){
                    entity.remove();
                }
            }
        }
    }

    public void clear(){
        //We prepare the rotation

        //We iterate over every coordinate that would be a copy location

        //The i is there to act as a first pass over the area to find the buttons to remove
        for (int i = 0; i < 2; i++) {
            for (int width = 0; width < doorWidth; width++) {
                for (int height = 0; height < 7; height++) {
                    for (int depth = -1; depth <= 1; depth++) {
                        //Takes the template location
                        Location tempCopyLocation = templateLocation.clone();
                        tempCopyLocation.add(width,height,depth);


                        //And gets the target paste destination ready
                        Location tempPasteLocation = locationToBuildFrom.clone();
                        tempPasteLocation.add(width,height,depth);

                        //Before pasting however, it makes sure to rotate the coordinate, around the root location,
                        // centered on north (Which is where all templates are built from)
                        // to its new rootBlockFace
                        tempPasteLocation = rotateCoordinate(tempPasteLocation,locationToBuildFrom,BlockFace.NORTH,rootBlockFace);

                        //And we check if the block should be removed, if so, we remove it.
                        if(doorMaterials.contains(tempPasteLocation.getBlock().getType())&&(tempPasteLocation.getBlock().getType().equals(Material.OAK_BUTTON)||i>0)){
                            tempPasteLocation.getBlock().setType(Material.AIR);
                        }

                        //======

                        //We get the location data of the copy region
                        tempCopyLocation = templateLocation.clone();
                        tempCopyLocation.add(width,height,depth);

                        //And we get the location data of the paste region
                        tempPasteLocation = locationToRotateFrom.clone();
                        int newWidth = width;
                        int newDepth = depth;
                        if(!doorKnobRight){
                            newWidth=(doorWidth-1)-width;
                            newDepth=depth*-1;
                        }
                        tempPasteLocation.add(newWidth,height,newDepth);

                        //Before pasting, we rotate the pasteLocation, around the location to rotate from,
                        //equal to a number that is spat out by getDifferenceInRotation.
                        //To get that number, we use the difference between the rootBlockFace (How the door started)
                        //And it's current blockface

                        tempPasteLocation = rotateCoordinate(tempPasteLocation,locationToRotateFrom,getInwardsOrOutwards()+getDifferenceInRotation(BlockFace.NORTH,rootBlockFace));

                        //And we check if the block should be removed, if so, we remove it.
                        if(doorMaterials.contains(tempPasteLocation.getBlock().getType())&&(tempPasteLocation.getBlock().getType().equals(Material.OAK_BUTTON)||i>0)){
                            tempPasteLocation.getBlock().setType(Material.AIR);
                        }

                    }
                }
            }
        }
    }



    //Returns a given location either clockwise or counter clockwise around a rootLocation
    //Assumes a resting position
    public Location rotateCoordinate(Location coordinate, Location rootLocation, BlockFace currentRotation, BlockFace toRotateTo) {
        return rotateCoordinate(coordinate, rootLocation,getDifferenceInRotation(currentRotation,toRotateTo));
    }

    public Location rotateCoordinate(Location coordinate, Location rootLocation, int rotationAngle){
        // Convert locations to vectors for easier calculation
        Vector coordVector = coordinate.toVector();
        Vector rootVector = rootLocation.toVector();

        // Translate coordinate to origin
        coordVector.subtract(rootVector);

        // Perform rotation
        double rad = Math.toRadians(rotationAngle);
        double newX = coordVector.getX() * Math.cos(rad) - coordVector.getZ() * Math.sin(rad);
        double newZ = coordVector.getX() * Math.sin(rad) + coordVector.getZ() * Math.cos(rad);

        // Translate back
        Vector rotatedVector = new Vector(newX, coordVector.getY(), newZ).add(rootVector);

        // Create a new Location object in the same world as the original coordinate
        return rotatedVector.toLocation(coordinate.getWorld());
    }

    public int getDifferenceInRotation(BlockFace currentBlockFace, BlockFace toRotateTo){
        // Define the order of faces in clockwise direction
        BlockFace[] order = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

        // Find indices of the 'from' and 'to' faces
        int fromIndex = -1;
        int toIndex = -1;
        for (int i = 0; i < order.length; i++) {
            if (order[i] == currentBlockFace) fromIndex = i;
            if (order[i] == toRotateTo) toIndex = i;
        }

        // Calculate the difference
        int diff = toIndex - fromIndex;

        // Adjust for wrap-around
        if (diff < -2) diff += 4;
        if (diff > 2) diff -= 4;

        return diff*90;
    }





}
