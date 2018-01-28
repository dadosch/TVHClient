package org.tvheadend.tvhclient.service;

import org.tvheadend.tvhclient.data.entity.Channel;
import org.tvheadend.tvhclient.data.entity.ChannelTag;
import org.tvheadend.tvhclient.data.entity.Program;
import org.tvheadend.tvhclient.data.entity.Recording;
import org.tvheadend.tvhclient.data.entity.SeriesRecording;
import org.tvheadend.tvhclient.data.entity.TimerRecording;

public class HTSUtils {
    private HTSUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static ChannelTag convertMessageToChannelTagModel(ChannelTag tag, HTSMessage msg) {
        /*
        if (msg.containsKey("tagId")) {
            tag.tagId = msg.getInt("tagId");
        }
        if (msg.containsKey("tagName")) {
            tag.tagName = msg.getString("tagName");
        }
        if (msg.containsKey("tagIndex")) {
            tag.tagIndex = msg.getInt("tagIndex");
        }
        if (msg.containsKey("tagIcon")) {
            tag.tagIcon = msg.getString("tagIcon");
        }
        if (msg.containsKey("tagTitledIcon")) {
            tag.tagTitledIcon = msg.getInt("tagTitledIcon");
        }
        if (msg.containsKey("members")) {
            tag.members = msg.getIntList("members", tag.members);
        }
        */
        return tag;
    }

    public static Channel convertMessageToChannelModel(Channel channel, HTSMessage msg) {
        /*
        if (msg.containsKey("channelId")) {
            channel.channelId = msg.getInt("channelId");
        }
        if (msg.containsKey("channelNumber")) {
            channel.channelNumber = msg.getInt("channelNumber");
        }
        if (msg.containsKey("channelNumberMinor")) {
            channel.channelNumberMinor = msg.getInt("channelNumberMinor");
        }
        if (msg.containsKey("channelName")) {
            channel.channelName = msg.getString("channelName");
        }
        if (msg.containsKey("channelIcon")) {
            channel.channelIcon = msg.getString("channelIcon");
        }
        if (msg.containsKey("eventId")) {
            channel.eventId = msg.getInt("eventId");
        }
        if (msg.containsKey("nextEventId")) {
            channel.nextEventId = msg.getInt("nextEventId");
        }
        if (msg.containsKey("tags")) {
            channel.tags = msg.getIntList("tags", channel.tags);
        }
        */
        return channel;
    }

    public static Recording convertMessageToRecordingModel(Recording recording, HTSMessage msg) {
        if (msg.containsKey("id")) {
            recording.setId(msg.getInt("id"));
        }
        if (msg.containsKey("channel")) {
            recording.setChannelId(msg.getInt("channel"));
        }
        if (msg.containsKey("start")) {
            recording.setStart(msg.getLong("start") * 1000);
        }
        if (msg.containsKey("stop")) {
            recording.setStop(msg.getLong("stop") * 1000);
        }
        if (msg.containsKey("startExtra")) {
            recording.setStartExtra(msg.getLong("startExtra"));
        }
        if (msg.containsKey("stopExtra")) {
            recording.setStopExtra(msg.getLong("stopExtra"));
        }
        if (msg.containsKey("retention")) {
            recording.setRetention(msg.getLong("retention"));
        }
        if (msg.containsKey("priority")) {
            recording.setPriority(msg.getInt("priority"));
        }
        if (msg.containsKey("eventId")) {
            recording.setEventId(msg.getInt("eventId"));
        }
        if (msg.containsKey("autorecId")) {
            recording.setAutorecId(msg.getString("autorecId"));
        }
        if (msg.containsKey("timerecId")) {
            recording.setTimerecId(msg.getString("timerecId"));
        }
        if (msg.containsKey("contentType")) {
            recording.setContentType(msg.getInt("contentType"));
        }
        if (msg.containsKey("title")) {
            recording.setTitle(msg.getString("title"));
        }
        if (msg.containsKey("subtitle")) {
            recording.setSubtitle(msg.getString("subtitle"));
        }
        if (msg.containsKey("summary")) {
            recording.setSummary(msg.getString("summary"));
        }
        if (msg.containsKey("description")) {
            recording.setDescription(msg.getString("description"));
        }
        if (msg.containsKey("state")) {
            recording.setState(msg.getString("state"));
        }
        if (msg.containsKey("error")) {
            recording.setError(msg.getString("error"));
        }
        if (msg.containsKey("owner")) {
            recording.setOwner(msg.getString("owner"));
        }
        if (msg.containsKey("creator")) {
            recording.setCreator(msg.getString("creator"));
        }
        if (msg.containsKey("subscriptionError")) {
            recording.setSubscriptionError(msg.getString("subscriptionError"));
        }
        if (msg.containsKey("streamErrors")) {
            recording.setStreamErrors(msg.getString("streamErrors"));
        }
        if (msg.containsKey("dataErrors")) {
            recording.setDataErrors(msg.getString("dataErrors"));
        }
        if (msg.containsKey("path")) {
            recording.setPath(msg.getString("path"));
        }
        if (msg.containsKey("dataSize")) {
            recording.setDataSize(msg.getLong("dataSize"));
        }
        if (msg.containsKey("enabled")) {
            recording.setEnabled(msg.getInt("enabled"));
        }
        return recording;
    }

