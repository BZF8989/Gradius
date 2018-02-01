public interface Ship extends Sprite {

    default int getX(){
        return 0;
    };

    default int getY(){
        return 0;
    };
}
