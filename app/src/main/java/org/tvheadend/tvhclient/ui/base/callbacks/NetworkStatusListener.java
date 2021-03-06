package org.tvheadend.tvhclient.ui.base.callbacks;

public interface NetworkStatusListener {

    /**
     * Called by a broadcast receiver when the network status has changed.
     * This method is called in the activity that listens to the broadcast.
     * The activity will then inform any fragments via this methods.
     *
     * @param isAvailable True is network is available, false otherwise
     */
    void onNetworkStatusChanged(boolean isAvailable);

    /**
     * Returns the status of the network availability. This method is used by
     * fragments to get the status from the activity which receives the
     * network status from the broadcast receiver
     *
     * @return True if network is available, false otherwise
     */
    boolean isNetworkAvailable();
}
