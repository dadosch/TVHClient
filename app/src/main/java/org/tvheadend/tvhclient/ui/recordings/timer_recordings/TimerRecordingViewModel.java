package org.tvheadend.tvhclient.ui.recordings.timer_recordings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.tvheadend.tvhclient.data.DataRepository;
import org.tvheadend.tvhclient.data.entity.TimerRecording;

import java.util.List;

public class TimerRecordingViewModel extends AndroidViewModel {

    private final DataRepository repository;
    private LiveData<List<TimerRecording>> recordings = new MutableLiveData<>();

    public TimerRecordingViewModel(Application application) {
        super(application);
        repository = new DataRepository(application);
        recordings = repository.getAllTimerRecordings();
    }

    public LiveData<List<TimerRecording>> getRecordings() {
        return recordings;
    }

    LiveData<TimerRecording> getRecording(String id) {
        return repository.getTimerRecording(id);
    }
}
