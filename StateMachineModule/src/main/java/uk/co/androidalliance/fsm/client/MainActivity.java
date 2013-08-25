/*
 * Copyright (c) 2013 Android Alliance LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.androidalliance.fsm.client;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import uk.co.androidalliance.fsm.State;
import uk.co.androidalliance.fsm.StateMachine;
import uk.co.androidalliance.fsm.client.R;
import uk.co.androidalliance.fsm.client.actiontypes.StartActionType;
import uk.co.androidalliance.fsm.client.bundles.Entering;
import uk.co.androidalliance.fsm.client.fragments.ClosedFragment;
import uk.co.androidalliance.fsm.client.fragments.LockedFragment;
import uk.co.androidalliance.fsm.client.fragments.OpenedFragment;
import uk.co.androidalliance.fsm.client.statetype.ClientStateType;
import uk.co.androidalliance.fsm.client.targettypes.ClientTargetType;
import uk.co.androidalliance.fsm.interfaces.ActionType;
import uk.co.androidalliance.fsm.interfaces.TargetType;
import uk.co.androidalliance.fsm.interfaces.TransitionAction;

import static uk.co.androidalliance.fsm.client.statetype.ClientStateType.CLOSED;
import static uk.co.androidalliance.fsm.client.statetype.ClientStateType.LOCKED;
import static uk.co.androidalliance.fsm.client.statetype.ClientStateType.OPENED;

public class MainActivity extends Activity implements StateMachine.Observer, TransitionAction {

    private final String TAG = MainActivity.class.getSimpleName();
    private StateMachine fsm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bootstrapFSM();
    }

    private void bootstrapFSM() {
        fsm = new StateMachine();
        fsm.addObserver(this);

        // use a bundle
        State openedState = new State.Builder(OPENED).setEntering(new Entering("event/openingEvent")).setExiting("event/aboutToCloseEvent").build();
        openedState.defineTrans(ClientTargetType.CLOSE, ClientStateType.CLOSED);
        //Log.d(TAG, openedState.toString());

        State closedState = new State.Builder(CLOSED).setEntering("event/closingEvent").setChanged("event/changedEvent").build();
        closedState.defineTrans(ClientTargetType.OPEN, ClientStateType.OPENED);
        closedState.defineTrans(ClientTargetType.LOCK, ClientStateType.LOCKED);

        State lockedState = new State.Builder(LOCKED).setEntering("event/lockingEvent").build();
        lockedState.defineTrans(ClientTargetType.UNLOCK, ClientStateType.CLOSED);

        fsm.registerInitialState(closedState);
        fsm.registerState(openedState);
        fsm.registerState(closedState);
        fsm.registerState(lockedState);
        fsm.start(new StartActionType("any data payload you wish to append"));
    }

    private void showFragment(Fragment fragment, String fragmentTag, boolean addToBackStack, boolean withAnimation) {
        Fragment previous = getFragmentManager().findFragmentByTag(fragmentTag);
        if (previous != null) {
            return;
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (withAnimation) {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        ft.replace(R.id.main_container, fragment, fragmentTag);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onStateEntering(State state) {
        Object obj = state.getEntering();
        if (obj instanceof Entering){
            Log.d(TAG, "onStateEntering() " + state.getStateType() + ", Entering: " + obj.toString());
        } else {
            Log.d(TAG, "onStateEntering() " + state.getStateType() + ", entering data: " + state.getEntering());
        }
    }

    @Override
    public void onStateExiting(State state) {
        Log.d(TAG, "onStateExiting() " + state.getStateType() + ", exiting data: " + state.getExiting());
    }

    @Override
    public void onStateChanged(State state) {
        Log.d(TAG, "onStateChanged() " + state.getStateType() + ", changed data: " + state.getChanged());
    }

    @Override
    public void onChanged(State state, ActionType actionType) {

        Log.d(TAG, "onChanged() " + state.getStateType().toString() + " : " + actionType.toString());

        if (state.getStateType().equals(ClientStateType.CLOSED)) {
            showClosed();
        } else if (state.getStateType().equals(ClientStateType.OPENED)) {
            showOpened();
        } else if (state.getStateType().equals(ClientStateType.LOCKED)) {
            showLocked();
        }
    }

    private void showLocked() {
        showFragment(LockedFragment.newInstance(), LockedFragment.FRAGMENT_TAG, false, true);
    }

    private void showOpened() {
        showFragment(OpenedFragment.newInstance(), OpenedFragment.FRAGMENT_TAG, false, true);
    }

    private void showClosed() {
        showFragment(ClosedFragment.newInstance(), ClosedFragment.FRAGMENT_TAG, false, true);
    }

    @Override
    public void onStateAction(TargetType targetType, ActionType actionType) {
        fsm.stateAction(targetType, actionType);
    }

    @Override
    public void onStateCancel(TargetType targetType, ActionType actionType) {
        fsm.stateCancel(targetType);
    }

}
