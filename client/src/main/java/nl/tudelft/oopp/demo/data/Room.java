package nl.tudelft.oopp.demo.data;

public class Room {

    private String id;

    private boolean open;

    public Room(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Get method for the status of the room.
     * @return true if the room is open, false otherwise.
     */
    public boolean isOpen() {
        return open;
    }


    /**
     * Opens the referred room.
     */
    private void setOpen() {
        this.open = true;
    }

    /**
     * Closes the referred room.
     */
    private void setClosed() {
        this.open = false;
    }

    @Override
    public String toString() {
        return "Room id:" + id;
    }


}
