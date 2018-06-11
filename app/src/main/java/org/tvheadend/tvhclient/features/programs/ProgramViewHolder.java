package org.tvheadend.tvhclient.features.programs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tvheadend.tvhclient.R;
import org.tvheadend.tvhclient.data.entity.Program;
import org.tvheadend.tvhclient.features.shared.UIUtils;
import org.tvheadend.tvhclient.features.shared.callbacks.RecyclerViewClickCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title)
    private TextView titleTextView;
    @BindView(R.id.time)
    private TextView timeTextView;
    @BindView(R.id.date)
    private TextView dateTextView;
    @BindView(R.id.duration)
    private TextView durationTextView;
    @BindView(R.id.progress)
    private TextView progressTextView;
    @BindView(R.id.summary)
    private TextView summaryTextView;
    @BindView(R.id.description)
    private TextView descriptionTextView;
    @BindView(R.id.series_info)
    private TextView seriesInfoTextView;
    @BindView(R.id.subtitle)
    private TextView subtitleTextView;
    @BindView(R.id.content_type)
    private TextView contentTypeTextView;
    @BindView(R.id.state)
    private ImageView stateTextView;
    @BindView(R.id.genre)
    private TextView genreTextView;

    ProgramViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(Context context, final Program program, RecyclerViewClickCallback clickCallback) {
        itemView.setTag(program);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showProgramSubtitle = sharedPreferences.getBoolean("program_subtitle_enabled", true);
        boolean showGenreColors = sharedPreferences.getBoolean("genre_colors_for_programs_enabled", false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCallback.onClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickCallback.onLongClick(view, getAdapterPosition());
                return true;
            }
        });

        if (program != null) {
            titleTextView.setText(program.getTitle());

            Drawable drawable = UIUtils.getRecordingState(context, program.getRecording());
            stateTextView.setVisibility(drawable != null ? View.VISIBLE : View.GONE);
            stateTextView.setImageDrawable(drawable);

            dateTextView.setText(UIUtils.getDate(context, program.getStart()));

            String time = UIUtils.getTimeText(context, program.getStart()) + " - " + UIUtils.getTimeText(context, program.getStop());
            timeTextView.setText(time);

            String durationTime = context.getString(R.string.minutes, (int) ((program.getStop() - program.getStart()) / 1000 / 60));
            durationTextView.setText(durationTime);

            String progressText = UIUtils.getProgressText(context, program.getStart(), program.getStop());
            progressTextView.setVisibility(!TextUtils.isEmpty(progressText) ? View.VISIBLE : View.GONE);
            progressTextView.setText(progressText);

            String contentType = UIUtils.getContentTypeText(context, program.getContentType());
            contentTypeTextView.setVisibility(!TextUtils.isEmpty(contentType) ? View.VISIBLE : View.GONE);
            contentTypeTextView.setText(contentType);

            String seriesInfo = UIUtils.getSeriesInfo(context, program);
            seriesInfoTextView.setVisibility(!TextUtils.isEmpty(seriesInfo) ? View.VISIBLE : View.GONE);
            seriesInfoTextView.setText(seriesInfo);

            subtitleTextView.setVisibility(showProgramSubtitle && !TextUtils.isEmpty(program.getSubtitle()) ? View.VISIBLE : View.GONE);
            subtitleTextView.setText(program.getSubtitle());

            descriptionTextView.setVisibility(!TextUtils.isEmpty(program.getDescription()) ? View.VISIBLE : View.GONE);
            descriptionTextView.setText(program.getDescription());

            summaryTextView.setVisibility(!TextUtils.isEmpty(program.getSummary()) ? View.VISIBLE : View.GONE);
            summaryTextView.setText(program.getSummary());

            if (showGenreColors) {
                int color = UIUtils.getGenreColor(context, program.getContentType(), 0);
                genreTextView.setBackgroundColor(color);
                genreTextView.setVisibility(View.VISIBLE);
            } else {
                genreTextView.setVisibility(View.GONE);
            }
        }
    }
}