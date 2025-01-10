package traffic;

public class Road {
    private final String name;
    private int index;
    private boolean open;
    private int remainingTime;

    public Road(String name, int index, int remainingTime) {
        this.name = name;
        this.index = index;
        this.open = false;
        this.remainingTime = remainingTime;
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }


}
