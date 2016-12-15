package es.udc.gii.mdb.motivation;

import es.udc.gii.mdb.perception.ActionPerceptionPair;
import es.udc.gii.mdb.perception.constraint.Constraint;
import es.udc.gii.mdb.robot.Sensor;
import es.udc.gii.mdb.robot.SensorMap;

/**
 *
 * @author Rodri
 */
public class SensorGoal implements MotivationGoal<ActionPerceptionPair> {

    private Sensor sensor;
    private double distance;
    private double value;

    public SensorGoal(String sensorId) {
        sensor = SensorMap.getInstance().getComponent(sensorId);
    }

    public SensorGoal(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public boolean goalReached(ActionPerceptionPair context) {
        value = sensor.getValue().getValue();
        return value > 1e-6;
    }

    @Override
    public Type getType() {
        return Type.REAL;
    }

//    @Override
//    public double getEU() {
//        return 1.0;
//    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    public double getValue() {
        return value;
    }

    @Override
    public ActionPerceptionPair getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    

}
