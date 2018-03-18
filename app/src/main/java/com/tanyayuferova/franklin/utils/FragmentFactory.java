package com.tanyayuferova.franklin.utils;

import android.support.v4.app.Fragment;

import com.tanyayuferova.franklin.ui.InfoFragment;
import com.tanyayuferova.franklin.ui.SettingsFragment;
import com.tanyayuferova.franklin.ui.VirtuesFragment;

/**
 * Created by Tanya Yuferova on 3/18/2018.
 */

public class FragmentFactory {

    public static Fragment createFragment(String screenKey, Object data) {
        switch (screenKey) {
            case VirtuesFragment.SCREEN_KEY:
                return VirtuesFragment.newInstance(data);
            case SettingsFragment.SCREEN_KEY:
                return SettingsFragment.newInstance(data);
            case InfoFragment.SCREEN_KEY:
                return InfoFragment.newInstance(data);
            default:
                throw new RuntimeException("Unknown screen key");
        }
    }
}
