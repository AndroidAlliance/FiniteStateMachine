/*
  ADAPTED FOR ANDROID FROM:
  RobotLegs / PureMVC AS3 Utility - StateMachine
  Copyright (c) 2008 Neil Manuell, Cliff Hall;
  Copyright (c) 2013 Android Alliance Ltd
  Your reuse is governed by the Creative Commons Attribution 3.0 License
  http://creativecommons.org/licenses/by/3.0/
 */
package uk.co.androidalliance.fsm;

import uk.co.androidalliance.fsm.interfaces.TargetType;
import uk.co.androidalliance.fsm.interfaces.StateType;

import java.util.HashMap;
import java.util.Map;

public class State {

    // Transition map of actions to target states
    protected Map<TargetType, StateType> transitions = new HashMap<TargetType, StateType>();

    // The state name
    protected StateType stateType;

    // The notification to dispatch when entering the state
    protected Object entering;

    // The notification to dispatch when exiting the state
    protected Object exiting;

    // The notification to dispatch when the state has actually changed
    protected Object changed;

    /**
     * Constructor.
     */
    private State(Builder builder) {
        this.stateType = builder.stateType;
        this.entering = builder.entering;
        this.exiting = builder.exiting;
        this.changed = builder.changed;
    }

    /**
     * Define a transition.
     *
     * @param targetType the name of the StateMachine.ACTION event type.
     * @param stateType the name of the target state to transition to.
     */
    public void defineTrans(TargetType targetType, StateType stateType) {
        if (getTarget(targetType) != null) {
            return;
        }
        transitions.put(targetType, stateType);
    }

    /**
     * Remove a previously defined transition.
     */
    public void removeTrans(TargetType targetType) {
        //transitions.put(action, null);
        transitions.remove(targetType);
    }

    /**
     * Get the target state name for a given action.
     */
    public StateType getTarget(TargetType targetType) {
        return transitions.get(targetType);
    }

    /**
     * Get the state type
     */
    public StateType getStateType(){
        return stateType;
    }

    /**
     * Get the entering object
     */
    public Object getEntering(){
        return entering;
    }

    /**
     * Get the exiting object
     */
    public Object getExiting(){
        return exiting;
    }

    /**
     * Get the changed object
     */
    public Object getChanged(){
        return changed;
    }

    @Override
    public String toString (){
        return "State: " + stateType + ", entering: " + entering + ", exiting: " + exiting + ", changed: " + changed;
    }

    public static class Builder {

        private final StateType stateType;
        private Object entering;
        private Object exiting;
        private Object changed;

        /**
         * @param stateType the id of the state
         */
        public Builder (StateType stateType){
            this.stateType = stateType;
        }

        /**
         *  @param entering an optional event Object to be sent when entering this state
         */
        public Builder setEntering(Object entering){
            this.entering = entering;
            return this;
        }

        /**
         * @param exiting an optional event Object to be sent when exiting this state
         */
        public Builder setExiting(Object exiting){
            this.exiting = exiting;
            return this;
        }
        /**
         * @param changed an optional event Object to be sent when fully transitioned to this state
         */
        public Builder setChanged(Object changed){
            this.changed = changed;
            return this;
        }

        public State build() {
            return new State(this);
        }
    }
}
