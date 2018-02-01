/**
 * Created by Bill on 11/5/2017.
 *
 */
public interface ShipLaser extends Ship {



    default void toBeRemove(){
    }

    default boolean needsToBeRemoved(){
        return false;
    }

}
