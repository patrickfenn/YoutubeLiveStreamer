# YoutubeLiveStreamer(Streamlink)

---------------------------------

Features:

*Automatically check youtube channels for live streams.

*Launch the stream with either youtube or streamlink. 

*Get notified when streamers go live via desktop notifications. 

*Easily take neatclips of live streams.

---------------------------------

In order to run it you need java installed:

Java: https://www.java.com/en/download/

For binaries: https://sourceforge.net/projects/youtubelivestreamer/files/YoutubeLiveStreamer.zip/download

Upon being run for the first time it will create a text file 'channels.txt'. Here you will need to add channelIDs of the youtube channel you want to check, ie:
Ice Poseidon's channel is : https://www.youtube.com/channel/UCv9Edl_WbtbPeURPtFDo-uA, we only need: 
UCv9Edl_WbtbPeURPtFDo-uA

Seperate each channel with a new line. 

It will then ask you for a one time authorization to youtube, which is required to make video api requests. 

---------------------------------

This java program is used to automatically check youtube channels for live streams every 5 minutes. It also has the ability to launch the stream in either Youtube or VLC using streamlink. In order to launch it in Streamlink it  is required to have the following installed:

VLC : https://www.videolan.org/vlc/

Streamlink : https://github.com/streamlink/streamlink
