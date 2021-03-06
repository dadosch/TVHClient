/*
 *  Copyright (C) 2013 Robert Siebert
 *
 * This file is part of TVHGuide.
 *
 * TVHGuide is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TVHGuide is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TVHGuide.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.tvheadend.tvhclient.ui.features.settings;

import android.os.Bundle;
import android.preference.ListPreference;

import org.tvheadend.tvhclient.R;
import org.tvheadend.tvhclient.domain.entity.Connection;
import org.tvheadend.tvhclient.domain.entity.ServerProfile;
import org.tvheadend.tvhclient.ui.base.callbacks.BackPressedInterface;
import org.tvheadend.tvhclient.ui.base.utils.SnackbarUtils;

import java.util.List;

public class SettingsProfilesFragment extends BasePreferenceFragment implements BackPressedInterface {

    private ListPreference recordingProfilesPreference;
    private ListPreference htspPlaybackProfilesPreference;
    private ListPreference httpPlaybackProfilesPreference;
    private ListPreference castingProfilesPreference;
    private int htspPlaybackServerProfileId;
    private int httpPlaybackServerProfileId;
    private int recordingServerProfileId;
    private int castingServerProfileId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_profiles);

        Connection connection = appRepository.getConnectionData().getActiveItem();
        toolbarInterface.setTitle(getString(R.string.pref_profiles));
        toolbarInterface.setSubtitle(connection != null ? connection.getName() : null);

        htspPlaybackProfilesPreference = (ListPreference) findPreference("htsp_playback_profiles");
        httpPlaybackProfilesPreference = (ListPreference) findPreference("http_playback_profiles");
        recordingProfilesPreference = (ListPreference) findPreference("recording_profiles");
        castingProfilesPreference = (ListPreference) findPreference("casting_profiles");

        if (savedInstanceState != null) {
            htspPlaybackServerProfileId = savedInstanceState.getInt("htsp_playback_profile_id");
            httpPlaybackServerProfileId = savedInstanceState.getInt("http_playback_profile_id");
            recordingServerProfileId = savedInstanceState.getInt("recording_profile_id");
            castingServerProfileId = savedInstanceState.getInt("casting_profile_id");
        } else {
            htspPlaybackServerProfileId = serverStatus.getHtspPlaybackServerProfileId();
            httpPlaybackServerProfileId = serverStatus.getHttpPlaybackServerProfileId();
            recordingServerProfileId = serverStatus.getRecordingServerProfileId();
            castingServerProfileId = serverStatus.getCastingServerProfileId();
        }

        addProfiles(htspPlaybackProfilesPreference,
                appRepository.getServerProfileData().getHtspPlaybackProfiles(),
                htspPlaybackServerProfileId);
        addProfiles(httpPlaybackProfilesPreference,
                appRepository.getServerProfileData().getHttpPlaybackProfiles(),
                httpPlaybackServerProfileId);
        addProfiles(recordingProfilesPreference,
                appRepository.getServerProfileData().getRecordingProfiles(),
                recordingServerProfileId);
        addProfiles(castingProfilesPreference,
                appRepository.getServerProfileData().getHttpPlaybackProfiles(),
                castingServerProfileId);

        setHttpPlaybackProfileListSummary();
        setHtspPlaybackProfileListSummary();
        setRecordingProfileListSummary();
        setCastingProfileListSummary();

        htspPlaybackProfilesPreference.setOnPreferenceChangeListener((preference, o) -> {
            htspPlaybackServerProfileId = Integer.valueOf((String) o);
            setHtspPlaybackProfileListSummary();
            return true;
        });
        httpPlaybackProfilesPreference.setOnPreferenceChangeListener((preference, o) -> {
            httpPlaybackServerProfileId = Integer.valueOf((String) o);
            setHttpPlaybackProfileListSummary();
            return true;
        });
        recordingProfilesPreference.setOnPreferenceChangeListener((preference, o) -> {
            recordingServerProfileId = Integer.valueOf((String) o);
            setRecordingProfileListSummary();
            return true;
        });

        if (isUnlocked) {
            castingProfilesPreference.setOnPreferenceChangeListener((preference, o) -> {
                castingServerProfileId = Integer.valueOf((String) o);
                setCastingProfileListSummary();
                return true;
            });
        } else {
            castingProfilesPreference.setOnPreferenceClickListener(preference -> {
                SnackbarUtils.sendSnackbarMessage(activity, R.string.feature_not_supported_by_server);
                return true;
            });
        }
    }

    private void setHtspPlaybackProfileListSummary() {
        if (htspPlaybackServerProfileId == 0) {
            htspPlaybackProfilesPreference.setSummary("None");
        } else {
            ServerProfile playbackProfile = appRepository.getServerProfileData().getItemById(htspPlaybackServerProfileId);
            htspPlaybackProfilesPreference.setSummary(playbackProfile != null ? playbackProfile.getName() : null);
        }
    }

    private void setHttpPlaybackProfileListSummary() {
        if (httpPlaybackServerProfileId == 0) {
            httpPlaybackProfilesPreference.setSummary("None");
        } else {
            ServerProfile playbackProfile = appRepository.getServerProfileData().getItemById(httpPlaybackServerProfileId);
            httpPlaybackProfilesPreference.setSummary(playbackProfile != null ? playbackProfile.getName() : null);
        }
    }

    private void setRecordingProfileListSummary() {
        if (recordingServerProfileId == 0) {
            recordingProfilesPreference.setSummary("None");
        } else {
            ServerProfile recordingProfile = appRepository.getServerProfileData().getItemById(recordingServerProfileId);
            recordingProfilesPreference.setSummary(recordingProfile != null ? recordingProfile.getName() : null);
        }
    }

    private void setCastingProfileListSummary() {
        if (castingServerProfileId == 0) {
            castingProfilesPreference.setSummary("None");
        } else {
            ServerProfile castingProfile = appRepository.getServerProfileData().getItemById(castingServerProfileId);
            castingProfilesPreference.setSummary(castingProfile != null ? castingProfile.getName() : null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("htsp_playback_profile_id", htspPlaybackServerProfileId);
        outState.putInt("http_playback_profile_id", httpPlaybackServerProfileId);
        outState.putInt("recording_profile_id", recordingServerProfileId);
        outState.putInt("casting_profile_id", castingServerProfileId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        serverStatus.setHtspPlaybackServerProfileId(htspPlaybackServerProfileId);
        serverStatus.setHttpPlaybackServerProfileId(httpPlaybackServerProfileId);
        serverStatus.setRecordingServerProfileId(recordingServerProfileId);
        if (isUnlocked) {
            serverStatus.setCastingServerProfileId(castingServerProfileId);
        }
        appRepository.getServerStatusData().updateItem(serverStatus);
        activity.finish();
    }

    private void addProfiles(final ListPreference listPreference, final List<ServerProfile> serverProfileList, int selectedIndex) {
        // Initialize the arrays that contain the profile values
        final int size = serverProfileList.size() + 1;
        CharSequence[] entries = new CharSequence[size];
        CharSequence[] entryValues = new CharSequence[size];

        entries[0] = "None";
        entryValues[0] = "0";

        // Add the available profiles to list preference
        int index = 0;
        for (int i = 1; i < size; i++) {
            ServerProfile profile = serverProfileList.get(i - 1);
            entries[i] = profile.getName();
            entryValues[i] = String.valueOf(profile.getId());
            if (selectedIndex == profile.getId()) {
                index = i;
            }
        }
        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);
        listPreference.setValueIndex(index);
    }
}
