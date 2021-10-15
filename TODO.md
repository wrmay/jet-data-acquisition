# To Do For Cloud / GreenGrass IoT Setup

- Move the connection and certificate related information out of the gg start script for "jet-audio-monitor" and replace it with configuration that can be specified at deployment time.
- Do the same for the s3 bucket that is in all of the gg recipes.  Is there a way to set this one time, maybe using a shared component that consists only of configuration ?
- Once those things are done, figure out a way to automate the whole process of uploading artifacts and component creation.
- Reduce the logging that occurs on the Raspberry Pi so as to avoid wearing out the SD card.  Make the logging configurable.
- 