    public static Program convertMessageToProgramModel(Program program, HTSMessage msg) {
/*
        if (msg.containsKey("eventId")) {
            program.eventId = msg.getInt("eventId");
        }
        if (msg.containsKey("channelId")) {
            program.channelId = msg.getInt("channelId");
        }
        if (msg.containsKey("start")) {
            program.start = msg.getLong("start") * 1000;
        }
        if (msg.containsKey("stop")) {
            program.stop = msg.getLong("stop") * 1000;
        }
        if (msg.containsKey("title")) {
            program.title = msg.getString("title");
        }
        if (msg.containsKey("subtitle")) {
            program.subtitle = msg.getString("subtitle");
        }
        if (msg.containsKey("summary")) {
            program.summary = msg.getString("summary");
        }
        if (msg.containsKey("description")) {
            program.description = msg.getString("description");
        }
        if (msg.containsKey("serieslinkId")) {
            program.serieslinkId = msg.getInt("serieslinkId");
        }
        if (msg.containsKey("episodeId")) {
            program.episodeId = msg.getInt("episodeId");
        }
        if (msg.containsKey("seasonId")) {
            program.seasonId = msg.getInt("seasonId");
        }
        if (msg.containsKey("brandId")) {
            program.brandId = msg.getInt("brandId");
        }
        if (msg.containsKey("contentType")) {
            program.contentType = msg.getInt("contentType");
        }
        if (msg.containsKey("ageRating")) {
            program.ageRating = msg.getInt("ageRating");
        }
        if (msg.containsKey("starRating")) {
            program.starRating = msg.getInt("starRating");
        }
        if (msg.containsKey("firstAired")) {
            program.firstAired = msg.getLong("firstAired");
        }
        if (msg.containsKey("seasonNumber")) {
            program.seasonNumber = msg.getInt("seasonNumber");
        }
        if (msg.containsKey("seasonCount")) {
            program.seasonCount = msg.getInt("seasonCount");
        }
        if (msg.containsKey("episodeNumber")) {
            program.episodeNumber = msg.getInt("episodeNumber");
        }
        if (msg.containsKey("episodeCount")) {
            program.episodeCount = msg.getInt("episodeCount");
        }
        if (msg.containsKey("partNumber")) {
            program.partNumber = msg.getInt("partNumber");
        }
        if (msg.containsKey("partCount")) {
            program.partCount = msg.getInt("partCount");
        }
        if (msg.containsKey("episodeOnscreen")) {
            program.episodeOnscreen = msg.getString("episodeOnscreen");
        }
        if (msg.containsKey("image")) {
            program.image = msg.getString("image");
        }
        if (msg.containsKey("dvrId")) {
            program.dvrId = msg.getInt("dvrId");
        }
        if (msg.containsKey("nextEventId")) {
            program.nextEventId = msg.getInt("nextEventId");
        }
        */
        return program;
    }

