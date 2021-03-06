package org.tvheadend.tvhclient.ui.features.playback.external;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import org.tvheadend.tvhclient.R;
import org.tvheadend.tvhclient.domain.entity.Channel;
import org.tvheadend.tvhclient.data.service.HtspService;

import androidx.annotation.Nullable;
import timber.log.Timber;

public class PlayChannelActivity extends BasePlaybackActivity {

    private int channelId;
    private Channel channel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            channelId = getIntent().getIntExtra("channelId", -1);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                channelId = bundle.getInt("channelId", -1);
            }
        }
    }

    @Override
    protected boolean requireHostnameToAddressConversion() {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("channelId", channelId);
    }

    @Override
    protected void getHttpTicket() {
        channel = appRepository.getChannelData().getItemById(channelId);
        if (channel != null) {
            Intent intent = new Intent(this, HtspService.class);
            intent.setAction("getTicket");
            intent.putExtra("channelId", channel.getId());
            startService(intent);
        } else {
            progressBar.setVisibility(View.GONE);
            statusTextView.setText(getString(R.string.error_starting_playback_no_channel));
        }
    }

    @Override
    protected void onHttpTicketReceived() {
        Timber.d("Playing channel from server with url " + serverUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(serverUrl), "video/*");
        if (channel != null && !TextUtils.isEmpty(channel.getName())) {
            intent.putExtra("itemTitle", channel.getName());
            intent.putExtra("title", channel.getName());
        }
        startExternalPlayer(intent);
    }
}
