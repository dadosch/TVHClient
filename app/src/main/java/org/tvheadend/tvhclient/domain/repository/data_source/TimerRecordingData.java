package org.tvheadend.tvhclient.domain.repository.data_source;

import android.os.AsyncTask;

import org.tvheadend.tvhclient.data.db.AppRoomDatabase;
import org.tvheadend.tvhclient.domain.entity.TimerRecording;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import timber.log.Timber;

public class TimerRecordingData implements DataSourceInterface<TimerRecording> {

    private final AppRoomDatabase db;

    public TimerRecordingData(AppRoomDatabase database) {
        this.db = database;
    }

    @Override
    public void addItem(TimerRecording item) {
        AsyncTask.execute(() -> db.getTimerRecordingDao().insert(item));
    }

    @Override
    public void updateItem(TimerRecording item) {
        AsyncTask.execute(() -> db.getTimerRecordingDao().update(item));
    }

    @Override
    public void removeItem(TimerRecording item) {
        AsyncTask.execute(() -> db.getTimerRecordingDao().delete(item));
    }

    @Override
    public LiveData<Integer> getLiveDataItemCount() {
        return db.getTimerRecordingDao().getRecordingCount();
    }

    public LiveData<List<TimerRecording>> getLiveDataItems() {
        return db.getTimerRecordingDao().loadAllRecordings();
    }

    @Override
    public LiveData<TimerRecording> getLiveDataItemById(@NonNull Object id) {
        return db.getTimerRecordingDao().loadRecordingById((String) id);
    }

    @Override
    public TimerRecording getItemById(@NonNull Object id) {
        try {
            return new TimerRecordingByIdTask(db, (String) id).execute().get();
        } catch (InterruptedException e) {
            Timber.d("Loading timer recording by id task got interrupted", e);
        } catch (ExecutionException e) {
            Timber.d("Loading timer recording by id task aborted", e);
        }
        return null;
    }

    @Override
    @NonNull
    public List<TimerRecording> getItems() {
        return new ArrayList<>();
    }

    private static class TimerRecordingByIdTask extends AsyncTask<Void, Void, TimerRecording> {
        private final AppRoomDatabase db;
        private final String id;

        TimerRecordingByIdTask(AppRoomDatabase db, String id) {
            this.db = db;
            this.id = id;
        }

        @Override
        protected TimerRecording doInBackground(Void... voids) {
            return db.getTimerRecordingDao().loadRecordingByIdSync(id);
        }
    }
}
