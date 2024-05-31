import java.util.Random;
public class Food {
    private int coordinateX;
    private int coordinateY;

    public Food(){
        coordinateX = generatePosition(Graphics.width);
        coordinateY = generatePosition(Graphics.height);
    }

    private int generatePosition(int size) {
        Random rand = new Random();
        return rand.nextInt(size / Graphics.part ) * Graphics.part;
    }

    public int getCoordinateX(){
        return coordinateX;
    }

    public int getCoordinateY(){
        return coordinateY;
    }
}