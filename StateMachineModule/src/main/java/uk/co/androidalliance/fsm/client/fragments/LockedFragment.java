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
package uk.co.androidalliance.fsm.client.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.co.androidalliance.fsm.client.targettypes.ClientTargetType;
import uk.co.androidalliance.fsm.interfaces.TransitionAction;
import uk.co.androidalliance.fsm.client.R;
import uk.co.androidalliance.fsm.client.actiontypes.UnlockActionTypes;

public class LockedFragment extends Fragment {

    private final String TAG = LockedFragment.class.getSimpleName();
    public static final String FRAGMENT_TAG = "LockedFragment";

    public static LockedFragment newInstance() {
        return new LockedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.locked_fragment, null);
        bindViews(v);
        return v;
    }

    private void bindViews(View v) {
        Button unlockBtn = (Button) v.findViewById(R.id.action_unlock_button);
        unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ((TransitionAction) getActivity()).onStateAction(ClientTargetType.UNLOCK, new UnlockActionTypes("dummy data"));
                }
            }
        });
    }

}