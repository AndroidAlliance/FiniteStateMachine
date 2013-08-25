/*
  ADAPTED FOR ANDROID FROM:
  RobotLegs / PureMVC AS3 Utility - StateMachine
  Copyright (c) 2008 Neil Manuell, Cliff Hall;
  Copyright (c) 2013 Android Alliance Ltd
  Your reuse is governed by the Creative Commons Attribution 3.0 License
  http://creativecommons.org/licenses/by/3.0/
 */
package uk.co.androidalliance.fsm;


import java.util.*;

import uk.co.androidalliance.fsm.interfaces.ActionType;
import uk.co.androidalliance.fsm.interfaces.TargetType;
import uk.co.androidalliance.fsm.interfaces.StateType;

public class StateMachine {

    public interface Observer {
        void onStateEntering(State state);
        void onStateExiting(State state);
        void onStateChanged(State state);
        void onChanged(State state, ActionType actionType);
    }

    protected List<Observer> observers;

    public void addObserver(Observer observer) {
        removeObserver(observer);
        if (observers == null ) {
            observers = new ArrayList<Observer>();
        }
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        if (observers != null) {
            Iterator<Observer> iterator = observers.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == observer) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    public void removeAllObservers() {
        observers.clear();
    }

    protected void dispatchStateEntering(State state) {
        if ( observers != null ) {
            Iterator<Observer> iterator = observers.iterator();
            while( iterator.hasNext() ) {
               iterator.next().onStateEntering(state);
            }
        }
    }

    protected void dispatchStateExiting(State state) {
        if ( observers != null ) {
            Iterator<Observer> iterator = observers.iterator();
            while( iterator.hasNext() ) {
                iterator.next().onStateExiting(state);
            }
        }
    }

    protected void dispatchStateChanged(State state) {
        if ( observers != null ) {
            Iterator<Observer> iterator = observers.iterator();
            while( iterator.hasNext() ) {
                iterator.next().onStateChanged(state);
            }
        }
    }

    protected void dispatchChanged(State state, ActionType actionType) {
        if ( observers != null ) {
            Iterator<Observer> iterator = observers.iterator();
            while( iterator.hasNext() ) {
                iterator.next().onChanged(state, actionType);
            }
        }
    }

    protected State currentState;

    /**
     * Map of States objects by name.
     */
    protected Map<StateType,State> states = new HashMap<StateType, State>();

    /**
     * The initial state of the FSM.
     */
    protected State initial = null;

    /**
     * The transition has been canceled.
     */
    protected Boolean canceled;

    /**
     * StateMachine Constructor
     */
    public StateMachine() {
    }

    public void start() {
        start(null);
    }

    public void start(ActionType actionType) {
        if (initial != null) {
            transitionTo(initial, actionType);
        }
    }

    public void stateAction(TargetType targetType, ActionType actionType) {
        StateType newStateTarget = currentState.getTarget(targetType);
        State newState = states.get(newStateTarget);
        if (newStateTarget!=null) {
            transitionTo(newState, actionType);
        }
    }

    public void stateCancel(TargetType action) {
        canceled = true;
    }

    /**
     * Registers the initial state
     *
     * @param state the state to which to register the above commands
     */
    public void registerInitialState(State state) {
        this.initial = state;
    }

    /**
     * Registers the entry and exit commands for a given state.
     *
     * @param state the state to which to register the above commands
     */
    public void registerState(State state) {
        if (state == null || states.get(state.getStateType()) != null) {
            return;
        }
        states.put(state.getStateType(), state);
    }

    /**
     * Remove a state mapping.
     * <p/>
     * Removes the entry and exit commands for a given state
     * as well as the state mapping itself.</P>
     *
     * @param state
     */
    public void removeState(State state) {
        if (!states.containsValue(state)) {
            return;
        }
        states.remove(state);
    }

    /**
     * Transitions to the given state from the current state.
     * <p/>
     * Sends the <code>exiting</code> StateEvent for the current state
     * followed by the <code>entering</code> StateEvent for the new state.
     * Once finally transitioned to the new state, the <code>changed</code>
     * StateEvent for the new state is sent.</P>
     * <p/>
     * If a data parameter is provided, it is included as the body of all
     * three state-specific transition notes.</P>
     * <p/>
     * Finally, when all the state-specific transition notes have been
     * sent, a <code>StateEvent.CHANGED</code> event is sent, with the
     * new <code>State</code> object as the <code>body</code> and the name of the
     * new state in the <code>type</code>.
     *
     * @param nextState the next State to transition to.
     * @param actionType is the optional Object that was sent in the <code>stateAction</code> method
     */
    protected void transitionTo(State nextState, ActionType actionType) {
        // Going nowhere?
        if (nextState == null) {
            return;
        }

        // Clear the cancel flag
        canceled = false;

        // Exit the current State 
        if (currentState != null && currentState.getExiting() != null) {
            //eventDispatcher.dispatchEvent( new StateEvent( currentState.exiting, null, data ));
            dispatchStateExiting(currentState);
        }

        // Check to see whether the exiting guard has been canceled
        if (canceled) {
            canceled = false;
            return;
        }

        // Enter the next State 
        if (nextState.getEntering() != null) {
            //eventDispatcher.dispatchEvent( new StateEvent( nextState.entering, null, data ));
            dispatchStateEntering(nextState);
        }

        // Check to see whether the entering guard has been canceled
        if (canceled) {
            canceled = false;
            return;
        }

        // change the current state only when both guards have been passed
        currentState = nextState;

        // Send the notification configured to be sent when this specific state becomes current 
        if (nextState.getChanged() != null) {
            //eventDispatcher.dispatchEvent( new StateEvent( currentState.changed, null, data ));
            dispatchStateChanged(currentState);
        }

        // Notify the app generally that the state changed and what the new state is
        // eventDispatcher.dispatchEvent( new StateEvent( StateEvent.CHANGED, currentState.name));
        dispatchChanged(currentState, actionType);
    }

    public State getCurrentState() {
        return currentState;
    }

}