    public static SeriesRecording convertMessageToSeriesRecordingModel(SeriesRecording seriesRecording, HTSMessage msg) {
        if (msg.containsKey("id")) {
            seriesRecording.setId(msg.getString("id"));
        }
        if (msg.containsKey("enabled")) {
            seriesRecording.setEnabled(msg.getInt("enabled"));
        }
        if (msg.containsKey("name")) {
            seriesRecording.setName(msg.getString("name"));
        }
        if (msg.containsKey("minDuration")) {
            seriesRecording.setMinDuration(msg.getInt("minDuration"));
        }
        if (msg.containsKey("maxDuration")) {
            seriesRecording.setMaxDuration(msg.getInt("maxDuration"));
        }
        if (msg.containsKey("retention")) {
            seriesRecording.setRetention(msg.getInt("retention"));
        }
        if (msg.containsKey("daysOfWeek")) {
            seriesRecording.setDaysOfWeek(msg.getInt("daysOfWeek"));
        }
        if (msg.containsKey("priority")) {
            seriesRecording.setPriority(msg.getInt("priority"));
        }
        if (msg.containsKey("approxTime")) {
            seriesRecording.setApproxTime(msg.getInt("approxTime"));
        }
        if (msg.containsKey("start")) {
            seriesRecording.setStart(msg.getLong("start") * 1000 * 60);
        }
        if (msg.containsKey("startWindow")) {
            seriesRecording.setStartWindow(msg.getLong("startWindow") * 1000 * 60);
        }
        if (msg.containsKey("startExtra")) {
            seriesRecording.setStartExtra(msg.getLong("startExtra"));
        }
        if (msg.containsKey("stopExtra")) {
            seriesRecording.setStopExtra(msg.getLong("stopExtra"));
        }
        if (msg.containsKey("title")) {
            seriesRecording.setTitle(msg.getString("title"));
        }
        if (msg.containsKey("fulltext")) {
            seriesRecording.setFulltext(msg.getInt("fulltext"));
        }
        if (msg.containsKey("directory")) {
            seriesRecording.setDirectory(msg.getString("directory"));
        }
        if (msg.containsKey("channel")) {
            seriesRecording.setChannelId(msg.getInt("channel"));
        }
        if (msg.containsKey("owner")) {
            seriesRecording.setOwner(msg.getString("owner"));
        }
        if (msg.containsKey("creator")) {
            seriesRecording.setCreator(msg.getString("creator"));
        }
        if (msg.containsKey("dupDetect")) {
            seriesRecording.setDupDetect(msg.getInt("dupDetect"));
        }
        return seriesRecording;
    }


    public static TimerRecording convertMessageToTimerRecordingModel(TimerRecording timerRecording, HTSMessage msg) {
        if (msg.containsKey("id")) {
            timerRecording.setId(msg.getString("id"));
        }
        if (msg.containsKey("title")) {
            timerRecording.setTitle(msg.getString("title"));
        }
        if (msg.containsKey("directory")) {
            timerRecording.setDirectory(msg.getString("directory", null));
        }
        if (msg.containsKey("enabled")) {
            timerRecording.setEnabled(msg.getInt("enabled"));
        }
        if (msg.containsKey("name")) {
            timerRecording.setName(msg.getString("name"));
        }
        if (msg.containsKey("configName")) {
            timerRecording.setConfigName(msg.getString("configName"));
        }
        if (msg.containsKey("channel")) {
            timerRecording.setChannelId(msg.getInt("channel"));
        }
        if (msg.containsKey("daysOfWeek")) {
            timerRecording.setDaysOfWeek(msg.getInt("daysOfWeek"));
        }
        if (msg.containsKey("priority")) {
            timerRecording.setPriority(msg.getInt("priority"));
        }
        if (msg.containsKey("start")) {
            timerRecording.setStart(msg.getLong("start") * 1000 * 60);
        }
        if (msg.containsKey("stop")) {
            timerRecording.setStop(msg.getLong("stop") * 1000 * 60);
        }
        if (msg.containsKey("retention")) {
            timerRecording.setRetention(msg.getInt("retention"));
        }
        if (msg.containsKey("owner")) {
            timerRecording.setOwner(msg.getString("owner"));
        }
        if (msg.containsKey("creator")) {
            timerRecording.setCreator(msg.getString("creator"));
        }
        return timerRecording;
    }

}
