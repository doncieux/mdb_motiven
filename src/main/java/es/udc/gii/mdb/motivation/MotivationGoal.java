package es.udc.gii.mdb.motivation;

/**
 *
 * @author Rodri
 * @param <E> represents a context needed to know if a goal has been reached
 */


public interface MotivationGoal<E> {
    
    public enum Type {REAL, SUBGOAL};
    
    public boolean goalReached(E context);
   
    public Type getType();
    
    //public double getEU();
    
    public void setDistance(double distance);
    
    public double getDistance();
 
    public E getData();
    
}
